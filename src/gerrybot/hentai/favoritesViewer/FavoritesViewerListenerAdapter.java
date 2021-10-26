package gerrybot.hentai.favoritesViewer;

import gerrybot.hentai.Hentai;
import gerrybot.hentai.NHentaiNet;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public class FavoritesViewerListenerAdapter extends ListenerAdapter {

	@Override
	public void onSelectionMenu(SelectionMenuEvent event) {
		event.deferReply();
		if(!event.getComponentId().equals("gerry:favoritesHentas")) return;
		
		SelectOption option = event.getSelectedOptions().get(0); // We can only select 1 option
		String numberChoosed = option.getLabel();
		String previousNumber = event.getMessage().getEmbeds().get(0).getTitle().substring(6,12);
		
		if(numberChoosed.equals(previousNumber)) return;
		
		// Since there's a limitation in discord that we cant edit attachments, we'll have to set ImageFile to null so we use cover url in embed
		try {
			Hentai newHenta = new NHentaiNet().genHentaiByNumber(numberChoosed);
			newHenta.setImageFile(null);
			MessageEmbed me = newHenta.genEmbedMessageHentai("Hentai " + numberChoosed);

			event.editMessageEmbeds(me).queue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
