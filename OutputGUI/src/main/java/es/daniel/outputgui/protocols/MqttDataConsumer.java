package es.daniel.outputgui.protocols;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.daniel.outputgui.data.ExtendedBucket;
import es.daniel.outputgui.data.BucketManagerListener;
import es.daniel.outputgui.data.ParsedPacket;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttDataConsumer extends AbstractConsumer implements MqttCallback {
    MqttClient client;

    public MqttDataConsumer() throws Exception{
        client = new MqttClient("tcp://server.local:1883", "ApplianesClient");
        client.connect();
        client.setCallback(this);
        client.subscribe("AppliancesBucket");
    }

    public void connectionLost(Throwable throwable) {
        //Do Nothing
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String str = new String(mqttMessage.getPayload());
        System.out.println(s + ": " + str);
        ObjectMapper om = new ObjectMapper();
        try {
            ParsedPacket b = om.readValue(str, ParsedPacket.class);
            bm.addPacket(b);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //do Nothing
    }
}
