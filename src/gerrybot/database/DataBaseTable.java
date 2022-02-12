package gerrybot.database;

public enum DataBaseTable {
	
	RUNE("runes"),
	ITEM("items"),
	GUILD_TIMER("henta_timers"),
	USER_FAVORITE_HENTAS("henta_favorites");
	
	private String tableName;
	
	private DataBaseTable(String name) {
		tableName = name;
	}
	
	public String getTableName() {
		return tableName;
	}
}
