package es.daniel.outputgui;

import es.daniel.outputgui.data.DataManager;
import es.daniel.outputgui.gui.OutPutFrame;
import es.daniel.outputgui.protocols.KafkaDataConsumer;
import es.daniel.outputgui.protocols.KafkaDataProducer;
import org.jfree.ui.RefineryUtilities;

public class KafkaProducer implements Runnable{
    DataManager dataMgr;

    KafkaProducer() throws  Exception{
        dataMgr = new DataManager();

        KafkaDataProducer kafkaDataProducer= new KafkaDataProducer();

        dataMgr.setListener(kafkaDataProducer);

    }

    public static void main(String... args) throws  Exception {
        KafkaProducer main = new KafkaProducer();
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

        //List<Bucket> lista = dataMgr.getAllBuckets();
    }
}
