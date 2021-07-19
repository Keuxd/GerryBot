package gerrybot.league;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.jsoup.nodes.Document;

public class Builds {

	private ArrayList<BufferedImage> items;
	
	public Builds(Document doc) {
		items = new ArrayList<BufferedImage>();
		downloadBuilds(doc);
	}

	private void downloadBuilds(Document doc) {
		doc.getElementsByClass("champion-stats__list").select("img[src~=(?i)\\.(png|jpe?g|gif)]").forEach(element -> {
			if(element.attr("src").contains("item")) {
				try {
					URLConnection connection = new URL("https:" + element.attr("src").replace("q_auto:", "w_50,h_50&")).openConnection();
					connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
					items.add(ImageIO.read(connection.getInputStream()));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	
	protected ArrayList<BufferedImage> getImages() {
		return this.items;
	}
}
