package gerrybot.hentai;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import gerrybot.core.Main;
import gerrybot.database.DataBaseTable;
import gerrybot.database.DataBaseUtils;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class DailyThread extends Thread {

	@Override
	public void run() {
		while(true) {
			try {
				ResultSet rs = DataBaseUtils.getHentaTimers();
				
				// Query is empty
				if(!rs.isBeforeFirst()) {
					System.out.println("There's no guild timer in database, 'Daily-Henta Thread' will sleep for 1 hour");
					TimeUnit.MINUTES.sleep(60);
					continue;
				}
				
				int minutesNow = getMinutesNow();
				int minorMinutesLeft = 1440; // In the worst case we do 24 hours sleep
				
				while(rs.next()) {
					int minutesInCurrentRow = rs.getInt(DataBaseTable.GUILD_TIMER.getColumnsNames()[1]);
					
					if(minutesInCurrentRow == minutesNow) {
						sendDailyHenta(rs.getLong(DataBaseTable.GUILD_TIMER.getColumnsNames()[0]));
					}
					
					int minutesLeftInCurrentRow = minutesLeft(minutesNow, minutesInCurrentRow);
					if(minutesLeftInCurrentRow < minorMinutesLeft) {
						minorMinutesLeft = minutesLeftInCurrentRow;
					}
				}
				
				System.out.println("Daily-Henta Thread will sleep for " + minorMinutesLeft + " minutes");
				TimeUnit.MINUTES.sleep(minorMinutesLeft);
			} catch (InterruptedException e) {
				System.out.println("hentime called, DailyThread woke up.");
			} catch (SQLException e) {
				System.out.println("The henta table in db doesn't exist, DailyThread will be killed.");
				break;
			}
		}
	}
	
	public void sendDailyHenta(long channelId) {
		TextChannel channel = Main.jda.getTextChannelById(channelId);
		
		LocalDateTime ldt = LocalDateTime.now();
		String dateNum = ldt.format(DateTimeFormatter.ofPattern("dMMyy"));
		
		Hentai dailyHenta = NHentaiNet.createHentaiByNumber(dateNum);
		
		if(dailyHenta.getTitle().equals("Not Found")) {
			channel.sendMessage("O dia de hoje não tem hentai.").queue();
			channel.sendMessage("https://cdn.discordapp.com/emojis/744921446136021062.png").queue();
			return;
		}
		
		dailyHenta.sendEmbedHentai(channel, "Hentai do Dia").queue(message -> {
			message.addReaction(Emoji.fromUnicode("U+1F51E")).queue();
			message.addReaction(Emoji.fromUnicode("U+2B50")).queue();
		});
	}
	
	private int getMinutesNow() {
		LocalDateTime now = LocalDateTime.now();
//		if(!Main.IS_TESTING) now = now.minusHours(3);

		return (now.getHour() * 60) + now.getMinute();
	}
	
	private int minutesLeft(int minutesStart, int minutesTarget) {
		if(minutesStart == minutesTarget) return 1440;
		
		int difference = minutesTarget - minutesStart;
		if(difference < 0) {
			difference += 1440;
		}
		
		return difference;
	}
}
