package es.daniel.fann.model;

import com.googlecode.fannj.ActivationFunction;
import com.googlecode.fannj.Fann;
import com.googlecode.fannj.Layer;
import com.googlecode.fannj.Trainer;
import es.daniel.fann.data.Appliance;
import es.daniel.fann.data.FannDataManager;
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

public class FannModelManager extends ModelManager<BasicNetwork,FannDataManager> {
    Fann network;
    Trainer trainer;
    File tmpFileTrain;
    File tmpFileTest;
    protected BasicNetwork createNetwork() {
        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(5));
        layers.add(Layer.create(9, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(5, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(Appliance.values().length, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
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
            tmpFileTrain = File.createTempFile("fannj_", ".train");
            tmpFileTrain.deleteOnExit();

            tmpFileTest = File.createTempFile("fannj_", ".test");
            tmpFileTest.deleteOnExit();

            MLDataSet train = getDataMgr().getTrainData();
            writeDataSet(train,tmpFileTrain);

            MLDataSet test = getDataMgr().getTestData();
            writeDataSet(test,tmpFileTest);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        trainer = new Trainer(network);

        return null;
    }

    private void writeDataSet(MLDataSet train, File outFile) throws Exception {
        System.out.println("Create Train DS: "+ outFile.getName());
        FileOutputStream fos = new FileOutputStream(outFile);
        fos.write((train.getRecordCount() + " 5 4\n").getBytes());
        for (MLDataPair pair : train) {
            print(fos, pair.getInput().getData());
            fos.write("\n".getBytes());
            print(fos, pair.getIdeal().getData());
            fos.write("\n".getBytes());
        }
        fos.close();
        System.out.println("Created Train DS: "+ outFile.getName());
    }

    @Override
    public void train(int iterations){
        throw new NotImplementedException();
    }

    @Override
    public void train(double error, int maxIterations) {
        init();
        float desiredError = (float) error;
        int iteration=0;
        int remain=maxIterations;
        if(cb!=null)cb.iterationEvent(this,iteration);
        do {
            int actual = Math.min(2000, remain);
            float mse = trainer.train(tmpFileTrain.getPath(), actual, 100, desiredError);
            iteration+=actual;
            remain-=actual;
            if(cb!=null)cb.iterationEvent(this,iteration);
            if(mse<error){
                remain=0;
            }
            float testMse=trainer.test(tmpFileTest.getPath());
            System.out.println("\n  Iteration: "+iteration);
            System.out.println("Train Error: "+mse);
            System.out.println(" Test Error: "+testMse);

        }while(remain>0);

        System.out.println("i6" + " " + network.getNumInputNeurons());
        System.out.println("o4" + " " + network.getNumOutputNeurons());
    }


    private void init(){
        if(network==null){
            createNetwork();
            createTraining();
        }
    }

    public double[] execute(MLData mlData) {
        float[] in = new float[5];
        double[] data = mlData.getData();
        in[0]= (float) data[0];
        in[1]= (float) data[1];
        in[2]= (float) data[2];
        in[3]= (float) data[3];
        in[4]= (float) data[4];
        //in[5]= (float) data[5];
        double ret[]=new double[4];
        if(network!=null) {
            float res[] = network.run(in);
            ret[0] = res[0];
            ret[1] = res[1];
            ret[2] = res[2];
            ret[3] = res[3];
        }
        return ret;
    }
}
