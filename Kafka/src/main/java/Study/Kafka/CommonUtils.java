package Study.Kafka;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommonUtils {

	public static Properties getProperties(String path) throws IOException {
		Properties properties = new Properties();

		InputStream is = CommonUtils.class.getClassLoader().getResourceAsStream(path);
		properties.load(is);
		return properties;
	}

}
