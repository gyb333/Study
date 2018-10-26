package Study.Kafka;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
 

 

public class KfkConsumer {
	private static final String path = "KafkaConsumer.Properties";
	private static final String topic = "my-topic";
 
/**
 * 	重复消费：不同消费组
 * 	不重复消费：同一个消费组
 * 			同一个组内，多个消费者同时阻塞消费数据
 * 			一个分区的数据，只能交给一个消费者消费
 * 			一个消费者可以消费多个分区中的数据，可以跨主题
 * 	负载均衡：尽可能将多个分区，分配给不同的消费者消费 			
 *	消费顺序：保证一个分区中的数据消费过程是有序的
 *  
 */
	public static void main(String[] args) {
		try {
			Properties props = CommonUtils.getProperties(path);
			final Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				public void run() {
					if(consumer!=null)
						consumer.close();
					System.out.println("closed............");
				}
			}));

			// 消费者订阅的topic, 可同时订阅多个
			consumer.subscribe(Arrays.asList(topic));
			while (true) {
				// 读取数据，读取超时时间为100ms 
				ConsumerRecords<String, String> records = consumer.poll(100);
				for (ConsumerRecord<String, String> record : records)
					System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(),
							record.value());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
