package gerrybot.database;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class DataBaseModel {
	
	// If all tables were created then returns true, if they don't means it already exists so return false.
	// There is no need to check them individually, because if it created one then 99% chance is it created the others.
	// If the first one gives us an exception then probably the others already exists too, so just return false.
	public static boolean createTables() throws SQLException {
		try {
			JDBC.state.executeUpdate("CREATE TABLE BUILDS(ID INT NOT NULL PRIMARY KEY, BASE64 VARCHAR(10000) NOT NULL)");
			JDBC.state.executeUpdate("CREATE TABLE RUNES(ID INT NOT NULL PRIMARY KEY, BASE64 VARCHAR(10000) NOT NULL)");
			JDBC.state.executeUpdate("CREATE TABLE HENTA_TIMERS(GUILD_ID LONG NOT NULL PRIMARY KEY, MINUTES INT NOT NULL)");
			JDBC.state.executeUpdate("CREATE TABLE HENTA_FAVORITES(USER_ID LONG NOT NULL, HENTA_CODE INT NOT NULL)");
			System.out.println("All tables were created, since they're all new, builds and runes will be now downloaded.");
			populateBuildsTable();
			return true;
		} catch(Exception e) {
			System.out.println("Tables were already created");
			e.printStackTrace();
			return false;
		}
	}
	
	public static void populateBuildsTable() throws JsonSyntaxException, MalformedURLException, IOException {
		JsonArray jso = getJson("https://lol-api-champion.op.gg/api/meta/items").getAsJsonArray("data");
		System.out.print("Starting Items Download: ");
		
		double completedPorcentage = 100.0 / jso.size();
		double eachPorcentage = completedPorcentage;
		
		for(int i = 0; i < jso.size(); i++, completedPorcentage += eachPorcentage) {
			String itemSrcLink = jso.get(i).getAsJsonObject().getAsJsonPrimitive("image_url").getAsString();
			downloadItem(itemSrcLink.split("/")[6].substring(0,4), itemSrcLink);
			System.out.printf("%.2f %%\n",completedPorcentage);
		}
			
		System.out.println("All items were downloaded");
	}
	
	private static void downloadItem(String itemID, String itemSrcLink) {
		BufferedImage itemImage = DataBaseUtils.getLeagueImage(itemID, DataBaseTable.ITEM);
		
		if(itemImage == null) {
			try {
				URLConnection connection = new URL(itemSrcLink.concat("?image=w_50,h_50")).openConnection();
				connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
				itemImage = ImageIO.read(connection.getInputStream());
				
				DataBaseUtils.insertLeagueImage(itemID, itemImage, DataBaseTable.ITEM);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static JsonObject getJson(String url) throws MalformedURLException, IOException, JsonSyntaxException {
		URLConnection con = new URL(url).openConnection();
		con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
		
		InputStream inputStream = con.getInputStream();
		
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		
		for(int length; (length = inputStream.read(buffer)) != -1;)
			result.write(buffer, 0, length);

		return JsonParser.parseString(result.toString()).getAsJsonObject();
	}
}
