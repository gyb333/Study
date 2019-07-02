package ZooKeeper.Balance.Client;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import ZooKeeper.Balance.Server.ServerData;
import ZooKeeper.ZkClient.ZkClientFactory;



public class DefaultBalanceProvider extends AbstractBalanceProvider<ServerData> {

 
	private final String serversPath;
	private final ZkClient zkClient=ZkClientFactory.getInstance();
	
 

	public DefaultBalanceProvider(String serversPath) {
		this.serversPath = serversPath;
	}

	@Override
	protected ServerData balanceAlgorithm(List<ServerData> items) {
		// TODO Auto-generated method stub
		if (items.size()>0){
			Collections.sort(items);
			return items.get(0);
		}else{
			return null;
		}
	}

	@Override
	protected List<ServerData> getBalanceItems() {
		// TODO Auto-generated method stub
		
		List<ServerData> sdList = new ArrayList<ServerData>();
		List<String> children = zkClient.getChildren(this.serversPath);
		for(int i=0; i<children.size();i++){
			ServerData sd = zkClient.readData(serversPath+"/"+children.get(i));
			sdList.add(sd);
		}		
		return sdList;
		
	}

}
