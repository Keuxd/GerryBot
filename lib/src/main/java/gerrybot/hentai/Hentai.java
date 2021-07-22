package gerrybot.hentai;

import java.io.InputStream;

import gerrybot.core.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class Hentai {
	private String title;
	private String numbers;
	private String link;
	private String concatedTags;
	
	private String coverLink;
	private InputStream imageFile;
	
	public MessageAction sendEmbedHentai(MessageChannel channel) {
		return sendEmbedHentai(channel, "Hentai " + this.numbers);
	}
	
	public MessageAction sendEmbedHentai(MessageChannel channel, String title) {
		EmbedBuilder embedHentai = new EmbedBuilder()
				  		.setTitle(title)
				  		.setDescription(this.title)
				  		.addField("Link", this.link, false)
				  		.addField("Tags", this.concatedTags, false)
				  		.addField("Capa", "" , false)
						.setColor(Main.cor);
		
		if(this.imageFile != null) {
			embedHentai.setImage("attachment://cover.jpg");
			return channel.sendFile(this.imageFile, "cover.jpg").embed(embedHentai.build());
		}
		else {
			embedHentai.setImage(this.coverLink);
			return channel.sendMessage(embedHentai.build());
		}
	}
	
	protected String getCoverLink() {
		return coverLink;
	}

	protected void setCoverLink(String coverLink) {
		this.coverLink = coverLink;
	}

	protected String getTitle() {
		return title;
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	protected String getLink() {
		return link;
	}

	protected void setLink(String link) {
		this.link = link;
	}

	protected String getConcatedTags() {
		return this.concatedTags;
	}

	protected void setTags(String concatedTags) {
		this.concatedTags = concatedTags;
	}

	protected String getNumbers() {
		return numbers;
	}

	protected void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	protected InputStream getImageFile() {
		return imageFile;
	}

	protected void setImageFile(InputStream imageFile) {
		this.imageFile = imageFile;
	}

}
