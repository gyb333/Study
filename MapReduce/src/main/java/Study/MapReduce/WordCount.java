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
<<<<<<< HEAD:src/main/java/Hadoop/MapReduce/WordCount.java
=======

import Study.HDFS.HDFSUtils;


>>>>>>> 3459df293fc53661da5ff61d7835913327788975:MapReduce/src/main/java/Study/MapReduce/WordCount.java
 




public class WordCount {

	public static Configuration getConfiguration() {
		Configuration conf =new Configuration();
		conf.set("fs.defaultFS", "hdfs://ns");
		conf.set("dfs.nameservices", "ns");
		conf.set("dfs.ha.namenodes.ns" , "nn1,nn2");
		conf.set("dfs.namenode.rpc-address.ns.nn1","hdfs://Master:9000");
		conf.set("dfs.namenode.rpc-address.ns.nn2" ,"hdfs://Second:9000");
		conf.set("dfs.client.failover.proxy.provider.ns","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
		
//		conf.set("mapreduce.framework.name", "yarn");
//		conf.set("yarn.resourcemanager.cluster-id", "yrc");
//		conf.set("yarn.resourcemanager.ha.rm-ids", "rm1,rm2");

//		conf.set("yarn.resourcemanager.hostname.rm1", "Master");
//		conf.set("yarn.resourcemanager.hostname.rm2", "Master");
//		conf.set("yarn.client.failover-proxy-provider", "org.apache.hadoop.yarn.client.ConfiguredRMFailoverProxyProvider");
		
		return conf;
	}
	
	static {
	    try {
	    	//强制加载dll
	    	System.load("D:\\work\\hadoop-3.0.0\\bin\\hadoop.dll");
	    } catch (UnsatisfiedLinkError e) {
	      System.err.println("Native code library failed to load.\n" + e);
	      System.exit(1);
	    }
	  }
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
//		String path =System.getProperty("user.dir")+"\\Resources\\";
//		PropertyConfigurator.configure(path+"log4j.properties");
		
<<<<<<< HEAD:src/main/java/Hadoop/MapReduce/WordCount.java
		String path =System.getProperty("user.dir")+"\\Resources\\";
		PropertyConfigurator.configure(path+"log4j.properties");
		
		//设置环境变量
		System.setProperty("hadoop.home.dir", "D:\\work\\hadoop-3.0.0");
		
		Configuration conf =getConfiguration();
		//设置job运行要访问的默认文件系统
	 
=======
		
		Configuration conf =HDFSUtils.getConfiguration();
		//设置job运行要访问的默认文件系统

		//设置job提交到哪运行
		conf.set("mapreduce.framework.name", "yarn");
		 
>>>>>>> 3459df293fc53661da5ff61d7835913327788975:MapReduce/src/main/java/Study/MapReduce/WordCount.java
		
		//设置job提交到哪运行
		
		 
		 
		
		//设置Job运行时系统参数
		System.setProperty("HADOOP_USER_NAME", "root");
		
		//如果要从windows系统运行这个job提交客户端程序，需要加跨平台提交参数
		conf.set("mapreduce.app-submission.cross-platform", "true");
		
		Job job =Job.getInstance(conf);
		
		//job.setJar("D:\\work\\Hadoop\\target\\Hadoop-0.0.1-SNAPSHOT.jar");
		//设置job获取jar包的位置
		job.setJarByClass(WordCount.class);
		
		
		
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.setInputPaths(job, new Path("/wordcount/input"));
		FileOutputFormat.setOutputPath(job, new Path("/wordcount/output/2/"));
		
		job.setNumReduceTasks(3);
		
		boolean res= job.waitForCompletion(true);
		
		System.exit(res?0:1);
		
		
		
		
		
		
	}
}
