package Study.ES;

import org.elasticsearch.action.ActionListener;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;

import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;


import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;


/**
 * Created by zx on 2017/9/5.
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/5.4/index.html
 */
public class EsCRUD {

    private TransportClient client = null;

    @Before
    public void init() throws Exception {
        //设置集群名称
        Settings settings = Settings.builder()
                .put("cluster.name", "es")
                //自动感知的功能（可以通过当前指定的节点获取所有es节点的信息）
                .put("client.transport.sniff", true)
                .build();
        //创建client
        client = new PreBuiltTransportClient(settings).addTransportAddresses(
                //用java访问ES用的端口是9300
                new TransportAddress(InetAddress.getByName("Master"), 9300),
                new TransportAddress(InetAddress.getByName("Second"), 9300),
                new TransportAddress(InetAddress.getByName("Slave"), 9300));

    }


    @Test
    public void testCreate() throws IOException {

        IndexResponse response = client.prepareIndex("gamelog", "users", "1")
                .setSource(
                        jsonBuilder()
                                .startObject()
                                    .field("username", "老赵")
                                    .field("gender", "male")
                                    .field("birthday", new Date())
                                    .field("fv", 9999)
                                    .field("message", "trying out Elasticsearch")
                                .endObject()
                ).get();


    }

    //查找一条
    @Test
    public void testGet() throws IOException {
        GetResponse response = client.prepareGet("gamelog", "users", "1").get();
        System.out.println(response.getSourceAsString());
    }


    //查找多条
    @Test
    public void testMultiGet() throws IOException {
        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("gamelog", "users", "1")
                .add("gamelog", "users", "2", "3")
                .add("news", "fulltext", "1")
                .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                String json = response.getSourceAsString();
                System.out.println(json);
            }
        }
    }

    @Test
    public void testUpdate() throws Exception {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("gamelog");
        updateRequest.type("users");
        updateRequest.id("2");
        updateRequest.doc(
                jsonBuilder()
                    .startObject()
                        .field("fv", 999.9)
                    .endObject());
        client.update(updateRequest).get();
    }

    @Test
    public void testDelete() {
        DeleteResponse response = client.prepareDelete("gamelog", "users", "2").get();
        System.out.println(response);
    }

    @Test
    public void testDeleteByQuery() {
        BulkByScrollResponse response =
                DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                        //指定查询条件
                        .filter(QueryBuilders.matchQuery("username", "老段"))
                        //指定索引名称
                        .source("gamelog")
                        .get();

        long deleted = response.getDeleted();
        System.out.println(deleted);


    }

    //异步删除
    @Test
    public void testDeleteByQueryAsync() {
        DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("gender", "male"))
                .source("gamelog")
                .execute(new ActionListener<BulkByScrollResponse>() {
                    @Override
                    public void onResponse(BulkByScrollResponse response) {
                        long deleted = response.getDeleted();
                        System.out.println("数据删除了");
                        System.out.println(deleted);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }
                });
        try {
            System.out.println("异步删除");
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRange() {

        QueryBuilder qb = rangeQuery("fv")
                // [88.99, 10000)
                .from(88.99).to(10000)
                .includeLower(true)
                .includeUpper(false);

        SearchResponse response = client.prepareSearch("gamelog").setQuery(qb).get();

        System.out.println(response);
    }




}
