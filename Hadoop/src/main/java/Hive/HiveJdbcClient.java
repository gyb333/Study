package Hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class HiveJdbcClient {

	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	private static String url = "jdbc:hive2://Master:10000";
	private static String user = "";
	private static String password = "";
	private static String sql;
	private static ResultSet res;
	private static final String path = System.getProperty("user.dir");
	
	
	public static void main(String[] args) {
		try {
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(url, user, password);

			Statement stmt = conn.createStatement();

			sql = "show tables";
			System.out.println("Running:" + sql);
			res = stmt.executeQuery(sql);

			while (res.next()) {
				System.out.println(res.getString(1));
			}

			String tableName = "testHiveDriverTable";
			stmt.execute("drop table if exists " + tableName);
			stmt.execute("create table " + tableName + " (key int, value string) row format delimited fields terminated by '\\t' stored as textfile ");
			// show tables
			String sql = "show tables '" + tableName + "'";
			System.out.println("Running: " + sql);
			ResultSet res = stmt.executeQuery(sql);
			if (res.next()) {
				System.out.println(res.getString(1));
			}
			// describe table
			sql = "describe " + tableName;
			System.out.println("Running: " + sql);
			res = stmt.executeQuery(sql);
			while (res.next()) {
				System.out.println(res.getString(1) + "\t" + res.getString(2));
			}

			// load data into table
			// NOTE: filepath has to be local to the hive server
			// NOTE: /tmp/a.txt is a ctrl-A separated file with two fields per line
			String filepath ="/usr/local/hive3/Data/Test.txt";
			sql = "load data local inpath '" + filepath + "' into table " + tableName;
			System.out.println("Running: " + sql);
			stmt.execute(sql);

			// select * query
			sql = "select * from " + tableName;
			System.out.println("Running: " + sql);
			res = stmt.executeQuery(sql);
			while (res.next()) {
				System.out.println(String.valueOf(res.getInt(1)) + "\t" + res.getString(2));
			}

			// regular hive query
			sql = "select count(1) from " + tableName;
			System.out.println("Running: " + sql);
			res = stmt.executeQuery(sql);
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
