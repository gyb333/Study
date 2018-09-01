package Study.Zookeeper.Curator;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

public class CuratorUtils {

	public static final CuratorFramework client = CuratorFactory.getInstance(true);

	public static void main(String[] args) throws Exception {
		System.out.println(DigestAuthenticationProvider.generateDigest("super:super"));
		// TODO Auto-generated method stub
		String path = "/CuratorNode";
		String data = "CuratorData";
		String zkPath;

		ACL aclIp = new ACL(Perms.READ | Perms.WRITE, new Id("ip", "192.168.0.32"));
		ACL aclDigest = new ACL(Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest("root:root")));
		ArrayList<ACL> acls = new ArrayList<ACL>();
		acls.add(aclDigest);
		// acls.add(aclIp);

		Stat stat = client.checkExists().forPath(path);
		if (stat == null) {
			zkPath = client.create().creatingParentsIfNeeded()
					.withMode(CreateMode.PERSISTENT)
					.withACL(acls)
					.forPath(path, data.getBytes());

			System.out.println(zkPath);
		}
		
		
		final NodeCache cache = new NodeCache(client, path);
		cache.start();
		cache.getListenable().addListener(new NodeCacheListener() {

			public void nodeChanged() throws Exception {
				// TODO Auto-generated method stub
				byte[] ret = cache.getCurrentData().getData();
				System.out.println("new data:" + new String(ret));
			}
		});

		final PathChildrenCache ccache = new PathChildrenCache(client, path, true);
		ccache.start();
		ccache.getListenable().addListener(new PathChildrenCacheListener() {

			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				// TODO Auto-generated method stub
				switch (event.getType()) {
				case CHILD_ADDED:
					System.out.println("CHILD_ADDED:" + event.getData());
					break;
				case CHILD_UPDATED:
					System.out.println("CHILD_UPDATED:" + event.getData());
					break;
				case CHILD_REMOVED:
					System.out.println("CHILD_REMOVED:" + event.getData());
					break;
				default:
					break;
				}
			}
		});

		
		
		
		
		
		stat = new Stat();
		byte[] ret = client.getData().storingStatIn(stat).forPath(path);
		String strNode = new String(ret);
		System.out.println(strNode);
		System.out.println(stat);

		client.setData().withVersion(stat.getVersion()).forPath(path, (strNode + "123").getBytes());

		ret = client.getData().forPath(path);
		strNode = new String(ret);
		System.out.println(strNode);

		zkPath = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).withACL(acls)
				.forPath(path + "/eNode", (data + "eNode").getBytes());
		System.out.println(zkPath);

		List<String> cList = client.getChildren().forPath(path);

		System.out.println(cList.toString());

		client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);

	}

}
