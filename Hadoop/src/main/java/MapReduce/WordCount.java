package MapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

//https://github.com/4ttty/winutils
public class WordCount {

    private static final String path = System.getProperty("user.dir");

    static {
        try {

            InputStream is = WordCount.class.getResourceAsStream("/log4j.properties");
            PropertyConfigurator.configure(is);

            System.load("D:\\Program Files\\hadoop-3.0.0\\bin\\hadoop.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        boolean isLocaltion = true;
        String strOutput = "/bigdata/output/WordCount";
        String strInput = "/bigdata/input/WordCount";
        Path baseDir = new Path(path).getParent();

        String strLocalInput = baseDir.toString() + "/Study" + strInput;
        String strLocalOutput = baseDir.toString() + "/Study" + strOutput;

        Configuration conf = new Configuration();


        if (isLocaltion) {
            System.setProperty("hadoop.home.dir", "D:\\Program Files\\hadoop-3.0.0");
            // 设置本地提交
            conf.set("fs.defaultFS", "local");
            conf.set("mapreduce.framework.name", "local");
        } else {
            //设置用户
            System.setProperty("HADOOP_USER_NAME", "root");
            conf.set("mapreduce.app-submission.cross-platform", "true");
            conf.addResource("core-site.xml");
            conf.addResource("hdfs-site.xml");
            conf.addResource("yarn-site.xml");
            conf.addResource("mapred-site.xml");
            System.out.println(conf.get("mapreduce.framework.name", "test"));
        }

        Job job = Job.getInstance(conf);

        if (isLocaltion)
            job.setJarByClass(WordCount.class);

        else
            job.setJar("D:\\work\\Study\\Hadoop\\target\\Hadoop-0.0.1-SNAPSHOT.jar");


        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

//        //設置分區方案
        job.setPartitionerClass(MyPartitioner.class);
//        job.setNumReduceTasks(4);

        //job.setCombinerClass(MyCombiner.class);

        Path pathOutput;
        Path pathInput;
        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);
            if (isLocaltion) {
                strInput = strLocalInput;
                strOutput = strLocalOutput;
            }

            pathInput = new Path(strInput);
            pathOutput = new Path(strOutput);
            if (fs.exists(pathOutput)) {
                fs.delete(pathOutput, true);
            }
            // 上传到HDFS
            if (!isLocaltion) {
                if (fs.exists(pathInput)) {
                    fs.delete(pathInput, true);
                }
                FSDataOutputStream out = null;
                FileInputStream in = null;
                try {
                    out = fs.create(pathInput);
                    in = new FileInputStream(strLocalInput);
                    IOUtils.copyBytes(in, out, 1024, true);
                } finally {
                    IOUtils.closeStream(in);
                    IOUtils.closeStream(out);
                }

            }

            FileInputFormat.setInputPaths(job, pathInput);
            FileOutputFormat.setOutputPath(job, pathOutput);



            boolean res = job.waitForCompletion(true);
            if (isLocaltion) {
                fs.deleteOnExit(new Path("\\tmp"));
                fs.deleteOnExit(new Path("\\usr"));
            }
            System.exit(res ? 0 : 1);

        } finally {
            if (fs != null) {
                fs.close();
            }
        }


    }
}
