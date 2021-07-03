/*	TODO
- Implement nHentai.net; i guess
- Change all system to a HentaiFactory
*/
package gerrybot.core;

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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public final class Hentai {
	private String imageLink;
	private String title;
	private String link;
	private String[] tags;
	private String numbers;
	
	private InputStream imageFile;
	private final String fileDir = "cover.jpg";
	
	private Document hentaiPageHTML;
	
	public Hentai() {}
	
	public Hentai(String numbers) throws Exception {
		loadNHentaiTo("https://nhentai.to/g/" + numbers);
	}
	
	protected String concatTags() {
		if(this.tags.length == 0) return "";
		
		String hentaiTags = new String();
		
		int sizeArray = this.tags.length-1;
		
		for(int i = 0; i < sizeArray; i++)
			hentaiTags += this.tags[i].substring(0,1).toUpperCase() + this.tags[i].substring(1) + ", ";
		
		return hentaiTags += this.tags[sizeArray].substring(0,1).toUpperCase() + this.tags[sizeArray].substring(1) + ".";
	}
	
	protected MessageAction sendEmbedHentai(MessageChannel channel, String title) {
		EmbedBuilder hentao = new EmbedBuilder()
				  		.setTitle(title)
				  		.setDescription(this.title)
				  		.addField("Link", this.link, false)
				  		.addField("Tags", this.concatTags(), false)
				  		.addField("Capa", "" , false)
						.setColor(Main.cor);
		
		if(this.imageFile != null) {
			hentao.setImage("attachment://cover.jpg");
			return channel.sendFile(this.imageFile, this.fileDir).embed(hentao.build());
		}
		else {
			hentao.setImage(this.imageLink);
			return channel.sendMessage(hentao.build());
		}
	}
	
	private void loadNHentaiTo(String link) throws Exception {		
		if(!validateLink(link))
			throw new Exception("Invalid hentai");

		this.setLink(this.hentaiPageHTML.baseUri());
		this.setNumbers(this.link.substring(21));
		
		Elements imagens = this.hentaiPageHTML.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
		this.setImagem(imagens.get(1).attr("src"));
			
		Element titulo = this.hentaiPageHTML.getElementById("info");
		Elements tituloS = titulo.getElementsByTag("h1");
		String tituloString = tituloS.toString().replace("<h1>", "").replace("</h1>", "");
			//everything that is between '<a ref=' and '</a>' is deleted
			tituloString = tituloString.replaceAll("(.*?"+"<a href=" + ")" + "(.*?)" + "(" + "</a>" + ".*)", "$1$3");
			tituloString = tituloString.replace("<a href=", "").replace("</a>", "");
		this.setTitle(tituloString);
				
		Element tags = this.hentaiPageHTML.getElementById("tags");
		Elements tagsS = tags.getElementsByClass("tags").select("a[href]"); 
			
		//clona a lista de elementos tagsS para que seja possivel o "for" remover os elementos que nao sao tags
		Elements tagsClone = tagsS.clone();
		int index = 0;
		for(Element tag : tagsS) {
			if(!tag.toString().contains("<a href=\"/tag/")) {
				tagsClone.remove(index);
				index--; //aqui voltamos um no ponteiro(index) pois ao .remove() todos os elementos posteriores voltam 1 casa
			}
			index++;
		}
			
		//converte o ".text()" de um tipo Elements em um vetor de Strings.
		int counter = 0;			
		String[] bbTags = new String[index];
		for(Element tag : tagsClone) {
				bbTags[counter] = tag.text().toString();
				counter++;
		}
		counter = 0;
		this.setTags(bbTags);
	}
	
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
	
	protected void randomHentai() throws Exception {
		loadNHentaiTo("https://nhentai.to/random/");
	}
	
	protected MessageAction sendEmbedHentai(MessageChannel channel) {
		return sendEmbedHentai(channel, "Hentai " + this.numbers);
	}
	
	protected InputStream getImageInFile() {
		return this.imageFile;
	}
	
	
	protected String getImagem() {
		return imageLink;
	}

	protected void setImagem(String imagem) {
		this.imageLink = imagem;
		
		try {
			URLConnection connection = new URL(imagem).openConnection();
					connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			this.imageFile = connection.getInputStream();
			PV.updateHentaState("Capa Baixada -> " + this.numbers);
		} catch(Exception e) {
			e.printStackTrace();
			this.imageFile = null;
			PV.updateHentaState("Erro no Download da Capa -> " + this.numbers + "\n\n" + e);
		}
		
	}

	protected String getTitle() {
		return title;
	}

	protected void setTitle(String titulo) {
		this.title = titulo;
	}

	protected String getLink() {
		return link;
	}

	protected void setLink(String link) {
		this.link = link;
	}

	protected String[] getTags() {
		return tags;
	}

	protected void setTags(String[] tags) {
		this.tags = tags;
	}

	protected String getNumbers() {
		return numbers;
	}

	protected void setNumbers(String numbers) {
		this.numbers = numbers;
	}
}
