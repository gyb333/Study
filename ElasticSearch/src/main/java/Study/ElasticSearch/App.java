package Study.ElasticSearch;


import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )  {
        try {
            HelloLucene.testDelete();
//            HelloLucene.testCreate();
            HelloLucene.testSearch();
//            HelloLucene.testUpdate();
            System.out.println("---------MultiField-----------");
            HelloLucene.testMultiField();
            System.out.println("---------MatchAll-----------");
            HelloLucene.testMatchAll();
            System.out.println("---------BooleanQuery-----------");
            HelloLucene.testBooleanQuery();
            System.out.println("---------QueryParser-----------");
            HelloLucene.testQueryParser();
            System.out.println("---------RangeQuery-----------");
            HelloLucene.testRangeQuery();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
