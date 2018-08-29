package Study.AOP.Cglib;

import java.lang.reflect.Method;

import Study.AOP.DynamicProxy;
import Study.AOP.ITask;
import Study.AOP.RealTask;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibProxy implements MethodInterceptor {

	private Object target;
	
	public Object getInstance(Object target) {
		this.target=target;
		Enhancer enhancer=new Enhancer();
		enhancer.setSuperclass(this.target.getClass());
		//回调方法
		enhancer.setCallback(this);
		//创建代理对象
		return enhancer.create();
		
		
	}
	
	
	
	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy  methodProxy) throws Throwable {
		Object result=null;
		System.out.println("开始");
		result=methodProxy.invoke(proxy, args);
		System.out.println("结束");
		return result;
	}

	
	
	
	
	 public static void main( String[] args )
	    {
		 	RealTask target=new RealTask();
	        RealTask proxy=(RealTask) new CglibProxy().getInstance(target);
	        proxy.task("业务方法");
	        
	        
	    }
	
	
	
}
