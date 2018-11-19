package Study.Concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 条件变量实现阻塞队列
 */
public class ConditionCommunication {

    /**
     * @param args
     */
    public static void main(String[] args) {

        final Business business = new Business();
        new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i <= 50; i++) {
                    business.sub(i);
                }

            }
        }).start();

        for (int i = 1; i <= 100; i++) {
            business.main(i);
        }

    }

    static class Business {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        private boolean bCondition = true;
        private int sub = 0;
        private int main = 0;

        public void sub(int i) {
            lock.lock();
            try {
                while (!bCondition) {
                    try {
                        condition.await();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
//                for (int j = 1; j <= 10; j++) {
//                    System.out.println("sub thread sequence of " + j + ",loop of " + i);
//                }
                sub++;
                System.out.println("sub thread sequence of " + sub + ",loop of " + i);
                if (sub >= 5)    //执行5次才交替
                {
                    sub = 0;
                    bCondition = false;
                }
                condition.signal();
            } finally {
                lock.unlock();
            }
        }

        public void main(int i) {
            lock.lock();
            try {
                while (bCondition) {
                    try {
                        condition.await();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
//                for (int j = 1; j <= 100; j++) {
//                    System.out.println("main thread sequence of " + j + ",loop of " + i);
//                }
                main++;
                System.out.println("main thread sequence of " + main + ",loop of " + i);
                if (main >= 10) {
                    main = 0;
                    bCondition = true;
                }
                condition.signal();
            } finally {
                lock.unlock();
            }
        }

    }
}
