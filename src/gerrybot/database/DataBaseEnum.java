package gerrybot.database;

public enum DataBaseEnum {
	
	RUNE("RUNES"),
	ITEM("BUILDS"),
	GUILDTIMER("HENTA_TIMERS");
	
	private String tableName;
	
	private DataBaseEnum(String name) {
		tableName = name;
	}
	
	public String getTableName() {
		return tableName;
	}
}
