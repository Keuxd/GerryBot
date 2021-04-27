package GerryBot;

final class Token {
	
	private static final String gerryToken = "Nzg5OTU5MzQ2MTg3OTI3NTYy.X95o-A.IkXCvqlkH005of2QjVUiHyFurCQ";
	private static final String testertToken = "Nzk1OTUwNzg3MTU4OTk5MDYx.X_Q07w.ZVvnm5WaASrXEPe6paQimeiYDRo";
	
	static String getToken() {
		if(Main.isTesting) return testertToken;
		else return gerryToken;
	}
}
