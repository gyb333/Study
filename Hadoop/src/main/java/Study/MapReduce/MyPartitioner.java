package Study.MapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class MyPartitioner extends Partitioner<Text, IntWritable>{

	@Override
	public int getPartition(Text key, IntWritable value, int numPartitions) {
		String strKey=key.toString();
		if(strKey.matches("^[A-Z]*")) {
			return 0%numPartitions;
		}else if(strKey.matches("^[a-z]*")) {
			return 1%numPartitions;
		}else if(strKey.matches("^[0-9]*")) {
			return 2%numPartitions;
		}else{
			return 3%numPartitions;
		}
 
	}

}
