package Study.MapReduce.DataType;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
 
 

public class FlowCountReducer extends Reducer<Text, FlowBean, Text, FlowBean>{

	int upSum=0;
	int downSum=0;
	  @Override
	protected void reduce(Text key, Iterable<FlowBean> values, Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		 for(FlowBean each :values) {
			 upSum+=each.getUpFlow();
			 downSum+=each.getDownFlow();
		 }
		 
		 context.write(key, new FlowBean(upSum, downSum, key.toString()));
	}
 

 
	 

	
}
