package es.daniel.outputgui.gui;

import javax.swing.*;

import es.daniel.outputgui.data.DataManagerListener;

import java.awt.*;

public class OutPutFrame extends JFrame{
    ProtocolFrame kafka;
    ProtocolFrame mqtt;

    ProtocolFrame rest;

    public OutPutFrame() throws HeadlessException {
        super("Appliances");
        kafka=new ProtocolFrame("Kafka");
        mqtt=new ProtocolFrame("MQTT");
        rest= new ProtocolFrame("Rest");

        GridLayout layout=new GridLayout(1,3);

        getContentPane().setLayout(layout);
        getContentPane().add(kafka);
        getContentPane().add(mqtt);
        getContentPane().add(rest);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public DataManagerListener getKafkaReceiver() {
        return kafka;
    }

    public DataManagerListener getMqttReceiver() {
        return mqtt;
    }

    public DataManagerListener getRestReceiver() {
        return rest;
    }
}
