package Study.Zookeeper.Balance.Server;

 
import org.I0Itec.zkclient.exception.ZkNoNodeException;

public class DefaultRegistProvider implements RegistProvider,IZkClientProvider {

	
	
	
	@Override
	public void regist(Object context) throws Exception {
		// 1:path
		// 2:serverData

		ZooKeeperRegistContext registContext = (ZooKeeperRegistContext) context;
		String path = registContext.getPath();
 

		try {
			zkClient.createEphemeral(path, registContext.getData());
		} catch (ZkNoNodeException e) {

			String parentDir = path.substring(0, path.lastIndexOf('/'));
			zkClient.createPersistent(parentDir, true);
			regist(registContext);
		}
	}

	@Override
	public void unRegist(Object context) throws Exception {
		// TODO Auto-generated method stub
		return;

	}

}
