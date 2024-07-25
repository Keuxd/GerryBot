package gerrybot.commands;

import gerrybot.core.Main;
import gerrybot.diceroller.DiceRoller;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DiceRollCommands {
	
	public void diceRoll(MessageReceivedEvent event) {
		DiceRoller dr = new DiceRoller(event.getMessage().getContentRaw().replaceAll(" ", ""));
		User author = event.getAuthor();
		
		EmbedBuilder eb = new EmbedBuilder()
			.setColor(Main.COLOR)
			.setTitle("**" + dr.getTotalSum() + "**")
			.setFooter(dr.dicesToString())
			.setAuthor(author.getEffectiveName(), null, author.getAvatarUrl());
	
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
		
//		dr.DEBUG_VALUES();
	}
}
