package gerrybot.commands.slash;

import gerrybot.core.Main;
import gerrybot.database.DataBaseUtils;
import gerrybot.hentai.Hentai;
import gerrybot.hentai.NHentaiNet;
import gerrybot.hentai.favoritesViewer.FavoritesViewer;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class HentaCommands {
	
	public void rhn(MessageReceivedEvent event) {
		try {
			Hentai randomHentai = NHentaiNet.createHentaiRandomly();
			randomHentai.sendEmbedHentai(event.getChannel()).queue(message -> {
				message.addReaction(Emoji.fromUnicode("U+2B50")).queue();
			});
		} catch(Exception e) {}
	}
	
	public void hn(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().toLowerCase().split("\\s+");
		
		Hentai hentai = NHentaiNet.createHentaiByNumber(args[1]);
		hentai.sendEmbedHentai(event.getChannel()).queue(message -> {
			if(!hentai.getTitle().equals("Not Found")) {
				message.addReaction(Emoji.fromUnicode("U+2B50")).queue();
			}
		});
	}
	
	public void hn(SlashCommandInteractionEvent event) {
		event.deferReply().queue();
		
		int code = Math.abs(event.getOption("code", OptionMapping :: getAsInt));
		
		Hentai hentai = NHentaiNet.createHentaiByNumber(code + "");

		hentai.sendEmbedHentai(event).queue(message -> {
			if(!hentai.getTitle().equals("Not Found")) {
//				message.addReaction(Emoji.fromUnicode("U+2B50")).queue();
			}
		});
	}
	
	public void hentime(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		
		GuildChannelUnion channel = event.getOption("channel", OptionMapping :: getAsChannel);
		String time = event.getOption("time", OptionMapping :: getAsString);
		
		if(channel.getType().isAudio()) {
			event.getHook().sendMessage("```Error: The selected channel can't be an audio type.```").queue();
			return;
		}
		
		if(!time.matches("([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]")) {
			event.getHook().sendMessage("```Error: Time must be 'xx:xx' 24h format```").queue();
			return;
		}
		
		try {
			DataBaseUtils.insertHentaTimer(channel.getIdLong(), time.split(":"));
			Main.dailyThread.interrupt(); // Will "restart" dailyThread to calculate sleep time again
			event.getHook().sendMessage("```Hentime changed to '" + time + "' in channel '" + channel.getName() +  "'```").queue();
		} catch(Exception e) {
			event.getHook().sendMessage("```Error: Hentime couldn't be registered, contact devs.```").queue();
		}
	}
	
	public void favorites(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().toLowerCase().split("\\s+");
		MessageChannelUnion channel = event.getChannel();
		
		String userId;
		if(args.length == 1) {
			userId = event.getAuthor().getId();
		} else {
			userId = args[1].replaceAll("[^0-9]", "");
		}
		
		try {
			new FavoritesViewer(userId).sendFavoritesHentas(channel).queue(message -> {
				message.addReaction(Emoji.fromUnicode("U+2B50")).queue();
			});
		} catch(Exception e) {
			channel.sendMessage("This user doesn't have favorite hentas").queue();
		}
	}
	
}
