package es.daniel.outputgui;

import es.daniel.outputgui.data.DataManager;
import es.daniel.outputgui.protocols.MqttBucketProducer;

public class MqttProducer implements Runnable{
    DataManager dataMgr;

    MqttProducer() throws  Exception{
        dataMgr = new DataManager();

        MqttBucketProducer mqttDataProducer= new MqttBucketProducer();

        dataMgr.setListener(mqttDataProducer);

    }

    public static void main(String... args) throws  Exception {
        MqttProducer main = new MqttProducer();
        new Thread(main).start();
    }

    public void run(){

        try {
            dataMgr.readData("SomAnalisys/data-20170716-15.csv");
            dataMgr.readData("SomAnalisys/data-20170716-16.csv");
            dataMgr.readData("SomAnalisys/data-20170716-17.csv");
            dataMgr.readData("SomAnalisys/data-20170716-18.csv");
            dataMgr.readData("SomAnalisys/data-20170716-19.csv");
            dataMgr.readData("SomAnalisys/data-20170716-20.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);

        //List<ExtendedBucket> lista = dataMgr.getAllBuckets();
    }
}
