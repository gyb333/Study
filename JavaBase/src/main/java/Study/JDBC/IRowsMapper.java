package Study.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IRowsMapper<T> {
	
	public List<T> mapRows(ResultSet rs) throws Exception;
	
	
}

