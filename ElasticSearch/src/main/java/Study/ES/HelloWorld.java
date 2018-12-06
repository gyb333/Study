package Study.ES;


import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;

import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * Created by zx on 2017/8/15.
 */
public class HelloWorld {

    public static void main(String[] args) {

        try {

            //设置集群名称
            Settings settings = Settings.builder()
                    .put("cluster.name", "es")
                    .build();
            //创建client
            TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(
                    //用java访问ES用的端口是9300
                    new TransportAddress(InetAddress.getByName("Master"), 9300),
                    new TransportAddress(InetAddress.getByName("Second"), 9300),
                    new TransportAddress(InetAddress.getByName("Slave"), 9300));
            //搜索数据（.actionGet()方法是同步的，没有返回就等待）
            GetResponse response = client.prepareGet("news", "fulltext", "1").execute().actionGet();
            //输出结果
            System.out.println(response);
            //关闭client
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
