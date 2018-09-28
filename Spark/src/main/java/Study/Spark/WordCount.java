package Study.Spark;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class WordCount {

	public static final String inputPath = "";
	public static final String outputPath = "";

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName("WordCount");
		JavaSparkContext jsc = new JavaSparkContext(conf);
		JavaRDD<String> lines = jsc.textFile(inputPath);
		JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
		JavaPairRDD<String, Integer> wordAndOne=words.mapToPair(w->new Tuple2<>(w,1));
		JavaPairRDD<String, Integer> reduced=wordAndOne.reduceByKey((m,n)->m+n);
		JavaPairRDD<Integer, String>   swaped=reduced.mapToPair(f->f.swap());
		JavaPairRDD<Integer, String> sorted =swaped.sortByKey(false);
		JavaPairRDD<String, Integer> result=sorted.mapToPair(f->f.swap());
		result.saveAsTextFile(outputPath);
		jsc.stop();
		
	}

}
