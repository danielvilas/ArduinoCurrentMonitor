package es.daniel.outputgui;

import es.daniel.outputgui.data.DataManager;
import es.daniel.outputgui.protocols.RestDataProducer;
import es.daniel.outputgui.protocols.SoapDataProducer;

public class SoapProducer implements Runnable{
    DataManager dataMgr;

    SoapProducer() throws  Exception{
        dataMgr = new DataManager();

        SoapDataProducer mqttDataProducer= new SoapDataProducer();

        dataMgr.setListener(mqttDataProducer);
    }

    public static void main(String... args) throws  Exception {
        SoapProducer main = new SoapProducer();
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
