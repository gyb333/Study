package Study.MapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 
 * combiner��reducer����ǰ���У�key��ͬ����

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
