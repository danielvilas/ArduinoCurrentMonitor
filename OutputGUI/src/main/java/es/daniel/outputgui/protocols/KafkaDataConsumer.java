package es.daniel.outputgui.protocols;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.daniel.outputgui.data.BucketManager;
import es.daniel.outputgui.data.ExtendedBucket;
import es.daniel.outputgui.data.BucketManagerListener;
import es.daniel.outputgui.data.ParsedPacket;
import org.apache.kafka.clients.consumer.*;

import java.util.Arrays;

public class KafkaDataConsumer extends AbstractConsumer implements Runnable {
    private Consumer<String,String> consumer;
    private boolean running=true;

    public KafkaDataConsumer(){
        consumer= KafkaUtils.getInstance().getConsumer();
        consumer.subscribe(Arrays.asList("AppliancesBucket"));
    }
    
    public void run(){
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.offset() + ": " + record.value());
                    ObjectMapper om = new ObjectMapper();
                    try {
                        ParsedPacket b = om.readValue(record.value(), ParsedPacket.class);
                        bm.addPacket(b);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


                try {
                    consumer.commitSync();
                } catch (CommitFailedException e) {
                    // application specific failure handling
                }
            }
        } finally {
            consumer.close();
        }
    }



    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
