package RPC.Simple;

import RPC.SayHelloService;
import RPC.SayHelloServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RPCTest {

    public static void main(String[] args) throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    RPCServer serviceServer = new RPCServerImpl(8088);
                    serviceServer.register(SayHelloService.class, SayHelloServiceImpl.class);
                    serviceServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        SayHelloService service = RPCClient.getRemoteProxyObj(SayHelloService.class, new InetSocketAddress("localhost", 8088));
        System.out.println(service.sayHello("hello"));
    }
}
