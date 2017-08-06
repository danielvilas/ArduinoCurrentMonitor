package es.daniel.somanalisys;

import es.daniel.datalogger.data.LogData;
import es.daniel.datalogger.data.LogPacket;
import org.encog.mathutil.randomize.RangeRandomizer;
import org.encog.mathutil.rbf.RBFEnum;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF;
import org.jtransforms.fft.DoubleFFT_1D;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 6/8/17.
 */
public class Main extends JFrame implements  Runnable{

    private MapPanel map;
    private SOM network;
    private Thread thread;
    private BasicTrainSOM train;
    private NeighborhoodRBF gaussian;

    public Main() {
        this.setSize(640, 480);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.network = createNetwork();
        this.getContentPane().add(map = new MapPanel(this.getNetwork()));
        this.gaussian = new NeighborhoodRBF(RBFEnum.Gaussian,MapPanel.WIDTH,
                MapPanel.HEIGHT);
        this.train = new BasicTrainSOM(this.network, 0.01, null, gaussian);
        train.setForceWinner(false);
        this.thread = new Thread(this);
        thread.start();
    }

    public static void main(String args[]){
        Main frame = new Main();
        frame.setVisible(true);
    }

    public SOM getNetwork() {
        return this.network;
    }

    private SOM createNetwork() {
        SOM result = new SOM(3,MapPanel.WIDTH * MapPanel.HEIGHT);
        result.reset();
        return result;
    }

    public MLDataSet readData(String file, MLDataSet in) throws Exception {

        BasicMLDataSet ret;
        if(in==null) {
            ret = new BasicMLDataSet();
        }else{
            ret= (BasicMLDataSet) in;
        }
        DataReader dt = new DataReader(file);
        List<LogPacket> list= dt.readFile();
        for(LogPacket lp : list) {
            double dData[] = new double[1000];
            for (int j = 0; j < 1000; j++) {
                LogData lg = lp.getData(j);
                dData[j]=(lg.getA0() - 512.0) /512.0;
            }

            double[] fft = new double[dData.length * 2];
            System.arraycopy(dData, 0, fft, 0, dData.length);
            DoubleFFT_1D fftDo = new DoubleFFT_1D(dData.length);
            fftDo.realForwardFull(fft);
            BasicMLData data = new BasicMLData(3);
            data.setData(0, getMagnitude(fft,50)/30.0);
            data.setData(1, getMagnitude(fft,150)/30.0);
            data.setData(2, getMagnitude(fft,250)/30.0);
            ret.add(data);
        }



        return ret;
    }

    public double getMagnitude(double[] fft, int i){
        double re = fft[2*i];
        double im = fft[2*i+1];
        return  Math.sqrt(re*re+im*im);
    }


    public void run() {
        MLDataSet data;
        try {
            data = readData("data-20170716-16.csv",null);
            data = readData("data-20170716-15.csv",data);
            data = readData("data-20170716-17.csv",data);
            data = readData("data-20170716-18.csv",data);
            data = readData("data-20170716-19.csv",data);
            data = readData("data-20170716-20.csv",data);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        this.train.setTraining(data);
        this.train.setAutoDecay(1000, 0.8, 0.003, 15, 5);

        for (int i = 0; i < 1000; i++) {

            this.train.iteration();
            this.train.autoDecay();
            this.map.repaint();
            System.out.println("Iteration " + i + "," + this.train.toString());
        }
        this.map.repaint();
    }
}
