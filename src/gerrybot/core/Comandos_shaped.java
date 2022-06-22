package gerrybot.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import gerrybot.hentai.Hentai;
import gerrybot.hentai.NHentaiNet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Comandos_shaped extends ListenerAdapter {
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		MessageChannel channel = event.getChannel();

		switch (args[0].toLowerCase()) {
			case ("!yo") :{
				channel.sendMessage("oporra").queue();
				break;
			}
			
			case ("!prime"): {
				channel.sendMessage("https://image.prntscr.com/image/s6hylNE8R-SdPs3cmNoFgg.png").queue();
				break;
			}
		
			case ("wendy"):{
				channel.sendMessage("OwO").queue();
				channel.sendMessage("https://image.prntscr.com/image/ZPrbxRXcQEmhZ0SRr0xbbA.png").queue();
				break;
			}
			
			case ("harry"):{
				channel.sendMessage("mais um levelzinho tou forte").queue();
				channel.sendMessage("https://image.prntscr.com/image/D5xZaWYYRMS9vqyPArdeKA.png").queue();
				break;
			}
			
			case ("montanha"):{
				channel.sendMessage("https://image.prntscr.com/image/Ufpgd9vcRmycF2KxeA_bgg.png").queue();
				break;
			}
			
			case ("naltan"):{
				channel.sendMessage("**CRAAAACK**").queue();
				break;
			}
			
			case ("!blazblue"):{
				channel.sendMessage("https://image.prntscr.com/image/4AiaUL81SkuYcdKCKQrKtA.gif").queue();
				break;
			}
			
			case("!tempo"):{
				LocalDateTime ldt = (Main.IS_TESTING) ? LocalDateTime.now() : LocalDateTime.now().minusHours(3);
				channel.sendMessage(ldt.toString()).queue();
				break;
			}
			
			case("!rhn"):{
				try {
					Hentai randomHentai = NHentaiNet.createHentaiRandomly();
					randomHentai.sendEmbedHentai(channel).queue(message -> {
						message.addReaction("U+2B50").queue();
					});
				} catch (Exception e) {}
				break;
			}
			
			case ("!henta"):{
				try {
					LocalDateTime ldt = (Main.IS_TESTING) ? LocalDateTime.now() : LocalDateTime.now().minusHours(3);
					String dataNum = ldt.getDayOfMonth() + ldt.format(DateTimeFormatter.ofPattern("MMyy"));
					
					Hentai dailyHenta = NHentaiNet.createHentaiByNumber(dataNum);
					dailyHenta.sendEmbedHentai(channel, "Hentai do Dia").queue(message -> {
						message.addReaction("U+1F51E").queue();
						message.addReaction("U+2B50").queue();
					});
				} catch (Exception e) {
					channel.sendMessage("O dia de hoje não tem hentai.").queue();
					channel.sendMessage("https://image.prntscr.com/image/0rMqADlyTRGyAJ-q36RyXw.png").queue();
				}
				break;
			}
			
			case("!comandos"):{
				channel.sendMessage("https://github.com/Keuxd/GerryBot/wiki").queue();
				break;
			}
			
			case("!updates"):{
				EmbedBuilder embed = new EmbedBuilder();
					embed.setTitle("Gerry 1.8.3");
					embed.setColor(Main.COLOR);
					embed.setDescription(""
							+ "- Fixed Wukong runes/builds.\n\n"
							+ "- Henta commands fixed(thanks sinkaroid).\n\n"
							+ "- Fixed league commands for aram.\n\n"
							);
				channel.sendMessageEmbeds(embed.build()).queue();
				break;
			}
		}	
	}
}