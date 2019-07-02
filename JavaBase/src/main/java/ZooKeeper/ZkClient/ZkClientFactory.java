package ZooKeeper.ZkClient;

import org.I0Itec.zkclient.ZkClient;
 
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;

 

public class ZkClientFactory {

	private static final String zkServer="Master:2181,Second:2181,slave:2181";
	private static final int sessionTimeout = 10 * 1000;
	private static final int connectionTimeout = 10 * 1000;
	
	
 
	public static ZkClient getInstance() {
		ZkClient zkClient=new ZkClient(zkServer,sessionTimeout,connectionTimeout,new SerializableSerializer());
		return zkClient;
	}
	
	public static ZkClient getInstance(ZkSerializer zkSerializer) {
		ZkClient zkClient=new ZkClient(zkServer,sessionTimeout,connectionTimeout,zkSerializer);
		return zkClient;
	}
	
	
}
