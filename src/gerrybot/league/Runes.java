package gerrybot.league;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import gerrybot.database.DataBaseTable;
import gerrybot.database.DataBaseUtils;

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
					String itemID = ("https:" + rune.attr("src")).split("/")[6].substring(0,4);
					BufferedImage runeImage = getRune(itemID, rune);
					
					images.add(runeImage);
				}
			}
		}
	}
	
	private BufferedImage getRune(String itemID, Element rune) {
		BufferedImage runeImage = DataBaseUtils.getLeagueImage(itemID, DataBaseTable.RUNE);
		
		if(runeImage == null) {
			try {
				URLConnection connection = new URL("https:" + rune.attr("src").replace("q_auto:", getRightImageSize(rune))).openConnection();
				connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
				runeImage = ImageIO.read(connection.getInputStream());
				
				DataBaseUtils.insertLeagueImage(itemID, runeImage, DataBaseTable.RUNE);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return runeImage;
	}
	
	protected String getRightImageSize(Element rune) {
		String parentStr = rune.parent().parent().className();
		System.out.println("Rune Being Downloaded -> " + parentStr);
		
		if(rune.parent().className().contains("mark")) {
			return "w_25,h_25&";
		} else if(parentStr.contains("keystone")) {
			return "w_50,h_50&";
		} else if(parentStr.contains("fragment")) {
			return "w_20,h_20&";
		} else {
			return "w_30,h_30&";
		}
	}
	
	protected ArrayList<BufferedImage> getImages() {
		return this.images;
	}
}