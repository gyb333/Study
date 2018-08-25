package Study.HDFS;
import java.io.UnsupportedEncodingException;
 
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

 

public class ConfigUtil {
	
	private static PropertiesConfiguration  config=null;
	private static final Logger logger=Logger.getLogger(ConfigUtil.class);
	 
	private static final String packageName = ConfigUtil.class.getPackage().getName(); 
	private static final String packagePath = packageName.replace(".", "/"); 
	private  static final String HDFS_FILEPATH=packagePath+"/hdfs.properties";
	
	
	static{
		try{		 
			 
			//config = new PropertiesConfiguration("hdfs.properties");
	
			config = new PropertiesConfiguration();
			config.setEncoding("UTF-8");
			config.load(HDFS_FILEPATH);
			
			 //鏂囦欢淇敼涔嬪悗鑷姩鍔犺浇
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
            //閰嶇疆鏂囦欢鑷姩淇濆瓨
            config.setAutoSave(true);
			
		}catch(Exception ex){
			logger.error(ex.getMessage());
		}
	}
	
	
	public static void save(String key,Object o){
		config.setProperty(key, o);
		try{
			config.save(HDFS_FILEPATH);
			config = new PropertiesConfiguration();
			config.setEncoding("UTF-8");
			config.load(HDFS_FILEPATH);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

		
	
	
	
	public static int getIntValue(String key){
		int reInt = 1;
		try{
			reInt = config.getInt(key);
		}catch(Exception ex){
			ex.fillInStackTrace();
			reInt = 0;
		}
		return reInt;
	}	
	
	public static Long getLongValue(String key) {
		Long reLong = 1l;
		try{
			reLong = config.getLong(key);
		}catch(Exception ex){
			ex.fillInStackTrace();
			reLong = 0l;
		}
		return reLong;
	}
	
	
	
	public static double getDoubleValue(String key){
		double reDouble = 1.0;
		try{
			reDouble = config.getDouble(key);
		}catch(Exception ex){
			ex.fillInStackTrace();
			reDouble =1.0;
		}
		return reDouble;
	}
	
	public static String getStringValue(String key){
		String str = "";
		try{
			str = config.getString(key);
		}catch(Exception ex){
			ex.fillInStackTrace();
			str = "";
		}
		return str;
	}
	
	public static Boolean getBooleanValue(String key) {
		Boolean flag = false;
		try{
			flag = config.getBoolean(key);
		}catch(Exception ex){
			ex.fillInStackTrace();
		}
		return flag;
	}
	
	
	
 
//	 閰嶇疆鏂囦欢浣跨敤ResourceBundle璇诲彇
	
	// 璇诲彇閰嶇疆鏂囦欢
		private static ResourceBundle cache = null;
	 
		static {
			try {
				cache = ResourceBundle.getBundle(packagePath+"/hdfs");
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
	 
		/**
		 * 鍔熻兘鎻忚堪锛氳幏鍙栭厤缃枃浠跺弬鏁板��
		 * 
		 * @param str(鍙傛暟KEY鍊�)
		 * @return
		 */
		public static String getValue(String str) {
			String s = cache.getString(str);
			try {
				s = new String(s.getBytes("ISO8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return s;
		}
		/**
		 * 鍔熻兘鎻忚堪锛氳幏鍙栨寚瀹氶厤缃枃浠跺弬鏁扮殑鍊�
		 * 
		 * @param strPropertiesFile(閰嶇疆鏂囦欢鍚嶇О)
		 * @param strItem(鍙傛暟鍚嶇О)
		 * @return
		 */
		public String getPropertiesValue(String strPropertiesFile, String strItem) {
			String strItemValue = "";
			ResourceBundle resources1 = null;
			try {
				resources1 = ResourceBundle.getBundle(strPropertiesFile);
				strItemValue = resources1.getString(strItem);
			} catch (MissingResourceException e) {
				System.out.println("ConfigInfo.getPropertiesValue error:"
						+ e.getMessage());
			}
			return strItemValue;
		}
	
 
	
	
	
	
}