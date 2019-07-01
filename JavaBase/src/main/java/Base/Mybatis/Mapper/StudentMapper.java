package Base.Mybatis.Mapper;

import Base.domain.Student;

public interface StudentMapper {

	 Student getStudentById(int id);
	 
	 int insertStudent(Student student);
	 
	 int updateStudent(Student student);
	 
	 int deleteStudentById(int id);
	 
	 int deleteStudent(Student student);
}
