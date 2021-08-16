package gerrybot.core;

import java.time.LocalDateTime;

import gerrybot.hentai.Hentai;
import gerrybot.hentai.NHentaiNet;
import gerrybot.league.League;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Comandos extends ListenerAdapter {
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		MessageChannel channel = event.getChannel();
		User autor = event.getAuthor();
		
		
		//dice roller -> at least lentgh 3 | first char be a number | last char be a number | the "middle" char must be 'd'
		if((args[0].length() >= 3 && String.valueOf(args[0].charAt(0)).matches("[0-9]*") && String.valueOf(args[0].charAt(args[0].length()-1)).matches("[0-9]*") &&  (args[0].charAt(1) == 'd' || args[0].charAt(2) == 'd'))) {
			try {
				String[] argumentos = args[0].split("d");
				String[] modificadores = null;
				
				if(argumentos[1].contains("+")) {
					modificadores = argumentos[1].split("\\+");
					modificadores[1] = "+" + modificadores[1];
				} 
				else if(argumentos[1].contains("-")) {
					modificadores = argumentos[1].split("-");
					modificadores[1] = "-" + modificadores[1];
				}
				else {
					modificadores = argumentos.clone();
					modificadores[0] = argumentos[1];
					modificadores[1] = null;
				}
				
				Dices dados = new Dices(Integer.parseInt(argumentos[0]),Integer.parseInt(modificadores[0]), modificadores[1]);
					dados.rodarDados();
					
				EmbedBuilder embed = new EmbedBuilder();
					embed.setColor(Main.cor);
					embed.setTitle(String.valueOf(dados.getTotal()));
					if(dados.modificador == null) {
						embed.setFooter(dados.getConcatDados(), autor.getAvatarUrl());
					}
					else {
						embed.setFooter(dados.getConcatDados() + " " + dados.modificador, autor.getAvatarUrl());
					}
					
				channel.sendMessageEmbeds(embed.build()).queue();
			}
			catch(Exception e) {
				e.printStackTrace();
				System.out.print("Erro Formato");
			}
		}
		
		//hentai numbers
		if(args.length == 2 && args[0].equals("!hn")) {
			try {
				Hentai hentai = new NHentaiNet().genHentaiByNumber(args[1]);
				hentai.sendEmbedHentai(channel).queue();
			} catch (Exception e) {
				channel.sendMessage("Esses numeros nao levam a nenhum nHentai.").queue();
				channel.sendMessage("https://cdn.discordapp.com/emojis/744921446136021062.png").queue();
			}
		}
		
		if(args[0].equals("!cm") && args.length == 2) {
			double cm = sizeD(Integer.parseInt(args[1]));
			channel.sendMessage("Essa personagem aguenta ate " + String.format("%.1f", cm) + "cm.").queue();
		}
		
		if(args.length == 3 && args[0].equals("!runa")) {
			try {				
				League champion = new League(args[1], args[2]);
				champion.loadRunes();
				champion.sendRunes(channel);
				
			} catch (Exception e) {
				channel.sendMessage("Campeao invalido.").queue();
				e.printStackTrace();
			}
		} else if(args.length == 3 && args[0].equals("!build")) {
			try {
				League champion = new League(args[1], args[2]);
				champion.loadBuilds();
				champion.sendBuilds(channel);
			} catch (Exception e) {
				channel.sendMessage("Campeao invalido").queue();
				e.printStackTrace();
			}
		}

	}
	
	static int minutesFormat() {
		LocalDateTime now = LocalDateTime.now();
		if(!Main.isTesting) now = now.minusHours(3);

		return (now.getHour() * 60) + now.getMinute();
	}
	
	static int minutosRestantes(int minutosAtual, int minutosAlvo) {
		if(minutosAtual == minutosAlvo) return 1440;
		
		int contador = 0;
		
		while(true) {
			minutosAtual++;
			contador++;
			
			if(minutosAtual == 1440) {
				minutosAtual = 0;
			}
			
			if(minutosAtual == minutosAlvo) {
				break;
			}
		}
		
		return contador;
	}

	static double sizeD(int heightCentimeters) {
		double size;
		size = (heightCentimeters * 100) / 161;
		size = (size / 100) * 8; 
		size *= 1.5625;
		
		return size + 1;
	}
}
