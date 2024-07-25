package gerrybot.commands;

import java.time.LocalDateTime;

import gerrybot.core.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GenericCommands {
	
	public void yo(MessageReceivedEvent event) {
		event.getChannel().sendMessage("oporra").queue();
	}
	
	public void prime(MessageReceivedEvent event) {
		event.getChannel().sendMessage("https://image.prntscr.com/image/s6hylNE8R-SdPs3cmNoFgg.png").queue();
	}
	
	public void wendy(MessageReceivedEvent event) {
		MessageChannelUnion channel = event.getChannel();
		channel.sendMessage("OwO").queue();
		channel.sendMessage("https://image.prntscr.com/image/ZPrbxRXcQEmhZ0SRr0xbbA.png").queue();
	}
	
	public void harry(MessageReceivedEvent event) {
		MessageChannelUnion channel = event.getChannel();
		channel.sendMessage("mais um levelzinho tou forte").queue();
		channel.sendMessage("https://image.prntscr.com/image/D5xZaWYYRMS9vqyPArdeKA.png").queue();
	}
	
	public void montanha(MessageReceivedEvent event) {
		event.getChannel().sendMessage("https://image.prntscr.com/image/Ufpgd9vcRmycF2KxeA_bgg.png").queue();
	}
	
	public void naltan(MessageReceivedEvent event) {
		event.getChannel().sendMessage("**CRAAAACK**").queue();
	}
	
	public void blazblue(MessageReceivedEvent event) {
		event.getChannel().sendMessage("https://image.prntscr.com/image/4AiaUL81SkuYcdKCKQrKtA.gif").queue();
	}
	
	public void tempo(MessageReceivedEvent event) {
		event.getChannel().sendMessage(LocalDateTime.now().toString()).queue();
	}
	
	public void comandos(MessageReceivedEvent event) {
		event.getChannel().sendMessage("https://github.com/Keuxd/GerryBot/wiki").queue();
	}
	
	public void updates(MessageReceivedEvent event) {
		EmbedBuilder embed = new EmbedBuilder()
				.setTitle("Gerry 2.0")
				.setColor(Main.COLOR)
				.setDescription("```" 
							+ "- Gerry is back !\n"
							+ "- Added multi dice rolls/modifiers.\n"
							+ "- Dice rolls now support white spaces between dices/modifiers.\n"
							+ "- Hentas not found added.\n"
							+ "```");
		event.getChannel().sendMessageEmbeds(embed.build()).queue();
	}
}
