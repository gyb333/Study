package Study.MapReduce;

import java.io.FileInputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.PropertyConfigurator;

public class DistributedJobBase {

	private static final String path = System.getProperty("user.dir");

	static {
		try {
			PropertyConfigurator.configure(path + "\\Resources\\log4j.properties");
			// 加载DLL
//			System.load("D:\\Program Files\\hadoop-3.0.0\\bin\\hadoop.dll");
			// 设置环境变量
			System.setProperty("HADOOP_USER_NAME", "root");
//			System.setProperty("hadoop.home.dir", "D:\\Program Files\\hadoop-3.0.0");

		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	public static Configuration getConfiguration() {
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

	public static void setJob(String clsName, boolean isLocaltion, Class<? extends Mapper> clsMapper,
			Class<? extends Reducer> clsReducer, Class<?> clsMapOutputKey, Class<?> clsMapOutputValue,
			Class<?> clsOutputKey, Class<?> clsOutputValue) throws Exception {

		String strOutput = "/HadoopData/output/" + clsName;
		String strInput = "/HadoopData/input/" + clsName;
		Path baseDir = new Path(path).getParent();
		String strLocalInput = baseDir.toString() + strInput;
		String strLocalOutput = baseDir.toString() + strOutput;
		String osName = System.getProperty("os.name");
		System.out.println(osName);

		Configuration conf = new Configuration();	// 默认只加载core-default.xml core-site.xml

		System.setProperty("HADOOP_USER_NAME", "root");

		conf.set("mapreduce.app-submission.cross-platform", "true");
		if (isLocaltion) {
			// 设置本地提交
			conf.set("fs.defaultFS", "local");
			conf.set("mapreduce.framework.name", "local");

		}

		Job job = Job.getInstance(conf);

		if (isLocaltion)
			job.setJarByClass(DistributedJobBase.class);

		else
			job.setJar("D:\\work\\Study\\MapReduce\\target\\MapReduce-0.0.1-SNAPSHOT.jar");

		job.setMapperClass(clsMapper);
		job.setReducerClass(clsReducer);

		job.setMapOutputKeyClass(clsMapOutputKey);
		job.setMapOutputValueClass(clsMapOutputValue);

		job.setOutputKeyClass(clsOutputKey);
		job.setOutputValueClass(clsOutputValue);

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
			// 上传到HDFS
			if (!isLocaltion) {
				if (fs.exists(pathInput)) {
					fs.delete(pathInput);
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

			job.setNumReduceTasks(3);

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
