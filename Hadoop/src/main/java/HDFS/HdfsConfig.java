package HDFS;

public class HdfsConfig {

	public  static final String FS_DEFAULTFS=ConfigUtil.getStringValue("fs.defaultFS");	
	
	public static final String DFS_NAMESERVICE=ConfigUtil.getStringValue("dfs.nameservices");
	
	public static final String DFS_HA_NAMENODES_NS="nn1,nn2";	//ConfigUtil.getStringValue("dfs.ha.namenodes.ns");
	
	public static final String DFS_NAMENODE_RPC_ADDRESS_NS_NN1=ConfigUtil.getStringValue("dfs.namenode.rpc.address.ns.nn1");
	
	public static final String DFS_NAMENODE_RPC_ADDRESS_NS_NN2=ConfigUtil.getStringValue("dfs.namenode.rpc.address.ns.nn2");
	
	public static final String DFS_CLIENT_FAILOVER_PROXY_PROVIDER_NS=ConfigUtil.getStringValue("dfs.client.failover.proxy.provider.ns");
	
	
	
	
	
	
	
	
	
	
	
}
