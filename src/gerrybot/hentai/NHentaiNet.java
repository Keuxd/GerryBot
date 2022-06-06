package gerrybot.hentai;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NHentaiNet {
	private Document hentaiPageHTML;
	
	public Hentai genHentaiByNumber(String numbers) throws Exception {
		return genHentai("https://nhentai.net/g/" + numbers);
	}
	
	public Hentai genRandomHentai() throws Exception {
		return genHentai("https://nhentai.net/random/");
	}

	private Hentai genHentai(String initLink) throws Exception {
		if(!loadLink(initLink))
			throw new Exception("Invalid hentai");
		Hentai nHentaiNet = new Hentai();
		
		String link = this.hentaiPageHTML.baseUri();
		nHentaiNet.setLink(link);
		nHentaiNet.setNumbers(link.substring(22, link.length()-1));
		
		String imageLink = this.hentaiPageHTML.select("img[src~=(?i)\\.(png|jpe?g|gif)]").first().attr("src");
		nHentaiNet.setCoverLink(imageLink);
		nHentaiNet.setImageFile(downloadImage(imageLink));
		
		nHentaiNet.setTitle(this.hentaiPageHTML.select("h1.title").select("span.pretty").text());
		
		Elements tags = this.hentaiPageHTML.select("div.tag-container.field-name").get(2).select("span.name");
		nHentaiNet.setTags(genConcatedTags(tags));
		
		return nHentaiNet;
	}
	
	private InputStream downloadImage(String imageURL) {
		try {
			URLConnection connection = new URL(imageURL).openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			return connection.getInputStream();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// it tries to load the 'Document' for this.hentaiPageHTML, if it can returns true, if it doesn't returns false
	private boolean loadLink(String link) {
		// while/if this page doesnt load continue trying loading it
		while(this.hentaiPageHTML == null) {
			try {
				this.hentaiPageHTML = Jsoup.connect(link).timeout(45*1000).get();
				return true;
				
			} catch(SocketTimeoutException e) {
				continue;
			
			} catch(HttpStatusException e) {
				e.printStackTrace();
				return false;
				
			} catch(IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	protected String genConcatedTags(Elements tags) {
		if(tags.size() == 0) return "";
		
		String hentaiTags = new String();
		
		for(Element tag : tags) {
			hentaiTags += tag.text().substring(0,1).toUpperCase() + tag.text().substring(1) + ", ";
		}
		
		return hentaiTags.substring(0, hentaiTags.length()-2) + ".";
	}
}