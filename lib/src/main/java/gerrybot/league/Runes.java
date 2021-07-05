package gerrybot.league;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.dv8tion.jda.api.entities.MessageChannel;

public class Runes {
	private Document doc;
	private String[][] runes;
	private int loadedPages;
	
	//connect
	public Runes(String champion, String role) throws Exception {
		String url = "https://br.op.gg/champion/"+ champion +"/statistics/" + role;
		this.doc = Jsoup.connect(url).get();
		
		if(doc.baseUri().equals("https://br.op.gg/champion/statistics"))
			throw new Exception("Invalid Champion");

		loadedPages = getRunes();
		decreaseRunesSize();
	}

	private void decreaseRunesSize() {		
		for(int i = 0; i < this.loadedPages; i++) {
			for(int j = 0; j < 11; j++) {
				this.runes[i][j] = this.runes[i][j].replace("q_auto:", "w_30,h_30&");
			}
		}
	}
	
	public void sendRunes(MessageChannel channel) {		
		for(int i = 0; i < this.loadedPages; i++) {
			channel.sendMessage("Rune Page 0" + (i+1)).queue();
			
			for(int j = 0; j < 11; j++)
				channel.sendMessage(this.runes[i][j]).queue();
		}
	}
	
	private int getRunes() {
		Elements runePage;
		int counter;
		this.runes = new String[2][];
		
		for(int i = 0; i < 2; i++) {
			try {
				runePage = doc.getElementsByClass("tabItem ChampionKeystoneRune-" + (i+1)).select("div.perk-page-wrap").first().select("img[src~=(?i)\\.(png|jpe?g|gif)]");
				this.runes[i] = new String[11];
				counter = 0;
						
				for(Element runes : runePage) {
					if(!runes.attr("src").contains("grayscale")) {
						this.runes[i][counter] = "https:" + runes.attr("src");
						counter++;
					}
				}	
			} catch(Exception e) {
				return 1;
			}			
		}
		return 2;
	}
	
}
