package Study.MapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


/**
 * map阶段输出：排过序的<key,1> shuffle之后合并成<key,list<1,1,1,1,1>
 */
public class WordCountMapper extends Mapper<LongWritable,Text,Text,IntWritable> {

	private final static IntWritable one=new IntWritable(1);

	private Text word = new Text();

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line =value.toString();
		String[] words = line.split(" ");

		for(String each:words) {
			word.set(each);
			context.write(word, one);
		}
		
	}
	
	
}
