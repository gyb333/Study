package Study.ConcurrencySynchronization;

import java.util.*;

/**
 * 传统的定时器
 */
public class TraditionalTimer {

    void Traditional(){
        	new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("bombing!");

			}
		}, 10000,3000);
    }


    private static int count = 0;
    static void recursiveTimer(){
        class MyTimerTask extends TimerTask {
            @Override
            public void run() {
                count = (count + 1) % 2;
                System.out.println("bombing!");
                new Timer().schedule(new MyTimerTask(), 2000 + 2000 * count);
            }
        }
        new Timer().schedule(new MyTimerTask(), 2000);

    }



    public static void main(String[] args) {
        recursiveTimer();
        while (true) {
            System.out.println(new GregorianCalendar().get(Calendar.SECOND));
//            System.out.println(new Date().getSeconds());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



}
