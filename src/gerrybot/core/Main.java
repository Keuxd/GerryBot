package gerrybot.core;

import java.io.File;

import javax.security.auth.login.LoginException;

import gerrybot.database.JDBC;
import gerrybot.hentai.DailyThread;
import gerrybot.hentai.HentaiReactionEvents;
import gerrybot.hentai.favoritesViewer.FavoritesViewerListenerAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {
	
	public static final boolean IS_TESTING = false;
	public static final int COLOR = 0x9e42f5;
	
	public static JDA jda;
	
	public static String gerryFolder;
	protected static Thread dailyThread;
	
	public static void main(String[] args) throws Exception {
		initJDA("Gerry 1.8.1 | !updates");
		
		initCacheFolder();
		if(!JDBC.connectDataBase()) return;
//		initSlashCommands();
		jda.awaitReady();

		// Initialize daily henta cycle in another thread
		dailyThread = new Thread(new DailyThread(), "Daily-Henta Thread");
		dailyThread.start();
		
		try {
			jda.getGuilds().get(0).getAudioManager().openAudioConnection(jda.getGuilds().get(0).getVoiceChannels().get(0));
			System.out.println("Conectado no canal de voz com sucesso.");
		}catch(Exception e) {
			System.out.println("Erro ao conectar no canal de voz.");
		}
	}
	
	private static void initJDA(String status) throws LoginException {
		if(jda != null) return;
		
		jda = JDABuilder.createDefault(Token.getToken())
				.addEventListeners(new Comandos())
				.addEventListeners(new Comandos_shaped())
				.addEventListeners(new PV())
				.addEventListeners(new HentaiReactionEvents())
				.addEventListeners(new FavoritesViewerListenerAdapter())
				.setStatus(OnlineStatus.ONLINE)
				.setActivity(Activity.playing(status))
				.enableCache(CacheFlag.VOICE_STATE)
				.build();
	}
	
	// Creating cache directory for external process
	private static void initCacheFolder() {
		File folder = new File("gerryCache");
		folder.mkdir();
		gerryFolder = folder.getAbsolutePath();
	}
	
	@SuppressWarnings("unused")
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