package Study.Zookeeper.ZkClient;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class ZkClientUtils {
	private static  ZkClient zkClient;
	
	
	static {
		zkClient=ZkClientFactory.getInstance();
	}
	
	
	public static String createNode(String path, Object data, CreateMode mode) {
		return zkClient.create(path, data, mode);
	}
	
	public static void updateNode(String path, Object data, final int expectedVersion) {
		  zkClient.writeData(path, data, expectedVersion);
	}
	
	public static void deleteNode(String path) {
		  zkClient.delete(path);
	}
	
	
	
}
