package gerrybot.core;

import java.io.File;
import java.util.List;

import javax.security.auth.login.LoginException;

import gerrybot.commands.Commands;
import gerrybot.database.DataBaseModel;
import gerrybot.database.JDBC;
import gerrybot.hentai.DailyThread;
import gerrybot.hentai.HentaiReactionEvents;
import gerrybot.hentai.favoritesViewer.FavoritesViewerListenerAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {
	
	public static final boolean IS_TESTING = true;
	public static final int COLOR = 0x9e42f5;
	
	public static JDA jda;
	
	public static String gerryFolder;
	public static Thread dailyThread;
	
	public static void main(String[] args) throws Exception {
		initCacheFolder();
		
		if(!JDBC.connectDataBase()) return;

		DataBaseModel.createTables();
		
		initJDA("Gerry 2.0 | !updates");

//		initSlashCommands();
		jda.awaitReady();
		
		// Initialize daily henta cycle in another thread
		dailyThread = new Thread(new DailyThread(), "Daily-Henta Thread");
		dailyThread.start();
		
		connectToAudioChannel("Just GerryTests", "Geral");
	}
	
	private static void connectToAudioChannel(String guildName, String channelName) {
		try {
			List<Guild> guilds = jda.getGuildsByName(guildName, true);
			if(guilds.size() == 0) throw new Exception("There're no guilds with this name");
			
			List<VoiceChannel> channels = guilds.get(0).getVoiceChannelsByName(channelName, true);
			if(channels.size() == 0) throw new Exception("There're no channels with this name");
			
			guilds.get(0).getAudioManager().openAudioConnection(channels.get(0));
			System.out.println("Conectado no canal '" + channelName + "' do servidor '" + guildName + "'.");
		} catch(Exception e) {
			System.out.println("Erro ao conectar no canal de voz -> " + e.getMessage());
		}
	}
	
	private static void initJDA(String status) throws LoginException {
		if(jda != null) return;
		
		jda = JDABuilder.createLight(Token.getToken())
				.addEventListeners(new GuildCommands())
				.addEventListeners(new PV())
				.addEventListeners(new HentaiReactionEvents())
				.addEventListeners(new FavoritesViewerListenerAdapter())
				.addEventListeners(new SlashCommands())
				.setStatus(OnlineStatus.ONLINE)
				.setActivity(Activity.playing(status))
				.enableIntents(GatewayIntent.MESSAGE_CONTENT)
				.enableCache(CacheFlag.VOICE_STATE)
				.build();
	}
	
	// Creating cache directory for external process
	private static void initCacheFolder() {
		File folder = new File("gerryCache");
		folder.mkdir();
		gerryFolder = folder.getAbsolutePath();
	}
	
	private static void initSlashCommands() {
		CommandListUpdateAction commands = jda.updateCommands();
		
		SlashCommandData hentime = Commands.slash("hentime", "Changes the Channel and Time hentas will be sent")
				.addOption(OptionType.CHANNEL, "channel", "Channel that daily hentais will be sent", true)
				.addOption(OptionType.STRING, "time", "Time that daily hentais will be sent xx:xx", true);
			
		SlashCommandData data1 = Commands.slash("yo", "oporra");
		
		commands.addCommands(hentime, data1);
		commands.queue();
	}
}