package gerrybot.database;

import static gerrybot.database.DataBaseTable.GUILD_TIMER;
import static gerrybot.database.DataBaseTable.ITEM;
import static gerrybot.database.DataBaseTable.RUNE;
import static gerrybot.database.DataBaseTable.USER_FAVORITE_HENTAS;

import gerrybot.league.OpggMetaBase;

public class DataBaseModel {
	
	// If all tables were created then returns true, if they don't means it already exists so return false.
	// There is no need to check them individually, because if it created one then 99% chance is it created the others.
	// If the first one gives us an exception then probably the others already exists too, so just return false.
	public static boolean createTables() {
		try {
			JDBC.state.executeUpdate("CREATE TABLE " + ITEM.getTableName() + " (ID INT NOT NULL PRIMARY KEY, ITEM_INFO VARBINARY(256000) NOT NULL)");
			JDBC.state.executeUpdate("CREATE TABLE " + RUNE.getTableName() + " (ID INT NOT NULL PRIMARY KEY, ITEM_INFO VARBINARY(256000) NOT NULL)");
			JDBC.state.executeUpdate("CREATE TABLE " + GUILD_TIMER.getTableName() + " (GUILD_ID LONG NOT NULL PRIMARY KEY, MINUTES INT NOT NULL)");
			JDBC.state.executeUpdate("CREATE TABLE " + USER_FAVORITE_HENTAS.getTableName() + " (USER_ID LONG NOT NULL, HENTA_CODE INT NOT NULL)");
			
			System.out.println("All tables were created, since they're all new, builds and runes will be now downloaded.");
			
			OpggMetaBase.populateBuildsTable();
			OpggMetaBase.populateRunesTable();
			
			return true;
		} catch(Exception e) {
			System.out.println("Tables already exist.");
			return false;
		}
	}
}
