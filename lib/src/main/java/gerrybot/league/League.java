package gerrybot.league;

public class League {
	private String champion;
	private String role;
	
	public League(String champion, String role) {
		this.champion = champion;
		this.role = role;
	}
	
	public void runeTest() {
		try {
			new Runes(this.champion, this.role);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
