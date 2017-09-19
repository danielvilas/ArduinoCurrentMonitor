package es.daniel.somanalisys.Model;

import com.googlecode.fannj.ActivationFunction;
import com.googlecode.fannj.Fann;
import com.googlecode.fannj.Layer;
import com.googlecode.fannj.Trainer;
import es.daniel.somanalisys.data.PairDataManager;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.train.BasicTraining;
import org.encog.neural.networks.BasicNetwork;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FannModelManager extends ModelManager<BasicNetwork,PairDataManager> {
    Fann network;
    Trainer trainer;
    File temp;
    protected BasicNetwork createNetwork() {
        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(3));
        layers.add(Layer.create(9, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        network = new Fann(layers);
        return null;
    }

    private void print(FileOutputStream fos, double[] data) throws Exception {
        StringBuffer sb = new StringBuffer();
        for(double d: data){
            sb.append(d);
            sb.append(" ");
        }
        fos.write(sb.toString().getBytes());
    }

    protected BasicTraining createTraining() {
        try {
            temp = File.createTempFile("fannj_", ".net");
            temp.deleteOnExit();
            MLDataSet train = getDataMgr().getTrainData();
            FileOutputStream fos = new FileOutputStream(temp);
            fos.write((train.getRecordCount() + " 3 3\n").getBytes());
            for (MLDataPair pair : train) {
                print(fos, pair.getInput().getData());
                fos.write("\n".getBytes());
                print(fos, pair.getIdeal().getData());
                fos.write("\n".getBytes());
            }
            fos.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        trainer = new Trainer(network);
        return null;
    }
    @Override
    public void train(int iterations){
        throw new NotImplementedException();
    }

    @Override
    public void train(double error, int maxIterations) {
        init();
        float desiredError = .001f;
        int iteration=0;
        int remain=maxIterations;
        do {
            int actual = Math.min(2000, remain);
            float mse = trainer.train(temp.getPath(), actual, 1000, desiredError);
            iteration+=actual;
            remain-=actual;
            if(cb!=null)cb.iterationEvent(this,iteration);
            if(mse<error){
                remain=0;
            }
        }while(remain>0);

        System.out.println(3 + " " + network.getNumInputNeurons());
        System.out.println(3 + " " + network.getNumOutputNeurons());
    }


    private void init(){
        if(network==null){
            createNetwork();
            createTraining();
        }
    }

    public double[] execute(MLData mlData) {
        float[] in = new float[3];
        double[] data = mlData.getData();
        in[0]= (float) data[0];
        in[1]= (float) data[1];
        in[2]= (float) data[2];
        float res[]=network.run(in);
        double ret[]=new double[3];
        ret[0]=res[0];
        ret[1]=res[1];
        ret[2]=res[2];
        return ret;
    }
}
