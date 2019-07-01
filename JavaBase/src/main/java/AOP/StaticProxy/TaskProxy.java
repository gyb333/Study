package AOP.StaticProxy;

import AOP.ITask;

public class TaskProxy implements ITask {

	private ITask tasker;
	
	
	public TaskProxy(ITask tasker) {
		this.tasker = tasker;
	}


	public void task(String msg) {
		tasker.task(msg);

	}

	public void task(String msg,String proxyMsg) {
		tasker.task(msg);
		proxy(proxyMsg);
	}
	
	private void proxy(String proxyMsg) {
		System.out.println(proxyMsg);
	}
}
