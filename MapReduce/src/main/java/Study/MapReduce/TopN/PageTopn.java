package Study.MapReduce.TopN;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

 
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.PropertyConfigurator;


 

public class PageTopn {
	

	
	
	
	public static class PageTopnMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
		 
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			 String line =value.toString();
			 String[] split=line.split(" ");
			 context.write(new Text(split[1]), new IntWritable(1));
		}
	}
	
	
	public static class PageTopnReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
		
		
		TreeMap<PageCount, Object> treeMap=new TreeMap<>();
		
		
		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		 
			int count=0;
			for(IntWritable each:values) {
				count+=each.get();
			}
			PageCount pageCount=new PageCount();
			pageCount.set(key.toString(), count);
			treeMap.put(pageCount, null);	
		}
		
		@Override
		protected void cleanup(Context context)
				throws IOException, InterruptedException {
			Configuration conf =context.getConfiguration();
			int top = conf.getInt("top.n", 5);
			
			Set<Entry<PageCount, Object>> es =treeMap.entrySet();
			int i=0;
			for(Entry<PageCount, Object> each :es) {
				String key=each.getKey().getPage();
				int value=each.getKey().getCount();
				context.write(new Text(key), new IntWritable(value));
				i++;
				if(i>=top)
					return;
			}
		}
		
		public class PageCount implements Comparable<PageCount>{
			private String page;
			private int count;
			
			 
			
			public void set(String page, int count) {
				this.page = page;
				this.count = count;
			}
			
			

			public String getPage() {
				return page;
			}
			public void setPage(String page) {
				this.page = page;
			}
			public int getCount() {
				return count;
			}
			public void setCount(int count) {
				this.count = count;
			}

			@Override
			public String toString() {
				return "PageCount [page=" + page + ", count=" + count + "]";
			}



			@Override
			public int compareTo(PageCount o) {
				int res=o.getCount()-this.getCount();
				return res==0?this.getPage().compareTo(o.getPage()):res;
			}
			
		}
		
	}
	
	
	public static void main(String[] args) throws Exception {
		String path = System.getProperty("user.dir");
		PropertyConfigurator.configure(path + "\\Resources\\log4j.properties");
		
		Configuration conf = new Configuration();
 
			//设置本地提交
		conf.set("fs.defaultFS", "local");
		conf.set("mapreduce.framework.name", "local");
		 
		
		Job job =Job.getInstance(conf);
		
		job.setJarByClass(PageTopn.class);
		
		job.setMapperClass(PageTopnMapper.class);
		job.setReducerClass(PageTopnReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		Path baseDir =new Path(path).getParent();
		
		Path output=new Path(baseDir.toString()+"\\HadoopData\\output\\PageTopn");
		FileSystem fs=FileSystem.get(conf);
		if(fs.exists(output))
			fs.delete(output);
		//fs.deleteOnExit(output);
		 
		
		FileInputFormat.setInputPaths(job, new Path(baseDir.toString()+"\\HadoopData\\input\\request.dat"));
		FileOutputFormat.setOutputPath(job, output);
		job.waitForCompletion(true);
		
		
		fs.deleteOnExit(new Path("\\tmp"));
		fs.deleteOnExit(new Path("\\usr"));
	}
	
}
