package Study.JDBC;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

 

public final class JdbcUtils {

	private static final String dbcp="DBCP.Properties";
	private static DataSource ds =null;
	
	static {
		 
			try {
				Properties properties=new Properties();
				InputStream is =JdbcUtils.class.getClassLoader().getResourceAsStream(dbcp);
				properties.load(is);
				String driverClassName = properties.getProperty("driverClassName");
				Class.forName(driverClassName);
				ds = BasicDataSourceFactory.createDataSource(properties);
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ExceptionInInitializerError(e);
			}
		 
	}
	
	public static DataSource getDataSource() {
		return ds;
	}
	
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
	
	
	
}
