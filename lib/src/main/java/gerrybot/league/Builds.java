package gerrybot.league;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Builds {

	private BufferedImage[][] builds;
	
	public Builds(Document doc) {
		downloadBuilds(getItemsSRC(doc));
	}
	
	//returns all download item links divided by line in array
	private String[][] getItemsSRC(Document doc) {
		Elements row = doc.getElementsByClass("champion-overview__table").next().select("ul.champion-stats__list");
		int rows = row.size();
		
		String[][] rowImages = new String[rows][];

		for(int i = 0; i < rows; i++) {
			Elements itemsInRow = row.get(i).select("li.champion-stats__list__item").select("img[src~=(?i)\\.(png|jpe?g|gif)]");
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
				try {
					URLConnection connection = new URL(items[i][j].replace("q_auto:", "w_50,h_50&")).openConnection();
					connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
					builds[i][j] = ImageIO.read(connection.getInputStream());
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected BufferedImage[][] getImages() {
		return this.builds;
	}
}
