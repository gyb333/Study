package RPC.HTTP;

import java.io.InputStream;
import java.io.OutputStream;

public class ProtocolUtil {
    
    public static void writeRequest(OutputStream out, Request request) {
        try {
            out.write(request.getEncode());
            //write一个int值会截取其低8位传输，丢弃其高24位，因此需要将基本类型转化为字节流
            //java采用Big Endian字节序，而所有的网络协议也都是以Big Endian字节序来进行传输，所以再进行数据的传输和接收时，需要先将数据转化成Big Endian字节序
            //out.write(request.getCommandLength());
            out.write(int2ByteArray(request.getCommandLength()));
            out.write(Encode.GBK.getValue() == request.getEncode() ? request.getCommand().getBytes("GBK") : request.getCommand().getBytes("UTF8"));
            out.flush();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * 将响应输出到客户端
     * @param os
     * @param response
     */
    public static void writeResponse(OutputStream os, Response response) {
        try {
            os.write(response.getEncode());
            os.write(int2ByteArray(response.getResponseLength()));
            os.write(Encode.GBK.getValue() == response.getEncode() ? response.getResponse().getBytes("GBK") : response.getResponse().getBytes("UTF8"));
            os.flush();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static Request readRequest(InputStream is) {
        Request request = new Request();
        try {
            //读取编码
            byte [] encodeByte = new byte[1];
            is.read(encodeByte);
            byte encode = encodeByte[0];
            //读取命令长度
            byte [] commandLengthByte = new byte[4];//缓冲区
            is.read(commandLengthByte);
            int commandLength = byte2Int(commandLengthByte);
            //读取命令
            byte [] commandByte = new byte[commandLength];
            is.read(commandByte);
            String command = Encode.GBK.getValue() == encode ? new String(commandByte, "GBK") : new String(commandByte, "UTF8");
            //组装请求返回
            request.setEncode(encode);
            request.setCommand(command);
            request.setCommandLength(commandLength);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return request;
    }
    
    public static Response readResponse(InputStream is) {
        Response response = new Response();
        try {
            byte [] encodeByte = new byte[1];
            is.read(encodeByte);
            byte encode = encodeByte[0];
            byte [] responseLengthByte = new byte[4];
            is.read(responseLengthByte);
            int commandLength = byte2Int(responseLengthByte);
            byte [] responseByte = new byte[commandLength];
            is.read(responseByte);
            String resContent = Encode.GBK.getValue() == encode ? new String(responseByte, "GBK") : new String(responseByte, "UTF8");
            response.setEncode(encode);
            response.setResponse(resContent);
            response.setResponseLength(commandLength);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return response;
    }
    
    public static int byte2Int(byte [] bytes) {
        int num = bytes[3] & 0xFF;
        num |= ((bytes[2] << 8) & 0xFF00);
        num |= ((bytes[1] << 16) & 0xFF0000);
        num |= ((bytes[0] << 24) & 0xFF000000);
        return num;
    }
    
    public static byte[] int2ByteArray(int i) {
        byte [] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }
    
}
