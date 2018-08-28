package Study.MapReduce.Example;

import java.io.IOException;
import java.util.ArrayList;
 
import java.util.Collections;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import Study.MapReduce.DistributedJobBase;
 

public class CommonFriendsOne {

	public static class CommonFriendsOneMapper extends Mapper<LongWritable, Text, Text, Text> {
		private  Text k =new Text();
		private  Text v = new Text();
		
		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			 String line = value.toString();
			 String[] words=line.split(":");
			 String strUser=words[0];
			 v.set(strUser);
			 String[] friends=words[1].split(",");
			 for(String each :friends) {
				 k.set(each);
				 context.write(k, v);
			 }
			 
			 
		}
	}

	public static class CommonFriendsOneReducer extends Reducer<Text, Text, Text, Text> {
		@Override
		protected void reduce(Text key, Iterable<Text> values,  Context context)
				throws IOException, InterruptedException {
			 ArrayList<String> userList=new ArrayList<>();
			 for (Text each : values) {
				userList.add(each.toString());
			}
			 
			 Collections.sort(userList);
			 
			 for(int i=0;i<userList.size()-1;i++){
					for(int j=i+1;j<userList.size();j++){
						context.write(new Text(userList.get(i)+"-"+userList.get(j)), key);
					}
				}
			 
		}
	}

	public static void main(String[] args) throws Exception {
		String clsName = CommonFriendsOne.class.getSimpleName();
		boolean isLocaltion = true;
		Class<? extends Mapper> clsMapper = CommonFriendsOneMapper.class;
		Class<? extends Reducer> clsReducer = CommonFriendsOneReducer.class;
		Class<?> clsMapOutputKey = Text.class;
		Class<?> clsMapOutputValue = Text.class;
		Class<?> clsOutputKey = Text.class;
		Class<?> clsOutputValue = Text.class;
		DistributedJobBase.setJob(clsName, isLocaltion, clsMapper, clsReducer, 
				clsMapOutputKey, clsMapOutputValue, clsOutputKey, clsOutputValue);

	}

}
