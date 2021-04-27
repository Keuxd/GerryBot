package GerryBot;

final class Token {
	
	private static final String gerryToken = "_";
	private static final String testertToken = "TSaN_TESTERT";
	
	static String getToken() {
		if(Main.isTesting) return testertToken;
		else return gerryToken;
	}
}
