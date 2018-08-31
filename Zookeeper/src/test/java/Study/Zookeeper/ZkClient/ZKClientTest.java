package Study.Zookeeper.ZkClient;

import static org.junit.Assert.assertNotNull;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import Study.Zookeeper.ZkClient.ZkClientFactory;
import Study.Zookeeper.ZkClient.ZkClientUtils;

 
@FixMethodOrder(MethodSorters.DEFAULT)
public class ZKClientTest {

	@Test
	public void TestZkClient() {
		ZkClient zkClient= ZkClientFactory.getInstance();
		assertNotNull(zkClient);
		
	}
	
	@Test
	public void Test1createNode() {
		User user =new User( );
		user.setId(Integer.valueOf(1));
		user.setName("ZkClient");
		ZkClient zkClient= ZkClientFactory.getInstance();
		String path ="/ZkClient";
		zkClient.createPersistent(path, user);
	 
		Assert.assertTrue(true); 
	}
	
	@Test
	public void Test2updateNode() {
		ZkClientUtils.updateNode("/ZkClient", "updateNode", 1);
		Assert.assertTrue(true);
	}
	
	@Test
	public void Test3deleteNode() {
		ZkClientUtils.deleteNode("/ZkClient");
		Assert.assertTrue(true);
	}
}
