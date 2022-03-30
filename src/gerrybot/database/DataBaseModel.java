package gerrybot.database;

import static gerrybot.database.DataBaseTable.GUILD_TIMER;
import static gerrybot.database.DataBaseTable.ITEM;
import static gerrybot.database.DataBaseTable.RUNE;
import static gerrybot.database.DataBaseTable.USER_FAVORITE_HENTAS;

import java.sql.ResultSet;

import gerrybot.league.OpggMetaBase;

public class DataBaseModel {
	
	// If all tables were created then returns true, if they don't means it already exists so return false.
	// There is no need to check them individually, because if it created one then 99% chance is it created the others.
	// If the first one gives us an exception then probably the others already exists too, so just return false.
	public static boolean createTables() {
		try {
			JDBC.state.executeUpdate(String.format("CREATE TABLE %s (%s INT NOT NULL PRIMARY KEY, %s VARBINARY(256000) NOT NULL)", ITEM.getTableName(), ITEM.getColumnsNames()[0], ITEM.getColumnsNames()[1]));
			JDBC.state.executeUpdate(String.format("CREATE TABLE %s (%s INT NOT NULL PRIMARY KEY, %s VARBINARY(256000) NOT NULL)", RUNE.getTableName(), RUNE.getColumnsNames()[0], RUNE.getColumnsNames()[1]));
			JDBC.state.executeUpdate(String.format("CREATE TABLE %s (%s LONG NOT NULL PRIMARY KEY, %s INT NOT NULL)", GUILD_TIMER.getTableName(), GUILD_TIMER.getColumnsNames()[0], GUILD_TIMER.getColumnsNames()[1]));
			JDBC.state.executeUpdate(String.format("CREATE TABLE %s (%s LONG NOT NULL, %s INT NOT NULL)", USER_FAVORITE_HENTAS.getTableName(), USER_FAVORITE_HENTAS.getColumnsNames()[0], USER_FAVORITE_HENTAS.getColumnsNames()[1]));
			
			System.out.println("All tables were created, since they're all new, builds and runes will be now downloaded.");
			
			OpggMetaBase.populateBuildsTable();
			OpggMetaBase.populateRunesTable();
			
			return true;
		} catch(Exception e) {
			System.out.println("Tables already exist.");
			return false;
		}
	}
	
	public static String getSqlUpdateCommand(DataBaseTable table) {
		try {
			ResultSet rs = JDBC.state.executeQuery("SELECT * FROM " + table.getTableName());
			
			StringBuilder sb = new StringBuilder();
			
			while(rs.next()) {
				sb.append('(');
				sb.append(rs.getString(table.getColumnsNames()[0]));
				sb.append(',');
				sb.append(rs.getString(table.getColumnsNames()[1]));
				sb.append("),");
			}
			
			rs.close();
			sb.deleteCharAt(sb.length()-1);
			
			return String.format("INSERT INTO %s VALUES %s", table.getTableName(), sb.toString());
		} catch(Exception e) {
			return "ERROR -> " + e.getMessage();
		}
	}
}