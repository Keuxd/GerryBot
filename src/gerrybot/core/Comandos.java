package gerrybot.core;

import java.sql.SQLException;
import java.util.List;

import gerrybot.database.DataBaseUtils;
import gerrybot.hentai.Hentai;
import gerrybot.hentai.NHentaiNet;
import gerrybot.hentai.favoritesViewer.FavoritesViewer;
import gerrybot.league.League;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Comandos extends ListenerAdapter {
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		MessageChannel channel = event.getChannel();
		User autor = event.getAuthor();
		
		// Dice roller case
		if(args[0].matches("^([1-9][[0-9]*]{0,1})?d[1-9][[0-9]*]{0,3}([\\+|-][[0-9]*]{0,2})?$")) {
			try {
				String[] argumentos = args[0].split("d");
				String[] modificadores = null;
				
				// For dX* case
				if(argumentos[0].length() == 0) {
					argumentos[0] = "1";
				}
				
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
			}
		}
		else // Henta numbers
		if(args.length == 2 && args[0].equals("!hn")) {
			try {
				Hentai hentai = new NHentaiNet().genHentaiByNumber(args[1]);
				hentai.sendEmbedHentai(channel).queue(message -> {
					message.addReaction("U+2B50").queue();
				});
			} catch (Exception e) {
				channel.sendMessage("Esses numeros nao levam a nenhum nHentai.").queue();
				channel.sendMessage("https://cdn.discordapp.com/emojis/744921446136021062.png").queue();
			}
		}
		else // d size
		if(args[0].equals("!cm") && args.length == 2) {
			double cm = sizeD(Integer.parseInt(args[1]));
			channel.sendMessage("Essa personagem aguenta ate " + String.format("%.1f", cm) + "cm.").queue();
		}
		else // runes
		if(args.length == 3 && args[0].equals("!runa")) {
			try {				
				League champion = new League(args[1], args[2]);
				champion.loadRunes();
				champion.sendRunes(channel);
			} catch (Exception e) {
				channel.sendMessage("Invalid Champion").queue();
				e.printStackTrace();
			}
		} 
		else // builds
		if(args.length == 3 && args[0].equals("!build")) {
			try {
				League champion = new League(args[1], args[2]);
				champion.loadBuilds();
				champion.sendBuilds(channel);
			} catch (Exception e) {
				channel.sendMessage("Invalid Champion").queue();
				e.printStackTrace();
			}
		}
		else
		if(args.length == 2 && args[0].equals("!hentime")) {
			if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				channel.sendMessage("You're not an administrator xD").queue();
				return;
			}
			
			if(!args[1].matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
				channel.sendMessage("Invalid Format, use xx:xx[00:00 ~ 23:59]").queue();
				return;
			}
			
			try {
				DataBaseUtils.insertHentaTimer(event.getGuild().getIdLong(), args[1].split(":"));
				channel.sendMessage("Hentime changed to " + args[1]).queue();
			} catch (SQLException e) {
				channel.sendMessage("Error: " + e.getMessage()).queue();
			}
			
		}
		else
		if(args.length == 2 && args[0].equals("!favorites")) {
			List<Member> members = event.getMessage().getMentionedMembers();
			String userId;
			
			if(members.size() == 1) { // An user was mentioned
				userId = members.get(0).getId();
			}
			else if(args[1].matches("^[0-9]+$")){ // No user mentioned(members.size is 0) but we have the id
				userId = args[1];
			}
			else { // No user mentioned(members.size is 0) and id is invalid(regex doesnt match)
				channel.sendMessage("No user mentioned or id is invalid.").queue();
				return;
			}
			
			try {
				new FavoritesViewer(userId).sendFavoritesHentas(channel).queue(message -> {
					message.addReaction("U+2B50").queue();
				});
			} catch (Exception e) {
				channel.sendMessage("This user doesn't have favorite hentas").queue();
			}
		}
	}

	static double sizeD(int heightCentimeters) {
		double size;
		size = (heightCentimeters * 100) / 161;
		size = (size / 100) * 8; 
		size *= 1.5625;
		
		return size + 1;
	}
}
