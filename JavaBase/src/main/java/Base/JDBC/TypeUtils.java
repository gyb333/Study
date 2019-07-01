package Base.JDBC;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Date;

public class TypeUtils<T> {

	
	
	
	/**
	 * 方法返回类型为int或Integer类型时,返回的SQL语句值.对应get
	 */
	public Integer getInt(Method method, T entity) throws Exception {
		return (Integer) method.invoke(entity, new Object[] {});
	}

	/**
	 * 方法返回类型为String时,返回的SQL语句拼装值.比如'abc',对应get
	 */
	public String getString(Method method, T entity) throws Exception {
		return (String) method.invoke(entity, new Object[] {});
	}

	/**
	 * 方法返回类型为Blob时,返回的SQL语句拼装值.对应get
	 */
	public InputStream getBlob(Method method, T entity) throws Exception {
		return (InputStream) method.invoke(entity, new Object[] {});
	}

	/**
	 * 方法返回类型为Date时,返回的SQL语句拼装值,对应get
	 */
	public Date getDate(Method method, T entity) throws Exception {
		return (Date) method.invoke(entity, new Object[] {});
	}

	/**
	 * 参数类型为Integer或int时,为entity字段设置参数,对应set
	 */
	public Integer setInt(Method method, T entity, Integer arg) throws Exception {
		return (Integer) method.invoke(entity, new Object[] { arg });
	}

	/**
	 * 参数类型为String时,为entity字段设置参数,对应set
	 */
	public String setString(Method method, T entity, String arg) throws Exception {
		return (String) method.invoke(entity, new Object[] { arg });
	}

	/**
	 * 参数类型为InputStream时,为entity字段设置参数,对应set
	 */
	public InputStream setBlob(Method method, T entity, InputStream arg) throws Exception {
		return (InputStream) method.invoke(entity, new Object[] { arg });
	}

	/**
	 * 参数类型为Date时,为entity字段设置参数,对应set
	 */
	public Date setDate(Method method, T entity, Date arg) throws Exception {
		return (Date) method.invoke(entity, new Object[] { arg });
	}

	public Boolean getBoolean(Method method, T entity) throws Exception {
		return (Boolean) method.invoke(entity, new Object[] {});
	}

	public Boolean setBoolean(Method method, T entity, Boolean arg) throws Exception {
		return (Boolean) method.invoke(entity, new Object[] { arg });
	}
}
