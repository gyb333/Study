package Study.JavaBase;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import Base.DAO.StudentDAO;
import Base.domain.Student;

 
public class StudentDAOTest {

	@Test
	public void TestSave() {
		StudentDAO sd = new StudentDAO();
		
		
		
		Student s = new Student("gyb33", 11, true);
		
		Object object = sd.save(s, "id");
		
		System.out.println(object);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("Id", 6);
		s= sd.findByKeys(maps);

		
		System.out.println(s);

		 //sd.delete(entity, where);
		
		
		Assert.assertTrue(true);
	}
	
}
