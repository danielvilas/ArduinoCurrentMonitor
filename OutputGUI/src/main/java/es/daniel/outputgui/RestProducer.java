package es.daniel.outputgui;

import es.daniel.outputgui.data.DataManager;
import es.daniel.outputgui.protocols.MqttDataProducer;
import es.daniel.outputgui.protocols.RestDataProducer;

public class RestProducer implements Runnable{
    DataManager dataMgr;

    RestProducer() throws  Exception{
        dataMgr = new DataManager();

        RestDataProducer mqttDataProducer= new RestDataProducer();

        dataMgr.setListener(mqttDataProducer);

    }

    public static void main(String... args) throws  Exception {
        RestProducer main = new RestProducer();
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
