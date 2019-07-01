package ConcurrencySynchronization.HighGrade;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock类也可以实现线程同步，而Lock获得锁需要执行lock方法，释放锁需要执行unLock方法
 * Lock类可以创建Condition对象，Condition对象用来是线程等待和唤醒线程，需要注意的是Condition对象的唤醒的是用同一个Condition执行await方法的线程，所以也就可以实现唤醒指定类的线程
 * Lock类分公平锁和不公平锁，公平锁是按照加锁顺序来的，非公平锁是不按顺序的，也就是说先执行lock方法的锁不一定先获得锁
 * Lock类有读锁和写锁，读读共享，写写互斥，读写互斥
 */
public class LockTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new LockTest().init();
	}
	
	private void init(){
		final Outputer outputer = new Outputer();
		new Thread(new Runnable(){
			public void run() {
				while(true){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					outputer.output("zhangxiaoxiang");
				}
				
			}
		}).start();
		
		new Thread(new Runnable(){
			public void run() {
				while(true){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					outputer.output("lihuoming");
				}
				
			}
		}).start();
		
	}

	static class Outputer{
		private final Lock lock = new ReentrantLock();
		public void output(String name){
			int len = name.length();
			lock.lock();
			try{
				for(int i=0;i<len;i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}finally{
				lock.unlock();
			}
		}
		
		public synchronized void output2(String name){
			int len = name.length();
			for(int i=0;i<len;i++){
					System.out.print(name.charAt(i));
			}
			System.out.println();
		}
		
		public static synchronized void output3(String name){
			int len = name.length();
			for(int i=0;i<len;i++){
					System.out.print(name.charAt(i));
			}
			System.out.println();
		}	
	}
}
