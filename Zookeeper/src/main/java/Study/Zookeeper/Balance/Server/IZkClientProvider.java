package Study.Zookeeper.Balance.Server;

import org.I0Itec.zkclient.ZkClient;

import Study.Zookeeper.ZkClient.ZkClientFactory;

public interface IZkClientProvider {
	
	public  final ZkClient  zkClient=ZkClientFactory.getInstance();
}
