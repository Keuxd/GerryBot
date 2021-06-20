package GerryBot;

import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.HttpStatusException;

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
					
					
				channel.sendMessage(embed.build()).queue();
			}
			catch(Exception e) {
				e.printStackTrace();
				System.out.print("Erro Formato");
			}
		}
		
		//hentai numbers
		if(args.length == 2 && args[0].equals("!hn")) {
			try {
				Hentai hentai = Hentai.nHentai(args[1]);
				
				hentai.sendEmbedHentai(channel).queue();
			}
			catch(SocketTimeoutException e){
				event.getChannel().sendMessage("Erro no carregamento, tente novamente mais tarde.").queue();
				event.getChannel().sendMessage("https://cdn.discordapp.com/emojis/753301782288924702.png").queue();
			}
			catch (HttpStatusException e) {
				event.getChannel().sendMessage("Esses numeros nao levam a nenhum hentai.").queue();
				event.getChannel().sendMessage("https://cdn.discordapp.com/emojis/744921446136021062.png").queue();
			}
			catch (Exception e) {
				System.out.println("Comandos_shaped -> !henta " + e);
				event.getChannel().sendMessage("**DEBUG**\n(Informe os moderadores)").queue();
			}
		}
		
		if(args[0].equals("!cm") && args.length == 2) {
			double cm = sizeD(Integer.parseInt(args[1]));
			channel.sendMessage("Essa personagem aguenta ate " + String.format("%.1f", cm) + "cm.").queue();
		}

	}
	
	static int newMinutesFormat() {
		LocalDateTime now = LocalDateTime.now();
		if(!Main.isTesting) now = now.minusHours(3);
		
		System.out.println(now.getHour());
		return (now.getHour() * 60) + now.getMinute();
	}
	
	static int minutesFormat() {		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		if(!Main.isTesting) now = now.minusHours(3);
		  
		String[] timeNow = dtf.format(now).split("\\s+")[1].split(":");	
		return (Integer.parseInt(timeNow[0]) * 60) + Integer.parseInt(timeNow[1]);
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
