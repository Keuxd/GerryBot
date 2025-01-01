package gerrybot.league;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import gerrybot.database.DataBaseTable;

public class League {
	
	private JsonObject info;
	private String url;
	
	protected League(String champion, String role, DataBaseTable type) {
		champion = nickNames(champion);
		
		if(role.equalsIgnoreCase("jg")) role = "jungle";
		else if(role.equalsIgnoreCase("sup") || role.equalsIgnoreCase("suporte")) role = "support";
		
		if(role.equalsIgnoreCase("aram")) {
			this.url = OpggEndPoints.API_NODE_BASE_URL + "/bypass/champions/br/aram/" + champion + "/none/";
		} else {
			this.url = OpggEndPoints.API_NODE_BASE_URL + "/bypass/champions/br/ranked" + "/" + champion + "/" + role + "/";
		}	
		
		this.info = OpggMetaBase.downloadJson(this.url );
	}
	
	private String nickNames(String champion) {
		switch(champion.toLowerCase()) {
			case "aurelion": return "aurelionsol";
			case "bardo": return "bard";
			case "blitz": return "blitzcrank";
			case "cait": return "caitlyn";
			case "cassio": return "cassiopeia";
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
			case "malza": return "malzahar";
			case "master":
			case "yi": return "masteryi";
			case "mf": return "missfortune";
			case "morde": return "mordekaiser";
			case "morg": return "morgana";
			case "naut": return "nautilus";
			case "nida": return "nidalee";
			case "noc": return "nocturne";
			case "ori": return "orianna";
			case "pant":
			case "panth" : return "pantheon";
			case "rek": return "reksai";
			case "renek": return "renekton";
			case "sera": return "seraphine";
			case "shyv": return "shyvana";
			case "tk": return "tahmkench";
			case "trynda": return "tryndamere";
			case "tf": return "twistedfate";
			case "vel": return "velkoz";
			case "vlad": return "vladimir";
			case "voli": return "volibear";
			case "wu":
			case "wukong": return "monkeyking";
			case "ww": return "warwick";
			case "xin": return "xinzhao";

			case "gata": return "yuumi";
			case "minion": return "sona";
			case "cantora": return "seraphine";
			case "eboy": return "viego";
			case "sapo": return "tahmkench";
			case "rato": return "twitch";
			case "rainha": return "qiyana";
			case "baiano": return "akshan";
			case "macaco": return "monkeyking";
			
			default: return champion;
		}
	}
	
	public String getSkillOrder() {
		try {
			JsonObject skillMasteriesObject = OpggMetaBase.downloadJson(this.url + "skills").getAsJsonObject("data").getAsJsonArray("skill_masteries").get(0).getAsJsonObject();
			
			JsonArray skillOrderArray = skillMasteriesObject.getAsJsonArray("builds").get(0).getAsJsonObject().getAsJsonArray("order");
			StringBuilder sb = new StringBuilder();

			for(JsonElement skillElement : skillOrderArray) {
				sb.append(" ");
				sb.append(skillElement.getAsString());
			}
			
			JsonArray skillMasteriesArray = skillMasteriesObject.getAsJsonArray("ids");
			
			return String.format("**%s %s %s** ->%s", skillMasteriesArray.get(0).getAsString(), skillMasteriesArray.get(1).getAsString(), skillMasteriesArray.get(2).getAsString(), sb.toString());
		} catch(Exception e) {
			return " ";
		}
	}
	
	protected JsonObject getJson() {
		return this.info;
	}
}