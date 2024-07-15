package gerrybot.league;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import gerrybot.core.Main;
import gerrybot.database.DataBaseTable;
import gerrybot.database.DataBaseUtils;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.FileUpload;

public class Runes extends League {
	
	private int[][] images;
	
	public Runes(String champion, String role) {
		super(champion, role, DataBaseTable.RUNE);

		getRunesIdsInArray();
	}
	
	private void getRunesIdsInArray() {
		JsonArray runePagesArray = this.getJson().getAsJsonObject("data").getAsJsonArray("rune_pages");
		int sizeRunePagesArray = (runePagesArray.size() >= 2) ? 2 : runePagesArray.size(); // It may be 0 or 1
		
		images = new int[sizeRunePagesArray*2][]; // In MOST cases
		
		int lines = 0;
		for(int i = 0; i < sizeRunePagesArray; i++) {
			JsonArray buildsArray = runePagesArray.get(i).getAsJsonObject().getAsJsonArray("builds");
			int sizeBuildsArray = (buildsArray.size() >= 2) ? 2 : buildsArray.size(); // It may be 0 or 1

			for(int j = 0, counter = 0; j < sizeBuildsArray; j++, lines++, counter = 0) {
				JsonObject buildObject = buildsArray.get(j).getAsJsonObject();
				
				images[lines] = new int[11];
				
				//Symbol rune 01
				images[lines][counter] = buildObject.getAsJsonPrimitive("primary_page_id").getAsInt();
				counter++;
				
				//pRune / sRunes 01
				JsonArray primaryRunesArray = buildObject.getAsJsonArray("primary_rune_ids");
				for(JsonElement primaryRune : primaryRunesArray) {
					images[lines][counter] = primaryRune.getAsInt();
					counter++;
				}
				
				//Symbol rune 02
				images[lines][counter] = buildObject.getAsJsonPrimitive("secondary_page_id").getAsInt();
				counter++;
				
				//sRunes 02
				JsonArray secondaryRunesArray = buildObject.getAsJsonArray("secondary_rune_ids");
				for(JsonElement secondaryRune : secondaryRunesArray) {
					images[lines][counter] = secondaryRune.getAsInt();
					counter++;
				}
				
				//mRunes
				JsonArray statModifiersArray = buildObject.getAsJsonArray("stat_mod_ids");
				for(JsonElement statModifier : statModifiersArray) {
					images[lines][counter] = statModifier.getAsInt();
					counter++;
				}
			}
		}
	}
	
	public void sendRunes(MessageChannel channel) throws IOException {
		File outputFile = new File(Main.gerryFolder + "/cover.png");
		BufferedImage runePage = new Draw().drawRunes(images, DataBaseUtils.getLeagueImages(images, DataBaseTable.RUNE));
		ImageIO.write(runePage, "png", outputFile);
		channel.sendFiles(FileUpload.fromData(outputFile, "cover.png")).queue();
	}
}