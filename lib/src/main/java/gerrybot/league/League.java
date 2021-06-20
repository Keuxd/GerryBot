package gerrybot.league;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class League {
	private String champion;
	
	public League(String champion) {
		this.champion = champion;
	}
	
	
	public void getRunes() throws IOException {
		String url = "https://br.op.gg/champion/"+ this.champion +"/statistics/";
		Document doc = Jsoup.connect(url).get();
		
		System.out.println(doc);
	}
}
