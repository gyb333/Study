package Study;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

import io.nats.client.*;

public class Subscriber {

    public static void  main(String[] args ){
        String[] servers ={"nats://Master:4222","nats://Second:4222","nats://Slave:4222"};     //Options.DEFAULT_URL;

        try {

            String subject="test";
            String queue="queue";
            System.out.println();

            Connection nc = Nats.connect(NatsUtils.createOptions(servers, true));
//            Sub(nc,subject,queue);
            Reply(nc,"request");
            nc.close();

        } catch (Exception exp) {
            exp.printStackTrace();
        }finally {
            System.out.println("Test finally");
        }
    }



    public static void Sub(Connection nc,String subject,String queue) throws TimeoutException, InterruptedException {
        //订阅 队列参数为多个客户端接收消息，每次只有一个客户端接收消息
        Subscription sub = nc.subscribe(subject,queue);
        nc.flush(Duration.ofSeconds(5));

        while (true){
            Message msg = sub.nextMessage(Duration.ofHours(1));

            System.out.printf("Received message \"%s\" on subject \"%s\"\n",
                    new String(msg.getData(), StandardCharsets.UTF_8),
                    msg.getSubject());
        }

    }


    public static void Reply(Connection nc,String subject) throws TimeoutException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(5); // dispatcher runs callback in another thread

        System.out.println();
        Dispatcher d = nc.createDispatcher((msg) -> {
            System.out.printf("Received message \"%s\" on subject \"%s\", replying to %s\n",
                    new String(msg.getData(), StandardCharsets.UTF_8),
                    msg.getSubject(), msg.getReplyTo());
            nc.publish(msg.getReplyTo(), msg.getData());
            latch.countDown();//每次-1
        });
        d.subscribe(subject);

        nc.flush(Duration.ofSeconds(5));

        latch.await();//为0时不再等待

        nc.closeDispatcher(d); // This isn't required, closing the connection will do it
    }
}
