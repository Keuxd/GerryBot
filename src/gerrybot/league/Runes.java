package gerrybot.league;

import static gerrybot.database.DataBaseTable.RUNE;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import gerrybot.core.Main;
import gerrybot.database.DataBaseUtils;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Runes extends League {
	
	private ArrayList<BufferedImage> images;
	
	public Runes(String champion, String role) {
		super(champion, role, RUNE);
		
		images = new ArrayList<BufferedImage>();
		
		getRunesImagesInList();
	}
	
	private void getRunesImagesInList() {
		JsonArray runePagesArray = this.getJson().getAsJsonObject("data").getAsJsonArray("rune_pages");
		int sizeRunePagesArray = (runePagesArray.size() >= 2) ? 2 : runePagesArray.size(); // It may be 0 or 1
		
		for(int i = 0; i < sizeRunePagesArray; i++) {
			JsonArray buildsArray = runePagesArray.get(i).getAsJsonObject().getAsJsonArray("builds");
			int sizeBuildsArray = (buildsArray.size() >= 2) ? 2 : buildsArray.size(); // It may be 0 or 1

			for(int j = 0; j < sizeBuildsArray; j++) {
				JsonObject buildObject = buildsArray.get(j).getAsJsonObject();
				
				//Symbol rune 01
				images.add(DataBaseUtils.getLeagueImage(buildObject.getAsJsonPrimitive("primary_page_id").getAsString(), RUNE));
				
				//pRune / sRunes 01
				JsonArray primaryRunesArray = buildObject.getAsJsonArray("primary_rune_ids");
				for(JsonElement primaryRune : primaryRunesArray) 
					images.add(DataBaseUtils.getLeagueImage(primaryRune.getAsString(), RUNE));
				
				//Symbol rune 02
				images.add(DataBaseUtils.getLeagueImage(buildObject.getAsJsonPrimitive("secondary_page_id").getAsString(), RUNE));
				
				//sRunes 02
				JsonArray secondaryRunesArray = buildObject.getAsJsonArray("secondary_rune_ids");
				for(JsonElement secondaryRune : secondaryRunesArray) 
					images.add(DataBaseUtils.getLeagueImage(secondaryRune.getAsString(), RUNE));
				
				//mRunes
				JsonArray statModifiersArray = buildObject.getAsJsonArray("stat_mod_ids");
				for(JsonElement statModifier : statModifiersArray)
					images.add(DataBaseUtils.getLeagueImage(statModifier.getAsString(), RUNE));
			}
		}
	}
	
	public void sendRunes(MessageChannel channel) throws IOException {
		File outputFile = new File(Main.gerryFolder + "/cover.png");
		ImageIO.write(new Draw().drawRunes(this.images), "png", outputFile);
		channel.sendFile(outputFile, "cover.png").queue();
	}
}