package es.daniel.outputgui.protocols;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.daniel.outputgui.data.Bucket;
import es.daniel.outputgui.data.DataManagerListener;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collection;

public class KafkaDataConsumer implements Runnable {
    private DataManagerListener out;
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
                        Bucket b = om.readValue(record.value(), Bucket.class);
                        out.addOrUpdateBucket(b);
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

    public DataManagerListener getOut() {
        return out;
    }

    public void setOut(DataManagerListener out) {
        this.out = out;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
