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
		if(!validateLink(initLink))
			throw new Exception("Invalid hentai");
		Hentai nHentaiNet = new Hentai();
		
		String link = this.hentaiPageHTML.baseUri();
		nHentaiNet.setLink(link);
		nHentaiNet.setNumbers(link.substring(22).replace("/", ""));
		
		String imageLink = this.hentaiPageHTML.select("img[src~=(?i)\\.(png|jpe?g|gif)]").first().attr("src");
		nHentaiNet.setCoverLink(imageLink);
		nHentaiNet.setImageFile(downloadCover(imageLink));
		
		nHentaiNet.setTitle(this.hentaiPageHTML.select("h1.title").select("span.pretty").text());
		
		Elements tags = this.hentaiPageHTML.select("div.tag-container.field-name").get(2).select("span.name");
		nHentaiNet.setTags(genConcatedTags(tags));
		
		return nHentaiNet;
	}
	
	private InputStream downloadCover(String coverURL) {
		try {
			URLConnection connection = new URL(coverURL).openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			return connection.getInputStream();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//if the link is valid returns true and generate the Document object for this.hentaiPageHTML
	private boolean validateLink(String link) {
		// while/if this page doesnt load continue trying loading it
		while(this.hentaiPageHTML == null) {
			try {
				this.hentaiPageHTML = Jsoup.connect(link).timeout(45*1000).get();
				return true;
				
			} catch(SocketTimeoutException e) {
				continue;
			
			} catch(HttpStatusException e) {
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
		
		return hentaiTags.substring(0,hentaiTags.length()-2) + ".";
	}
}