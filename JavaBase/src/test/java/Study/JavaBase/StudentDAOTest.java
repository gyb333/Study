package Study.JavaBase;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;

import Study.DAO.StudentDAO;
import Study.domain.Student;

 
public class StudentDAOTest {

	@Test
	public void TestSave() {
		Student s = new Student("gyb33", 11, true);
		StudentDAO sd = new StudentDAO();
		sd.save(s, "id");
		Assert.assertTrue(true);
	}
	
}
