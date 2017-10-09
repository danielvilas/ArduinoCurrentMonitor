package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.Bucket;
import es.daniel.outputgui.data.DataManagerListener;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttDataProducer implements DataManagerListener {

    MqttClient client;

    public MqttDataProducer() throws Exception {
        client = new MqttClient("tcp://server.local:1883", MqttClient.generateClientId());
        client.connect();
    }

    public void addOrUpdateBucket(Bucket bucket) {
        try {
            String str = bucket.toJsonString();
            MqttMessage message = new MqttMessage();
            message.setPayload(str.getBytes());
            client.publish("AppliancesBucket", message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(client!=null && client.isConnected()){
            client.disconnect();
            client.close();
        }
    }
}
