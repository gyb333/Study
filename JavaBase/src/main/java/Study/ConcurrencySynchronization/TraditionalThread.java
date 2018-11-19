package Study.ConcurrencySynchronization;

public class TraditionalThread {

	/**
	 *传统的线程
	 */

	static void thread(){
		Thread thread = new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("1:" + Thread.currentThread().getName());
					System.out.println("2:" + this.getName());
				}
			}
		};
		thread.start();
	}

	static void runnable(){
		Thread thread2 = new Thread(new Runnable(){
			public void run() {
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("1:" + Thread.currentThread().getName());

				}

			}
		});
		thread2.start();

	}


	public static void main(String[] args) {

		thread();
		runnable();
		new Thread(
				new Runnable(){
					public void run() {
						while(true){
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println("runnable :" + Thread.currentThread().getName());

						}							
					}
				}
		){
			public void run() {
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("thread :" + Thread.currentThread().getName());

				}	
			}
		}.start();
		
		
	}

}
