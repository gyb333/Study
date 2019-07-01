package AOP;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler {

	private Object target;

	public Object bind(Object target) {
		this.target = target;
		ClassLoader loader = this.target.getClass().getClassLoader();
		Class<?>[] interfaces = this.target.getClass().getInterfaces();

		return Proxy.newProxyInstance(loader, interfaces, this);
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		System.out.println("切面之前执行");
		result = method.invoke(target, args);
		System.out.println("切面之后执行");
		return result;
	}

	public static void main(String[] args) {
 

		ITask task = (ITask) new DynamicProxy().bind(new RealTask());
		task.task("业务执行");
	}

}
