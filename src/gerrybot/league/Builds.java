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

	private BufferedImage[][] builds;
	
	public Builds(String champion, String role) {
		super(champion, role, DataBaseTable.ITEM);
		
		getItemsImagesInArray();
	}
	
	private void getItemsImagesInArray() {
		JsonObject buildsDataObject = this.getJson().getAsJsonObject("data");
		
		JsonArray starterItemsArray = buildsDataObject.getAsJsonArray("starter_items");
		int starterItemsRowsCount = (starterItemsArray.size() >= 2) ? 2 : starterItemsArray.size(); // It may be 0 or 1
		
		JsonArray coreItemsArray = buildsDataObject.getAsJsonArray("core_items");
		int coreItemsRowsCount = (coreItemsArray.size() >= 5) ? 5 : coreItemsArray.size(); // It may be 0 - 4
		
		JsonArray bootsArray = buildsDataObject.getAsJsonArray("boots");
		int bootsRowsCount = (bootsArray.size() >= 3) ? 3 : bootsArray.size(); // It may be 0 - 2

		builds = new BufferedImage[starterItemsRowsCount + coreItemsRowsCount + bootsRowsCount][];
		
		int buildsIndex = 0;	
		
		for(int i = 0; i < starterItemsRowsCount; i++, buildsIndex++)
			builds[buildsIndex] = getImagesInRow(starterItemsArray.get(i).getAsJsonObject().getAsJsonArray("ids"));
		
		for(int i = 0; i < coreItemsRowsCount; i++, buildsIndex++)
			builds[buildsIndex] = getImagesInRow(coreItemsArray.get(i).getAsJsonObject().getAsJsonArray("ids"));
		
		for(int i = 0; i < bootsRowsCount; i++, buildsIndex++)
			builds[buildsIndex] = getImagesInRow(bootsArray.get(i).getAsJsonObject().getAsJsonArray("ids"));
	}
	
	private BufferedImage[] getImagesInRow(JsonArray rowArray) {
		BufferedImage[] rowImages = new BufferedImage[rowArray.size()];
		
		for(int i = 0; i < rowArray.size(); i++) {
			String imageId = rowArray.get(i).getAsJsonPrimitive().getAsString();
			rowImages[i] = DataBaseUtils.getLeagueImage(imageId, DataBaseTable.ITEM);			
		}
		
		return rowImages;
	}
	
	public void sendBuilds(MessageChannel channel) throws IOException {
		File outputFile = new File(Main.gerryFolder + "/cover.png");
		ImageIO.write(new Draw().drawBuilds(builds), "png", outputFile);
		channel.sendMessage(this.getSkillOrder()).addFile(outputFile, "cover.png").queue();
	}
}
