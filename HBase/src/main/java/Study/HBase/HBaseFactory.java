package Study.HBase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class HBaseFactory {

	private static final String ZKHBase = "Master:2181,Second:2181,Slave:2181";
	private static Configuration conf;

	static {
		// 构建一个连接对象
		conf = HBaseConfiguration.create();
		//会自动加载hbase-site.xml
		//System.out.println(System.getenv("HBASE_CONF_DIR"));
		
		 //Add any necessary configuration files (hbase-site.xml, core-site.xml)
//		conf.addResource(new Path(System.getenv("HBASE_CONF_DIR"), "hbase-site.xml"));
//		conf.addResource(new Path(System.getenv("HADOOP_CONF_DIR"), "core-site.xml"));
	
		conf.set("hbase.zookeeper.quorum", ZKHBase);
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
