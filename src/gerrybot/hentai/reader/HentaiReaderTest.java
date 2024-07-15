package gerrybot.hentai.reader;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class HentaiReaderTest {
	
	private String link;
	private Document hentaiPageHTML;
	
	public HentaiReaderTest(String link) {
//		this.link = "https://nhentai.net/g/" + numbers + "/1/";
		this.link = link;
	}
	
	public MessageCreateAction replySlashCommand(SlashCommandInteractionEvent event) {
		event.deferReply();
		return event.getChannel().sendMessage(this.link).setActionRow( 
			Button.secondary("previousPage", "Previous"),
			Button.primary("nextPage", "Next"),
			Button.danger("closePage","Close")
		);
	}
	
	//it tries to load the 'Document' for this.hentaiPageHTML, if it can returns true, if it doesn't returns false
	@SuppressWarnings("unused")
	private boolean loadLink(String link) {
		// while/if this page doesnt load continue trying loading it
		while(this.hentaiPageHTML == null) {
			try {
				this.hentaiPageHTML = Jsoup.connect(link).timeout(45*1000).get();
				return true;
				
			} catch(SocketTimeoutException e) {
				continue;
			
			} catch(HttpStatusException e) {
				return false;
				
			} catch(IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public void nextPage() {
		
		String subString = link.substring(link.length()-2);
		System.out.println(subString);
		
		
	}
}
