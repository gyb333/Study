package Study.MapReduce.DataType;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class FlowCountMapper extends Mapper<LongWritable, Text, Text, FlowBean>{

	private Text mKey=new Text();
	private FlowBean mValue=new FlowBean();
	@Override
	protected void map(LongWritable key, Text value,  Context context)
			throws IOException, InterruptedException {
		String line =value.toString();
		String[] fields = line.split("\t");
		String phone=fields[1];
		int upFlow = Integer.parseInt(fields[fields.length-3]);
		int downFlow=Integer.parseInt(fields[fields.length-2]);
		mKey.set(phone);
		mValue.set(upFlow, downFlow, phone);
		context.write(mKey, mValue);
	}


}
