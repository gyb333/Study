package Study.SpringBoot.Bicycle.mapper;

import java.util.List;

import Study.SpringBoot.Bicycle.pojo.User;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {

	public User getById(Long id);
	
	public List<User> findAll();

	public void save(User user);

	public void deleteByIds(Long[] ids);

	public void update(User user);

	public User login(User user);
}
