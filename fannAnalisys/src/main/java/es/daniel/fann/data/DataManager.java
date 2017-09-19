package es.daniel.somanalisys.data;

import es.daniel.datalogger.data.LogData;
import es.daniel.datalogger.data.LogPacket;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.jtransforms.fft.DoubleFFT_1D;

import java.util.Date;
import java.util.List;

public abstract class DataManager<DataType> {

    public static final double[] YELLOW={1.0,1.0,0.0};
    public static final double[] RED={1.0,0.0,0.0};
    public static final double[] GREEN={0.0,1.0,0.0};
    public static final double[] BLUE={0.0,0.0,1.0};

    public static final int TIME_MARGIN =1;
    public static final int TIME_SIZE = 2;

    private MLDataSet trainData;
    private MLDataSet testData;
    private MLDataSet allData;

    public DataManager(){
        trainData=new BasicMLDataSet();
        testData=new BasicMLDataSet();
        allData=new BasicMLDataSet();
    }

    public void readData(String file) throws Exception{
        DataReader dt = new DataReader(file);
        List<LogPacket> list= dt.readFile();
        for(LogPacket lp : list) {
            Date tmp = lp.getData(0).getDate();
            TimeClasifier stateType= getState(tmp);
            double dData[] = new double[1000];
            for (int j = 0; j < 1000; j++) {
                LogData lg = lp.getData(j);
                dData[j]=(lg.getA0() - 512.0) /512.0;
            }

            double[] fft = new double[dData.length * 2];
            System.arraycopy(dData, 0, fft, 0, dData.length);
            DoubleFFT_1D fftDo = new DoubleFFT_1D(dData.length);
            fftDo.realForwardFull(fft);
            //BasicMLData data = new BasicMLData(3);
            double in[]=new double[]{getMagnitude(fft,50),getMagnitude(fft,150),getMagnitude(fft,250)};
            DataType data = buildData(in,stateType,file,tmp);
            if(data instanceof BasicMLData){
                fillDataSet((BasicMLData) data,stateType);
            }else if(data instanceof BasicMLDataPair){
                fillDataSet((BasicMLDataPair) data,stateType);
            }
        }

    }

    private void fillDataSet(BasicMLData data, TimeClasifier stateType){
        if(stateType !=null) {
            if (stateType.isTest()) {
                testData.add(data);
            } else {
                trainData.add(data);
            }
        }
        allData.add(data);
    }

    private void fillDataSet(BasicMLDataPair data, TimeClasifier stateType){
        if(stateType !=null) {
            if (stateType.isTest()) {
                testData.add(data);
            } else {
                trainData.add(data);
            }
        }
        allData.add(data);
    }


    protected abstract DataType buildData(double[] in, TimeClasifier type, String file, Date time);

    public double getMagnitude(double[] fft, int i){
        double re = fft[2*i];
        double im = fft[2*i+1];
        return  Math.sqrt(re*re+im*im);
    }

    private TimeClasifier getState(Date tmp){
        int h = tmp.getHours();
        int m= tmp.getMinutes();

        for(TimeClasifier tc: TimeClasifier.values()){
            if(h==tc.getHour()){
                if(m>=tc.getMinute()+TIME_MARGIN && m<=tc.getMinute()+TIME_MARGIN+TIME_SIZE){
                    return tc;
                }
            }
        }
        return null;
    }

    public MLDataSet getTrainData() {
        return trainData;
    }

    public MLDataSet getTestData() {
        return testData;
    }

    public MLDataSet getAllData() {
        return allData;
    }

    public void printMaxTrainData(){
        find(trainData,0);
        find(trainData,1);
        find(trainData,2);
    }

    private void find(MLDataSet data, int i) {
        double interes=0;
        for(MLDataPair d:data){
            double tmp = d.getInput().getData(i);
            if(tmp>interes)
                interes=tmp;
        }
        System.out.println("El maximo de "+i+" es: "+interes);
    }
}
