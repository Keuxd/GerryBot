package gerrybot.hentai.favoritesViewer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gerrybot.database.DataBaseUtils;
import gerrybot.hentai.Hentai;
import gerrybot.hentai.NHentaiNet;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class FavoritesViewer {
	private String userId;
	private String firstHentaCode;
	
	public FavoritesViewer(String userId) {
		this.userId = userId;
	}
	
	public MessageCreateAction sendFavoritesHentas(MessageChannel channel) throws Exception {
		StringSelectMenu hentasMenu = genHentasMenu();
		Hentai hn = NHentaiNet.createHentaiByNumber(this.firstHentaCode);
		hn.setImageFile(null);
		return hn.sendEmbedHentai(channel).setActionRow(hentasMenu);
	}
	
	private StringSelectMenu genHentasMenu() throws SQLException {
		ResultSet rs = DataBaseUtils.getFavoritesHentas(this.userId); // ResultSet is in the first row at this point
		
		ArrayList<SelectOption> options = new ArrayList<SelectOption>();
		this.firstHentaCode = rs.getString("HENTA_CODE");
		options.add(SelectOption.of(this.firstHentaCode, "henta_0"));
		
		int counter = 1;
		while(rs.next()) {
			options.add(SelectOption.of(rs.getString("HENTA_CODE"), "henta_" + counter));
			counter++;
		}
		rs.close();
		
		return StringSelectMenu.create("gerry:favoritesHentas")
				.setPlaceholder(this.firstHentaCode)
				.setRequiredRange(1, 1)
				.addOptions(options)
				.build();
	}
}