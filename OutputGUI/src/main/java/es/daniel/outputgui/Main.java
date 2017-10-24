package es.daniel.outputgui;

import es.daniel.outputgui.gui.OutPutFrame;
import es.daniel.outputgui.protocols.KafkaDataConsumer;
import es.daniel.outputgui.protocols.MqttDataConsumer;
import es.daniel.outputgui.protocols.RestDataConsumer;
import es.daniel.outputgui.protocols.SoapDataConsumer;
import org.jfree.ui.RefineryUtilities;

public class Main{


    public static void main(String... args) throws  Exception {

        OutPutFrame frame = new OutPutFrame();
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);


        KafkaDataConsumer receiver = new KafkaDataConsumer();
        receiver.setOut(frame.getKafkaReceiver());
        new Thread(receiver).start();

        try {
            MqttDataConsumer mqtt = new MqttDataConsumer();
            mqtt.setOut(frame.getMqttReceiver());
        }catch (Exception e){
            e.printStackTrace();
        }

        RestDataConsumer rest = new RestDataConsumer();
        rest.setOut(frame.getRestReceiver());
        new Thread(rest).start();

        SoapDataConsumer soap = new SoapDataConsumer();
        soap.setOut(frame.getSoapReceiver());
        new Thread(soap).start();

    }
}
