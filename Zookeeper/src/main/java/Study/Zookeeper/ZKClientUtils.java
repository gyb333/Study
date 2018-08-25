package Study.Zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class ZKClientUtils {

	public static final String zkServers = "Master:2181,Second:2181,slave:2181";
	public static final int sessionTimeout = 10 * 1000;
	public static final int connectionTimeout = 10 * 1000;

	public ZkClient zkClient = null;

	public ZKClientUtils() {
		zkClient = new ZkClient(zkServers, sessionTimeout, connectionTimeout, new SerializableSerializer());
	}

	public String createZNode(String path, Object data, CreateMode mode) {
		return zkClient.create(path, data, mode);
	}

	public Object getData(String path) {
		Stat stat = new Stat();
		// 获取 节点中的对象
		return zkClient.readData(path, stat);

	}

	public void updateData(String path, Object data) {

		// 更新节点中的对象
		zkClient.writeData(path, data);

	}

	public boolean deleteZNode(String path) {
		// 删除单独一个节点，返回true表示成功
		return zkClient.delete(path);
	}

	public boolean deleteZNodeByCascade(String path) {
		// 返回 true表示节点成功 ，false表示删除失败
		// 删除含有子节点的节点
		return zkClient.deleteRecursive(path);
	}
}
