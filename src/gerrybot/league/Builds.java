package gerrybot.league;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import gerrybot.database.DataBaseTable;
import gerrybot.database.DataBaseUtils;

public class Builds {

	private BufferedImage[][] builds;
	
	public Builds(Document doc) {
		downloadBuilds(getItemsSRC(doc));
	}
	
	//returns all download item links divided by line in array
	private String[][] getItemsSRC(Document doc) {
		Elements row = doc.getElementsByClass("champion-overview__table").next().select("ul.champion-stats__list");
		String itemClassInRow = "li.champion-stats__list__item";
		
		int rows = row.size();
		
		String[][] rowImages = new String[rows][];
		
		for(int i = 0; i < rows; i++) {
			Elements itemsInRow = row.get(i).select(itemClassInRow).select("img[src~=(?i)\\.(png|jpe?g|gif)]");
			rowImages[i] = new String[itemsInRow.size()];
			
			for(int j = 0; j < rowImages[i].length; j++) {
				rowImages[i][j] = "https:" + itemsInRow.get(j).attr("src");
			}
		}
		return rowImages;
	}
	
	private void downloadBuilds(String[][] items) {
		int rows = items.length;
		builds = new BufferedImage[rows][];
		
		for(int i = 0; i < rows; i++) {
			builds[i] = new BufferedImage[items[i].length];
			for(int j = 0; j < items[i].length; j++) {
				String itemID = (items[i][j]).split("/")[6].substring(0,4);
				
				builds[i][j] = getItem(itemID, items[i][j]);
			}
		}
	}
	
	// It consults the DB for the item, if it doesn't exist it'll download it and save it in DB, it returns the image
	private BufferedImage getItem(String itemID, String itemSrcLink) {
		BufferedImage itemImage = DataBaseUtils.getLeagueImage(itemID, DataBaseTable.ITEM);
		
		if(itemImage == null) {
			try {
				URLConnection connection = new URL(itemSrcLink.replace("q_auto:", "w_50,h_50&")).openConnection();
				connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
				itemImage = ImageIO.read(connection.getInputStream());
				
				DataBaseUtils.insertLeagueImage(itemID, itemImage, DataBaseTable.ITEM);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return itemImage;
	}

	protected BufferedImage[][] getImages() {
		return this.builds;
	}
}
