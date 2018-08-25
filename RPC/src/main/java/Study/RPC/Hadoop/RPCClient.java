package Study.RPC.Hadoop;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
 

public class RPCClient {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		final ClientProtocol proxy=(ClientProtocol)RPC.waitForProxy(ClientProtocol.class
				, ClientProtocol.VERSION
				, new InetSocketAddress(RPCServer.SERVER_ADDRESS,RPCServer.SERVER_PORT)
				, new Configuration());
		String strRes=proxy.Hello("test");
		System.out.print(strRes);
		RPC.stopProxy(proxy);
		
	}

}
