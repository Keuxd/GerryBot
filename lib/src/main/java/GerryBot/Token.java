package GerryBot;

final class Token {
	
	private static final String gerryToken = "Nzg5OTU5MzQ2MTg3OTI3NTYy.X95o-A.f_bqqna4NqgWnmVG6N-vbDTLpdU";
	private static final String testertToken = "Nzk1OTUwNzg3MTU4OTk5MDYx.X_Q07w.Urjk_d-BURF3zAGkVQmMQFs_ar8";
	
	static String getToken() {
		if(Main.isTesting) return testertToken;
		else return gerryToken;
	}
}
