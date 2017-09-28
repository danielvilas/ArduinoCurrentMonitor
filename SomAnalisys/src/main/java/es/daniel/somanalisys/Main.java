package es.daniel.somanalisys;

import com.googlecode.fannj.ActivationFunction;
import com.googlecode.fannj.Fann;
import com.googlecode.fannj.Layer;
import com.googlecode.fannj.Trainer;
import es.daniel.somanalisys.Model.BasicModelManager;
import es.daniel.somanalisys.Model.FannModelManager;
import es.daniel.somanalisys.Model.SomModelManager;
import es.daniel.somanalisys.data.PairDataManager;
import es.daniel.somanalisys.data.SomDataManager;
import es.daniel.somanalisys.gui.BasicAnalisysFrame;
import es.daniel.somanalisys.gui.BasicTimeLineFrame;
import es.daniel.somanalisys.gui.FannTimeLineFrame;
import es.daniel.somanalisys.gui.MlpTimeLineFrame;
import org.apache.commons.io.IOUtils;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 6/8/17.
 */
public class Main{

    public Main() {

    }

    public static void main(String args[]) throws Exception{
        BasicAnalisysFrame frame = new BasicAnalisysFrame();
        //BasicTimeLineFrame frame = new BasicTimeLineFrame();
        //MlpTimeLineFrame frame = new  MlpTimeLineFrame();
        //FannTimeLineFrame frame = new FannTimeLineFrame();
        frame.setVisible(true);
        //run();
        //runFann();

    }

    private static void runFann() throws Exception {
        File temp = File.createTempFile("fannj_", ".net");
        temp.deleteOnExit();
       /* IOUtils.copy(Main.class.getResourceAsStream("/xor.data"),  new FileOutputStream(temp));

        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(2));
        layers.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(1, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        Fann fann = new Fann(layers);
        Trainer trainer = new Trainer(fann);
        float desiredError = .001f;
        float mse = trainer.train(temp.getPath(), 500000, 1000, desiredError);
        System.out.println(2 + "" + fann.getNumInputNeurons());
        System.out.println(1 + "" + fann.getNumOutputNeurons());
        System.out.println(-1f + "" + fann.run(new float[]{-1, -1})[0]);
        System.out.println(1f + "" + fann.run(new float[]{-1, 1})[0]);
        System.out.println(1f + "" + fann.run(new float[]{1, -1})[0]);
        System.out.println(-1f + "" + fann.run(new float[]{1, 1})[0]);
        fann.close();*/

        PairDataManager dataMgr=new PairDataManager();
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

        MLDataSet train = dataMgr.getTrainData();
        FileOutputStream fos = new FileOutputStream(temp);
        fos.write((train.getRecordCount()+" 3 3\n").getBytes());
        for(MLDataPair pair:train){
            print(fos,pair.getInput().getData());
            fos.write("\n".getBytes());
            print(fos,pair.getIdeal().getData());
            fos.write("\n".getBytes());
        }
        fos.close();

        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(3));
        layers.add(Layer.create(9, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        Fann fann = new Fann(layers);
        Trainer trainer = new Trainer(fann);
        float desiredError = .001f;
        float mse = trainer.train(temp.getPath(), 500000, 1000, desiredError);
        System.out.println(3 + " " + fann.getNumInputNeurons());
        System.out.println(3 + " " + fann.getNumOutputNeurons());

    }

    private static void print(FileOutputStream fos, double[] data) throws Exception {
        StringBuffer sb = new StringBuffer();
        for(double d: data){
            sb.append(d);
            sb.append(" ");
        }
        fos.write(sb.toString().getBytes());
    }

    private static void run(){
        PairDataManager dataMgr=new PairDataManager();
        FannModelManager modelMgr=new FannModelManager();
        modelMgr.setDataMgr(dataMgr);

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
        modelMgr.train(0.01,50*1000);
        BasicMLDataPair tmp= (BasicMLDataPair) dataMgr.getTrainData().get(0);
        double[] ret = modelMgr.execute(tmp.getInput());
        System.out.println(ret);

    }
}
