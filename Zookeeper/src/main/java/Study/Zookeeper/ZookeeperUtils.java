package Study.Zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZookeeperUtils {

	private ZooKeeper zookeeper;

	public ZookeeperUtils() {

		try {
			zookeeper = ZookeeperFactory.getInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ZookeeperUtils(Watcher watcher) {

		try {
			zookeeper = ZookeeperFactory.getInstance(watcher);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ZooKeeper getZooKeeper() {
		return zookeeper;
	}

	/**
	 * 创建znode结点
	 * 
	 * @param path 结点路径
	 * @param data 结点数据
	 * @return true 创建结点成功 false表示结点存在
	 * @throws Exception
	 */
	public boolean addZnodeData(String path, String data, CreateMode mode) {

		try {
			if (zookeeper.exists(path, true) == null) {
				zookeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, mode);
				return true;
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
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
	public boolean addPZnode(String path, String data) {
		return addZnodeData(path, data, CreateMode.PERSISTENT);
	}

	/**
	 * 创建临时znode结点
	 * 
	 * @param path 结点路径
	 * @param data 结点数据
	 * @return true 创建结点成功 false表示结点存在
	 * @throws Exception
	 */
	public boolean addZEnode(String path, String data) {
		return addZnodeData(path, data, CreateMode.EPHEMERAL);
	}

	/**
	 * 修改znode
	 * 
	 * @param path 结点路径
	 * @param data 结点数据
	 * @return 修改结点成功 false表示结点不存在
	 */
	public boolean updateZnode(String path, String data) {

		Stat stat = null;
		try {
			if ((stat = zookeeper.exists(path, true)) != null) {
				zookeeper.setData(path, data.getBytes(), stat.getVersion());
				return true;
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
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
	public boolean deleteZnode(String path) {

		Stat stat = null;
		try {
			if ((stat = zookeeper.exists(path, true)) != null) {
				List<String> subPaths = zookeeper.getChildren(path, false);
				if (subPaths.isEmpty()) {
					zookeeper.delete(path, stat.getVersion());
					return true;
				} else {
					for (String subPath : subPaths) {
						deleteZnode(path + "/" + subPath);
					}
				}
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
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
	public String getZnodeData(String path) {

		String data = null;

		Stat stat = null;
		try {
			if ((stat = zookeeper.exists(path, true)) != null) {
				data = new String(zookeeper.getData(path, true, stat));
			} else {
				System.out.println("znode:" + path + ",不存在");
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

}
