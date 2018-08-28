package Study.MapReduce.DataType;

 
import java.io.IOException;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.PropertyConfigurator;



public class FlowCount {

	public void TreeMapTest() {
		 TreeMap<FlowBean, String> treeMap= new TreeMap<>(new Comparator<FlowBean>() {

			@Override
			public int compare(FlowBean o1, FlowBean o2) {
				int result=o2.getAmountFlow()-o1.getAmountFlow();
				if(result==0)
					result= o1.getPhone().compareTo(o2.getPhone());
				return result;
			}
		});
		 
		 Set<Entry<FlowBean, String>> entrySet=treeMap.entrySet();
		 for(Entry<FlowBean, String> each : entrySet) {
			 
		 }
		 
		 
		 
	}
	
	
	public static void main(String[] args) throws Exception {
		String path = System.getProperty("user.dir") ;
		PropertyConfigurator.configure(path + "\\Resources\\log4j.properties");
		
		Configuration conf = new Configuration();
 
		//设置本地提交
		conf.set("fs.defaultFS", "local");
		conf.set("mapreduce.framework.name", "local");
		 
		
		Job job =Job.getInstance(conf);
		
		job.setJarByClass(FlowCount.class);
		
		job.setMapperClass(FlowCountMapper.class);
		job.setReducerClass(FlowCountReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FlowBean.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);
		Path baseDir =new Path(path).getParent();
		
		Path output=new Path(baseDir.toString()+"\\HadoopData\\output\\FlowCount");
		FileSystem fs=FileSystem.get(conf);
		if(fs.exists(output))
			fs.delete(output);
		//fs.deleteOnExit(output);
		 
		
		FileInputFormat.setInputPaths(job, new Path(baseDir.toString()+"\\HadoopData\\input\\flow.log"));
		FileOutputFormat.setOutputPath(job, output);
		job.waitForCompletion(true);
		
		
		fs.deleteOnExit(new Path("\\tmp"));
		fs.deleteOnExit(new Path("\\usr"));
		
	}
}
