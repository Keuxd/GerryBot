package gerrybot.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import gerrybot.database.DataBaseModel;
import gerrybot.database.DataBaseTable;
import gerrybot.database.DataBaseUtils;
import gerrybot.database.JDBC;
import gerrybot.league.OpggMetaBase;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PV extends ListenerAdapter {
	
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		
		if(!event.getAuthor().getId().equals("345259449184944142")) return;
		
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		switch(args[0].toLowerCase()) {
			case(".help"):{
				event.getChannel().sendMessage(".send '_numbers_' -> Envia um nHentai aos canais 'henta'.\n"
											 + ".clear -> Limpa os canais 'henta'.\n"
											 + ".sql '_args_' -> Envia um update command para o banco de dados.\n"
											 + ".runes -> Retorna os ids e a quantidade de runas unicas no banco de dados.\n"
											 + ".builds -> Retorna os ids e a quantidade de itens unicos no banco de dados.\n"
											 + ".htimers -> Retorna os dados da table de htimers.\n"
											 + ".downloadbase -> Retorna o arquivo central do banco de dados.\n"
											 + ".transfer -> Retorna o comando sql para transferencia de db.\n"
											 + ".repopulate -> Apaga as tabelas de Builds/Runas e as baixa novamente.\n"
											  ).queue();
				break;
			}
			case(".send"):{
				List<TextChannel> canais = Main.jda.getTextChannelsByName("henta", true);
				for(TextChannel tc : canais) {
					tc.sendMessage("!hn " + args[1]).queue();
				}		
				event.getMessage().getChannel().sendMessage("Hentai enviado.").queue();			
				
				break;
			}
			case(".clear"):{
				try {
					List<TextChannel> canais = Main.jda.getTextChannelsByName("henta", true);
					
					for(TextChannel canal : canais) {
						List<Message> mensagens = canal.getHistory().retrievePast(30).complete();
						canal.purgeMessages(mensagens);
					}
					event.getMessage().getChannel().sendMessage("Canais 'henta' limpos.").queue();
				}catch(Exception e) {
					event.getMessage().getChannel().sendMessage("Erro ao limpar canais.").queue();
				}
				break;
			}
			case (".sql"): {
				try {
					StringBuilder sb = new StringBuilder();
					
					for(int i = 1; i < args.length; i++)
						sb.append(args[i] + " ");
					
					JDBC.state.executeUpdate(sb.toString());
					event.getChannel().sendMessage("Comando enviado").queue();
				} catch(Exception e) {e.printStackTrace();}
				break;
			}
			case(".runes") : {
				try {
					ResultSet rs = JDBC.state.executeQuery("select * from runes");
					if(!rs.next()) {
						event.getChannel().sendMessage("Runes table is empty").queue();
						return;
					}
					
					int counter = 0;
					StringBuilder sb = new StringBuilder();
					while(rs.next()) {
						sb.append(rs.getInt("id") + " ");
						counter++;
					}
					event.getChannel().sendMessage(sb + "[" + counter + "]").queue();
				} catch(Exception e) {}
				break;
			}
			case(".builds") : {
				try {
					ResultSet rs = JDBC.state.executeQuery("select * from builds");
					if(!rs.next()) {
						event.getChannel().sendMessage("Builds table is empty").queue();
						return;
					}
					
					int counter = 0;
					StringBuilder sb = new StringBuilder();
					while(rs.next()) {
						sb.append(rs.getInt("id") + " ");
						counter++;
					}
					event.getChannel().sendMessage(sb + "[" + counter + "]").queue();
					
				} catch(Exception e) {}
				break;
			}
			case(".downloadbase") : {
				DataBaseUtils.sendDataBaseFile(event.getChannel());
				break;
			}
			case(".htimers") : {
				try {
					ResultSet rs = DataBaseUtils.getHentaTimers();
					if(!rs.next()) {
						event.getChannel().sendMessage("DataBase Vazia").queue();
						return;
					}
					rs.beforeFirst();
					StringBuilder sb = new StringBuilder();
					while(rs.next()) {
						sb.append(" [" + rs.getLong("GUILD_ID") + " -> " + rs.getInt("MINUTES") + "] ");
					}
					
					event.getChannel().sendMessage(sb.toString()).queue();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			}
			case(".transfer") : {
				
				String guildTimer = DataBaseModel.getSqlUpdateCommand(DataBaseTable.GUILD_TIMER);
				String userFavoriteHentas = DataBaseModel.getSqlUpdateCommand(DataBaseTable.USER_FAVORITE_HENTAS);
				
				if(guildTimer.length() > 2000) event.getChannel().sendMessage(DataBaseTable.GUILD_TIMER.getTableName() + "is over 2000 chars.").queue();
				else if(userFavoriteHentas.length() > 2000) event.getChannel().sendMessage(DataBaseTable.USER_FAVORITE_HENTAS.getTableName() + " command is over 2000 chars.").queue();
				else {
					event.getChannel().sendMessage(guildTimer).queue();
					event.getChannel().sendMessage(userFavoriteHentas).queue();
				}
				
				break;
			}
			case(".repopulate") : {
				try {
					event.getChannel().sendMessage("Starting Items/Runes deletion...").queue();
					JDBC.state.execute("DELETE FROM " + DataBaseTable.ITEM.getTableName());
					JDBC.state.execute("DELETE FROM " + DataBaseTable.RUNE.getTableName());
					event.getChannel().sendMessage("Items/Runes successfully deleted.\nStarting Runes population...").queue();
					
					OpggMetaBase.populateRunesTable();
					event.getChannel().sendMessage("Runes were successfully populated\nStarting Items").queue();
					OpggMetaBase.populateBuildsTable();
					event.getChannel().sendMessage("Items were successfully populated.").queue();
				} catch (Exception e) {
					event.getChannel().sendMessage("Something gone wrong -> " + e.getMessage()).queue();
					e.printStackTrace();
				}
				break;
			}
		}
	}
}