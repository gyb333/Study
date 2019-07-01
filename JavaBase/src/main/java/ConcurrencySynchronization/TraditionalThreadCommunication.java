package ConcurrencySynchronization;

public class TraditionalThreadCommunication {

    /**
     * 传统线程同步通信
     */
    public static void main(String[] args) {

        final Business business = new Business();
        new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i <= 50; i++) {
                    business.sub(i);
                }
            }
        }
        ).start();

        for (int i = 1; i <= 100; i++) {
            business.main(i);
        }

    }

}

/**
 * sub 与 main 方法交替执行
 * sub 执行5次，main执行10次
 */
class Business {
    private boolean bCondition = true;  //条件变量用作线程同步通信

    private int sub =0;
    private int main=0;
    //先子线程
    public synchronized void sub(int i) {
        while (!bCondition) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//        for (int j = 1; j <= 10; j++) {
//            System.out.println("sub thread sequence of " + j + ",loop of " + i);
//        }
        sub++;
        System.out.println("sub thread sequence of " + sub + ",loop of " + i);

        if(sub>=5)    //执行10次才交替
        {
            sub=0;
            bCondition = false;
        }
        this.notify();
    }

    public synchronized void main(int i) {
        while (bCondition) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//        for (int j = 1; j <= 100; j++) {
//            System.out.println("main thread sequence of " + j + ",loop of " + i);
//        }
        main++;
        System.out.println("main thread sequence of " + main + ",loop of " + i);
        if(main>=10){
            main=0;
            bCondition = true;
        }
        this.notify();
    }
}
