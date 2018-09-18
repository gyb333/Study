package Study.JavaBase;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import Study.DAO.StudentDAO;
import Study.domain.Student;

 
public class StudentDAOTest {

	@Test
	public void TestSave() {
		Student s = new Student("gyb33", 11, true);
		StudentDAO sd = new StudentDAO();
		Object object = sd.save(s, "id");
		
		List<String> where =  new ArrayList<>();
		sd.update(s, where);
		System.out.println(object);
		Assert.assertTrue(true);
	}
	
}
