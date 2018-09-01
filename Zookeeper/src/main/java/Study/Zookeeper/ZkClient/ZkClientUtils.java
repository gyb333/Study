package Study.Zookeeper.ZkClient;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

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
	
	public static boolean existsNode(String path) {
		  return zkClient.exists(path);
	}
	
	@SuppressWarnings("unchecked")
	public static  <T extends Object> T getNode(String path,Stat stat) {
		 
		return (T)zkClient.readData(path,stat);
	}
	
	
	
	
	
	public static void SubscribeChildChanges(String path) {
		
		
		zkClient.subscribeChildChanges(path,new IZkChildListener() {
			
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				// TODO Auto-generated method stub
				System.out.println(parentPath);
				System.out.println(currentChilds.toString());
			}
		});	
		
	}
	
	
	public static void SubscribeDataChanges(String path) {
		zkClient.subscribeDataChanges(path, new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub
				System.out.println(dataPath);
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// TODO Auto-generated method stub
				System.out.println(dataPath+":"+data.toString());
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
}
