package Study.ES;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
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
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class EsSQL {

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

    /**
     curl -H "Content-Type:application/json" -XPUT 'http://Master:9200/player_info/player/1' -d '{ "name": "curry", "age": 29, "salary": 3500,"team": "war", "position": "pg"}'
     curl -H "Content-Type:application/json" -XPUT 'http://Master:9200/player_info/player/2' -d '{ "name": "thompson", "age": 26, "salary": 2000,"team": "war", "position": "pg"}'
     curl -H "Content-Type:application/json" -XPUT 'http://Master:9200/player_info/player/3' -d '{ "name": "irving", "age": 25, "salary": 2000,"team": "cav", "position": "pg"}'
     curl -H "Content-Type:application/json" -XPUT 'http://Master:9200/player_info/player/4' -d '{ "name": "green", "age": 26, "salary": 2000,"team": "war", "position": "pf"}'
     curl -H "Content-Type:application/json" -XPUT 'http://Master:9200/player_info/player/5' -d '{ "name": "james", "age": 33, "salary": 4000,"team": "cav", "position": "sf"}'
     */
    @Test
    public void testAddPlayer() throws IOException {

        IndexResponse response = client.prepareIndex("player_info", "player", "1")
                .setSource(
                        jsonBuilder()
                                .startObject()
                                .field("name", "James")
                                .field("age", 33)
                                .field("salary", 3000)
                                .field("team", "cav")
                                .field("position", "sf")
                                .endObject()
                ).get();


    }

    /**
     * https://elasticsearch.cn/article/102
     *
     * select team, count(*) as player_count from player group by team;
     */
    @Test
    public void testAgg1() {

        //指定索引和type
        SearchRequestBuilder builder = client.prepareSearch("player_info").setTypes("player");
        //按team分组然后聚合，但是并没有指定聚合函数
        TermsAggregationBuilder teamAgg = AggregationBuilders.terms("player_count").field("team");
        //添加聚合器
        builder.addAggregation(teamAgg);
        //触发
        SearchResponse response = builder.execute().actionGet();
        //System.out.println(response);
        //将返回的结果放入到一个map中
        Map<String, Aggregation> aggMap = response.getAggregations().getAsMap();
//        Set<String> keys = aggMap.keySet();
//
//        for (String key: keys) {
//            System.out.println(key);
//        }

//        //取出聚合属性
        StringTerms terms = (StringTerms) aggMap.get("player_count");



//        //依次迭代出分组聚合数据
//        for (Terms.Bucket bucket : terms.getBuckets()) {
//            //分组的名字
//            String team = (String) bucket.getKey();
//            //count，分组后一个组有多少数据
//            long count = bucket.getDocCount();
//            System.out.println(team + " " + count);
//        }

        Iterator<StringTerms.Bucket> teamBucketIt = terms.getBuckets().iterator();
        while (teamBucketIt .hasNext()) {
            Terms.Bucket bucket = teamBucketIt.next();
            String team = (String) bucket.getKey();

            long count = bucket.getDocCount();

            System.out.println(team + " " + count);
        }

    }

    /**
     * select team, position, count(*) as pos_count from player group by team, position;
     */
    @Test
    public void testAgg2() {
        SearchRequestBuilder builder = client.prepareSearch("player_info").setTypes("player");
        //指定别名和分组的字段
        TermsAggregationBuilder teamAgg = AggregationBuilders.terms("team_name").field("team");
        TermsAggregationBuilder posAgg= AggregationBuilders.terms("pos_count").field("position");
        //添加两个聚合构建器
        builder.addAggregation(teamAgg.subAggregation(posAgg));
        //执行查询
        SearchResponse response = builder.execute().actionGet();
        //将查询结果放入map中
        Map<String, Aggregation> aggMap = response.getAggregations().getAsMap();
        //根据属性名到map中查找
        StringTerms teams = (StringTerms) aggMap.get("team_name");
        //循环查找结果
        for (Terms.Bucket teamBucket : teams.getBuckets()) {
            //先按球队进行分组
            String team = (String) teamBucket.getKey();
            Map<String, Aggregation> subAggMap = teamBucket.getAggregations().getAsMap();
            StringTerms positions = (StringTerms) subAggMap.get("pos_count");
            //因为一个球队有很多位置，那么还要依次拿出位置信息
            for (Terms.Bucket posBucket : positions.getBuckets()) {
                //拿到位置的名字
                String pos = (String) posBucket.getKey();
                //拿出该位置的数量
                long docCount = posBucket.getDocCount();
                //打印球队，位置，人数
                System.out.println(team + " " + pos + " " + docCount);
            }


        }
    }


    /**
     * select team, max(age) as max_age from player group by team;
     */
    @Test
    public void testAgg3() {
        SearchRequestBuilder builder = client.prepareSearch("player_info").setTypes("player");
        //指定安球队进行分组
        TermsAggregationBuilder teamAgg = AggregationBuilders.terms("team_name").field("team");
        //指定分组求最大值
        MaxAggregationBuilder maxAgg = AggregationBuilders.max("max_age").field("age");
        //分组后求最大值
        builder.addAggregation(teamAgg.subAggregation(maxAgg));
        //查询
        SearchResponse response = builder.execute().actionGet();
        Map<String, Aggregation> aggMap = response.getAggregations().getAsMap();
        //根据team属性，获取map中的内容
        StringTerms teams = (StringTerms) aggMap.get("team_name");
        for (Terms.Bucket teamBucket : teams.getBuckets()) {
            //分组的属性名
            String team = (String) teamBucket.getKey();
            //在将聚合后取最大值的内容取出来放到map中
            Map<String, Aggregation> subAggMap = teamBucket.getAggregations().getAsMap();
            //取分组后的最大值
            InternalMax ages = (InternalMax)subAggMap.get("max_age");
            double max = ages.getValue();
            System.out.println(team + " " + max);
        }
    }

    /**
     * select team, avg(age) as avg_age, sum(salary) as total_salary from player group by team;
     */
    @Test
    public void testAgg4() {
        SearchRequestBuilder builder = client.prepareSearch("player_info").setTypes("player");
        //指定分组字段
        TermsAggregationBuilder termsAgg = AggregationBuilders.terms("team_name").field("team");
        //指定聚合函数是求平均数据
        AvgAggregationBuilder avgAgg = AggregationBuilders.avg("avg_age").field("age");
        //指定另外一个聚合函数是求和
        SumAggregationBuilder sumAgg = AggregationBuilders.sum("total_salary").field("salary");
        //分组的聚合器关联了两个聚合函数
        builder.addAggregation(termsAgg.subAggregation(avgAgg).subAggregation(sumAgg));
        SearchResponse response = builder.execute().actionGet();
        Map<String, Aggregation> aggMap = response.getAggregations().getAsMap();
        //按分组的名字取出数据
        StringTerms teams = (StringTerms) aggMap.get("team_name");
        for (Terms.Bucket teamBucket : teams.getBuckets()) {
            //获取球队名字
            String team = (String) teamBucket.getKey();
            Map<String, Aggregation> subAggMap = teamBucket.getAggregations().getAsMap();
            //根据别名取出平均年龄
            InternalAvg avgAge = (InternalAvg)subAggMap.get("avg_age");
            //根据别名取出薪水总和
            InternalSum totalSalary = (InternalSum)subAggMap.get("total_salary");
            double avgAgeValue = avgAge.getValue();
            double totalSalaryValue = totalSalary.getValue();
            System.out.println(team + " " + avgAgeValue + " " + totalSalaryValue);
        }
    }


    /**
     * select team, sum(salary) as total_salary from player group by team order by total_salary desc;
     */
    @Test
    public void testAgg5() {
        SearchRequestBuilder builder = client.prepareSearch("player_info").setTypes("player");
        //按team进行分组，然后指定排序规则
        TermsAggregationBuilder termsAgg = AggregationBuilders.terms("team_name")
                .field("team").order(BucketOrder.aggregation("total_salary ", true));
        SumAggregationBuilder sumAgg = AggregationBuilders.sum("total_salary").field("salary");
        builder.addAggregation(termsAgg.subAggregation(sumAgg));
        SearchResponse response = builder.execute().actionGet();
        Map<String, Aggregation> aggMap = response.getAggregations().getAsMap();
        StringTerms teams = (StringTerms) aggMap.get("team_name");
        for (Terms.Bucket teamBucket : teams.getBuckets()) {
            String team = (String) teamBucket.getKey();
            Map<String, Aggregation> subAggMap = teamBucket.getAggregations().getAsMap();
            InternalSum totalSalary = (InternalSum)subAggMap.get("total_salary");
            double totalSalaryValue = totalSalary.getValue();
            System.out.println(team + " " + totalSalaryValue);
        }
    }
}
