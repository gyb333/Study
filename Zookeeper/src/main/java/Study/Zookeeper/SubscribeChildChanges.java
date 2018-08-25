package Study.Zookeeper;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;

/**
 * 
 * 订阅节点的信息改变（创建节点，删除节点，添加子节点）
 * 
 *
 */
public class SubscribeChildChanges {

	private static class ZKChildListener implements IZkChildListener {
		/**
		 * handleChildChange： 用来处理服务器端发送过来的通知 parentPath：对应的父节点的路径
		 * currentChilds：子节点的相对路径
		 */
		public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {

			System.out.println(parentPath);
			System.out.println(currentChilds.toString());

		}

	}

	// zkClient.subscribeChildChanges("/testUserNode3", new ZKChildListener())

}
