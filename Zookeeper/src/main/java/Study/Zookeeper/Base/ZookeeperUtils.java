package Study.Zookeeper.Base;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

public class ZookeeperUtils {

	private static ZooKeeper zookeeper;
	private  static boolean IsExisted = false;
	
	static {

		try {
			zookeeper = ZookeeperFactory.getInstance();
			zookeeper.addAuthInfo("digest", "root:root".getBytes());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean createZNodeAuth(String path, String data, CreateMode mode) {

		try {
			if (zookeeper.exists(path, true) == null) {

				ACL aclIp = new ACL(Perms.READ|Perms.WRITE, new Id("ip", "10.228.222.65"));
				ACL aclDigest = new ACL(Perms.READ | Perms.WRITE,
						new Id("digest", DigestAuthenticationProvider.generateDigest("root:root")));
				ArrayList<ACL> acls = new ArrayList<ACL>();
				acls.add(aclDigest);
				acls.add(aclIp);
				

				zookeeper.create(path, data.getBytes(), acls, mode);
				return true;
			}
		} catch (KeeperException | NoSuchAlgorithmException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("znode" + path + "结点已存在");
		return false;
	}

	
	
	/**
	 * 创建znode结点
	 * 
	 * @param path 结点路径
	 * @param data 结点数据
	 * @return true 创建结点成功 false表示结点存在
	 * @throws Exception
	 */
	public static boolean createZNode(String path, String data, CreateMode mode) {

		try {
			if (zookeeper.exists(path, true) == null) {
				zookeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, mode);
				return true;
			}
		} catch (KeeperException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("znode" + path + "结点已存在");
		return false;
	}

	/**
	 * 创建永久znode结点
	 * 
	 * @param path 结点路径
	 * @param data 结点数据
	 * @return true 创建结点成功 false表示结点存在
	 * @throws Exception
	 */
	public static boolean createPZNode(String path, String data) {
		return createZNode(path, data, CreateMode.PERSISTENT);
	}

	/**
	 * 创建临时znode结点
	 * 
	 * @param path 结点路径
	 * @param data 结点数据
	 * @return true 创建结点成功 false表示结点存在
	 * @throws Exception
	 */
	public static boolean createZENode(String path, String data) {
		return createZNode(path, data, CreateMode.EPHEMERAL);
	}

	/**
	 * 修改znode
	 * 
	 * @param path 结点路径
	 * @param data 结点数据
	 * @return 修改结点成功 false表示结点不存在
	 */
	public static boolean updateZNode(String path, String data) {

		Stat stat = null;
		try {
			if ((stat = zookeeper.exists(path, true)) != null) {
				zookeeper.setData(path, data.getBytes(), stat.getVersion());
				return true;
			}
		} catch (KeeperException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 
	 * 删除结点
	 * 
	 * @param path 结点
	 * @return true 删除键结点成功 false表示结点不存在
	 */
	public static boolean deleteZNode(String path) {

		Stat stat = null;
		try {
			if ((stat = zookeeper.exists(path, true)) != null) {
				List<String> subPaths = zookeeper.getChildren(path, false);
				if (subPaths.isEmpty()) {
					zookeeper.delete(path, stat.getVersion());
					return true;
				} else {
					for (String subPath : subPaths) {
						deleteZNode(path + "/" + subPath);
					}
				}
			}
		} catch (KeeperException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 取到结点数据
	 * 
	 * @param path 结点路径
	 * @return null表示结点不存在 否则返回结点数据
	 */
	public static String getZnodeData(String path) {

		String data = null;

		Stat stat = null;
		try {
			if ((stat = zookeeper.exists(path, true)) != null) {
				data = new String(zookeeper.getData(path, true, stat));
			} else {
				System.out.println("znode:" + path + ",不存在");
			}
		} catch (KeeperException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return data;
	}

}
