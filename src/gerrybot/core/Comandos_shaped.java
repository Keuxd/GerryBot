package gerrybot.core;

import java.time.LocalTime;

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
				LocalTime horario = java.time.LocalTime.now();
				if(!Main.isTesting) horario = horario.minusMinutes(3);
				String data = java.time.LocalDate.now().toString();
				channel.sendMessage(data + "\n" + horario.toString().substring(0,8)).queue();
				break;
			}
			
			case("!rhn"):{
				try {
					Hentai randomHentai = new NHentaiNet().genRandomHentai();
					randomHentai.sendEmbedHentai(channel).queue();
				} catch (Exception e) {}
				break;
			}
			
			case ("!henta"):{
				char[] data = java.time.LocalDate.now().toString().toCharArray();
				data[0] = '-'; data[1] = '-';
				data = String.valueOf(data).replace("-", "").toCharArray();
				String dataNum = new String();
					
				//a data esta em formato americano, esse looping coloca os numeros na string dataNum ao contrario
				for(int i = 4; i >= 0; i-=2) {
					if(i == 4 && Integer.parseInt(String.valueOf(data[i] + "" + data[i+1])) < 10) {
						dataNum += String.valueOf(data[i+1]);
						continue;
					}
					dataNum += String.valueOf(data[i] + "" + data[i+1]);
				}					
				
				try {
					Hentai dailyHenta = new NHentaiNet().genHentaiByNumber(dataNum);
					dailyHenta.sendEmbedHentai(channel, "Hentai do Dia").queue(message -> {
						message.addReaction("U+1F51E").queue();

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
					embed.setTitle("Gerry 1.7.1");
					embed.setColor(Main.cor);
					embed.setDescription(""
							+ "- !hentime _xx:xx_ criado(ADMIN ONLY)\n\n"
							+ "- Lista dos comandos na wiki atualizados.\n\n"
							+ "- !runa _champion_ _role_ arrumado.\n\n"
							+ "- !build _champion_ _role_ arrumado.\n\n"
							+ "- Dominio de busca dos comandos de hentai alterado(nHentai.to -> nHentai.net).\n\n"
							+ "- "
							);
					
				
				channel.sendMessageEmbeds(embed.build()).queue();
				break;
			}
//			case("!test"): {
//				SelectionMenu menu = SelectionMenu.create("menu:class")
//						.setPlaceholder("test1_0")
//						.setRequiredRange(1, 1)
//						.addOption("sonicfox", "furry")
//						.addOption("dekilsage", "furryFriend")
//						
//						.build();
//				
//				channel.sendMessage("_ _").setActionRow(menu).queue();
//				break;
//			}
//			case("!teste"): {				
//				Main.jda.getGuilds().forEach(guild -> {
//					System.out.println(guild.getName() + " -> " + guild.getId() + " -> " + guild.getIdLong());
//				});
//				break;
//			}
		}
		
	}
	

}





