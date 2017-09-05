package es.daniel.somanalisys.Model;

import es.daniel.somanalisys.data.DataManager;
import es.daniel.somanalisys.data.SomDataManager;
import es.daniel.somanalisys.gui.MapPanel;
import org.encog.mathutil.rbf.RBFEnum;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.train.BasicTraining;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF;

public class SomModelManager extends ModelManager<SOM,SomDataManager>{

    BasicTrainSOM train;

    public SomModelManager(){
        super();
    }

    @Override
    protected SOM createNetwork() {
        SOM result = new SOM(6,MapPanel.WIDTH * MapPanel.HEIGHT);
        result.reset();
        return result;
    }

    @Override
    protected BasicTraining createTraining() {
        NeighborhoodRBF gaussian = new NeighborhoodRBF(RBFEnum.Gaussian, MapPanel.WIDTH,
                MapPanel.HEIGHT);
        this.train = new BasicTrainSOM(getNetwork(), 0.01, getDataMgr().getTrainData(), gaussian);
        train.setForceWinner(false);
        train.setAutoDecay(2000, 0.8, 0.003, 15, 1);
        return  train;
    }

    @Override
    protected void doIteration(int i) {
        train.autoDecay();
        super.doIteration(i);
    }

    @Override
    public double[] execute(MLData mlData) {
        SOM network = getNetwork();
        MLData in = new BasicMLData(3);
        for(int i=0;i<3;i++){
            in.setData(i,mlData.getData(i));
        }
        int index = network.classify(in);
        return network.getWeights().getRow(index).getData()[0];
    }
}
