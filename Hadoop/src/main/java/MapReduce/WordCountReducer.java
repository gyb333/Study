package MapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;


/**
 * reduce阶段输入：
 * key list<1,1,1,1>
 */
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable>{

	private IntWritable result = new IntWritable();
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0;	//重置每个Key的开始量
		Iterator<IntWritable> it = values.iterator();	
		while(it.hasNext()) {
			IntWritable val = it.next();
			sum += val.get();
		}
		result.set(sum);
		context.write(key, result);
		
	}

}
