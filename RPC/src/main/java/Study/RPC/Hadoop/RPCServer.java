package Study.RPC.Hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Server;


public class RPCServer {
    final static String SERVER_ADDRESS = "localhost";
    final static int SERVER_PORT = 12345;

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

//		final Server server = RPC.getServer(instance, SERVER_ADDRESS, SERVER_PORT, 
//				new Configuration());

        Configuration conf = new Configuration();
        Server server = new RPC.Builder(conf)
                .setProtocol(ClientProtocol.class)
                .setInstance(new ClientProtocolImpl())
                .setBindAddress(SERVER_ADDRESS)
                .setPort(SERVER_PORT)
                .setNumHandlers(5).build();

        server.start();

    }

}
