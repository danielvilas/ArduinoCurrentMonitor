package es.daniel.outputgui;

import es.daniel.outputgui.data.Bucket;
import es.daniel.outputgui.data.DataManager;
import es.daniel.outputgui.gui.OutPutFrame;
import es.daniel.outputgui.protocols.KafkaDataConsumer;
import es.daniel.outputgui.protocols.KafkaDataProducer;
import es.daniel.outputgui.protocols.MqttDataConsumer;
import es.daniel.outputgui.protocols.RestDataConsumer;
import org.jfree.ui.RefineryUtilities;

import java.util.List;

public class Main{


    public static void main(String... args) throws  Exception {

        OutPutFrame frame = new OutPutFrame();
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);

        KafkaDataConsumer receiver = new KafkaDataConsumer();
        receiver.setOut(frame.getKafkaReceiver());
        new Thread(receiver).start();

        MqttDataConsumer mqtt =new MqttDataConsumer();
        mqtt.setOut(frame.getMqttReceiver());

        RestDataConsumer rest = new RestDataConsumer();
        rest.setOut(frame.getRestReceiver());
        new Thread(rest).start();


    }
}
