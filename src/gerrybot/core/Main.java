package gerrybot.core;

import java.awt.Color;
import java.io.File;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.swing.BorderFactory;
import javax.swing.UIManager;

import gerrybot.commands.chatlog.ChatLogCommands;
import gerrybot.commands.slash.SlashCommands;
import gerrybot.database.DataBaseModel;
import gerrybot.database.JDBC;
import gerrybot.diceroller.DRFrame;
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
import net.dv8tion.jda.internal.utils.JDALogger;

public class Main {
	
	public static final boolean IS_TESTING = false;
	public static final int COLOR = 0x9e42f5;
	
	public static JDA jda;
	
	public static String gerryFolder;
	public static DailyThread dailyThread;
	
	public static void main(String[] args) throws Exception {		
		DRFrame.getInstance().setVisible(true);
		initCacheFolder();

		if(!JDBC.connectDataBase()) return;

		DataBaseModel.createTables();
		
		initJDA("Gerry 2.0 | !updates");
		
		jda.updateCommands().addCommands(SlashCommands.commands).queue();
		jda.awaitReady();
		
		DRFrame.getInstance().removeLoading();
		
//		dailyThread = new DailyThread();
//		dailyThread.start();
		
		connectToAudioChannel("Dark City", "Sessãosinha");
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
		
		JDALogger.setFallbackLoggerEnabled(false);
		
		jda = JDABuilder.createLight(Token.getToken())
				.addEventListeners(new ChatLogCommands())
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
	
}