package gerrybot.league;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Runes {
	
	private ArrayList<BufferedImage> images;
	
	public Runes(Document doc) {
		images = new ArrayList<BufferedImage>();
		downloadRunes(doc);
	}
	
	private void downloadRunes(Document doc) {
		Elements elements = null;
		
		//load n download all images on tabItem(runePage) 1 and 2, also adds them in images list
		for(int i = 1; i <= 2; i++) {
			elements = doc.getElementsByClass("tabItem ChampionKeystoneRune-" + i).select("div.perk-page-wrap").select("img[src~=(?i)\\.(png|jpe?g|gif)]");

			for(Element rune : elements) {
				if(!rune.attr("src").contains("grayscale")) {
					try {
						URLConnection connection = new URL("https:" + rune.attr("src").replace("q_auto:", "w_50,h_50&")).openConnection();
						connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
						images.add(ImageIO.read(connection.getInputStream()));
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	protected ArrayList<BufferedImage> getImages() {
		return this.images;
	}
}