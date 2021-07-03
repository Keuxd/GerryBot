package gerrybot.league;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.dv8tion.jda.api.entities.MessageChannel;

public class Runes {
	private Document doc;
	private String[] runes;
	
	//connect
	public Runes(String champion, String role) throws Exception {
		String url = "https://br.op.gg/champion/"+ champion +"/statistics/" + role + "/runes";
		this.doc = Jsoup.connect(url).get();
		
		if(doc.baseUri().equals("https://br.op.gg/champion/statistics"))
			throw new Exception("Invalid Champion");
		
		getRunes();
		decreaseRunesSize();
	}
	
	private void getRunes() {
		Elements runePage = doc.getElementsByClass("perk-page-wrap").first().select("img[src~=(?i)\\.(png|jpe?g|gif)]");
		runes = new String[11];
		
		int counter = 0;
		for(Element rune : runePage) {
			if(!rune.attr("src").contains("grayscale")) {
				this.runes[counter] = "https:" + rune.attr("src");
				counter++;
			}
		}
	}
	
	private void decreaseRunesSize() {
		int arraySize = this.runes.length;
		
		for(int i = 0; i < arraySize; i++) {
			this.runes[i] = this.runes[i].replace("q_auto:", "w_30,h_30&"); 
		}
	}
	
	public void sendRunes(MessageChannel channel) {
		for(String rune : this.runes) {
			channel.sendMessage(rune).queue();;
		}
	}

}
