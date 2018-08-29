package Study.Hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcClient {

	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	private static String url = "jdbc:hive2://Master:10000/hive3";
	private static String user = "";
	private static String password = "";
	private static String sql;
	private static ResultSet res;
 
	public static void main(String[] args) {
		try {
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(url, user, password);


			Statement stmt = conn.createStatement();
 
			sql = "show tables";//显示全部表
			System.out.println("Running:" + sql);
			res = stmt.executeQuery(sql);
			System.out.println("执行结果:");
			while (res.next()) {
				System.out.println(res.getString(1));
			}
 
			conn.close();
			conn = null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			 
 
		} catch (SQLException e) {
			e.printStackTrace();
 
		}
 
	}

	
	
}
