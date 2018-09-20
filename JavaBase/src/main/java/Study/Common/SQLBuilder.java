package Study.Common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class SQLBuilder {

	public static String getInsertSQL(String tableName, List<Method> list) {
		return getInsertSQL(tableName, list, null);
	}

	public static String getInsertSQL(String tableName, List<Method> list, String AutoIncrementColumn) {
		// SQL语句,insert into table name (
		String sql = "insert into " + tableName + "(";
		String value = "";
		Iterator<Method> iter = list.iterator();
		String columnName;
		// 拼接字段顺序 insert into table name(id,name,email,
		while (iter.hasNext()) {
			Method method = iter.next();
			columnName = method.getName().substring(3).toLowerCase();
			if (!columnName.equals(AutoIncrementColumn)) {
				sql += columnName + ",";
				value += "?,";
			}

		}

		// 去掉最后一个,符号insert insert into table name(id,name,email) values(
		sql = sql.substring(0, sql.lastIndexOf(",")) + ") values(";

		// 拼装预编译SQL语句insert insert into table name(id,name,email) values(?,?,?,
		sql += value;

		// 去掉SQL语句最后一个,符号insert insert into table name(id,name,email) values(?,?,?);
		sql = sql.substring(0, sql.lastIndexOf(",")) + ")";

		// 到此SQL语句拼接完成,打印SQL语句
		System.out.println(sql);
		return sql;
	}

	public static String getUpdateSQL(String tableName, List<Method> list, List<String> where) {
		String sql = "update " + tableName + " set ";
		String sqlWhere = " where 1= 1 ";
		Method method;
		String columnName;
		// 把迭代指针移到第一位
		Iterator<Method> iter = list.iterator();
		while (iter.hasNext()) {
			method = iter.next();
			columnName = method.getName().substring(3).toLowerCase();
			if (where != null && where.contains(columnName)) {
				sqlWhere += " AND " + columnName + " = ?";
			} else {
				sql += columnName + "= ?,";
			}
		}
		// 去掉最后一个,符号
		sql = sql.substring(0, sql.lastIndexOf(","));

		// 添加条件
		sql += sqlWhere;
		// SQL拼接完成,打印SQL语句
		System.out.println(sql);
		return sql;
	}

	public static String getDeleteSQL(String tableName, List<Method> list, List<String> where) {
		String sql = "delete from " + tableName + " where 1= 1 ";
		Method method;
		String columnName;
		// 把迭代指针移到第一位
		Iterator<Method> iter = list.iterator();
		while (iter.hasNext()) {
			method = iter.next();
			columnName = method.getName().substring(3).toLowerCase();
			if (where != null && where.contains(columnName)) {
				sql += " AND " + columnName + " = ?";
			}
		}

		// SQL拼接完成,打印SQL语句
		System.out.println(sql);
		return sql;
	}

	public static String getSelectSQL(String tableName, List<Method> list) {
		String sql = "select * from " + tableName + " where 1=1 ";
		Method method;
		String columnName;
		// 把迭代指针移到第一位
		Iterator<Method> iter = list.iterator();
		while (iter.hasNext()) {
			method = iter.next();
			columnName = method.getName().substring(3).toLowerCase();

			sql += " AND " + columnName + " = ?";

		}
		// SQL拼接完成,打印SQL语句
		System.out.println(sql);
		return sql;
	}

}
