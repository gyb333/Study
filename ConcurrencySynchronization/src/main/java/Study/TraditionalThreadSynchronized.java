package Study;

public class TraditionalThreadSynchronized {

    /**
     * 线程与线程之间互斥
     * 非静态方法synchronized与synchronized (this) 互斥同步
     * 静态方法synchronized与synchronized (Outputer.class) 互斥同步
     */
    public static void main(String[] args) {
        new TraditionalThreadSynchronized().init();
    }

    private void init() {
        final Outputer outputer = new Outputer();
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    outputer.output2("zhangxiaoxiang");
                }

            }
        }).start();

        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    outputer.outputlock("lihuoming");
                }

            }
        }).start();

    }

    static class Outputer {
        /**
         * 非静态方法synchronized与synchronized (this) 互斥同步
         *
         */
        public synchronized void output(String name) {
            int len = name.length();
            for (int i = 0; i < len; i++) {
                System.out.print(name.charAt(i));
            }
            System.out.println();
        }
        /**
         * 静态方法synchronized与synchronized (Outputer.class) 互斥同步
         *
         */
        public static synchronized void output2(String name) {
            int len = name.length();
            for (int i = 0; i < len; i++) {
                System.out.print(name.charAt(i));
            }
            System.out.println();
        }

        public void outputlock(String name) {
            int len = name.length();
            synchronized (Outputer.class) {
                for (int i = 0; i < len; i++) {
                    System.out.print(name.charAt(i));
                }
                System.out.println();
            }
        }



    }
}
