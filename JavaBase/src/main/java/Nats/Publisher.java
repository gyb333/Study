package Nats;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;


import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Publisher {

    public static  void main(String[] args ){
        String[] servers ={"nats://Master:4222","nats://Second:4222","nats://Slave:4222"};
        try {
            Connection nc = Nats.connect(NatsUtils.createOptions(servers, true));
            System.out.println();
            Scanner sc = new Scanner(System.in);
            String message;

            while(sc.hasNextLine()) {
                message = sc.nextLine();
//                pub(nc,"test",message);
                Request(nc,"request",message);
            }

            nc.flush(Duration.ofSeconds(5));
            nc.close();

        } catch (Exception exp) {
            exp.printStackTrace();
        }finally {
            System.out.println("Test finally");
        }
    }

    /**
     * 发布订阅模式
     * @param nc
     * @param subject
     * @param message
     */
    public static void pub(Connection nc,String subject,String message){
        System.out.printf("Sending %s on %s, server is %s\n", message, subject, nc.getConnectedUrl());
        System.out.println();
        //发布
        nc.publish(subject, message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 请求回复模式
     * @param nc
     * @param subject
     * @param message
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public static void Request(Connection nc,String subject,String message) throws InterruptedException, ExecutionException, TimeoutException {
        Future<Message> replyFuture = nc.request(subject, message.getBytes(StandardCharsets.UTF_8));
        Message reply = replyFuture.get(5, TimeUnit.SECONDS);

        System.out.println();
        System.out.printf("Received reply \"%s\" on subject \"%s\"\n",
                new String(reply.getData(), StandardCharsets.UTF_8),
                reply.getSubject());
    }
}
