package es.daniel.outputgui.gui;

import javax.swing.*;

import es.daniel.outputgui.data.DataManagerListener;

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

    public DataManagerListener getKafkaReceiver() {
        return kafka;
    }

    public DataManagerListener getMqttReceiver() {
        return mqtt;
    }

    public DataManagerListener getRestReceiver() {
        return rest;
    }

    public DataManagerListener getSoapReceiver(){
        return soap;
    }
}
