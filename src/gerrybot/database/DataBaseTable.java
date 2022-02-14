package gerrybot.database;

public enum DataBaseTable {
	
	RUNE("runes", new String[] {"ID", "ITEM_INFO"}),
	ITEM("items", new String[] {"ID", "ITEM_INFO"}),
	GUILD_TIMER("henta_timers", new String[] {"GUILD_ID", "MINUTES"}),
	USER_FAVORITE_HENTAS("henta_favorites", new String[] {"USER_ID", "HENTA_CODE"});
	
	private String tableName;
	private String[] columnsNames;
	
	private DataBaseTable(String tableName, String[] columnsNames) {
		this.tableName = tableName;
		this.columnsNames = columnsNames;
	}
	
	public String getTableName() {
		return this.tableName;
	}
	
	public String[] getColumnsNames() {
		return this.columnsNames;
	}
}
