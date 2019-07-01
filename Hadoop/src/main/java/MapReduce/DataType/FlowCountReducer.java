package MapReduce.DataType;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


public class FlowCountReducer extends Reducer<Text, FlowBean, Text, FlowBean>{

	int upSum=0;
	int downSum=0;
	private FlowBean mValue=new FlowBean();
	  @Override
	protected void reduce(Text key, Iterable<FlowBean> values, Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		 for(FlowBean each :values) {
			 upSum+=each.getUpFlow();
			 downSum+=each.getDownFlow();
		 }
		  mValue.set(upSum, downSum, key.toString());
		 context.write(key, mValue);
	}
 

 
	 

	
}
