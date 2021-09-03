package gerrybot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
	public static Connection con;
	public static Statement state;
	
	public static void connectDataBase() {
		try {
			con = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
			state = con.createStatement();
			System.out.println("Database connected");
		} catch (SQLException e) {
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
