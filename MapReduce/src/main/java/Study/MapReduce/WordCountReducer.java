package Study.MapReduce;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int count=0;
		Iterator<IntWritable> it = values.iterator();	
		while(it.hasNext()) {
			IntWritable value = it.next();
			count+=value.get();
		}
		context.write(key, new IntWritable(count));
		
	}

}
