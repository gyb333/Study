package Study.MapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;

import java.io.IOException;

/**
 * 输入的内容
 * 1.html
 * hadoop hadoop hadoop is good
 * 2.html
 * hadoop hbase hbase is better is good
 * 3.html
 * hbase hadoop hive hive is nice is good
 * <p>
 * 输出的内容
 * <p>
 * hadoop:1.html:3,2.html:1,3.html:1
 * is:1.html:1,2.html:2,3.html:2
 */
public class InvertedIndex   {

    public  static  void main(String[] args) throws Exception {
        DistributedJob job= new DistributedJob(){
            @Override
            public void setJobConfig(Job job) {
                job.setCombinerClass(MyCombiner.class);
                job.setOutputFormatClass(SequenceFileOutputFormat.class);
            }
        };
        job.execJob("InvertedIndex",true,MyMapper.class,MyReducer.class
                ,Text.class,Text.class,Text.class,Text.class,false
        );
    }


    /**
     * 输入的内容
     * 1.html
     * hadoop hadoop hadoop is good
     * 2.html
     * hadoop hbase hbase is better is good
     * 3.html
     * hbase hadoop hive hive is nice is good
     *
     * 输出的内容
     *
     * hadoop_1.html 1
     * hadoop_1.html 1
     * hadoop_1.html 1
     * is_1.html 1
     * good_1.html 1
     *
     */
    public static class MyMapper extends Mapper<LongWritable, Text, Text, Text> {

        private final static Text one = new Text("1");

        private Text word = new Text();

        private String fileName;
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //获取文件名字
            FileSplit fs = (FileSplit) context.getInputSplit();
            fileName = fs.getPath().getName();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String line = value.toString();
            String[] words = line.split(" ");
            for (String each : words) {
                word.set(each + "_" + fileName);
                context.write(word, one);
            }
        }
    }

    /**
     * 输入：
     * hadoop_1.html 1
     * hadoop_1.html 1
     * hadoop_1.html 1
     * is_1.html 1
     * good_1.html 1
     * 输出：
     * hadoop 1.html:3
     * is 1.html:1
     * good 1.html:1
     */
    public static class MyCombiner extends Reducer<Text, Text, Text, Text> {
        private Text rKey=new Text();
        private Text rValue=new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String str[] = key.toString().split("_");
            int sum = 0;
            for (Text each : values) {
                sum += Integer.parseInt(each.toString());
            }
            rKey.set(str[0]);
            rValue.set(str[1] + ":" +sum);
            context.write(rKey, rValue);
        }
    }





        /**
     * 输入的数据格式
     * key			 value
     * hadoop 		1.html:3
     * hadoop 		2.html:1
     * 输出的内容
     * <p>
     * hadoop:1.html:3,2.html:1,3.html:1
     * is:1.html:1,2.html:2,3.html:2
     */
    public static class MyReducer extends Reducer<Text, Text, Text, Text> {
        private Text Result=new Text();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
                String value="";
                for(Text each:values){
                    value+=","+each.toString();
                }
                Result.set(value.substring(1));
                context.write(key,Result);
            }
    }

}
