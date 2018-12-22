package Study.MapReduce;

import Study.HDFS.FileUtils;
import Study.HDFS.HDFSUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class DistributedJob implements JobBase {

    private static final String path = System.getProperty("user.dir");

    private Job job;
    static {
        try {
            InputStream is = WordCount.class.getResourceAsStream("/log4j.properties");
            PropertyConfigurator.configure(is);
            //PropertyConfigurator.configure(path + "\\Resources\\log4j.properties");

//			System.load("D:\\Program Files\\hadoop-3.0.0\\bin\\hadoop.dll");

            System.setProperty("HADOOP_USER_NAME", "root");


        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }

    public Configuration getConfiguration() {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://ns");
        conf.set("dfs.nameservices", "ns");
        conf.set("dfs.ha.namenodes.ns", "nn1,nn2");
        conf.set("dfs.namenode.rpc-address.ns.nn1", "hdfs://Master:9000");
        conf.set("dfs.namenode.rpc-address.ns.nn2", "hdfs://Second:9000");
        conf.set("dfs.client.failover.proxy.provider.ns",
                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        conf.set("mapred.remote.os", "Linux");

        return conf;
    }

    @Override
    public void setJobConfig(Job job) {

    }


    public void execJob(String clsName, boolean isLocaltion, Class<? extends Mapper> clsMapper,
                        Class<? extends Reducer> clsReducer, Class<?> clsMapOutputKey, Class<?> clsMapOutputValue,
                        Class<?> clsOutputKey, Class<?> clsOutputValue,boolean isFile) throws Exception {


        String strOutput = "/bigdata/output/" + clsName;
        String strInput = "/bigdata/input/" + clsName;
        Path baseDir = new Path(path).getParent();
        String strLocalInput = baseDir.toString() + "/Study" + strInput;
        String strLocalOutput = baseDir.toString() + "/Study" + strOutput;
        //String osName = System.getProperty("os.name");


        Configuration conf = new Configuration();    // Ĭ��ֻ����core-default.xml core-site.xml


        conf.set("mapreduce.app-submission.cross-platform", "true");
        if (isLocaltion) {
            conf.set("fs.defaultFS", "local");
            conf.set("mapreduce.framework.name", "local");
            System.setProperty("hadoop.home.dir", "D:\\Program Files\\hadoop-3.0.0");
        } else {
            //�����û�
            System.setProperty("HADOOP_USER_NAME", "root");
            conf.set("mapreduce.app-submission.cross-platform", "true");
            conf.addResource("core-site.xml");
            conf.addResource("hdfs-site.xml");
            conf.addResource("yarn-site.xml");
            conf.addResource("mapred-site.xml");
        }

        Job job = Job.getInstance(conf);

        if (isLocaltion)
            job.setJarByClass(DistributedJob.class);

        else
            job.setJar("D:\\work\\Study\\Hadoop\\target\\Hadoop-0.0.1-SNAPSHOT.jar");

        job.setMapperClass(clsMapper);


        job.setMapOutputKeyClass(clsMapOutputKey);
        job.setMapOutputValueClass(clsMapOutputValue);

        job.setReducerClass(clsReducer);
        job.setOutputKeyClass(clsOutputKey);
        job.setOutputValueClass(clsOutputValue);
        //默认的输入组件
        job.setInputFormatClass(TextInputFormat.class);
        //默认的输出组件
        job.setOutputFormatClass(TextOutputFormat.class);
//        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        setJobConfig(job);

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
                fs.delete(pathOutput);
            }

            if (!isLocaltion) {
                if (fs.exists(pathInput)) {
                    fs.delete(pathInput);
                }
                if(isFile) {
                    HDFSUtils.UploadFile(fs,strInput,strLocalInput);
                }else{
                    File[] files = FileUtils.GetFiles(strLocalInput);
                    for(File file :files){
                        HDFSUtils.UploadFile(fs,strInput+"/"+file.getName(), file.getPath());
                    }
                }

            }

            if(isFile){
                FileInputFormat.setInputPaths(job, pathInput);
            }else
            {
                FileInputFormat.addInputPath(job,pathInput);
            }

            FileOutputFormat.setOutputPath(job, pathOutput);

//            job.setNumReduceTasks(3);


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
