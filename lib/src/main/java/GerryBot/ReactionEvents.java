package GerryBot;

import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionEvents extends ListenerAdapter {
	
	public void	onMessageReactionAdd(MessageReactionAddEvent event) {
		@SuppressWarnings("unused")
		MessageReaction reaction = event.getReaction();
		ReactionEmote emote = event.getReactionEmote();
		MessageChannel channel = event.getChannel();
		User autor = event.getUser();

		if(autor.isBot()) return;
		
		if(channel.getName().equals("henta") && emote.getAsCodepoints().equals("U+1f51e")) {
			List<Message> mensagens = channel.getHistory().retrievePast(30).complete();
			
			channel.purgeMessages(mensagens);
			
			channel.sendMessage("O Hentai do dia eh cursed.").queue();
			channel.sendMessage("https://i.ytimg.com/vi/O8J7yyyFtIU/maxresdefault.jpg").queue();
			
			EmbedBuilder embed = new EmbedBuilder();
				embed.setAuthor(autor.getName(),null,autor.getAvatarUrl());
				embed.setColor(Main.cor);
			channel.sendMessage(embed.build()).queue();
		}
	}
}