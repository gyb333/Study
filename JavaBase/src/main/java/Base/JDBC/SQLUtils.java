package Base.JDBC;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;



public class SQLUtils<T> {

	// PreparedStatement防止SQL注入进行优化，Statement 会使数据库频繁编译SQL，可能造成数据库缓冲区溢出

	public static Connection getConnection() throws SQLException {
		return JdbcUtils.getConnection();
	}

	public boolean executeUpdate(String sql, Object[] params) throws SQLException {
		boolean flag = false;
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = JdbcUtils.getConnection();
			pstmt = conn.prepareStatement(sql);
			paramsMapper(pstmt, params);
			result = pstmt.executeUpdate();
			flag = result > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e);
		} finally {
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		}
		return flag;
	}

	 
	public Object executeQuerySingle(String sql, Object[] params) throws SQLException {
		Object object = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			pstmt = conn.prepareStatement(sql);
			paramsMapper(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				object = rs.getObject(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e);
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		}
		return object;
	}

	public T executeQuery(String sql, Object[] params) throws Exception {
		T t = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			pstmt = conn.prepareStatement(sql);
			paramsMapper(pstmt, params);
			rs = pstmt.executeQuery();
			t = executeQuery(sql, params, new RowsMapper<T>());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e);
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		}
		return t;
	}

	public T executeQuery(String sql, Object[] params, IRowMapper<T> rm) throws Exception {
		T t = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			pstmt = conn.prepareStatement(sql);
			paramsMapper(pstmt, params);
			rs = pstmt.executeQuery();
			t = rm.mapRow(rs);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e);
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		}
		return t;
	}

	public List<T> executeQueryMany(String sql, Object[] params) throws Exception {

		return executeQueryMany(sql, params, new RowsMapper<T>());
	}

	public List<T> executeQueryMany(String sql, Object[] params, IRowsMapper<T> rm) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<T> list =null;
		try {
			conn = JdbcUtils.getConnection();
			pstmt = conn.prepareStatement(sql);
			paramsMapper(pstmt, params);
			rs = pstmt.executeQuery();
			list= rm.mapRows(rs);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e);
		}finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		}
		return list;
	}

	private void paramsMapper(PreparedStatement pstmt, Object[] params) throws SQLException {

		if (params != null && params.length > 0) {
			int index = 1;
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(index++, params[i]);
			}
		}
	}

}
