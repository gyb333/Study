package ZooKeeper.Balance.Server;

import org.I0Itec.zkclient.ZkClient;

import ZooKeeper.ZkClient.ZkClientFactory;

public interface IZkClientProvider {
	
	public  final ZkClient  zkClient=ZkClientFactory.getInstance();
}
