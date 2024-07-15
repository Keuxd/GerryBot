package gerrybot.hentai;

import java.sql.SQLException;
import java.util.List;

import gerrybot.core.Main;
import gerrybot.database.DataBaseUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HentaiReactionEvents extends ListenerAdapter {
	
	@Override
	public void	onMessageReactionAdd(MessageReactionAddEvent event) {
		String emoji = event.getReaction().getEmoji().asUnicode().getAsCodepoints();
		MessageChannel channel = event.getChannel();
		long messageId = event.getMessageIdLong();
		User author = event.getUser();

		// Shouldn't interact with bots reactions/Emojis have unicode notation emotes dont
		if(author.isBot()) return;

		if(channel.getName().equals("henta") && emoji.equals("U+1f51e")) {
			cursedAction(channel, author);
		}
		else if(emoji.equals("U+2b50")) {
			try {
				favoriteAction(channel, author, messageId);
			} catch (SQLException e) {
				channel.sendMessage("Error, call the police: " + e.getMessage()).queue();
				e.printStackTrace();
			}
		}
	}
	
	private void cursedAction(MessageChannel channel, User author) {
		List<Message> messages = channel.getHistory().retrievePast(30).complete();
		channel.purgeMessages(messages);
		
		channel.sendMessage("O Hentai do dia eh cursed.").queue();
		channel.sendMessage("https://i.ytimg.com/vi/O8J7yyyFtIU/maxresdefault.jpg").queue();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(author.getName(), null, author.getAvatarUrl());
		embed.setColor(Main.COLOR);
		
		channel.sendMessageEmbeds(embed.build()).queue();
	}
	
	private void favoriteAction(MessageChannel channel, User author, long messageId) throws SQLException {
		Field linkField = null;
		try { // The message at this point should have 1 embed and 3 fields so we know that's our message, so if any OutOfBoundsException happen in the process, it's not our message.
			linkField = channel.retrieveMessageById(messageId).complete().getEmbeds().get(0).getFields().get(0);
		} catch(Exception e) {
			return;
		}
		
		// If for some reason the condition above get match with the wrong message we'll check the field name and value.
		String link = linkField.getValue();
		if(!linkField.getName().equals("Link") && !link.matches("^$"))
			return;
		
		String numbers = link.substring(22);
		
		boolean wasHentaAdded = DataBaseUtils.interactFavoriteHenta(author.getId(), numbers);
		
		if(wasHentaAdded) // If it was then we say 'added', if it wasn't then it was 'removed'
			channel.sendMessage("Henta " + numbers + " added. " + author.getAsMention()).queue();
		else
			channel.sendMessage("Henta " + numbers + " removed. " + author.getAsMention()).queue();
	}
}