package gerrybot.hentai.favoritesViewer;

import gerrybot.hentai.Hentai;
import gerrybot.hentai.NHentaiNet;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class FavoritesViewerListenerAdapter extends ListenerAdapter {

	@Override
	public void onStringSelectInteraction(StringSelectInteractionEvent event) {
		
		if(!event.getComponentId().equals("gerry:favoritesHentas")) return;
		event.deferReply().queue();
		
		SelectOption option = event.getSelectedOptions().get(0); // We can only select 1 option
		String numberChoosed = option.getLabel();
		String previousNumber = event.getMessage().getEmbeds().get(0).getTitle().substring(6);
		
		if(numberChoosed.equals(previousNumber)) return;
		
		// Since there's a limitation in discord that we cant edit attachments, we'll have to set ImageFile to null so we use cover url in embed
		try {
			Hentai newHenta = NHentaiNet.createHentaiByNumber(numberChoosed);
			newHenta.setImageFile(null);
			MessageEmbed me = newHenta.genEmbedMessageHentai("Hentai " + numberChoosed);

			StringSelectMenu sm = event.getSelectMenu().createCopy().setPlaceholder(numberChoosed).build();

			event.getHook().deleteOriginal().queue();
			event.getHook().editMessageEmbedsById(event.getMessageId(), me).setActionRow(sm).queue();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
