package Zookeeper;

import org.apache.zookeeper.CreateMode;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
 
import org.junit.runners.MethodSorters;

import ZooKeeper.Base.ZookeeperUtils;
 

@FixMethodOrder(MethodSorters.DEFAULT)
public class ZookeeperUtilsAuthTest {

	
	@Test
	public void ZNode1AuthTest() {
		boolean bflag= ZookeeperUtils.createZNodeAuth("/TestAuth", "AuthData",CreateMode.PERSISTENT ) ;
		Assert.assertTrue(bflag);	
	}
	
	@Test
	public void Znode2DataTest() {
		 Assert.assertEquals(ZookeeperUtils.getZnodeData("/TestAuth"),"AuthData") ; 
	}
	
	@Test
	public void ZNode3Test() {
		boolean bflag= ZookeeperUtils.updateZNode("/TestAuth","updateAuthData") ; 
		Assert.assertTrue(bflag);	
	}
	
	@Test
	public void ZNode4Test() {
		boolean bflag= ZookeeperUtils.deleteZNode("/TestAuth") ; 
		Assert.assertTrue(bflag);	
	}
	
	
	
}
