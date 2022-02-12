package gerrybot.league;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.h2.util.IOUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gerrybot.database.DataBaseTable;
import gerrybot.database.DataBaseUtils;

public class OpggMetaBase {
	
	public static void populateBuildsTable() {
		JsonArray jso = downloadJson("https://lol-api-champion.op.gg/api/meta/items").getAsJsonArray("data");
		System.out.println("Starting to download '" + jso.size() + "' items:");
		
		double eachPercentage = 100.0 / jso.size();
		double completedPercentage = 0.0;
		
		for(JsonElement itemElement : jso) {
			JsonObject itemObject = itemElement.getAsJsonObject();

			downloadItem(itemObject.getAsJsonPrimitive("id").getAsString(), itemObject.getAsJsonPrimitive("image_url").getAsString(), 50, DataBaseTable.ITEM);

			completedPercentage += eachPercentage;
			System.out.printf("%.2f %%\n", completedPercentage);
		}

		System.out.println("All items were downloaded.");
	}
	
	public static void populateRunesTable() {
		JsonArray jsaRunes = downloadJson("https://lol-api-champion.op.gg/api/meta/runes").getAsJsonArray("data");
		JsonArray jsaRuneMarks = downloadJson("https://lol-api-champion.op.gg/api/meta/rune-pages").getAsJsonArray("data");
		JsonArray jsaStatModifiers = downloadJson("https://lol-api-champion.op.gg/api/meta/stat-mods").getAsJsonArray("data");
		System.out.println("Starting to download '" + (jsaRunes.size() + jsaRuneMarks.size() + jsaStatModifiers.size()) + "' runes:");
		
		double eachPercentage = 100.0 / (jsaRunes.size() + jsaRuneMarks.size() + jsaStatModifiers.size());
		double completedPercentage = 0.0;
		
		for(JsonElement runeElement : jsaRunes) {
			JsonObject runeObject = runeElement.getAsJsonObject();
			int size = (runeObject.getAsJsonPrimitive("slot_sequence").getAsInt() == 0) ? 50 : 30;
			
			downloadItem(runeObject.getAsJsonPrimitive("id").getAsString(), runeObject.getAsJsonPrimitive("image_url").getAsString(), size, DataBaseTable.RUNE);
			
			completedPercentage += eachPercentage;
			System.out.printf("%.2f %%\n", completedPercentage);			
		}
		
		for(JsonElement runeMarkElement : jsaRuneMarks) {
			JsonObject runeMarkObject = runeMarkElement.getAsJsonObject();
			
			downloadItem(runeMarkObject.getAsJsonPrimitive("id").getAsString(), runeMarkObject.getAsJsonPrimitive("image_url").getAsString(), 25, DataBaseTable.RUNE);

			completedPercentage += eachPercentage;
			System.out.printf("%.2f %%\n", completedPercentage);
		}
		
		for(JsonElement statModifierElement : jsaStatModifiers) {
			JsonObject statModifierObject = statModifierElement.getAsJsonObject();
			
			downloadItem(statModifierObject.getAsJsonPrimitive("id").getAsString(), statModifierObject.getAsJsonPrimitive("image_url").getAsString(), 20, DataBaseTable.RUNE);
			
			completedPercentage += eachPercentage;
			System.out.printf("%.2f %%\n", completedPercentage);
		}
		
		System.out.println("All runes were downloaded.");
	}
	
	private static void downloadItem(String itemID, String itemSrcLink, int size, DataBaseTable type) {		
		try {
			URLConnection connection = new URL(itemSrcLink.concat("?image=w_" + size)).openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			
			byte[] info = IOUtils.readBytesAndClose(connection.getInputStream(), -1);
			DataBaseUtils.insertLeagueImage(itemID, info, type);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static JsonObject downloadJson(String url) {
		try {
			URLConnection con = new URL(url).openConnection();
			con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			
			InputStream inputStream = con.getInputStream();
			
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			
			for(int length; (length = inputStream.read(buffer)) != -1;)
				result.write(buffer, 0, length);
			
			return JsonParser.parseString(result.toString()).getAsJsonObject();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}