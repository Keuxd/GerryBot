package GerryBot;

final class Token {
	
	private static final String gerryToken = "TSDN_GERRY";
	private static final String testertToken = "TSDN_TESTERT";
	
	static String getToken() {
		if(Main.isTesting) return testertToken;
		else return gerryToken;
	}
}
