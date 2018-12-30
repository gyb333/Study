package Study.MapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Random;


/**
 * 解决数据倾斜问题，让key增加随机数据,生成中间临时结果mapreduce，针对中间结果在做一个mapreduce
 * map阶段输出：排过序的<key,1> shuffle之后合并成<key,list<1,1,1,1,1>
 */
public class WordCountMapper extends Mapper<LongWritable,Text,Text,IntWritable> {

	private final static IntWritable one=new IntWritable(1);

	private Text word = new Text();

	private int tasks=0;

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		tasks=context.getNumReduceTasks();
	}

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String line =value.toString();
		String[] words = line.split(" ");

		for(String each:words) {
			word.set(each);
			new Random().nextInt(tasks);
			context.write(word, one);
		}
		
	}
	
	
}
