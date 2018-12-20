package Study.MapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public interface JobBase {



	void setJobConfig(Job job);
	
	Configuration getConfiguration();
	
	void execJob(String clsName, boolean isLocaltion, Class<? extends Mapper> clsMapper,
                 Class<? extends Reducer> clsReducer, Class<?> clsMapOutputKey, Class<?> clsMapOutputValue,
                 Class<?> clsOutputKey, Class<?> clsOutputValue,boolean isFile) throws Exception;
}
