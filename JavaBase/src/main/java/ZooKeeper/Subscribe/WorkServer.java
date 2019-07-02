package ZooKeeper.Subscribe;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

import com.alibaba.fastjson.JSON;

import ZooKeeper.ZkClient.ZkClientFactory;
 
 



public class WorkServer {

	private String serversPath;
	private String configPath;
	
	private  ZkClient zkClient=ZkClientFactory.getInstance();
	
	private ConfigNodeData configNodeData;
	
    private ServerNodeData serverNodeData;
    
	private IZkDataListener dataListener;//数据监听器
	
	
	/**
	 * 
	 * @param configPath 代表config节点的路径
	 * @param serversPath 代表servers节点的路径
	 * @param serverData   代表当前服务器的基本信息
	 * @param zkClient     底层与zookeeper集群通信的组件
	 * @param initconfig   当前服务器的初始配置
	 */
	public WorkServer(ServerNodeData serverNodeData, ConfigNodeData initconfig){
		
		this.configPath = ZkConfig.CONFIG_PATH;
		this.serversPath = ZkConfig.SERVERS_PATH;
	    this.serverNodeData = serverNodeData;
		this.configNodeData = initconfig;
		
		/**
		 * dataListener 用于监听config节点的数据改变
		 */
		this.dataListener = new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String arg0) throws Exception {
				
			}
			
			/**
			 * 当数据的值改变时处理的
			 * Object data,这个data是将ServerConfig对象转成json字符串存入
			 * 可以通过参数中的Object data 拿到当前数据节点最新的配置信息
			 * 拿到这个data信息后将它反序列化成ServerConfig对象，然后更新到自己的serverconfig属性中
			 */
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				String retJson = new String((byte[])data);
				ConfigNodeData serverConfigLocal = JSON.parseObject(retJson,ConfigNodeData.class);
			    //更新配置
			    updateConfig(serverConfigLocal);
			    System.out.println("new work server config is:"+serverConfigLocal.toString());
			}
		};
		
	}
	
	/**
	 * 服务的启动
	 */
	public void start(){
		System.out.println("work server start...");
		initRunning();
	}
	
	/**
	 * 服务的停止
	 */
	public void stop(){
		System.out.println("work server stop...");
		//取消监听
		zkClient.unsubscribeDataChanges(configPath, dataListener);
	
	}
	
	/**
	 * 服务器的初始化
	 */
	private void initRunning(){
		registMeToZookeeper();
		//订阅config节点的改变
		zkClient.subscribeDataChanges(configPath, dataListener);
	}
	
	/**
	 * 启动时向zookeeper注册自己
	 */
	private void registMeToZookeeper(){
		//向zookeeper中注册自己的过程其实就是向servers节点下注册一个临时节点
		//构造临时节点
		String mePath = serversPath.concat("/").concat(serverNodeData.getAddress());
	    try{
	    	//存入是将json序列化
			zkClient.createEphemeral(mePath, JSON.toJSONString(serverNodeData).getBytes());	
	    } catch (ZkNoNodeException e) {
	    	//父节点不存在
			zkClient.createPersistent(serversPath, true);
			registMeToZookeeper();
		}
		
	}
	
	/**
	 * 当监听到zookeeper中config节点的配置信息改变时，要读取配置信息来更新自己的配置信息
	 */
	private void updateConfig(ConfigNodeData serverConfig){
		this.configNodeData = serverConfig;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		if(zkClient!=null) {
			zkClient.close();
			zkClient=null;
		}
	}
	
}
