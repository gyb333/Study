package Study.HDFS;

import java.io.IOException;



import junit.framework.TestCase;

public class HDFSUtilsTest extends TestCase {

	public void CreateFolder() {
		try {
			HDFSUtils.CreateFolder("/Test");
			assertTrue(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
