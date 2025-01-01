package gerrybot.league;

public enum OpggEndPoints {
	API_NEXUS("https://lol-api-nexus.op.gg/api"),
	API_CHAMPION("https://lol-api-champion.op.gg/api"),
	API_SUMMONER("https://lol-api-summoner.op.gg/api"),
	API_POLICIES("https://policy.op.gg/api"),
	API_GEO("https://geo-internal.op.gg/api"),
	API_NODE_BASE_URL("https://lol-web-api.op.gg/api/v1.0/internal"),
	API_MEMBER("https://member-api.op.gg");
	
	private String endPoint;
	
	OpggEndPoints(String endPoint) {
		this.endPoint = endPoint;
	}
	
	@Override
	public String toString() {
		return this.endPoint;
	}
	
}
