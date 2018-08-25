package Study.MapReduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.PropertyConfigurator;

import Study.HDFS.HDFSUtils;


 

public class WordCount {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
//		String path =System.getProperty("user.dir")+"\\Resources\\";
//		PropertyConfigurator.configure(path+"log4j.properties");
		
		
		Configuration conf =HDFSUtils.getConfiguration();
		//设置job运行要访问的默认文件系统

		//设置job提交到哪运行
		conf.set("mapreduce.framework.name", "yarn");
		 
		
		
		
		//设置Job运行时系统参数
		System.setProperty("HADOOP_USER_NAME", "root");
		
		//如果要从windows系统运行这个job提交客户端程序，需要加跨平台提交参数
		conf.set("mapreduce.app-submission.cross-platform", "true");
		
		Job job =Job.getInstance(conf);
		
		//设置job获取jar包的位置
		job.setJarByClass(WordCount.class);
		
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.setInputPaths(job, new Path("/wordcount/input"));
		FileOutputFormat.setOutputPath(job, new Path("/wordcount/output"));
		
		job.setNumReduceTasks(3);
		
		boolean res= job.waitForCompletion(true);
		
		System.exit(res?0:1);
		
		
		
		
		
		
	}
}
