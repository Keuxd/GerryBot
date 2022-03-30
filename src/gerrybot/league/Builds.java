package gerrybot.league;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import gerrybot.core.Main;
import gerrybot.database.DataBaseTable;
import gerrybot.database.DataBaseUtils;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Builds extends League {

	private int[][] builds;
	
	public Builds(String champion, String role) {
		super(champion, role, DataBaseTable.ITEM);
		
		getItemsIdsInArray();
	}
	
	private void getItemsIdsInArray() {
		JsonObject buildsDataObject = this.getJson().getAsJsonObject("data");
		
		JsonArray starterItemsArray = buildsDataObject.getAsJsonArray("starter_items");
		int starterItemsRowsCount = (starterItemsArray.size() >= 2) ? 2 : starterItemsArray.size(); // It may be 0 or 1
		
		JsonArray coreItemsArray = buildsDataObject.getAsJsonArray("core_items");
		int coreItemsRowsCount = (coreItemsArray.size() >= 5) ? 5 : coreItemsArray.size(); // It may be 0 - 4
		
		JsonArray bootsArray = buildsDataObject.getAsJsonArray("boots");
		int bootsRowsCount = (bootsArray.size() >= 3) ? 3 : bootsArray.size(); // It may be 0 - 2

		builds = new int[starterItemsRowsCount + coreItemsRowsCount + bootsRowsCount][];
		
		int buildsIndex = 0;	
		
		for(int i = 0; i < starterItemsRowsCount; i++, buildsIndex++)
			builds[buildsIndex] = getIdsInRow(starterItemsArray.get(i).getAsJsonObject().getAsJsonArray("ids"));
		
		for(int i = 0; i < coreItemsRowsCount; i++, buildsIndex++)
			builds[buildsIndex] = getIdsInRow(coreItemsArray.get(i).getAsJsonObject().getAsJsonArray("ids"));
		
		for(int i = 0; i < bootsRowsCount; i++, buildsIndex++)
			builds[buildsIndex] = getIdsInRow(bootsArray.get(i).getAsJsonObject().getAsJsonArray("ids"));
	}
	
	private int[] getIdsInRow(JsonArray rowArray) {
		int[] rowImages = new int[rowArray.size()];
		
		for(int i = 0; i < rowArray.size(); i++) {
			rowImages[i] = rowArray.get(i).getAsJsonPrimitive().getAsInt();		
		}
		
		return rowImages;
	}
	
	public void sendBuilds(MessageChannel channel) throws IOException {
		File outputFile = new File(Main.gerryFolder + "/cover.png");
		BufferedImage buildsPage = new Draw().drawBuilds(builds, DataBaseUtils.getLeagueImages(builds, DataBaseTable.ITEM));
		ImageIO.write(buildsPage, "png", outputFile);
		channel.sendMessage(this.getSkillOrder()).addFile(outputFile, "cover.png").queue();
	}
}