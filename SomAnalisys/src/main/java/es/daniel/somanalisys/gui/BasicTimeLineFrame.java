package es.daniel.somanalisys.gui;

import es.daniel.somanalisys.Model.ModelManager;
import es.daniel.somanalisys.Model.ModelManagerListener;
import es.daniel.somanalisys.Model.SomModelManager;
import es.daniel.somanalisys.data.DataManager;
import es.daniel.somanalisys.data.SomDataManager;

import javax.swing.*;

public class BasicTimeLineFrame extends JFrame implements Runnable,ModelManagerListener {
    private TimePanel time;
    private Thread thread;
    private SomModelManager modelMgr;
    private SomDataManager dataMgr;


    public BasicTimeLineFrame(){
        dataMgr=new SomDataManager();
        modelMgr=new SomModelManager();
        modelMgr.setDataMgr(dataMgr);
        modelMgr.setListener(this);
        time = new TimePanel(dataMgr,modelMgr);
        this.setSize(800,600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().add(new JScrollPane(time));

        this.thread = new Thread(this);
        thread.start();
    }
    public void run() {
        try {
            dataMgr.readData("data-20170716-15.csv");
            dataMgr.readData("data-20170716-16.csv");
            dataMgr.readData("data-20170716-17.csv");
            dataMgr.readData("data-20170716-18.csv");
            dataMgr.readData("data-20170716-19.csv");
            dataMgr.readData("data-20170716-20.csv");
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        dataMgr.printMaxTrainData();
        modelMgr.train(3000);
        this.time.repaint();
    }

    public void iterationEvent(ModelManager src, int iteration) {
        if(iteration%5==0)
        this.time.repaint();
    }
}
