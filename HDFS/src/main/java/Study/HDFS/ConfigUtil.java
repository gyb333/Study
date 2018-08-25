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
			String path =System.getProperty("user.dir")+"\\Resources\\";
			PropertyConfigurator.configure(path+"log4j.properties");
			//config = new PropertiesConfiguration("hdfs.properties");
	
			config = new PropertiesConfiguration();
			config.setEncoding("UTF-8");
			config.load(HDFS_FILEPATH);
			
			 //文件修改之后自动加载
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
            //配置文件自动保存
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
	
	
	
 
//	 配置文件使用ResourceBundle读取
	
	// 读取配置文件
		private static ResourceBundle cache = null;
	 
		static {
			try {
				cache = ResourceBundle.getBundle(packagePath+"/hdfs");
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
	 
		/**
		 * 功能描述：获取配置文件参数值
		 * 
		 * @param str(参数KEY值)
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
		 * 功能描述：获取指定配置文件参数的值
		 * 
		 * @param strPropertiesFile(配置文件名称)
		 * @param strItem(参数名称)
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