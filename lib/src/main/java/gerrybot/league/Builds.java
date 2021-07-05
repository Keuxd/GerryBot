package gerrybot.league;

import java.util.ArrayList;

import org.jsoup.nodes.Document;

import net.dv8tion.jda.api.entities.MessageChannel;

public class Builds {
//	private String builds[];
	private ArrayList<String> al;
	
	public Builds(Document doc) {
//		this.builds = new String[3];
		al = new ArrayList<String>();
		loadBuilds(doc);
	}
	
	private void loadBuilds(Document doc) {
		doc.getElementsByClass("champion-overview__row champion-overview__row--first").select("img[src~=(?i)\\.(png|jpe?g|gif)]").forEach(element -> {
			if(element.attr("src").contains("item")) {
				al.add("https:" + element.attr("src"));
			}
		});

	}
	
	private void decreaseBuildsSize() {
		
	}
	
	protected void sendBuilds(MessageChannel channel) {
		for(String a : al) {
			channel.sendMessage(a).queue();
		}
	}
}
