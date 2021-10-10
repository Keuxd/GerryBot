package gerrybot.league;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import gerrybot.database.DataBaseEnum;
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
					String itemNum = ("https:" + rune.attr("src")).split("/")[6].substring(0,4);
					BufferedImage rune64 = DataBaseUtils.getLeagueImage(itemNum, DataBaseEnum.RUNE);

					if(rune64 == null) { // if it doesnt exist so download it and add in db
						try {
							URLConnection connection = new URL("https:" + rune.attr("src").replace("q_auto:", getRightImageSize(rune))).openConnection();
							connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
							rune64 = ImageIO.read(connection.getInputStream());
							
							DataBaseUtils.insertLeagueImage(itemNum, rune64, DataBaseEnum.RUNE);
							
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
					
					images.add(rune64);
				}
			}
		}
	}
	
	protected ArrayList<BufferedImage> getImages() {
		return this.images;
	}
	
	protected String getRightImageSize(Element rune) {
		String parentStr = rune.parent().parent().className();
		System.out.println(parentStr);
		
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
}