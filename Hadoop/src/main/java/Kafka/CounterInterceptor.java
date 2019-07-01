package Kafka;


import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class CounterInterceptor implements ProducerInterceptor<String,String> {
    private  long successCount=0;
    private  long errorCount=0;


    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        return producerRecord;
    }

    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if(e==null){
            successCount++;
        }else{
            errorCount++;
        }
    }

    public void close() {
        System.out.printf("成功的个数："+successCount);
        System.out.printf("失败的个数："+errorCount);
    }

    public void configure(Map<String, ?> map) {

    }
}
