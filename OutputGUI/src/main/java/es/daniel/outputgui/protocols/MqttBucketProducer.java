package es.daniel.outputgui.protocols;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.daniel.outputgui.data.DataManagerListener;
import es.daniel.outputgui.data.ExtendedBucket;
import es.daniel.outputgui.data.BucketManagerListener;
import es.daniel.outputgui.data.ParsedPacket;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttBucketProducer implements DataManagerListener {

    MqttClient client;

    public MqttBucketProducer() throws Exception {
        client = new MqttClient("tcp://server.local:1883", MqttClient.generateClientId());
        client.connect();
    }

    public void addPacket(ParsedPacket p) {
        try {
            ObjectMapper om= new ObjectMapper();
            String str= om.writeValueAsString(p);
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
