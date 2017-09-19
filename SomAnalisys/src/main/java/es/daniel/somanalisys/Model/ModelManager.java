package es.daniel.somanalisys.Model;

import es.daniel.somanalisys.data.DataManager;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.train.BasicTraining;


public abstract class ModelManager<NetWork, DataMgr extends  DataManager> {
    private DataMgr dataMgr;

    private NetWork network;
    private BasicTraining train;
    protected ModelManagerListener cb;

    public ModelManager(){
    }

    protected abstract NetWork createNetwork();
    protected abstract BasicTraining createTraining();

    public NetWork getNetwork() {
        init();
        return this.network;
    }
    public DataMgr getDataMgr() {
        return dataMgr;
    }

    public void setDataMgr(DataMgr dataMgr) {
        this.dataMgr = dataMgr;
    }

    private void init(){
        if(network==null){
            network=createNetwork();
            train=createTraining();
        }
    }

    public void train(int iterations){
        init();
        MLDataSet data = dataMgr.getTrainData();
        this.train.setTraining(data);

        for (int i = 0; i < iterations; i++) {
            doIteration(i);
        }
    }

    public void train(double error, int maxIterations){
        init();
        int epoch = 1;

        do {
            train.iteration();
            if(cb!=null)cb.iterationEvent(this,epoch);
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            epoch++;
        } while(train.getError() > error && epoch<=maxIterations);
        train.finishTraining();
    }

    protected void doIteration(int i) {
        this.train.iteration();
        if(cb!=null)cb.iterationEvent(this,i);
        System.out.println("Iteration " + i + "," + this.train.toString());
    }


    public void setListener(ModelManagerListener cb) {
        this.cb = cb;
    }

    public abstract double[] execute(MLData mlData);
}
