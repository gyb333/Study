package Study.ConcurrencySynchronization;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程之间共享内部类Runnable 可以通过static 变量共享
 * AtomicInteger
 */
public class MultiThreadShareData {

    private static ShareData data = new ShareData();
    static AtomicInteger ai = new AtomicInteger(0);

    public static void main(String[] args) {
        new Thread(new Dec(data)).start();
        new Thread(new Inc(data)).start();

        final ShareData data = new ShareData();
        new Thread(new Runnable() {
            public void run() {
                data.decrement();
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                data.increment();
            }
        }).start();

        for(int i=0;i<10;i++){
            final  int j=i;
            new Thread(new Runnable() {
                public void run() {
                    int val=1;
                    if(j%2==0)val=-1;
                    System.out.println(j+" "+ai.addAndGet(val));
                }
            }).start();
        }
    }

}

class Dec implements Runnable {
    private ShareData data;

    public Dec(ShareData data) {
        this.data = data;
    }

    public void run() {
        data.decrement();

    }
}

class Inc implements Runnable {
    private ShareData data;

    public Inc(ShareData data) {
        this.data = data;
    }

    public void run() {
        data.increment();
    }
}

class ShareData {

    private int j = 0;

    public synchronized void increment() {
        j++;
        System.out.println(j);
    }

    public synchronized void decrement() {
        j--;
        System.out.println(j);
    }
}