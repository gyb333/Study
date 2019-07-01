package Base.JDBC;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class RowsMapper<T> implements IRowsMapper<T>,IRowMapper<T> {

	private Class<T> cls;

	@SuppressWarnings("unchecked")
	public RowsMapper() {
		// 获得参数化类型
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		cls = (Class<T>) type.getActualTypeArguments()[0];
	}

	
	

	public List<T> mapRows(ResultSet rs) throws Exception {

		List<T> list = new ArrayList<T>();
		ResultSetMetaData metaData = rs.getMetaData();
		int length = metaData.getColumnCount();
		while (rs.next()) {
			// 通过反射机制创建一个实例
			T t = cls.newInstance();
			for (int i = 0; i < length; i++) {
				String columnName = metaData.getColumnName(i + 1);
				Object columnValue = rs.getObject(columnName);
				if (columnValue == null) {
					columnValue = "";
				}
				Field field = cls.getDeclaredField(columnName);
				field.setAccessible(true); // 打开javabean的访问权限
				field.set(t, columnValue);
			}
			list.add(t);
		}
		return list;
	}




	public T mapRow(ResultSet rs) throws Exception {
		// TODO Auto-generated method stub
		return mapRows(rs).get(0);
	}

}
