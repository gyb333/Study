package Study.Zookeeper;

import org.junit.Assert;
import org.junit.Test;

import Study.Zookeeper.Base.ZookeeperUtils;

 

 

public class ZookeeperUtilsTest {

	@Test
	public void deleteZNodeTest() {
		boolean bflag= ZookeeperUtils.deleteZNode("/Test");
		Assert.assertTrue(bflag);	
	}
	
	@Test
	public void createZNodeTest() {
		boolean bflag= ZookeeperUtils.createPZNode("/Test", "data");
		Assert.assertTrue(bflag);	
	}
	
	
	@Test
	public void createZENodeTest() {
		boolean bflag= ZookeeperUtils.createZENode("/Test/createZENode", "edata");
		Assert.assertTrue(bflag);	
	}
	
	@Test
	public void updateZNodeTest() {
		boolean bflag= ZookeeperUtils.updateZNode("/Test", "updateZNode");
		Assert.assertTrue(bflag);	
	}
	
	
	@Test
	public void getZnodeDataTest() {
		String res= ZookeeperUtils.getZnodeData("/Test");
		Assert.assertEquals(res, "updateZNode");
	}
	
}
