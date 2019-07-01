package Base.IDAO;

import java.util.List;
import java.util.Map;

public interface IBaseDAO<T> {

	Object save(T entity, String AutoIncrementColumn);
	
	void update(T entity, List<String> where);
	
	void delete(T entity, List<String> where);
	
	T findByKeys(Map<String,Object> maps);
	
	
	
	
	
}
