package gerrybot.commands;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import gerrybot.core.Main;
import gerrybot.database.DataBaseUtils;
import gerrybot.hentai.Hentai;
import gerrybot.hentai.NHentaiNet;
import gerrybot.hentai.favoritesViewer.FavoritesViewer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HentaCommands {
	
	public void rhn(MessageReceivedEvent event) {
		try {
			Hentai randomHentai = NHentaiNet.createHentaiRandomly();
			randomHentai.sendEmbedHentai(event.getChannel()).queue(message -> {
				message.addReaction(Emoji.fromUnicode("U+2B50")).queue();
			});
		} catch(Exception e) {}
	}
	
	public void henta(MessageReceivedEvent event) {
		MessageChannelUnion channel = event.getChannel();
		
		LocalDateTime ldt = LocalDateTime.now();
		String dataNum = ldt.getDayOfMonth() + ldt.format(DateTimeFormatter.ofPattern("MMyy"));
		
		Hentai dailyHenta = NHentaiNet.createHentaiByNumber(dataNum);
		
		if(dailyHenta.getTitle().equals("Not Found")) {
			channel.sendMessage("O dia de hoje não tem hentai.").queue();
			channel.sendMessage("https://cdn.discordapp.com/emojis/744921446136021062.png").queue();
			return;
		}
		
		dailyHenta.sendEmbedHentai(channel, "Hentai do Dia").queue(message -> {
			message.addReaction(Emoji.fromUnicode("U+1F51E")).queue();
			message.addReaction(Emoji.fromUnicode("U+2B50")).queue();
		});
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
	
	public void hentimeUsage(MessageReceivedEvent event) {
		event.getChannel().sendMessage("**Usage:** ```!hentime xx:xx [00:00 ~ 23:59]\n(You must be an Administrator)```").queue();
	}
	
	public void hentime(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().toLowerCase().split("\\s+");
		MessageChannelUnion channel = event.getChannel();
		Member author = event.getMember();
		
		if(!author.hasPermission(Permission.ADMINISTRATOR)) {
			channel.sendMessage("You're not an administrator " + author.getAsMention()).queue();
			return;
		}
		
		try {
			DataBaseUtils.insertHentaTimer(event.getGuild().getIdLong(), args[1].split(":"));
			Main.dailyThread.interrupt(); // Will "restart" dailyThread to calculate sleep time again
			channel.sendMessage("Hentime changed to " + args[1]).queue();
		} catch(SQLException e) {
			channel.sendMessage("Error, contact devs: " + e.getMessage()).queue();
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
