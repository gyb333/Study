package Study.Kafka;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KfkProducer {
	private static final String path = "KafkaProducer.Properties";
	private static final String topic = "my-topic";

	public static void main(String[] args) {

		try {
			Properties props = CommonUtils.getProperties(path);
			Producer<String, String> producer = new KafkaProducer<String, String>(props);
			for (int i = 1001; i <= 1100; i++) {
//				producer.send(new ProducerRecord<String, String>(topic, "test-msg" + i, Integer.toString(i)));
				producer.send(new ProducerRecord<String, String>(topic,"cb-key"+i, "cb-value" + i), new Callback() {
					@Override
					public void onCompletion(RecordMetadata metadata, Exception exception) {
						if (metadata != null) {
							System.out.println(metadata.partition() + "---" + metadata.offset());
						}
					}
				});
			}

			producer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
