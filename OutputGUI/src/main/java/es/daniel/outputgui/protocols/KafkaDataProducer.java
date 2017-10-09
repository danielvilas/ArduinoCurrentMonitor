package es.daniel.outputgui.protocols;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import es.daniel.outputgui.data.Bucket;
import es.daniel.outputgui.data.DataManagerListener;
import org.apache.kafka.clients.producer.*;
import org.apache.zookeeper.ZooKeeper;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaDataProducer implements DataManagerListener {
    private Producer<String, String> producer;


    public KafkaDataProducer() throws Exception {
        producer=KafkaUtils.getInstance().createProducer();
    }


    public void addOrUpdateBucket(Bucket bucket) {
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
