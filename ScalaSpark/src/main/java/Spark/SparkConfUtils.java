package Study.Spark;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import org.apache.spark.SparkConf;
 

public class SparkConfUtils {
	
//	public SparkConf getHadoopConf(String hadoopConfPath) {
//		SparkConf hadoopConf = new SparkConf();
//
//		try {
//			Map<String, String> hiveConfMap = parseXMLToMap(hadoopConfPath + "/hive-site.xml");
//			Map<String, String> hadoopConfMap = parseXMLToMap(hadoopConfPath + "/core-site.xml");
//			hadoopConfMap.putAll(parseXMLToMap(hadoopConfPath + "/hdfs-site.xml"));
//			hadoopConfMap.putAll(parseXMLToMap(hadoopConfPath + "/yarn-site.xml"));
//			hadoopConfMap.putAll(parseXMLToMap(hadoopConfPath + "/mapred-site.xml"));
//
//			for (Map.Entry<String, String> entry : hiveConfMap.entrySet()) {
//				hadoopConf.set(entry.getKey(), entry.getValue());
//			}
//			for (Map.Entry<String, String> entry : hadoopConfMap.entrySet()) {
//				hadoopConf.set("spark.hadoop." + entry.getKey(), entry.getValue());
//			}
//			return hadoopConf;
//		} catch (DocumentException e) {
//			logger.error("读取xml文件失败！");
//			throw new RuntimeException(e);
//		}
//
//	}
//
//	// 将xml解析为HashMap
//	private Map<String, String> parseXMLToMap(String xmlFilePath) throws DocumentException {
//		Map<String, String> confMap = new HashMap<>();
//		SAXReader reader = new SAXReader();
//		Document document = reader.read(new File(xmlFilePath));
//		Element configuration = document.getRootElement();
//		Iterator iterator = configuration.elementIterator();
//		while (iterator.hasNext()) {
//			Element property = (Element) iterator.next();
//			String name = property.element("name").getText();
//			String value = property.element("value").getText();
//			confMap.put(name, value);
//		}
//		return confMap;
//	}

}
