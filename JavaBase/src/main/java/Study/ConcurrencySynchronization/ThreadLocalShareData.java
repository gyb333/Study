package Study.ConcurrencySynchronization;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadLocalShareData {

    private static ThreadLocal<Integer> x = new ThreadLocal<Integer>();

//	private static ThreadLocal<ThreadScopeData> myThreadScopeData = new ThreadLocal<ThreadScopeData>();

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                public void run() {
                    int data = new Random().nextInt();
                    System.out.println(Thread.currentThread().getName()
                            + " has put data :" + data);
                    x.set(data);
                    ThreadScopeData tsd = ThreadScopeData.getThreadInstance();
                    tsd.setName("name" + data);
                    tsd.setAge(data);
                    new Moudle().get("A");
                    new Moudle().get("B");
                }
            }).start();
        }
    }

    static class Moudle {
        public void get(String moudle) {
            int data = x.get();
            System.out.println(moudle+" from " + Thread.currentThread().getName()
                    + " get data :" + data);

            ThreadScopeData tsd = ThreadScopeData.getThreadInstance();
            System.out.println(moudle+" from " + Thread.currentThread().getName()
                    + " getMyData: " + tsd.getName() + "," + tsd.getAge());
        }
    }


}

class ThreadScopeData {
    private ThreadScopeData() {
    }

    public static /*synchronized*/ ThreadScopeData getThreadInstance() {
        ThreadScopeData instance = map.get();
        if (instance == null) {
            instance = new ThreadScopeData();
            map.set(instance);
        }
        return instance;
    }

    //private static ThreadScopeData instance = null;//new ThreadScopeData();
    private static ThreadLocal<ThreadScopeData> map = new ThreadLocal<ThreadScopeData>();

    private String name;
    private int age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
