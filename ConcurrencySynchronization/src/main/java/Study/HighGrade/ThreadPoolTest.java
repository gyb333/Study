package Study.HighGrade;

import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

    static void SingleThread(ExecutorService threadPool) {

        for (int i = 1; i <= 10; i++) {
            final int task = i;
            threadPool.execute(new Runnable() {
                public void run() {
                    for (int j = 1; j <= 10; j++) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + " is looping of " + j + " for  task of " + task);
                    }
                }
            });
        }
        System.out.println("all of 10 tasks have committed! ");

    }

    public static void main(String[] args) {
//        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        ExecutorService threadPool = Executors.newCachedThreadPool();
//        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        SingleThread(threadPool);

        Scanner scanner = new Scanner(System.in);
        scanner.next();


        final Calendar calendar = new GregorianCalendar();
        Executors.newScheduledThreadPool(3)
                .scheduleAtFixedRate(           //initialDelay之后执行，每隔多久period在执行
                        new Runnable() {
                            public void run() {
                                System.out.println(calendar.get(Calendar.SECOND) + " bombing!");
                            }
                        }, 6, 2, TimeUnit.SECONDS);


    }


}
