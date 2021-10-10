package gerrybot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gerrybot.core.Main;

public class JDBC {
	public static Connection con;
	public static Statement state;
	
	public static void connectDataBase() {
		try {
			con = DriverManager.getConnection("jdbc:h2:" + Main.gerryFolder + "/test;MODE=MYSQL", "sa", "");
			state = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			System.out.println("Database connected");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error connecting to Database");
		}
	}
	
	public static void disconnectDataBase() {
		try {
			if(state != null) state.close();
			state = null;
			
			if(con != null) con.close();
			con = null;
			System.out.println("Database disconnected");
		} catch(SQLException e) {
			System.out.println("Error disconnecting to database");
		}
	}
}
