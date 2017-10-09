package es.daniel.outputgui.protocols;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.daniel.outputgui.data.Bucket;
import es.daniel.outputgui.data.DataManagerListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttDataConsumer implements MqttCallback {
    private DataManagerListener out;
    MqttClient client;

    public MqttDataConsumer() throws Exception{
        client = new MqttClient("tcp://server.local:1883", "ApplianesClient");
        client.connect();
        client.setCallback(this);
        client.subscribe("AppliancesBucket");
    }

    public DataManagerListener getOut() {
        return out;
    }

    public void setOut(DataManagerListener out) {
        this.out = out;
    }

    public void connectionLost(Throwable throwable) {
        //Do Nothing
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String str = new String(mqttMessage.getPayload());
        System.out.println(s + ": " + str);
        ObjectMapper om = new ObjectMapper();
        try {
            Bucket b = om.readValue(str, Bucket.class);
            out.addOrUpdateBucket(b);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //do Nothing
    }
}
