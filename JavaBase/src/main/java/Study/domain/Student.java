package Study.domain;

public class Student {

	public Student() {
		
	}
	
	public Student(String name, Integer age, Boolean sex) {
		this.name = name;
		this.age = age;
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", age=" + age + ", sex=" + sex + "]";
	}

	private Integer id;
	
	private String name;
	
	private Integer age;
	
	private Boolean sex;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}



	
	
	
	
}
