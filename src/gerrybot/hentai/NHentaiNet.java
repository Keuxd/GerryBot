package gerrybot.hentai;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import gerrybot.league.OpggMetaBase;

public class NHentaiNet {

	private static final String NHENTAI_ZELDA = "https://nhentai.net/";
	private static final String NHENTAI_COVER = "https://t.nhentai.net/galleries/";
	
	public static Hentai createHentaiByNumber(String numbers) {
		return genHentaiFromJson(NHENTAI_ZELDA + "api/gallery/" + numbers);
	}
	
	public static Hentai createHentaiRandomly() throws Exception {
		return genHentaiFromHtml(NHENTAI_ZELDA + "random");
	}

	private static Hentai genHentaiFromHtml(String initLink) throws NullPointerException {
		Document hentaiPageHtml = loadDocument(initLink);
		if(hentaiPageHtml == null) {
			throw new NullPointerException("Error loading hentai from HTML");
		}
		
		Hentai nHentaiNet = new Hentai();
		
		nHentaiNet.setNumbers(hentaiPageHtml.select("h3#gallery_id").text().replace("#", ""));
		nHentaiNet.setLink("https://nhentai.net/g/" + nHentaiNet.getNumbers());
		
		String imageLink = hentaiPageHtml.select("img[src~=(?i)\\.(png|jpe?g|gif)]").first().attr("src");
		nHentaiNet.setCoverLink(imageLink);
		nHentaiNet.setImageFile(downloadImage(imageLink));
		
		nHentaiNet.setTitle(hentaiPageHtml.select("h1.title").select("span.pretty").text());
		
		Elements tags = hentaiPageHtml.select("div.tag-container.field-name").get(2).select("span.name");
		nHentaiNet.setTags(genConcatedTags(tags));
		
		return nHentaiNet;
	}
	
	private static Hentai genHentaiFromJson(String link) {
		JsonObject jo = OpggMetaBase.downloadJson(link);
		
		// If we couldn't download the henta then we return a not found one
		if(jo == null) {
			return genHentaiNotFound(link);
		}
		
		Hentai nHentaiNet = new Hentai();
		nHentaiNet.setNumbers(jo.get("id").getAsString());
		nHentaiNet.setLink("https://nhentai.net/g/" + nHentaiNet.getNumbers());

		String imageType = jo.get("images").getAsJsonObject().get("cover").getAsJsonObject().get("t").getAsString();
		
		String fileName = "/cover." + (imageType.equals("j") ? "jpg" : "png");
		String imageLink = NHENTAI_COVER + jo.get("media_id").getAsString() + fileName;
		nHentaiNet.setCoverLink(imageLink);
		nHentaiNet.setImageFile(downloadImage(imageLink));
		
		nHentaiNet.setTitle(jo.get("title").getAsJsonObject().get("pretty").getAsString());
		
		JsonArray tags = jo.get("tags").getAsJsonArray();
		nHentaiNet.setTags(genConcatedTags(tags));
		
		return nHentaiNet;
	}
	
	private static Hentai genHentaiNotFound(String link) {
		Hentai nHentaiNet = new Hentai();
		nHentaiNet.setNumbers(link.split("/")[5]);
		nHentaiNet.setLink(NHENTAI_ZELDA + "g/" + nHentaiNet.getNumbers());
		nHentaiNet.setCoverLink("https://cdn.discordapp.com/emojis/744921446136021062.png");
		nHentaiNet.setTitle("Not Found");
		nHentaiNet.setTags("Not Found");
		return nHentaiNet;
	}
	
	private static InputStream downloadImage(String imageURL) {
		try {
			URLConnection connection = new URL(imageURL).openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			return connection.getInputStream();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Document loadDocument(String link) {
		Document hentaiPageHtml = null;
		
		// while/if this page doesn't load continue trying loading it
		while(hentaiPageHtml == null) {
			try {
				hentaiPageHtml =  Jsoup.connect(link)
						.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
						.timeout(45*1000)
						.get();
				return hentaiPageHtml;
			} catch(SocketTimeoutException e) {
				continue;
			
			} catch(HttpStatusException e) {
				e.printStackTrace();
				return null;
				
			} catch(IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return hentaiPageHtml;
	}
	
	private static String genConcatedTags(Elements tags) {
		if(tags.size() == 0) return "";
		
		String hentaiTags = new String();
		
		for(Element tag : tags) {
			hentaiTags += tag.text().substring(0,1).toUpperCase() + tag.text().substring(1) + ", ";
		}
		
		return hentaiTags.substring(0, hentaiTags.length()-2) + ".";
	}
	
	private static String genConcatedTags(JsonArray array) {
		if(array.size() == 0) return "";
		
		StringBuilder sb = new StringBuilder();
		
		for(JsonElement element : array) {
			JsonObject object = element.getAsJsonObject();
			
			if(!object.get("type").getAsString().equals("tag")) {
				continue;
			}
			
			String tag = object.get("name").getAsString();
			sb.append(tag.substring(0,1).toUpperCase() + tag.substring(1) + ", ");
		}
		sb.setCharAt(sb.length() - 2, '.');
		
		return sb.toString();
	}
	
	// DEBUG ONLY
	public static void pingCheck(int IP) {
		try {
			Process process = Runtime.getRuntime().exec("ping " + NHENTAI_ZELDA);
			
			InputStream is = process.getInputStream();
			Scanner sc = new Scanner(is, "UTF-8");
			
			while(sc.hasNextLine()) {
				System.out.println(sc.nextLine());
			}
			sc.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}