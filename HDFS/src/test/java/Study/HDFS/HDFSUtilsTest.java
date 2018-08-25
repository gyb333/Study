package Study.HDFS;

 

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.Test;

public class HDFSUtilsTest {

	@Test
	public void CreateFolder() {
		try {
			
			 
			HDFSUtils.CreateFolder("/Test");
			Assert.assertTrue(true);
		} catch (IOException e) {
			Assert.assertTrue(false);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
