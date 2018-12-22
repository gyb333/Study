package Study.MapReduce.Example;


import Study.MapReduce.DistributedJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class OrderTopN  extends DistributedJob{



	public static class OrderIdPartitioner extends Partitioner<OrderBean, NullWritable>{

		@Override
		public int getPartition(OrderBean key, NullWritable value, int numPartitions) {

			return (key.getOrderId().hashCode() & Integer.MAX_VALUE) % numPartitions;
		}
	 	
	}

	public static class OrderIdGroupingComparator extends WritableComparator{

		public OrderIdGroupingComparator() {
			super(OrderBean.class,true);
		}
		
		@Override
		public int compare(WritableComparable a, WritableComparable b) {
			OrderBean o1 = (OrderBean) a;
			OrderBean o2 = (OrderBean) b;
			
			return o1.getOrderId().compareTo(o2.getOrderId());
		}
		   
		
	}
	
	@Override
	public void setJobConfig(Job job) {
		
		job.setPartitionerClass(OrderIdPartitioner.class);
		job.setGroupingComparatorClass(OrderIdGroupingComparator.class);
		
	}
	
	public static class OrderTopNMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable>{
		private OrderBean orderBean = new OrderBean();
		private static final NullWritable v = NullWritable.get();
		
		@Override
		protected void map(LongWritable key, Text value,Context context)
				throws IOException, InterruptedException {
			
			String[] fields = value.toString().split(",");
			
			orderBean.set(fields[0], fields[1], fields[2], Float.parseFloat(fields[3]), Integer.parseInt(fields[4]));
			
			context.write(orderBean,v);
		}
		
	}


	/**
	 * reduce获取数据是已经排过序的
	 */
	public static class OrderTopNReducer extends Reducer< OrderBean, NullWritable,  OrderBean, NullWritable>{

		private  int topn;
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			Configuration conf = context.getConfiguration();
			topn = conf.getInt("top.n", 3);
		}

		@Override
		protected void reduce(OrderBean key, Iterable<NullWritable> values,Context context)
				throws IOException, InterruptedException {
			int i=0;
			for (NullWritable v : values) {
				context.write(key, v);
				if(++i==topn) return;
			}
			
		}
		
		
	}


	/**
	 * 分组topN
	 */
	public static void main(String[] args) throws Exception {
		 
		String clsName = OrderTopN.class.getSimpleName();
		boolean isLocaltion = false;
		Class<? extends Mapper> clsMapper = OrderTopNMapper.class;
		Class<? extends Reducer> clsReducer = OrderTopNReducer.class;
		Class<?> clsMapOutputKey = OrderBean.class;
		Class<?> clsMapOutputValue = NullWritable.class;
		Class<?> clsOutputKey = OrderBean.class;
		Class<?> clsOutputValue = NullWritable.class;
		new OrderTopN().execJob(clsName, isLocaltion, clsMapper, clsReducer, 
				clsMapOutputKey, clsMapOutputValue, clsOutputKey, clsOutputValue
				,true);

	}

}
