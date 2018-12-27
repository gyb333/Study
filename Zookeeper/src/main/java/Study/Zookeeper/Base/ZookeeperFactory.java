package Study.Zookeeper.Base;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;


/**
 * 1）Zookeeper的Watcher是一次性的，每次触发之后都需要重新进行注册； 
（2）Session超时之后没有实现重连机制； 
（3）异常处理繁琐，Zookeeper提供了很多异常，对于开发人员来说可能根本不知道该如何处理这些异常信息； 
（4）只提供了简单的byte[]数组的接口，没有提供针对对象级别的序列化； 
（5）创建节点时如果节点存在抛出异常，需要自行检查节点是否存在； 
（6）删除节点无法实现级联删除；
 *
 */

public class ZookeeperFactory {

	private static String connectString="Master:2181,Second:2181,slave:2181";

	private static int sessionTimeout = 10 * 1000;

	public static ZooKeeper getInstance() throws IOException, InterruptedException {
		
		return getInstance(connectString,sessionTimeout);
	}
	
	
	
	public static ZooKeeper getInstance(String connectString,int sessionTimeout) throws IOException, InterruptedException {
		
		final CountDownLatch signal = new CountDownLatch(1);
		ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			// 为避免连接还未完成就执行zookeeper的get/create/exists操作引起的
			//（KeeperErrorCode = ConnectionLoss)  等Zookeeper的连接完成才返回实例
			public void process(WatchedEvent event) {
				if (event.getState() == Event.KeeperState.SyncConnected) {
					if (event.getType()==EventType.None && null==event.getPath()){
						signal.countDown();
					}
					
				}

			}
		});
		signal.await(sessionTimeout, TimeUnit.MILLISECONDS);
		return zooKeeper;
	}

	
	
	
	public static String getConnectionString() {
		return connectString;
	}

 

	public static int getSessionTimeout() {
		return sessionTimeout;
	}

 
}
