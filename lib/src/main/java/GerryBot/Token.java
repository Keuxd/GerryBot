package GerryBot;

final class Token {
	
	private static final String gerryToken = "Nzg5OTU5MzQ2MTg3OTI3NTYy.X95o-A.uMbMwry5slQ8dmc3wMNGSQqB62g";
	private static final String testertToken = "Nzk1OTUwNzg3MTU4OTk5MDYx.X_Q07w.uuBv7-Yj_8Bibcv5E59rK9eyhBQ";
	
	
	
	static String getToken() {
		if(Main.isTesting) return testertToken;
		else return gerryToken;
	}
}
