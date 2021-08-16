package gerrybot.hentai;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

public class HentaiReaderListenerAdapter extends ListenerAdapter{
	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		if(!event.isFromGuild()) return;

		String commandName = event.getName();

		switch(commandName) {
			case "read" : {
				try {
					Hentai hentai = new NHentaiNet().genHentaiByNumber(event.getOption("numbers").getAsString());
					hentai.sendEmbedHentai(event).addActionRow(Button.secondary("previousPage", "Previous"),Button.primary("nextPage", "Next"),Button.danger("closePage", "Close")).queue();
					
				} catch (Exception e) {
					e.printStackTrace();
					event.reply(e.getMessage()).queue();
				}
				break;
			}
		}
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		String id = event.getComponentId();
		event.deferEdit().queue();

		switch(id) {
			case "nextPage" : {
				System.out.println(event.getMessage().getEmbeds().get(0).getImage().getUrl());
				event.getChannel().sendMessage("Next").queue();
				break;
			}
			case "previousPage" : {
				event.getChannel().sendMessage("Previous").queue();
				break;
			}
			case "closePage" : {
				event.getChannel().sendMessage("Close").queue();
				break;
			}
		}
	}
	
//	private SelectionMenu genActionRow(SlashCommandEvent event) {
//		return SelectionMenu.create("menu:hentaiArrows")
//					.setPlaceholder("Choose one")
//					.setRequiredRange(1, 1)
//					.addOption("Next", "nextPage")
//					.addOption("Previous", "previousPage")
//					.build();
//	}
}
