package Study.JDBC.DataSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import javax.sql.DataSource;
import javax.xml.bind.Binder;

public class ConnectionHandler implements InvocationHandler {

	private Connection realConnection;

	private Connection warpedConnection;
	

	Connection bind(Connection realConn) {
		this.realConnection = realConn;
		ClassLoader loader = this.getClass().getClassLoader();
		Class<?>[] interfaces = new Class[] { Connection.class };
		this.warpedConnection = (Connection) Proxy.newProxyInstance(loader, interfaces, this);
		return warpedConnection;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if ("close".equals(method.getName())) {
			// 动态代理处理close方法,可以附加相应的逻辑
			realConnection.close();	//这里暂时不做处理
		}
		return method.invoke(realConnection, args);
	}

}
