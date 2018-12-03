package Study.ElasticSearch;



import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

public class HelloLucene {

    private  static final  String indexPath = "D:\\work\\lucene\\index";
    /**
     * 往用lucene写入数据
     * @throws IOException
     */

    public static void testCreate() throws IOException {
        Article article = new Article();
        long r= new Random().nextInt(100);
        article.setId(108L+r);
        article.setAuthor("老王");
        article.setTitle("学习大数据");
        article.setContent("学数据，迎娶丁老师！");
        article.setUrl("http://www.edu360.cn/a10011");


        FSDirectory fsDirectory = FSDirectory.open(Paths.get(indexPath));
        //创建一个标准分词器，一个字分一次
        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer(true);
        //写入索引的配置，设置了分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        //指定了写入数据目录和配置
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);
        //创建一个文档对象
        Document document = article.toDocument();

        //通过IndexWriter写入
        indexWriter.addDocument(document);
        indexWriter.close();
    }


    public static void testSearch() throws IOException, ParseException {

        Analyzer analyzer = new IKAnalyzer(true);
        //Analyzer analyzer = new IKAnalyzer(true);
        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        //索引查询器
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        String queryStr = "数据";
        //创建一个查询条件解析器
        QueryParser parser = new QueryParser("content", analyzer);
        //对查询条件进行解析
        Query query = parser.parse(queryStr);

        //TermQuery将查询条件当成是一个固定的词
        //Query query = new TermQuery(new Term("url", "http://www.edu360.cn/a10010"));
        //在【索引】中进行查找
        TopDocs topDocs = indexSearcher.search(query, 10);

        //获取到查找到的文文档ID和得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            //从索引中查询到文档的ID，
            int doc = scoreDoc.doc;
            //在根据ID到文档中查找文档内容
            Document document = indexSearcher.doc(doc);
            //将文档转换成对应的实体类
            Article article = Article.parseArticle(document);
            System.out.println(article);
        }

        directoryReader.close();
    }


    public static void testDelete() throws IOException, ParseException {

        Analyzer analyzer = new IKAnalyzer(true);
        FSDirectory fsDirectory = FSDirectory.open(Paths.get(indexPath));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);

        //Term词条查找，内容必须完全匹配，不分词
        //indexWriter.deleteDocuments(new Term("content", "学好"));

        //QueryParser parser = new QueryParser("title", analyzer);
        //Query query = parser.parse("大数据老师");

        //LongPoint是建立索引的
//        Query query = LongPoint.newRangeQuery("id", 99L, 150L);
        Query query = LongPoint.newExactQuery("id", 108L);


        indexWriter.deleteDocuments(query);

        indexWriter.commit();
        indexWriter.close();
    }

    /**
     * lucene的update比较特殊，update的代价太高，先删除，然后在插入
     * @throws IOException
     * @throws ParseException
     */

    public static void testUpdate() throws IOException, ParseException {


        StandardAnalyzer analyzer = new StandardAnalyzer();
        FSDirectory fsDirectory = FSDirectory.open(Paths.get(indexPath));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);


        Article article = new Article();
        article.setId(168L);
        article.setAuthor("老赵");
        article.setTitle("学好大数据，要找赵老师");
        article.setContent("迎娶白富美，走上人生巅峰！！！");
        article.setUrl("http://www.edu360.cn/a111");
        Document document = article.toDocument();

        indexWriter.updateDocument(new Term("author", "老王"), document);

        indexWriter.commit();
        indexWriter.close();
    }

    /**
     * 可以从多个字段中查找
     * @throws IOException
     * @throws ParseException
     */

    public static void testMultiField() throws IOException, ParseException {


        Analyzer analyzer = new IKAnalyzer(true);
        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        String[] fields = {"title", "content"};
        //多字段的查询转换器
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
        Query query = queryParser.parse("老师");

        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            Article article = Article.parseArticle(document);
            System.out.println(article);
        }

        directoryReader.close();
    }

    /**
     * 查找全部的数据
     * @throws IOException
     * @throws ParseException
     */

    public static void testMatchAll() throws IOException, ParseException {


        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Query query = new MatchAllDocsQuery();

        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            Article article = Article.parseArticle(document);
            System.out.println(article);
        }

        directoryReader.close();
    }

    /**
     * 布尔查询，可以组合多个查询条件
     * @throws Exception
     */

    public static void testBooleanQuery() throws Exception {

        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Query query1 = new TermQuery(new Term("title", "大数据"));
        Query query2 = new TermQuery(new Term("content", "白富美"));
        BooleanClause bc1 = new BooleanClause(query1, BooleanClause.Occur.MUST);
        BooleanClause bc2 = new BooleanClause(query2, BooleanClause.Occur.MUST_NOT);
        BooleanQuery boolQuery = new BooleanQuery.Builder().add(bc1).add(bc2).build();
        System.out.println(boolQuery);

        TopDocs topDocs = indexSearcher.search(boolQuery, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            Article article = Article.parseArticle(document);
            System.out.println(article);
        }

        directoryReader.close();
    }


    public static void testQueryParser() throws Exception {

        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        //创建一个QueryParser对象。参数1：默认搜索域 参数2：分析器对象。
        QueryParser queryParser = new QueryParser("title", new IKAnalyzer(true));

        //Query query = queryParser.parse("数据");
        Query query = queryParser.parse("title:学好 OR title:学习");
        System.out.println(query);

        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            Article article = Article.parseArticle(document);
            System.out.println(article);
        }

        directoryReader.close();
    }



    public static void testRangeQuery() throws Exception {

        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);


        Query query = LongPoint.newRangeQuery("id", 108L, 168L);

        System.out.println(query);

        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            Article article = Article.parseArticle(document);
            System.out.println(article);
        }

        directoryReader.close();
    }

}
