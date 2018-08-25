package Study.Zookeeper;

import org.I0Itec.zkclient.IZkDataListener;

/**
 * 
 *订阅节点的数据内容的变化
 *
 */
public class SubscribeDataChanges {
	private static class ZKDataListener implements IZkDataListener{
		 
		public void handleDataChange(String dataPath, Object data) throws Exception {
			
			System.out.println(dataPath+":"+data.toString());
		}
 
		public void handleDataDeleted(String dataPath) throws Exception {
			
			System.out.println(dataPath);
			
		}
       
		
	}
	
	
	//zkClient.subscribeDataChanges("/testUserNode", new ZKDataListener());
}
