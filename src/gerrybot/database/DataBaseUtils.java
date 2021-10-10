package gerrybot.database;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import gerrybot.core.Main;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class DataBaseUtils {
	// query 'id' n returns the BufferedImage from the base64 string
	public static BufferedImage getLeagueImage(String id, DataBaseEnum type) {
		try {
			String sql = String.format("SELECT * FROM %s WHERE ID = %s",
					type.getTableName(), id);
			
			ResultSet rs = JDBC.state.executeQuery(sql);
			rs.next();
			return convertBase64ToBufferedImage(rs.getString("BASE64"));
		} catch(SQLException e) {
			return null; // returns null if 'id' doesnt exist
		}
	}
	
	public static void insertLeagueImage(String id, BufferedImage bi, DataBaseEnum type) {
		try {
			String sql = String.format("INSERT IGNORE INTO %s VALUES(%s,'%s')",
					type.getTableName(), id, convertBufferedImageToBase64(bi));
			
//			String sql2 = sql +  "ON DUPLICATE KEY UPDATE id = id";
			
			JDBC.state.executeUpdate(sql);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage convertBase64ToBufferedImage(String image64) {
		try {
			String[] strings = image64.split(",");
			String extension = null;
		
			if(strings.length == 1) { // most images in db are png
				strings = new String[]{"data:image/png;base64", strings[0]};
			}
			
			switch(strings[0]) {
				case "data:image/png;base64": extension = "png"; break;
				case "data:image/jpg;base64": extension = "jpg"; break;
				case "data:image/jpeg;base64": extension = "jpeg"; break;
			}
		
			byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
			File file = new File(Main.gerryFolder + "/cover." + extension);
			
			OutputStream output = new BufferedOutputStream(new FileOutputStream(file));
			output.write(data);
			output.close();
			return ImageIO.read(file);
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String convertBufferedImageToBase64(BufferedImage bi) {
		try {
			File file = new File(Main.gerryFolder + "/cover.png");
			ImageIO.write(bi, "png", file);
			
			String encodedFile = null;
			
			@SuppressWarnings("resource")
			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] bytes = new byte[(int)file.length()];
			fileInputStream.read(bytes);

			Encoder encoder = Base64.getEncoder();
			encodedFile = new String(encoder.encode(bytes), "UTF-8");
			
			return encodedFile;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void sendDataBaseFile(PrivateChannel channel) {
		JDBC.disconnectDataBase();
		
		File dataBaseFile = new File(Main.gerryFolder + "/test.mv.db");
		channel.sendFile(dataBaseFile).queue();
		
		JDBC.connectDataBase();
	}
	
	public static void insertHentaTimer(long guildId, String[] time) throws SQLException {
		int minutes = (Integer.parseInt(time[0]) * 60) + Integer.parseInt(time[1]);
		
		String sql = String.format("INSERT INTO %s VALUES(%d,%d) ON DUPLICATE KEY UPDATE MINUTES = %d",
				DataBaseEnum.GUILDTIMER.getTableName(), guildId, minutes, minutes);

		JDBC.state.executeUpdate(sql);
	}
	
	public static ResultSet getHentaTimers() throws SQLException {
		String sql = String.format("SELECT * FROM %s",
				DataBaseEnum.GUILDTIMER.getTableName());
		
		return JDBC.state.executeQuery(sql);
	}
}
