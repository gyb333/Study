package Study.MapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable,Text,Text,IntWritable> {
	Text text = new Text();
	IntWritable iw=new IntWritable();

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line =value.toString();
		String[] words = line.split(" ");

		for(String each:words) {
			text.set(each);
			iw.set(1);
			context.write(text, iw);
		}
		
	}
	
	
}
