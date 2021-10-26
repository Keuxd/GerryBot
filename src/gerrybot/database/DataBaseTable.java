package gerrybot.database;

public enum DataBaseTable {
	
	RUNE("RUNES"),
	ITEM("BUILDS"),
	GUILD_TIMER("HENTA_TIMERS"),
	USER_FAVORITE_HENTAS("HENTA_FAVORITES");
	
	private String tableName;
	
	private DataBaseTable(String name) {
		tableName = name;
	}
	
	public String getTableName() {
		return tableName;
	}
}
