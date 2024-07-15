package gerrybot.hentai;

import java.io.InputStream;

import gerrybot.core.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;

public class Hentai {
	private String title;
	private String numbers;
	private String link;
	private String concatedTags;
	
	private String coverLink;
	private InputStream imageFile;
	
	public MessageCreateAction sendEmbedHentai(MessageChannel channel, String... embedTitle) {
		//if there's title so use title, if there isn't use generic title with this.numbers
		embedTitle = (embedTitle.length == 1) ? embedTitle : new String[]{"Hentai " + this.numbers};
		
		MessageEmbed embedHentai = genEmbedMessageHentai(embedTitle[0]);

		if(this.imageFile != null)
			return channel.sendFiles(FileUpload.fromData(this.imageFile, "cover.jpg")).setEmbeds(embedHentai);
		else
			return channel.sendMessageEmbeds(embedHentai);
	}
	
	public ReplyCallbackAction sendEmbedHentai(SlashCommandInteractionEvent event, String... embedTitle) {
		//if there's title so use title, if there isn't use generic title with this.numbers
		embedTitle = (embedTitle.length == 1) ? embedTitle : new String[]{"Hentai " + this.numbers};
		
		if(this.imageFile != null)
			return event.replyEmbeds(genEmbedMessageHentai(embedTitle[0])).addFiles(FileUpload.fromData(this.imageFile, "cover.jpg"));
		else
			return event.replyEmbeds(genEmbedMessageHentai(embedTitle[0]));
	}
	
	public MessageEmbed genEmbedMessageHentai(String title) {
		EmbedBuilder embedHentai = new EmbedBuilder()
		  		.setTitle(title)
		  		.setDescription(this.title)
		  		.addField("Link", this.link, false)
		  		.addField("Tags", this.concatedTags, false)
		  		.addField("Capa", "" , false)
				.setColor(Main.COLOR)
				.setImage((this.imageFile != null) ? "attachment://cover.jpg" : this.coverLink);
		
		return embedHentai.build();
	}
	
	public String getCoverLink() {
		return coverLink;
	}

	public void setCoverLink(String coverLink) {
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

	public String getNumbers() {
		return numbers;
	}

	protected void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	public InputStream getImageFile() {
		return imageFile;
	}

	public void setImageFile(InputStream imageFile) {
		this.imageFile = imageFile;
	}

}
