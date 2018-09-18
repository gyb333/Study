package Study.JDBC;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.Utilities;

import org.apache.commons.dbcp2.cpdsadapter.PStmtKeyCPDS;

import Study.Common.CommonUtils;
import Study.Common.SQLBuilder;
import Study.IDAO.IBaseDAO;

public class BaseDAO<T> extends PreparedSQL<T> implements IBaseDAO<T> {

	private Connection connection = MySqlFactory.getConnection();

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

	/**
	 * 保存
	 */
	@Override
	public Object save(T entity, String AutoIncrementColumn) {
		Object autoId =null;
		String tableName = entity.getClass().getSimpleName().toLowerCase();
		// 获得带有字符串get的所有方法的对象
		List<Method> list = this.matchPojoMethods(entity, "get");

		String sql = SQLBuilder.getInsertSQL(tableName, list, AutoIncrementColumn);
		try {
			// 获得预编译对象的引用
			pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			preparedInsertSQL(entity, list, AutoIncrementColumn);
			// 执行
			pstmt.executeUpdate();
			if (!CommonUtils.isNullOrEmpty(AutoIncrementColumn)) {
				ResultSet rs = pstmt.getGeneratedKeys();
				while (rs.next()) {
					autoId = rs.getObject(1);
				}
				rs.close();
			}
			
			pstmt.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}finally {
			
		}
		return autoId;
	}

	/**
	 * 修改
	 */
	@Override
	public void update(T entity, List<String> where) {
		String tableName = entity.getClass().getSimpleName().toLowerCase();
		// 获得带有字符串get的所有方法的对象
		List<Method> list = this.matchPojoMethods(entity, "get");
		String sql = SQLBuilder.getUpdateSQL(tableName, list, where);

		try {
			pstmt = connection.prepareStatement(sql);

			preparedUpdateSQL(entity, list, where);
			// 执行SQL语句
			pstmt.executeUpdate();

			// 关闭预编译对象
			pstmt.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}

	}

	/**
	 * 删除
	 */
	@Override
	public void delete(T entity, List<String> where) {
		String tableName = entity.getClass().getSimpleName().toLowerCase();
		List<Method> list = this.matchPojoMethods(entity, "get");
		String sql = SQLBuilder.getDeleteSQL(tableName, list, where);
		try {
			pstmt = this.connection.prepareStatement(sql);
			preparedDeleteSQL(entity, list, where);

			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}

	}

	/**
	 * 通过ID查询
	 */
	@Override
	public T findByKeys(Object object, List<String> keys) {
		String tableName = persistentClass.getSimpleName().toLowerCase();
		// 通过子类的构造函数,获得参数化类型的具体类型.比如BaseDAO<T>也就是获得T的具体类型
		T entity;
		try {
			entity = persistentClass.newInstance();
			List<Method> list = this.matchPojoMethods(entity, "get");

			String sql = SQLBuilder.getSelectSQL(tableName, list, keys);

			// 获得连接
			pstmt = this.connection.prepareStatement(sql);

			preparedSelectSQL(entity, list, keys);

			// 执行sql,取得查询结果集.
			rs = pstmt.executeQuery();

			// 把指针指向迭代器第一行
			Iterator<Method> iter = list.iterator();

			// 封装
			while (rs.next()) {
				while (iter.hasNext()) {
					Method method = iter.next();
					preparedEntity(entity, method);
				}
			}

			rs.close();
			pstmt.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}

		return entity;
	}

}