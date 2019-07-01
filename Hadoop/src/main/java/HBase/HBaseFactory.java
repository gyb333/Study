package HBase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class HBaseFactory {

	private static final String ZKHBase = "Master:2181,Second:2181,Slave:2181";
	private static Configuration conf;

	private static final String packageName = HBaseFactory.class.getPackage().getName();
	private static final String packagePath = packageName.replace(".", "/");

	static {
		// 构建一个连接对象
		conf = HBaseConfiguration.create();
//		conf.set("hbase.zookeeper.quorum", ZKHBase);

		//会自动加载hbase-site.xml
		//System.out.println(System.getenv("HBASE_CONF_DIR"));	//获取环境变量路径

//		System.out.println(packagePath);

	

	}

	public static Connection getInstance() {
		Connection connection=null;
		try {
			connection = ConnectionFactory.createConnection(conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

}
