package Study.JavaBase;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

 
import Study.Mybatis.MybatisUtils;
import Study.Mybatis.Mapper.StudentMapper;
import Study.domain.Student;

public class MybatisTest {

	@Test
	public void getStudentById() {
		SqlSession session= MybatisUtils.getSqlSession();
 
		try {
			StudentMapper mapper=session.getMapper(StudentMapper.class);
			Student student= mapper.getStudentById(3);
			System.out.println(student);
			int range =new Random().nextInt(50);
			student.setName("update"+range);
			student.setAge(18+range);
			mapper.updateStudent(student);
			System.out.println(student);
			
			student.setName("Insert");
			int res= mapper.insertStudent(student);
			System.out.println(student);
			System.out.println(res);
			
			
			mapper.deleteStudentById(student.getId());
			
			mapper.deleteStudent(student);
			session.commit();
			assertTrue(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			session.close();
		}
	}
	
}
