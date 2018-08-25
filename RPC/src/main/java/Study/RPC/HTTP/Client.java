package Study.RPC.HTTP;

import java.net.Socket;
import java.net.URLEncoder;

 

 
 
 

/**
 * 客户端
 *
 */
public class Client {
    
    public static void main(String[] args) throws Exception {
        //组装请求数据
        Request request = new Request();
        request.setCommand("HELLO");
        request.setCommandLength(request.getCommand().length());
         
        request.setEncode(Encode.UTF8.getValue());
        Socket client = new Socket("127.0.0.1", 1234);
        //发送请求
        ProtocolUtil.writeRequest(client.getOutputStream(), request);
        //读取相应
        Response response = ProtocolUtil.readResponse(client.getInputStream());
        System.out.println(response);
    }
}




