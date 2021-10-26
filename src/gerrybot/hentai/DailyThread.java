package gerrybot.hentai;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import gerrybot.core.Main;
import gerrybot.database.DataBaseUtils;

public class DailyThread implements Runnable {

	@Override
	public void run() {
		while(true) {
			//Array sempre ordenado
			//Query na DB pegando todos os minutos
			//Checar se o minuto da vez é igual ao minutesNow()
			//	true: para cada guildID -> channels 'henta' -> clear channel -> send !henta
			
			//Calculando o tempo que thread deve dormir
			//Ir para o próximo index do array de horários
			//	se o index atual for igual ao index inicial então todos os valores são iguais, durma 1440(24h)
			//	se o próximo index for out of bounds volte pro index 0
			//	se o minuto da db for igual ao do index inicial então vai pro proximo index
			//	caso contrário calcula o tempo dele e durma.

			try {
				ResultSet rs = DataBaseUtils.getHentaTimers();
				if(!rs.next()) {
					System.out.println("There's no guild timer in database, 'Daily-Henta Thread' will sleep for 1 hour");
					TimeUnit.MINUTES.sleep(60);
					continue;
				}
				rs.beforeFirst(); // Since the check above will move the cursor forward we need to go back for full iteration
				int minutesNow = getMinutesNow();
				int minorMinutesLeft = 1440; // In the worst case we do 24 hours sleep
				
				while(rs.next()) {
					int minutesInCurrentRow = rs.getInt("MINUTES");
					if(minutesInCurrentRow == minutesNow) {
						Main.jda.getGuildById(rs.getLong("GUILD_ID")).getTextChannelsByName("henta", true).forEach(channel -> {
							channel.purgeMessages(channel.getHistory().retrievePast(30).complete());
							channel.sendMessage("!henta").queue();
						});
					}
					int minutesLeftInCurrentRow = minutesLeft(minutesNow, minutesInCurrentRow);
					if(minutesLeftInCurrentRow < minorMinutesLeft) {
						minorMinutesLeft = minutesLeftInCurrentRow;
					}
				}
				System.out.println("Daily-Henta Thread will sleep for " + minorMinutesLeft + " minutes");
				TimeUnit.MINUTES.sleep(minorMinutesLeft);
			} catch (SQLException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private int getMinutesNow() {
		LocalDateTime now = LocalDateTime.now();
		if(!Main.isTesting) now = now.minusHours(3);

		return (now.getHour() * 60) + now.getMinute();
	}
	
	private int minutesLeft(int minutesStart, int minutesTarget) {
		if(minutesStart == minutesTarget) return 1440;
		
		int counter = 0;
		
		while(minutesStart != minutesTarget) {
			minutesStart++;
			counter++;
			
			if(minutesStart == 1440) {
				minutesStart = 0;
			}
		}
		
		return counter;
	}
}
