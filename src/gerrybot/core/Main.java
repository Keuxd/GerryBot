package gerrybot.core;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gerrybot.database.JDBC;
import gerrybot.hentai.HentaiReaderListenerAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {
	public static final int cor = 0x9e42f5;
	public static JDA jda;
	protected static final boolean isTesting = true;
	
	public static void main(String[] args) throws Exception {
		
		String status = "Gerry 1.7.0 | !updates";
		
		//Instance builder
		jda = JDABuilder.createDefault(Token.getToken())
	    				.addEventListeners(new Comandos())
	    				.addEventListeners(new Comandos_shaped())
	    				.addEventListeners(new PV())
	    				.addEventListeners(new ReactionEvents())
	    				.addEventListeners(new HentaiReaderListenerAdapter())
	    				.setStatus(OnlineStatus.ONLINE)
	    				.setActivity(Activity.playing(status))
	    				.enableCache(CacheFlag.VOICE_STATE)
	    				.build();
			
		/*
		*	jda.getPresence().setStatus(OnlineStatus.ONLINE);
		*	jda.getPresence().setActivity(Activity.playing("D&D"));
		*	jda.addEventListener(new Comandos());
		*	jda.addEventListener(new Comandos_shaped());
		*/
		
		new File(".gerry").mkdir(); // creating cache directory for external process
		JDBC.connectDataBase();
		initSlashCommands();
		jda.awaitReady();
		
		if(!isTesting) new Stonks().start();
		
		try {
			jda.getGuilds().get(0).getAudioManager().openAudioConnection(jda.getGuilds().get(0).getVoiceChannels().get(0));
			System.out.println("Conectado no canal de voz com sucesso.");
		}catch(Exception e) {
			System.out.println("Erro ao conectar no canal de voz.");
		}
	}
	
	private static void initSlashCommands() {
		CommandListUpdateAction commands = jda.updateCommands();
		commands.addCommands(
					new CommandData("read", "Creates a nHentai reader from the given numbers.")
						.addOptions(new OptionData(OptionType.INTEGER, "numbers", "nHentai numbers")
										.setRequired(true))
				);
		commands.queue();
	}
}

class Stonks extends Thread {
	
	@Override
	public void run() {
		while(true) {
			List <TextChannel> textCh = Main.jda.getTextChannelsByName("henta", true);
			
			for(TextChannel canal : textCh) {
				List<Message> mensagens = canal.getHistory().retrievePast(30).complete();
				canal.purgeMessages(mensagens);
			}
			
			int minutesTillNow = Comandos.minutesFormat();
			final int hora = 14 * 60;
			
			if(minutesTillNow == hora) {
				for(TextChannel canal : textCh) canal.sendMessage("!henta").queue();
			}
			
			try {
				TimeUnit.MINUTES.sleep(Comandos.minutosRestantes(minutesTillNow, hora));
			} catch (InterruptedException e) {}
			
		}		
	}
}