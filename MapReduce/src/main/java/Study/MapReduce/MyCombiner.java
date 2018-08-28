package Study.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 
 * combiner是reducer的提前執行，key相同優化

 *
 */
public class MyCombiner extends Reducer<Text, IntWritable, Text, Text>{

	@Override
	protected void reduce(Text key, Iterable<IntWritable> values ,Context context) throws IOException, InterruptedException {
		 String str[] =key.toString().split("-");
		 int count=0;
		 for(IntWritable each:values) {
			 count+=each.get();
		 }
		 context.write(new Text(str[0]), new Text(str[1]+":"+count));
	}
	
}
