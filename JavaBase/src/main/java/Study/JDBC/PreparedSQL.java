package Study.JDBC;

 
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PreparedSQL<T> extends TypeUtils<T> {

 
	protected ResultSet rs = null;

	protected void EntityMapper(T entity, Map<String, Object> maps) throws Exception {
		if (maps != null && maps.size() > 0) {
			Method method;

			for (Entry<String, Object> entry : maps.entrySet()) {
				String typeName = entry.getValue().getClass().getTypeName();

				method = entity.getClass().getMethod("set" + entry.getKey(), Class.forName(typeName));
				method.invoke(entity, entry.getValue());
			}
		}
	}

	/**
	 * 过滤当前Pojo类所有带传入字符串的Method对象,返回List集合.
	 */
	protected List<Method> matchPojoMethods(T entity, String methodName) {
		 
		return matchPojoMethods(entity, methodName,null);
	}

	protected List<Method> matchPojoMethods(T entity, String methodName, Collection<String> where) {
		// 获得当前Pojo所有方法对象
		Method[] methods = entity.getClass().getDeclaredMethods();
		String strMethodName;
		// List容器存放所有带get字符串的Method对象
		List<Method> list = new ArrayList<Method>();
		// 过滤当前Pojo类所有带get字符串的Method对象,存入List容器
		for (int index = 0; index < methods.length; index++) {
			strMethodName = methods[index].getName();
			if (strMethodName.indexOf(methodName) != -1) {
				if (where == null || (where != null && where.contains(strMethodName.substring(3))))
					list.add(methods[index]);
			}
		}
		return list;
	}

	private void preparedSQL(T entity,PreparedStatement pstmt, Method method, int i) throws SQLException, Exception {
		// 此初判断返回值的类型,因为存入数据库时有的字段值格式需要改变,比如String,SQL语句是'"+abc+"'
		if (method.getReturnType().getSimpleName().indexOf("String") != -1) {
			pstmt.setString(i, this.getString(method, entity));
		} else if (method.getReturnType().getSimpleName().indexOf("Date") != -1) {
			pstmt.setDate(i, this.getDate(method, entity));
		} else if (method.getReturnType().getSimpleName().indexOf("InputStream") != -1) {
			pstmt.setAsciiStream(i, this.getBlob(method, entity), 1440);
		} else if (method.getReturnType().getSimpleName().indexOf("Boolean") != -1) {
			pstmt.setBoolean(i, this.getBoolean(method, entity));
		} else {
			 
			pstmt.setInt(i, getInt(method, entity));
		}
	}

	protected void preparedEntity(T entity, Method method) throws SQLException, Exception {
		if (method.getParameterTypes()[0].getSimpleName().indexOf("String") != -1) {
			// 由于list集合中,method对象取出的方法顺序与数据库字段顺序不一致(比如:list的第一个方法是setDate,而数据库按顺序取的是"123"值)
			// 所以数据库字段采用名字对应的方式取.
			this.setString(method, entity, rs.getString(method.getName().substring(3).toLowerCase()));
		} else if (method.getParameterTypes()[0].getSimpleName().indexOf("Date") != -1) {
			this.setDate(method, entity, rs.getDate(method.getName().substring(3).toLowerCase()));
		} else if (method.getParameterTypes()[0].getSimpleName().indexOf("InputStream") != -1) {
			this.setBlob(method, entity, rs.getBlob(method.getName().substring(3).toLowerCase()).getBinaryStream());
		} else if (method.getParameterTypes()[0].getSimpleName().indexOf("Boolean") != -1) {
			this.setBoolean(method, entity, rs.getBoolean(method.getName().substring(3).toLowerCase()));
		} else {
			this.setInt(method, entity, rs.getInt(method.getName().substring(3).toLowerCase()));
		}
	}

	public void preparedInsertSQL(T entity, PreparedStatement pstmt,List<Method> list, String AutoIncrementColumn)
			throws SQLException, Exception {
		int i = 0;
		// 把指向迭代器最后一行的指针移到第一行.
		Iterator<Method> iter = list.iterator();
		String columnName;
		while (iter.hasNext()) {
			Method method = iter.next();
			columnName = method.getName().substring(3).toLowerCase();
			if (columnName.equals(AutoIncrementColumn)) {
				continue;
			}
			preparedSQL(entity,pstmt, method, ++i);
		}

	}

	public void preparedUpdateSQL(T entity,PreparedStatement pstmt, List<Method> list, List<String> where) throws SQLException, Exception {
		int i = 0;
		int whereIndex = 0;
		if (where != null) {
			whereIndex = list.size() - where.size();
		}
		// 把指向迭代器最后一行的指针移到第一行.
		Iterator<Method> iter = list.iterator();
		String columnName;
		while (iter.hasNext()) {
			Method method = iter.next();
			columnName = method.getName().substring(3).toLowerCase();
			if (where != null && where.contains(columnName)) {
				preparedSQL(entity,pstmt, method, ++whereIndex);
			} else {
				preparedSQL(entity,pstmt, method, ++i);
			}

		}
	}

	public void preparedDeleteSQL(T entity,PreparedStatement pstmt, List<Method> list, List<String> where) throws SQLException, Exception {
		preparedWhereSQL(entity,pstmt, list);
	}

	public void preparedSelectSQL(T entity,PreparedStatement pstmt, List<Method> list) throws SQLException, Exception {
		preparedWhereSQL(entity,pstmt, list);
	}

	public void preparedWhereSQL(T entity,PreparedStatement pstmt, List<Method> list) throws SQLException, Exception {
		int i = 0;
		// 把指向迭代器最后一行的指针移到第一行.
		Iterator<Method> iter = list.iterator();
		while (iter.hasNext()) {
			Method method = iter.next();
			preparedSQL(entity,pstmt, method, ++i);

		}
	}

}
