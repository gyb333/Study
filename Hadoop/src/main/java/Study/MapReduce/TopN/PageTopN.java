package Study.MapReduce.TopN;

import Study.MapReduce.DistributedJob;

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

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class PageTopN {


    public static class PageTopNMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        private Text mKey = new Text();
        private final static IntWritable one = new IntWritable(1);
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString();
            String[] split = line.split(" ");
            mKey.set(split[1]);
            context.write(mKey, one);
        }
    }


    public static class PageTopNReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        TreeSet<PageCount> treeSet =new TreeSet<>();



        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

            int count = 0;
            for (IntWritable each : values) {
                count += each.get();
            }
            PageCount pageCount = new PageCount();
            pageCount.set(key.toString(), count);

            treeSet.add(pageCount);


        }

        private Text cKey=new Text();
        private IntWritable cValue=new IntWritable();

        @Override
        protected void cleanup(Context context)
                throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            int top = conf.getInt("top.n", 5);

//            Set<Entry<PageCount, Object>> es = treeMap.entrySet();
//            int i = 0;
//            for (Entry<PageCount, Object> each : es) {
//                String key = each.getKey().getPage();
//                cKey.set(key);
//                int value = each.getKey().getCount();
//                cValue.set(value);
//                context.write(cKey, cValue);
//                i++;
//                if (i >= top)
//                    return;
//            }
            int i = 0;
            for(PageCount each:treeSet){
                cKey.set(each.getPage());
                cValue.set(each.getCount());
                context.write(cKey, cValue);
                i++;
                if (i >= top)
                    return;
            }

        }

        public class PageCount implements Comparable<PageCount> {
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
                int res = o.getCount() - this.getCount();
                return res == 0 ? this.getPage().compareTo(o.getPage()) : res;
            }

        }

    }


    public static void main(String[] args) throws Exception {
        new DistributedJob().execJob(PageTopN.class.getSimpleName(),
                true,PageTopNMapper.class,PageTopNReducer.class,
                Text.class,IntWritable.class,Text.class,IntWritable.class,true
        );

    }

}
