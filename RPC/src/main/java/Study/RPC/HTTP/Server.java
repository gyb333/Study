package Study.RPC.HTTP;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端
 *
 */
public class Server {
    
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(1234);
        while(true) {
            Socket client = server.accept();
            //读取请求数据
            Request request = ProtocolUtil.readRequest(client.getInputStream());
            //封装响应数据
            Response response = new Response();
            response.setEncode(Encode.UTF8.getValue());
            response.setResponse(request.getCommand().equals("HELLO") ? "hello!" : "bye bye");
            response.setResponseLength(response.getResponse().length());
            //响应到客户端
            ProtocolUtil.writeResponse(client.getOutputStream(), response);
        }
    }
}

