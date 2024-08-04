package gerrybot.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildCommands extends ListenerAdapter {

	private final Map<String, Consumer<MessageReceivedEvent>> commandsMap = new HashMap<>();
	
	public GuildCommands() {
		GenericCommands gc = new GenericCommands();
			commandsMap.put("!yo", gc :: yo);
			commandsMap.put("!prime", gc :: prime);
			commandsMap.put("wendy", gc :: wendy);
			commandsMap.put("harry", gc :: harry);
			commandsMap.put("montanha", gc :: montanha);
			commandsMap.put("naltan", gc :: naltan);
			commandsMap.put("!blazblue", gc :: blazblue);
			commandsMap.put("!tempo", gc :: tempo);
			commandsMap.put("!updates", gc :: updates);
			
		HentaCommands hc = new HentaCommands();
			commandsMap.put("!rhn", hc :: rhn);
			commandsMap.put("!henta", hc :: henta);
			commandsMap.put("!hn [0-9]+$", hc :: hn);
			commandsMap.put("!hentime", hc :: hentimeUsage);
			commandsMap.put("!hentime ([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]", hc :: hentime);
			commandsMap.put("!favorites( (<@)?[0-9]+>?)?", hc :: favorites);
			
		LeagueCommands lc = new LeagueCommands();
			commandsMap.put("!runa( [a-zA-Z]+){2}", lc :: runes);
			commandsMap.put("!build( [a-zA-Z]+){2}", lc :: builds);
			
		DiceRollCommands drc = new DiceRollCommands();
			commandsMap.put("^([1-9][0-9]?)?d[1-9][0-9]{0,3}( )?([+-]( )?(([1-9][0-9]?)?d[1-9][0-9]{0,3}( )?|[0-9]{1,2}( )?))*$", drc :: diceRoll);
		
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		if(!event.isFromGuild()) return;
		
		String message = event.getMessage().getContentRaw().toLowerCase();
		//System.out.println(event.getAuthor().getEffectiveName() + " -> " + message);
		
		executeCommand(message, event);
	}
	
	private void executeCommand(String commandKey, MessageReceivedEvent parameterEvent) {
		Set<String> patterns = commandsMap.keySet();
		
		for(String pattern : patterns) {
			if(commandKey.matches(pattern)) {
				commandsMap.get(pattern).accept(parameterEvent);
			}
		}
	}
}
