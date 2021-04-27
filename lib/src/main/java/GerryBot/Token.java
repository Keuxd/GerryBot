package GerryBot;

final class Token {
	
	private static final String gerryToken = "TOKEN_GERRY";
	private static final String testertToken = "TOKEN_TESTERT";
	
	static String getToken() {
		if(Main.isTesting) return testertToken;
		else return gerryToken;
	}
}
