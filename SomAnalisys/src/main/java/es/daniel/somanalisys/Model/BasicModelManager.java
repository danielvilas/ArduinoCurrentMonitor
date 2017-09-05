package es.daniel.somanalisys.Model;

import es.daniel.somanalisys.data.DataManager;
import es.daniel.somanalisys.data.PairDataManager;
import es.daniel.somanalisys.data.SomDataManager;
import es.daniel.somanalisys.gui.MapPanel;
import org.encog.engine.network.activation.ActivationElliott;
import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.mathutil.rbf.RBFEnum;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.train.BasicTraining;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF;

public class BasicModelManager extends ModelManager<BasicNetwork,PairDataManager> {

    public BasicModelManager(){
        super();
    }

    protected BasicNetwork createNetwork() {

        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null,true,3));
        network.addLayer(new BasicLayer(new ActivationElliott(),true,9));
        network.addLayer(new BasicLayer(new ActivationElliott(),true,6));
        //network.addLayer(new BasicLayer(new ActivationSigmoid(),true,10));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),true,3));
        network.getStructure().finalizeStructure();
        network.reset();

        return network;
    }

    @Override
    protected BasicTraining createTraining() {
        final Backpropagation train = new Backpropagation(getNetwork(), getDataMgr().getTrainData());
        return train;
    }

    public double[] execute(MLData mlData) {
        BasicNetwork network = getNetwork();
        MLData out = network.compute(mlData);
        return out.getData();
    }
}
