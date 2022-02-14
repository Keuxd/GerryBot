package gerrybot.database;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import gerrybot.core.Main;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class DataBaseUtils {
	
	public static void sendDataBaseFile(PrivateChannel channel) {
		JDBC.disconnectDataBase();
		
		File dataBaseFile = new File(Main.gerryFolder + "/test.mv.db");
		channel.sendFile(dataBaseFile).queue();
		
		JDBC.connectDataBase();
	}
	
	public static void insertLeagueImage(String id, byte[] bytes, DataBaseTable type) {
		try {
			PreparedStatement ps = JDBC.con.prepareStatement("INSERT IGNORE INTO " + type.getTableName() + " VALUES (" + id + ",?)");
			ps.setBytes(1, bytes);
			ps.execute();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage getLeagueImage(String id, DataBaseTable type) {
		try {
			ResultSet rs = JDBC.state.executeQuery("SELECT * FROM " + type.getTableName() +  " WHERE ID = " + id);
			rs.first();
			byte[] info = rs.getBytes("ITEM_INFO");
			
			return ImageIO.read(new ByteArrayInputStream(info));
		} catch(Exception e) {
			System.out.println("DataBaseUtils.getLeagueImage() tried to get an non-existent image in table \"" + type.getTableName() + "\" with ID -> " + id);
			return null;
		}
	}
	
	public static void insertHentaTimer(long guildId, String[] time) throws SQLException {
		int minutes = (Integer.parseInt(time[0]) * 60) + Integer.parseInt(time[1]);
		
		String sql = String.format("INSERT INTO %s VALUES(%d,%d) ON DUPLICATE KEY UPDATE MINUTES = %d",
				DataBaseTable.GUILD_TIMER.getTableName(), guildId, minutes, minutes);

		JDBC.state.executeUpdate(sql);
	}
	
	public static ResultSet getHentaTimers() throws SQLException {
		String sql = String.format("SELECT * FROM %s",
				DataBaseTable.GUILD_TIMER.getTableName());
		
		return JDBC.state.executeQuery(sql);
	}
	
	/**
	 * It'll check if both values already exist in database, if they do so the respective row will be deleted, if they don't then both values will be inserted.
	 * @param userId
	 * @param hentaNumbers
	 * @throws SQLException If something goes wrong, duh.
	 * @return Returns true if it did an add action and false if it did a delete action.
	 */
	public static boolean interactFavoriteHenta(String userId, String hentaNumbers) throws SQLException {
		// Check if both values already exist in db
		// If they do then delete the row
		// If the don't then insert it
		String sql = String.format("SELECT CASE WHEN EXISTS(SELECT * FROM %s WHERE %s = %s and %s = %s) THEN true ELSE false END AS RESULT",
				DataBaseTable.USER_FAVORITE_HENTAS.getTableName(), "USER_ID", userId, "HENTA_CODE", hentaNumbers);
		
		ResultSet rs = JDBC.state.executeQuery(sql);
		rs.first();
		
		boolean areBothValuesInDB = rs.getBoolean("RESULT");
		rs.close();
		
		if(areBothValuesInDB)
			sql = String.format("DELETE FROM %s WHERE %s = %s and %s = %s",
					DataBaseTable.USER_FAVORITE_HENTAS.getTableName(), "USER_ID", userId, "HENTA_CODE", hentaNumbers);
		else
			sql = String.format("INSERT INTO %s VALUES(%s,%s)",
					DataBaseTable.USER_FAVORITE_HENTAS.getTableName(), userId, hentaNumbers);
		
		JDBC.state.executeUpdate(sql);
		
		return !areBothValuesInDB;
	}
	
	public static ResultSet getFavoritesHentas(String userId) throws SQLException {
		String sql = String.format("SELECT %s FROM %s WHERE %s = %s",
				"HENTA_CODE", DataBaseTable.USER_FAVORITE_HENTAS.getTableName(), "USER_ID", userId);
		
		ResultSet rs = JDBC.state.executeQuery(sql);
		
		if(!rs.next())
			throw new SQLException("This user has no favorite hentas in database.");
		
		return rs;
	}
}
