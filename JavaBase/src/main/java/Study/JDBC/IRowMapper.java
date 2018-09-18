package Study.JDBC;

import java.sql.ResultSet;
 

public interface IRowMapper<T> {
	public T mapRow(ResultSet rs) throws Exception;
}
