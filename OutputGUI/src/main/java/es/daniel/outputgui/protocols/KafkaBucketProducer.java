package es.daniel.outputgui.protocols;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.daniel.outputgui.data.DataManagerListener;
import es.daniel.outputgui.data.ExtendedBucket;
import es.daniel.outputgui.data.BucketManagerListener;
import es.daniel.outputgui.data.ParsedPacket;
import org.apache.kafka.clients.producer.*;

public class KafkaBucketProducer implements DataManagerListener {
    private Producer<String, String> producer;


    public KafkaBucketProducer() throws Exception {
        producer=KafkaUtils.getInstance().createProducer();
    }


    public void addPacket(ParsedPacket p) {
        try {

            ObjectMapper om= new ObjectMapper();
            String data= om.writeValueAsString(p);

            ProducerRecord<String, String> record = new ProducerRecord<String, String>("AppliancesBucket", data);
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
