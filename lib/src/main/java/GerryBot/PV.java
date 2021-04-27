package GerryBot;

import java.util.List;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PV extends ListenerAdapter{
	
	private static PrivateChannel myChannel;
	
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		switch(args[0].toLowerCase()) {
			case(".help"): {
				event.getChannel().sendMessage(".set -> Para dar set no user a receber as informacoes.\n"
											 + ".send _numbers_ -> Enviar um nHentai aos canais 'henta'.\n"
											 + ".clear -> Limpar os canais 'henta'.\n"
											 + ".say -> Dizer algo nos canais 'geral'."
											  ).queue();
				break;
			}
		
		
			case(".set"):{
				if(myChannel == null) {
					myChannel = event.getChannel();
					myChannel.sendMessage(myChannel.getUser().getAsTag() + " setted").queue();
				}
				else {
					event.getMessage().getPrivateChannel().sendMessage(myChannel.getUser().getAsTag() + " already setted").queue();
				}
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
			
//TODO Otimizar isso depois :kek:
			case(".say"):{
				if(args.length == 1) return;
				
				String finalWord = "";
				
				for(int i = 1; i < args.length; i++) {
					finalWord += args[i];
				}
				
				try {
					List<TextChannel> ch = Main.jda.getTextChannelsByName("geral", true);
					
					for(TextChannel tx : ch) {
						tx.sendMessage(finalWord).queue();
					}
					
				event.getChannel().sendMessage("_" + finalWord + "_" + " **Sended**").queue();
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public static void updateHentaState(String lastHenta) {
		if(myChannel == null) return;
		myChannel.sendMessage(lastHenta).queue();
	}
	
}
