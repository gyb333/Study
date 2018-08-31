package Study.Zookeeper.ZkClient;

 

import org.I0Itec.zkclient.ZkClient;

import org.apache.zookeeper.CreateMode;

public class CreateNode {

	public static void main(String[] args) {
		ZkClient zc = ZkClientFactory.getInstance();
		System.out.println("conneted ok!");
		
		
		User u = new User();
		u.setId(1);
		u.setName("test");
		String path = zc.create("/jike5", u, CreateMode.PERSISTENT);
		System.out.println("created path:"+path);
	}
	
}
