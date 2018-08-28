package Study.MapReduce.DataType;

 
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

 
import org.apache.hadoop.io.Text;
 
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
 

import Study.MapReduce.DistributedJobBase;
 

public class FlowCount {

	public void TreeMapTest() {
		TreeMap<FlowBean, String> treeMap = new TreeMap<>(new Comparator<FlowBean>() {

			@Override
			public int compare(FlowBean o1, FlowBean o2) {
				int result = o2.getAmountFlow() - o1.getAmountFlow();
				if (result == 0)
					result = o1.getPhone().compareTo(o2.getPhone());
				return result;
			}
		});

		Set<Entry<FlowBean, String>> entrySet = treeMap.entrySet();
		for (Entry<FlowBean, String> each : entrySet) {

		}

	}



	public static void main(String[] args) throws Exception {
		// execJob();
		String clsName = FlowCount.class.getSimpleName();
		boolean isLocaltion = false;
		Class<? extends Mapper> clsMapper = FlowCountMapper.class;
		Class<? extends Reducer> clsReducer = FlowCountReducer.class;
		Class<?> clsMapOutputKey = Text.class;
		Class<?> clsMapOutputValue = FlowBean.class;
		Class<?> clsOutputKey = Text.class;
		Class<?> clsOutputValue = FlowBean.class;
		DistributedJobBase.setJob(clsName, isLocaltion, clsMapper, clsReducer, 
				clsMapOutputKey, clsMapOutputValue, clsOutputKey, clsOutputValue);
	}
}
