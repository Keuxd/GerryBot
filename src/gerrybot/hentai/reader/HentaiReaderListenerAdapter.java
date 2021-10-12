package gerrybot.hentai.reader;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HentaiReaderListenerAdapter extends ListenerAdapter{
	
	private final String BASE_LINK = "https://nhentai.net/g/";
	
	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		if(!event.isFromGuild()) return;

		String commandName = event.getName();
		String fullLink = BASE_LINK + event.getOption("numbers").getAsString() + "/1/";
		
		switch(commandName) {
			case "read" : {
				try {
					HentaiReaderTest hrt = new HentaiReaderTest(fullLink);
					hrt.replySlashCommand(event).queue();
					
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

		String message = event.getMessage().getContentRaw();
		System.out.println(message);
		HentaiReaderTest hrt = new HentaiReaderTest(message);
		
		
		switch(id) {
			case "nextPage" : {
//				System.out.println(event.getMessage().getEmbeds().get(0).getImage().getUrl());
				hrt.nextPage();
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
