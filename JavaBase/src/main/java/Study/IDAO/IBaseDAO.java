package Study.IDAO;

import java.util.List;

public interface IBaseDAO<T> {

	Object save(T entity, String AutoIncrementColumn);
	
	void update(T entity, List<String> where);
	
	void delete(T entity, List<String> where);
	
	T findByKeys(Object object,List<String> keys);
	
	
	
	
	
}
