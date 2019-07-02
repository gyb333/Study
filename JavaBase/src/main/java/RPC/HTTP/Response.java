package RPC.HTTP;

/**
 * 协议响应
 * 
 *
 */
public class Response {
    /**
     * 编码
     */
    private byte encode;
    /**
     * 响应
     */
    private String response;
    /**
     * 响应长度
     */
    private int responseLength;

    
    
    
    
    
    public byte getEncode() {
        return encode;
    }

    public void setEncode(byte encode) {
        this.encode = encode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getResponseLength() {
        return responseLength;
    }

    public void setResponseLength(int responseLength) {
        this.responseLength = responseLength;
    }

    @Override
    public String toString() {
        return "Response [encode=" + encode + ", response=" + response + ", responseLength=" + responseLength + "]";
    }
    
}
