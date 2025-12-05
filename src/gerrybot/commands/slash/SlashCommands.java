package gerrybot.commands.slash;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class SlashCommands extends ListenerAdapter {
	
	public static SlashCommandData[] commands = {
//			Commands.slash("hentime", "Changes the Channel and Time hentas will be sent")
//				.addOption(OptionType.CHANNEL, "channel", "Channel that daily hentais will be sent", true)
//				.addOption(OptionType.STRING, "time", "Time that daily hentais will be sent xx:xx", true)
//				.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
//				.setGuildOnly(true),
//				
//			Commands.slash("hn", "Brings information about a specific nuclear code")
//				.addOption(OptionType.INTEGER, "code", "Nuclear code", true),
//				
			Commands.slash("yo", "wtf"),
	};
	
	private final Map<String, Consumer<SlashCommandInteractionEvent>> commandsMap = new HashMap<>();
	
	public SlashCommands() {
		HentaCommands hc = new HentaCommands();
			commandsMap.put("hentime", hc :: hentime);
			commandsMap.put("hn", hc :: hn);
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		commandsMap.get(event.getName()).accept(event);
	}
}
