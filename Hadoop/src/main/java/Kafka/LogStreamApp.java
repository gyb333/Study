package Kafka;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;

import java.util.Properties;

public class LogStreamApp  {

    public static void main(String[] args) {
        String fromTopic="fromTopic";
        String toTopic="toTopic";

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,"logProcessor");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"Master:9092,Second:9092,Slave:9092");



        Topology top=new Topology();
        top.addSource("SOURCE",fromTopic)
                .addProcessor("PROCESSOR", new ProcessorSupplier() {
                    public Processor get() {
                        return new LogProcess();
                    }
                },"SOURCE")
                .addSink("SINK",toTopic,"PROCESSOR");

        KafkaStreams streams=new KafkaStreams(top,props);
        streams.start();

    }

}
