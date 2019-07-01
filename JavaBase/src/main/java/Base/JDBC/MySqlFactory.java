package Base.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

 



public class MySqlFactory {

	public static final String url = "jdbc:mysql://Master:3306/sqoop2?serverTimezone=UTC";
	public static final String user = "root";
	public static final String password = "root";
	public static final String  className="com.mysql.cj.jdbc.Driver";
	static {
		try {
//			DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver()); //register了两次			
//			System.setProperty("jdbc.drivers", className);
			Class.forName(className);	//classLoader加载对应驱动
		} 
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 
		}		 
	}

	public static Connection getConnection() {

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connection;
	}

}
