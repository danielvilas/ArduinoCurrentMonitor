package es.daniel.outputgui.gui;

import javax.swing.*;

import es.daniel.outputgui.data.BucketManagerListener;

import java.awt.*;

public class OutPutFrame extends JFrame{
    ProtocolFrame kafka;
    ProtocolFrame mqtt;

    ProtocolFrame rest;
    ProtocolFrame soap;

    public OutPutFrame() throws HeadlessException {
        super("Appliances");
        kafka=new ProtocolFrame("Kafka");
        mqtt=new ProtocolFrame("MQTT");
        rest= new ProtocolFrame("Rest");
        soap=new ProtocolFrame("SOAP");

        GridLayout layout=new GridLayout(1,4);



        JPanel pane = new JPanel();
        pane.setLayout(layout);
       // getContentPane().setLayout(layout);
        pane.add(kafka);
        pane.add(mqtt);
        pane.add(rest);
        pane.add(soap);

        JScrollPane scroll = new JScrollPane(pane);
        getContentPane().add(scroll);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public BucketManagerListener getKafkaReceiver() {
        return kafka;
    }

    public BucketManagerListener getMqttReceiver() {
        return mqtt;
    }

    public BucketManagerListener getRestReceiver() {
        return rest;
    }

    public BucketManagerListener getSoapReceiver(){
        return soap;
    }
}
