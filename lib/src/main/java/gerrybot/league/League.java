package gerrybot.league;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.dv8tion.jda.api.entities.MessageChannel;

public class League {
	private String champion;
	private String role;
	
	private Document doc;
	
	private Runes runes;
	private Builds builds;
	
	public League(String champion, String role) throws Exception {
		this.champion = champion;
		this.role = role;
		connect();
	}
	
	private void connect() throws Exception {
		String url = "https://br.op.gg/champion/"+ champion +"/statistics/" + role;
		this.doc = Jsoup.connect(url).get();
		
		if(doc.baseUri().equals("https://br.op.gg/champion/statistics"))
			throw new Exception("Invalid Champion");
	}
	
	public void loadRunes() throws Exception {
		this.runes = new Runes(this.doc);
	}
	
	public void loadBuilds() throws Exception {
		this.builds = new Builds(this.doc);
	}
	

	public void sendRunes(MessageChannel channel) {
		this.runes.sendRunes(channel);
	}
	
	public void sendBuilds(MessageChannel channel) {
		this.builds.sendBuilds(channel);
	}
}
