package AOP.StaticProxy;

import AOP.ITask;
import AOP.RealTask;

public class ProxyPattern {

	public static void main(String[] args) {
		ITask tasker=new RealTask();
		TaskProxy taskProxy=new TaskProxy(tasker);
		taskProxy.task("正常执行","代理额外执行");
	}
}
