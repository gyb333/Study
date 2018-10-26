package Study.Kafka;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

public class LogProcess implements Processor<byte[],byte[]> {

    private ProcessorContext context;

    public void init(ProcessorContext processorContext) {
        context=processorContext;
    }

    public void process(byte[] key, byte[] value) {
        String data = new String(value);
        if(data.contains(">>>")){
            data=data.split(">>>")[1];
        }
        context.forward(key,data.getBytes());
    }

    public void close() {

    }
}


