package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.ExtendedBucket;
import es.daniel.outputgui.data.DataManagerListener;
import org.apache.kafka.clients.producer.*;

public class KafkaDataProducer implements DataManagerListener {
    private Producer<String, String> producer;


    public KafkaDataProducer() throws Exception {
        producer=KafkaUtils.getInstance().createProducer();
    }


    public void addOrUpdateBucket(ExtendedBucket bucket) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("AppliancesBucket", bucket.toJsonString());
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    System.out.println("Sent, " + recordMetadata);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
