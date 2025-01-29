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
	
	private final Map<String, Consumer<SlashCommandInteractionEvent>> commandsMap = new HashMap<>();
	
	public SlashCommands() {
		
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		switch(event.getName()) {
			case "hentime":
				event.deferReply().queue();
				
				GuildChannelUnion channel = event.getOption("channel", OptionMapping :: getAsChannel);
				String model = event.getOption("time", OptionMapping :: getAsString);
				
				event.getHook().sendMessage("Hentime changed to " + channel.getName() + " in " + model).queue();
				break;
				
			case "yo":
				event.deferReply().queue();
				//event.reply("opa").queue();
				event.getHook().sendMessage("opa").queue();
				break;
				
				
		}
	}
}
