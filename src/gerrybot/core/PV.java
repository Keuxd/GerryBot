package gerrybot.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import gerrybot.database.DataBaseUtils;
import gerrybot.database.JDBC;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PV extends ListenerAdapter{
	
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		
		if(!event.getAuthor().getAsTag().equals("Keu#3384")) {
			event.getChannel().sendMessage("Acesso Negado").queue();
			return;
		}
		
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		switch(args[0].toLowerCase()) {
			case(".help"):{
				event.getChannel().sendMessage(".set -> Seta o admin user a receber as informacoes.\n"
											 + ".send '_numbers_' -> Envia um nHentai aos canais 'henta'.\n"
											 + ".clear -> Limpa os canais 'henta'.\n"
											 + ".sql '_args_' -> Envia um update command para o banco de dados, use '-' para espaços.\n"
											 + ".runes -> Retorna os ids e a quantidade de runas unicas no banco de dados.\n"
											 + ".builds -> Retorna os ids e a quantidade de itens unicos no banco de dados.\n"
											 + ".htimers -> Retorna os dados da table de htimers.\n"
											 + ".downloadbase -> Retorna o arquivo central do banco de dados."
											  ).queue();
				break;
			}
			case(".send"):{
				List<TextChannel> canais = Main.jda.getTextChannelsByName("henta", true);
				for(TextChannel tc : canais) {
					tc.sendMessage("!hn " + args[1]).queue();
				}		
				event.getMessage().getPrivateChannel().sendMessage("Hentai enviado.").queue();			
				
				break;
			}
			case(".clear"):{
				try {
					List<TextChannel> canais = Main.jda.getTextChannelsByName("henta", true);
					
					for(TextChannel canal : canais) {
						List<Message> mensagens = canal.getHistory().retrievePast(30).complete();
						canal.purgeMessages(mensagens);
					}
					event.getMessage().getPrivateChannel().sendMessage("Canais 'henta' limpos.").queue();
				}catch(Exception e) {
					event.getMessage().getPrivateChannel().sendMessage("Erro ao limpar canais.").queue();
				}
				break;
			}
			case (".sql"): {
				try {
					JDBC.state.executeUpdate(args[1].replace("-", " "));
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
		}
	}
}
