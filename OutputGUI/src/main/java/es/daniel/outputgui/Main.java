package es.daniel.outputgui;

import es.daniel.outputgui.data.Bucket;
import es.daniel.outputgui.data.DataManager;
import es.daniel.outputgui.gui.OutPutFrame;
import org.jfree.ui.RefineryUtilities;

import java.util.List;

public class Main {
    public static void main(String... args) throws  Exception{

        OutPutFrame frame = new OutPutFrame();
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);

        DataManager dataMgr = new DataManager();

        dataMgr.setListener(frame);

        dataMgr.readData("SomAnalisys/data-20170716-15.csv");
        dataMgr.readData("SomAnalisys/data-20170716-16.csv");
        dataMgr.readData("SomAnalisys/data-20170716-17.csv");
        dataMgr.readData("SomAnalisys/data-20170716-18.csv");
        dataMgr.readData("SomAnalisys/data-20170716-19.csv");
        dataMgr.readData("SomAnalisys/data-20170716-20.csv");
        //List<Bucket> lista = dataMgr.getAllBuckets();


    }
}
