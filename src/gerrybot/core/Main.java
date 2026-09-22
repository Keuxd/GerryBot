package gerrybot.core;

import java.io.File;

import javax.security.auth.login.LoginException;

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

    public static final int COLOR = 0x9e42f5;

    public static JDA jda;

    public static String gerryFolder;
    public static DailyThread dailyThread;

    public static void main(String[] args) throws Exception {
        //DRFrame.getInstance().setVisible(true);
        initCacheFolder();

        if (!JDBC.connectDataBase()) return;

        DataBaseModel.createTables();

        initJDA("Gerry 2.0 | !updates");

        jda.updateCommands().addCommands(SlashCommands.commands).queue();
        jda.awaitReady();

        DRFrame.getInstance().removeLoading();

//		dailyThread = new DailyThread();
//		dailyThread.start();

//        connectToAudioChannel("790008324317380631");
    }

    private static void connectToAudioChannel(String voiceChannelId) {
        try {
            VoiceChannel vc = jda.getVoiceChannelById(voiceChannelId);
            Guild guild = vc.getGuild();
//            guild.getAudioManager().setSendingHandler();
            guild.getAudioManager().openAudioConnection(vc);
            System.out.println("Conectado no canal '" + vc.getName() + "' do servidor '" + guild.getName() + "'.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initJDA(String status) throws LoginException {
        if (jda != null) return;

        JDALogger.setFallbackLoggerEnabled(false);

        jda = JDABuilder.createDefault(Token.getToken())
                .addEventListeners(new ChatLogCommands())
                .addEventListeners(new PV())
                .addEventListeners(new HentaiReactionEvents())
                .addEventListeners(new FavoritesViewerListenerAdapter())
                .addEventListeners(new SlashCommands())
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing(status))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES)
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