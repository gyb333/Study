package Study.MapReduce.DataType;


import Study.MapReduce.DistributedJob;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;


public class FlowCount extends DistributedJob{

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


	/**
	 * 13480253104     180, 180, 360
	 * 13502468823     7515, 110529, 118044
	 * 13560436666     8631, 111483, 120114
	 * 13560439658     10665, 117375, 128040
	 * 13602846565     12603, 120285, 132888
	 * 13660577991     19563, 120975, 140538
	 * 13719199419     19803, 120975, 140778
	 * 13726230503     22284, 145656, 167940
	 * 13726238888     24765, 170337, 195102
	 * 13760778710     24885, 170457, 195342
	 * 13826544101     25149, 170457, 195606
	 * 13922314466     28157, 174177, 202334
	 * 13925057413     39215, 222420, 261635
	 * 13926251106     39455, 222420, 261875
	 * 13926435656     39587, 223932, 263519
	 * 15013685858     43246, 227470, 270716
	 * 15920133257     46402, 230406, 276808
	 * 15989002119     48340, 230586, 278926
	 * 18211575961     49867, 232692, 282559
	 * 18320173382     59398, 235104, 294502
	 * 84138413        63514, 236536, 300050
	 *
	 * 根据手机所属省份分区
	 */
	public static void main(String[] args) throws Exception {
  
		String clsName = FlowCount.class.getSimpleName();
		boolean isLocaltion = false;
		Class<? extends Mapper> clsMapper = FlowCountMapper.class;
		Class<? extends Reducer> clsReducer = FlowCountReducer.class;
		Class<?> clsMapOutputKey = Text.class;
		Class<?> clsMapOutputValue = FlowBean.class;
		Class<?> clsOutputKey = Text.class;
		Class<?> clsOutputValue = FlowBean.class;
		new FlowCount().execJob(clsName, isLocaltion, clsMapper, clsReducer, 
				clsMapOutputKey, clsMapOutputValue, clsOutputKey, clsOutputValue
				,true);
 
	}
}
