package gerrybot.league;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
		this.champion = nickNames(champion);
		
		if(role.equalsIgnoreCase("jg")) role = "jungle";
		else if(role.equalsIgnoreCase("sup")) role = "support";
		
		this.role = role;
		connect();
	}
	
	private void connect() throws Exception {
		String url = "https://br.op.gg/champion/"+ champion +"/statistics/" + role;
		this.doc = Jsoup.connect(url).get();
		
		if(doc.baseUri().equals("https://br.op.gg/champion/statistics"))
			throw new Exception("Invalid Champion");
	}
	
	private String nickNames(String champion) {
		switch(champion.toLowerCase()) {
			case "aurelion": return "aurelionsol";
			case "cait": return "caitlyn";
			case "cho": return "chogath";
			case "mundo": return "drmundo";
			case "eve": return "evelynn";
			case "ez": return "ezreal";
			case "fiddle": return "fiddlesticks";
			case "gp": return "gangplank";
			case "heca": return "hecarim";
			case "heimer": return "heimerdinger";
			case "j4":
			case "jarvan": return "jarvaniv";
			case "kat": return "katarina";
			case "kog": return "kogmaw";
			case "lb": return "leblanc";
			case "lee": return "leesin";
			case "master":
			case "yi": return "masteryi";
			case "mf": return "missfortune";
			case "morg": return "morgana";
			case "nida": return "nidalee";
			case "noc": return "nocturne";
			case "renek": return "renekton";
			case "tk": return "tahmkench";
			case "trynda": return "tryndamere";
			case "tf": return "twistedfate";
			case "vel": return "velkoz";
			case "vlad": return "vladimir";
			case "voli": return "volibear";
			case "ww": return "warwick";
			case "xin": return "xinzhao";

			case "gata": return "yuumi";
			case "minion": return "sona";
			case "cantora": return "seraphine";
			case "eboy": return "viego";
			case "sapo": return "tahmkench";
			case "rato": return "twitch";
			case "rainha": return "qiyana";
			
			default: return champion;
		}
	}
	
	public void loadRunes() throws Exception {
		this.runes = new Runes(this.doc);
	}
	
	public void loadBuilds() throws Exception {
		this.builds = new Builds(this.doc);
	}

	public void sendRunes(MessageChannel channel) throws IOException {	
		File outputFile = new File("cover.png");
		ImageIO.write(new Draw().drawRunes(this.runes.getImages()), "png", outputFile);
		channel.sendFile(outputFile, "cover.png").queue();
	}
	
	public void sendBuilds(MessageChannel channel) throws IOException {
		File outputFile = new File("cover.png");
		ImageIO.write(new Draw().drawBuilds(this.builds.getImages()), "png", outputFile);
		channel.sendMessage(getSkillOrder()).addFile(outputFile, "cover.png").queue();
	}
	
	private String getSkillOrder() {
		try {
			String skillsPriority = this.doc.getElementsByClass("champion-stats__list").get(2).getElementsByTag("span").text();
			String skillsByLevel = this.doc.getElementsByClass("champion-skill-build__table").text().substring(35);
		
			String finalString = new String("**" + skillsPriority + "**  -> " + skillsByLevel);
		
			return finalString;	
		} catch(Exception e) {
			e.printStackTrace();
			return " ";
		}

	}
}