package Zookeeper;

import static org.junit.Assert.assertNotNull;

import ZooKeeper.ZkClient.User;
import ZooKeeper.ZkClient.ZkClientFactory;
import ZooKeeper.ZkClient.ZkClientUtils;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public class ZKClientTest {

	@Test
	public void Test0ZkClient() {
		ZkClient zkClient = ZkClientFactory.getInstance();
		assertNotNull(zkClient);

	}

	@Test
	public void Test1createNode() {
		User user = new User();
		user.setId(Integer.valueOf(1));
		user.setName("ZkClient");
		ZkClient zkClient = ZkClientFactory.getInstance();
		String path = "/ZkClient";
		zkClient.createPersistent(path, user);

		Assert.assertTrue(true);
	}
	
	@Test
	public void Test2ReadNode() {
		Stat stat = new Stat();
		User user = ZkClientUtils.getNode("/ZkClient", stat);
		System.out.println(user.toString());
		System.out.println(stat);
		Assert.assertTrue(true);
	}
	
	@Test
	public void Test3updateNode() {
		String path = "/ZkClient";
		if (ZkClientUtils.existsNode(path))
			ZkClientUtils.updateNode(path, "updateNode", -1);
		Assert.assertTrue(true);
	}

	

	
	@Test
	public void Test4deleteNode() {
		ZkClientUtils.deleteNode("/ZkClient");
		Assert.assertTrue(true);
	}
}
