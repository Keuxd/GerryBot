package gerrybot.commands;

import gerrybot.league.Builds;
import gerrybot.league.Runes;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LeagueCommands {
	
	public void runes(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().toLowerCase().split("\\s+");
		MessageChannelUnion channel = event.getChannel();
		
		try {
			Runes runes = new Runes(args[1], args[2]);
			runes.sendRunes(channel);
		} catch(Exception e) {
			channel.sendMessage("Invalid Champion").queue();
			e.printStackTrace();
		}
	}
	
	public void builds(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().toLowerCase().split("\\s+");
		MessageChannelUnion channel = event.getChannel();
		
		try {
			Builds builds = new Builds(args[1], args[2]);
			builds.sendBuilds(channel);
		} catch(Exception e) {
			channel.sendMessage("Invalid Champion").queue();
			e.printStackTrace();
		}
	}
}
