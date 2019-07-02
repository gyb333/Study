package RPC.HTTP;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class ExecHttpClient {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        //url前加上http协议头，标明该请求为http请求
        String url = "https://www.baidu.com";
        //组装请求

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);


        //接收响应
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        byte[] byteArray = EntityUtils.toByteArray(entity);
        String result = new String(byteArray, "utf8");
        System.out.println(result);
    }

}
