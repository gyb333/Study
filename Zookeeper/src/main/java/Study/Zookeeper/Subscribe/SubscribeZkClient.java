package Study.Zookeeper.Subscribe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

public class SubscribeZkClient {

	

    //需要多少个workserver
	private static final int  CLIENT_QTY = 5;
	 
  
    
    
    public static void main(String[] args) throws Exception
    {
        //用来存储所有的workservers
        List<WorkServer>  workServers = new ArrayList<WorkServer>();
        ManagerServer manageServer = null;

        try
        {
        	ConfigNodeData initConfig = new ConfigNodeData();
        	initConfig.setDbPwd("root");
        	initConfig.setDbUrl("jdbc:mysql://localhost:3306/mydb");
        	initConfig.setDbUser("root");
        	
        	 
        	manageServer = new ManagerServer(initConfig);
        	manageServer.start();
        	
        	//根据定义的work服务个数，创建服务器后注册，然后启动
            for ( int i = 0; i < CLIENT_QTY; ++i )
            {
                
                ServerNodeData serverData = new ServerNodeData();
                serverData.setId(i);
                serverData.setName("WorkServer#"+i);
                serverData.setAddress("192.168.1."+i);

                WorkServer  workServer = new WorkServer( serverData,  initConfig);
                workServers.add(workServer);
                workServer.start();	                
                
            }	            
            System.out.println("敲回车键退出！\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
            
        }finally{
        	//将workserver和client给关闭
        	
            System.out.println("Shutting down...");
            manageServer.stop(); 
            for ( WorkServer workServer : workServers )
            {
            	try {
            		workServer.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}           	
            }
            
        }
    }
	
	
	
	
	
	
	
	
	
	
}
