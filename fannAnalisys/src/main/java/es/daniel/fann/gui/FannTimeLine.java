package es.daniel.fann.gui;

import es.daniel.fann.data.FannDataManager;
import es.daniel.fann.model.FannModelManager;
import es.daniel.fann.model.ModelManager;
import es.daniel.fann.model.ModelManagerListener;

import javax.swing.*;

public class FannTimeLine extends JFrame implements ModelManagerListener, Runnable {
    private JPanel time;
    private Thread thread;
    private FannModelManager modelMgr;
    private FannDataManager dataMgr;


    public FannTimeLine() {
        dataMgr = new FannDataManager();
        modelMgr = new FannModelManager();
        modelMgr.setDataMgr(dataMgr);
        modelMgr.setListener(this);
        //time = new TimePanel(dataMgr, modelMgr);
        time = new TimePanelContinous(dataMgr, modelMgr);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().add(new JScrollPane(time));

        this.thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            dataMgr.readData("SomAnalisys/data-20170716-15.csv");
            dataMgr.readData("SomAnalisys/data-20170716-16.csv");
            dataMgr.readData("SomAnalisys/data-20170716-17.csv");
            dataMgr.readData("SomAnalisys/data-20170716-18.csv");
            dataMgr.readData("SomAnalisys/data-20170716-19.csv");
            dataMgr.readData("SomAnalisys/data-20170716-20.csv");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        dataMgr.printMaxTrainData();
        modelMgr.train(0.01, 400000);
        this.time.repaint();
    }

    public void iterationEvent(ModelManager src, int iteration) {
        if (iteration % 50 == 0)
            this.time.repaint();
    }
}
