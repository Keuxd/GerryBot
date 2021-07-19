package gerrybot.league;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jsoup.nodes.Document;

public class Builds {

	private ArrayList<BufferedImage> items;
	
	public Builds(Document doc) {
		items = new ArrayList<BufferedImage>();
		downloadBuilds(doc);
	}
	
	private void downloadBuilds(Document doc) {
		doc.getElementsByClass("champion-overview__row champion-overview__row--first").select("img[src~=(?i)\\.(png|jpe?g|gif)]").forEach(element -> {
			if(element.attr("src").contains("item")) {
				
			}
		});

	}
	
	protected ArrayList<BufferedImage> getImages() {
		return this.items;
	}
}
