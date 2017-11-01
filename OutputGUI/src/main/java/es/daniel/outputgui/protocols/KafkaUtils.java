package es.daniel.outputgui.protocols;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaUtils {
    private String connectionString;
    private ZooKeeper zk;
    private KafkaUtils() throws Exception{
        zk = new ZooKeeper("server.local:2181", 10000, null);
        List<String> brokerList = new ArrayList<String>();

        List<String> ids = zk.getChildren("/brokers/ids", false);
        for (String id : ids) {
            String brokerInfoString = new String(zk.getData("/brokers/ids/" + id, false, null));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsBroker= objectMapper.readTree(brokerInfoString);
            System.out.println(jsBroker);
            ArrayNode array = (ArrayNode) jsBroker.get("endpoints");
            for(JsonNode n:array){
                brokerList.add(n.textValue());
            }
        }
        connectionString=toList(",", brokerList);
    }

    Producer<String,String> createProducer(){
        Properties props = new Properties();
        //props.put("metadata.broker.list", String.join(",", brokerList));
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,connectionString);

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<String, String>(props);
    }

    public Consumer<String,String> getConsumer(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,connectionString);
        props.put("group.id", "ApplianesClient");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaConsumer<String, String>(props);
    }

    public String toList(String delim, List<String> all){
        String ret=null;
        for(String s:all){
            if(ret==null){
                ret=s;
            }else{
                ret+=delim+s;
            }
        }
        return ret;
    }

    private static KafkaUtils instance;

    public static KafkaUtils getInstance() {
        synchronized (KafkaUtils.class){
            if(instance==null){
                try {
                    instance = new KafkaUtils();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return instance;
    }
}
