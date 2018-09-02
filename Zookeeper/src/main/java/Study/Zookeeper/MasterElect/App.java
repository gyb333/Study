package Study.Zookeeper.MasterElect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class App {
	//启动的服务个数
    private static final int        CLIENT_QTY = 10;
    
    
	public static void main(String[] args) throws Exception {
		 //保存所有服务的列表
        List<WorkServer>  workServers = new ArrayList<WorkServer>();

        try
        {
            for ( int i = 0; i < CLIENT_QTY; ++i )
            {
            	 
                //创建serverData
                NodeData runningData = new NodeData();
                runningData.setCid(Long.valueOf(i));
                runningData.setName("Client #" + i);
                //创建服务
                WorkServer  workServer = new WorkServer(runningData);
                
                workServers.add(workServer);
                workServer.start();
            }
            System.out.println("敲回车键退出！\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        }
        finally
        {
            System.out.println("Shutting down...");

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
