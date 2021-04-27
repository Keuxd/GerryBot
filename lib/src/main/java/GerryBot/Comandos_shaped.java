package GerryBot;


import java.net.SocketTimeoutException;

import org.jsoup.HttpStatusException;

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
				String horario = java.time.LocalTime.now().toString();
				String data = java.time.LocalDate.now().toString();
				channel.sendMessage(data + "\n" + horario.substring(0,8)).queue();
				break;
			}
			
			case("!rhn"):{
				Hentai hentai = null;
				int i = 1;
				while(hentai == null) {					
					try {
						hentai = Hentai.nHentai(String.valueOf(new Dices(1,999999).getDados()[0]));
					}catch (Exception e) {}
					i++;
				}
				
				PV.updateHentaState("Try -> " + i);
				hentai.sendEmbedHentai(channel).queue();
				break;
			}

			
			
			case ("!henta"):{
				try {
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
					
					Hentai hentai = Hentai.nHentai(dataNum);
					hentai.sendEmbedHentai(channel, "Hentai do Dia").queue(message -> message.addReaction("U+1F51E").queue());
				}
				catch (SocketTimeoutException e) {
					channel.sendMessage("Erro no carregamento, tente novamente mais tarde.").queue();
					channel.sendMessage("https://cdn.discordapp.com/emojis/753301782288924702.png").queue();
				}
				catch (HttpStatusException e) {
					channel.sendMessage("O dia de hoje nao tem hentai").queue();
					channel.sendMessage("https://cdn.discordapp.com/emojis/744921446136021062.png").queue();
				}
				catch (Exception e) {
					//e.printStackTrace();
					System.out.println("Comandos_shaped -> !henta " + e);
					channel.sendMessage("**DEBUG**\n(Informe os moderadores)").queue();
				}
				break;
			}
			
			case("!comandos"):{
				EmbedBuilder embed = new EmbedBuilder();
					embed.setTitle("Comandos");
					embed.setColor(Main.cor);
			
					embed.addField("Dados", "Digite diretamente o dado que quer rodar. Ex: 2d10+1 (limite de 99 dados)", false);
					embed.addField("!hn", "Digite !hn XXXXXX e receba seu nHentao!", false);
					embed.addField("!rhn", "Digite !rhn e receba um nHentao random !", false);
					embed.addField("!cm", "Digite !cm (alturaEmCentimetros) e receba quantos cm aguentaria. **(Formula Feminina)**", false);
					embed.addField("!yo","Comando Legado", false);
					embed.addField("!prime", "So 9,90",false);
					embed.addField("!blazblue","_bobleis_", false);
					embed.addField("!tempo","Retorna o horario/data do host(dev only)",false);
					embed.addField("wendy","OwO",false);
					embed.addField("harry","_confia_",false);
					embed.addField("montanha","nha :3",false);
					embed.addField("naltan","ovO maTa U reRi",false);
					
				channel.sendMessage(embed.build()).queue();
				break;
			}
			
			case("!updates"):{
				EmbedBuilder embed = new EmbedBuilder();
					embed.setTitle("Gerry 1.6.2");
					embed.setColor(Main.cor);
					embed.setDescription(""
							+ "- Adicionado lista de comandos(!comandos).\n\n"
							+ "- Adicionado botao de reacao substituindo !cursed em 'henta'.\n\n"
							+ "- Algoritmo de aleatoriedade de dados melhorado, suporte a modificadores adicionado.\n\n"
							+ "- Corrigido bug onde links apareciam no meio do titulo do !henta/!hn.\n\n"
							+ "- Corrigido bug de mal funcionamento do botao cursed em 'henta'.\n\n"
							+ "- Corrigido bug onde a capa de alguns hentais nao carregava.\n\n"
							+ "- !cm, !hn/!rhn e !blazblue adicionados.\n\n"
							+ "- !zoe, !dl e !desgraca removidos."
							
							);
					
					channel.sendMessage(embed.build()).queue();
				break;
			}
			
		}
		
	}
}





