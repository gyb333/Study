package Study.JDBC;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.Utilities;

public class BaseDAO<T> extends TypeUtils<T> {

	private Connection connection = MySqlFactory.getConnection();

	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	@SuppressWarnings("unused")
	private Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public BaseDAO() {

		// 获得参数化类型
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		persistentClass = (Class<T>) type.getActualTypeArguments()[0];
	}

	private void close() {
		try {

			if (rs != null)
				rs.close();

			if (pstmt != null)
				pstmt.close();

			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		close();
	}

	private String getInsertSQL(String tableName, List<Method> list) {
		// SQL语句,insert into table name (
		String sql = "insert into " + tableName + "(";
		Iterator<Method> iter = list.iterator();

		// 拼接字段顺序 insert into table name(id,name,email,
		while (iter.hasNext()) {
			Method method = iter.next();
			sql += method.getName().substring(3).toLowerCase() + ",";
		}

		// 去掉最后一个,符号insert insert into table name(id,name,email) values(
		sql = sql.substring(0, sql.lastIndexOf(",")) + ") values(";

		// 拼装预编译SQL语句insert insert into table name(id,name,email) values(?,?,?,
		for (int j = 0; j < list.size(); j++) {
			sql += "?,";
		}

		// 去掉SQL语句最后一个,符号insert insert into table name(id,name,email) values(?,?,?);
		sql = sql.substring(0, sql.lastIndexOf(",")) + ")";

		// 到此SQL语句拼接完成,打印SQL语句
		System.out.println(sql);
		return sql;
	}

	private PreparedStatement getPreparedSQL(T entity, List<Method> list) throws SQLException, Exception {
		int i = 0;
		// 把指向迭代器最后一行的指针移到第一行.
		Iterator<Method> iter = list.iterator();
		while (iter.hasNext()) {
			Method method = iter.next();
			// 此初判断返回值的类型,因为存入数据库时有的字段值格式需要改变,比如String,SQL语句是'"+abc+"'
			if (method.getReturnType().getSimpleName().indexOf("String") != -1) {
				pstmt.setString(++i, this.getString(method, entity));
			} else if (method.getReturnType().getSimpleName().indexOf("Date") != -1) {
				pstmt.setDate(++i, this.getDate(method, entity));
			} else if (method.getReturnType().getSimpleName().indexOf("InputStream") != -1) {
				pstmt.setAsciiStream(++i, this.getBlob(method, entity), 1440);
			} else {
				pstmt.setInt(++i, this.getInt(method, entity));
			}
		}

		return pstmt;
	}

	/**
	 * 保存
	 */
	public void save(T entity) throws Exception {
		String tableName = entity.getClass().getSimpleName().toLowerCase();
		// 获得带有字符串get的所有方法的对象
		List<Method> list = this.matchPojoMethods(entity, "get");

		String sql = getInsertSQL(tableName, list);
		// 获得预编译对象的引用
		pstmt = connection.prepareStatement(sql);

		// 执行
		pstmt.executeUpdate();

	}

	/**
	 * 修改
	 */
	public void update(T entity) throws Exception {
		String sql = "update " + entity.getClass().getSimpleName().toLowerCase() + " set ";

		// 获得该类所有get方法对象集合
		List<Method> list = this.matchPojoMethods(entity, "get");

		// 临时Method对象,负责迭代时装method对象.
		Method tempMethod = null;

		// 由于修改时不需要修改ID,所以按顺序加参数则应该把Id移到最后.
		Method idMethod = null;
		Iterator<Method> iter = list.iterator();
		while (iter.hasNext()) {
			tempMethod = iter.next();
			// 如果方法名中带有ID字符串并且长度为2,则视为ID.
			if (tempMethod.getName().lastIndexOf("Id") != -1 && tempMethod.getName().substring(3).length() == 2) {
				// 把ID字段的对象存放到一个变量中,然后在集合中删掉.
				idMethod = tempMethod;
				iter.remove();
				// 如果方法名去掉set/get字符串以后与pojo + "id"想符合(大小写不敏感),则视为ID
			} else if ((entity.getClass().getSimpleName() + "Id").equalsIgnoreCase(tempMethod.getName().substring(3))) {
				idMethod = tempMethod;
				iter.remove();
			}
		}

		// 把迭代指针移到第一位
		iter = list.iterator();
		while (iter.hasNext()) {
			tempMethod = iter.next();
			sql += tempMethod.getName().substring(3).toLowerCase() + "= ?,";
		}

		// 去掉最后一个,符号
		sql = sql.substring(0, sql.lastIndexOf(","));

		// 添加条件
		sql += " where " + idMethod.getName().substring(3).toLowerCase() + " = ?";

		// SQL拼接完成,打印SQL语句
		System.out.println(sql);

		PreparedStatement statement = this.connection.prepareStatement(sql);

		int i = 0;
		iter = list.iterator();
		while (iter.hasNext()) {
			Method method = iter.next();
			// 此初判断返回值的类型,因为存入数据库时有的字段值格式需要改变,比如String,SQL语句是'"+abc+"'
			if (method.getReturnType().getSimpleName().indexOf("String") != -1) {
				statement.setString(++i, this.getString(method, entity));
			} else if (method.getReturnType().getSimpleName().indexOf("Date") != -1) {
				statement.setDate(++i, this.getDate(method, entity));
			} else if (method.getReturnType().getSimpleName().indexOf("InputStream") != -1) {
				statement.setAsciiStream(++i, this.getBlob(method, entity), 1440);
			} else {
				statement.setInt(++i, this.getInt(method, entity));
			}
		}

		// 为Id字段添加值
		if (idMethod.getReturnType().getSimpleName().indexOf("String") != -1) {
			statement.setString(++i, this.getString(idMethod, entity));
		} else {
			statement.setInt(++i, this.getInt(idMethod, entity));
		}

		// 执行SQL语句
		statement.executeUpdate();

		// 关闭预编译对象
		statement.close();

		// 关闭连接
		connection.close();
	}

	/**
	 * 删除
	 */
	public void delete(T entity) throws Exception {
		String sql = "delete from " + entity.getClass().getSimpleName().toLowerCase() + " where ";

		// 存放字符串为"id"的字段对象
		Method idMethod = null;

		// 取得字符串为"id"的字段对象
		List<Method> list = this.matchPojoMethods(entity, "get");
		Iterator<Method> iter = list.iterator();
		while (iter.hasNext()) {
			Method tempMethod = iter.next();
			// 如果方法名中带有ID字符串并且长度为2,则视为ID.
			if (tempMethod.getName().lastIndexOf("Id") != -1 && tempMethod.getName().substring(3).length() == 2) {
				// 把ID字段的对象存放到一个变量中,然后在集合中删掉.
				idMethod = tempMethod;
				iter.remove();
				// 如果方法名去掉set/get字符串以后与pojo + "id"想符合(大小写不敏感),则视为ID
			} else if ((entity.getClass().getSimpleName() + "Id").equalsIgnoreCase(tempMethod.getName().substring(3))) {
				idMethod = tempMethod;
				iter.remove();
			}
		}

		sql += idMethod.getName().substring(3).toLowerCase() + " = ?";

		PreparedStatement statement = this.connection.prepareStatement(sql);

		// 为Id字段添加值
		int i = 0;
		if (idMethod.getReturnType().getSimpleName().indexOf("String") != -1) {
			statement.setString(++i, this.getString(idMethod, entity));
		} else {
			statement.setInt(++i, this.getInt(idMethod, entity));
		}

		statement.executeUpdate();

	}

	/**
	 * 通过ID查询
	 */
	public T findById(Object object) throws Exception {
		String sql = "select * from " + persistentClass.getSimpleName().toLowerCase() + " where ";

		// 通过子类的构造函数,获得参数化类型的具体类型.比如BaseDAO<T>也就是获得T的具体类型
		T entity = persistentClass.newInstance();

		// 存放Pojo(或被操作表)主键的方法对象
		Method idMethod = null;

		List<Method> list = this.matchPojoMethods(entity, "set");
		Iterator<Method> iter = list.iterator();

		// 过滤取得Method对象
		while (iter.hasNext()) {
			Method tempMethod = iter.next();
			if (tempMethod.getName().indexOf("Id") != -1 && tempMethod.getName().substring(3).length() == 2) {
				idMethod = tempMethod;
			} else if ((entity.getClass().getSimpleName() + "Id").equalsIgnoreCase(tempMethod.getName().substring(3))) {
				idMethod = tempMethod;
			}
		}
		// 第一个字母转为小写
		sql += idMethod.getName().substring(3, 4).toLowerCase() + idMethod.getName().substring(4) + " = ?";

		// 封装语句完毕,打印sql语句
		System.out.println(sql);

		// 获得连接
		pstmt = this.connection.prepareStatement(sql);

		// 判断id的类型
		if (object instanceof Integer) {
			pstmt.setInt(1, (Integer) object);
		} else if (object instanceof String) {
			pstmt.setString(1, (String) object);
		}

		// 执行sql,取得查询结果集.
		rs = pstmt.executeQuery();

		// 记数器,记录循环到第几个字段
		int i = 0;

		// 把指针指向迭代器第一行
		iter = list.iterator();

		// 封装
		while (rs.next()) {
			while (iter.hasNext()) {
				Method method = iter.next();
				if (method.getParameterTypes()[0].getSimpleName().indexOf("String") != -1) {
					// 由于list集合中,method对象取出的方法顺序与数据库字段顺序不一致(比如:list的第一个方法是setDate,而数据库按顺序取的是"123"值)
					// 所以数据库字段采用名字对应的方式取.
					this.setString(method, entity, rs.getString(method.getName().substring(3).toLowerCase()));
				} else if (method.getParameterTypes()[0].getSimpleName().indexOf("Date") != -1) {
					this.setDate(method, entity, rs.getDate(method.getName().substring(3).toLowerCase()));
				} else if (method.getParameterTypes()[0].getSimpleName().indexOf("InputStream") != -1) {
					this.setBlob(method, entity,
							rs.getBlob(method.getName().substring(3).toLowerCase()).getBinaryStream());
				} else {
					this.setInt(method, entity, rs.getInt(method.getName().substring(3).toLowerCase()));
				}
			}
		}

 
 
		return entity;
	}

	/**
	 * 过滤当前Pojo类所有带传入字符串的Method对象,返回List集合.
	 */
	private List<Method> matchPojoMethods(T entity, String methodName) {
		// 获得当前Pojo所有方法对象
		Method[] methods = entity.getClass().getDeclaredMethods();

		// List容器存放所有带get字符串的Method对象
		List<Method> list = new ArrayList<Method>();

		// 过滤当前Pojo类所有带get字符串的Method对象,存入List容器
		for (int index = 0; index < methods.length; index++) {
			if (methods[index].getName().indexOf(methodName) != -1) {
				list.add(methods[index]);
			}
		}
		return list;
	}

}