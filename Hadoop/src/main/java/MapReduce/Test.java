package MapReduce;

import org.apache.hadoop.conf.Configuration;


public class Test {

	public static void TestConfiguration() {
		
		Configuration conf =new Configuration();
		conf.addResource(Test.class.getResourceAsStream("/Param.xml"));

		conf.setBoolean("mapreduce.map.output.compress", true);
		conf.set("mapreduce.map.output.compress.codec", "org.apache.hadoop.io.compress.DefaultCodec");
		
		conf.setBoolean("mapreduce.output.fileoutputformat.compress", true);
		conf.set("mapreduce.output.fileoutputformat.compress.type", "RECORD");
		conf.set("mapreduce.output.fileoutputformat.compress.codec", "org.apache.hadoop.io.compress.DefaultCodec");
		String value =conf.get("name");
		System.out.println(value);
	}
	
	
	public static void main(String[] args) {
		TestConfiguration();
	}

}
