package es.daniel.fann.data;

import es.daniel.datalogger.data.DataReader;
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

            /*if(stateType!=null && !stateType.isTest()){
                for(int i=0;i<1023-300;i++){
                    createData(file, lp, tmp, stateType,i);//all possibles
                }
            }else*/{
                //Test or run
                //TODO adjust
                createData(file, lp, tmp, stateType,0);//[0,300)
                createData(file, lp, tmp, stateType,300);//[300,600)
                createData(file, lp, tmp, stateType,600);//[600,900)
                createData(file, lp, tmp, stateType,1023-300);//[724,1024)
            }

        }

    }

    private void createData(String file, LogPacket lp, Date tmp, TimeClasifier stateType,int offset) {
        double[] in = getFftData(lp,offset);
        DataType data = buildData(in,stateType,file,tmp);
        fillData(stateType, data);
    }

    private double[] getFftData(LogPacket lp, int offset) {
        double dData[] = new double[300];
        double average =0.0;
        for (int j = 0; j < 300; j++) {
            LogData lg = lp.getData(j+offset);
            double tmp=(lg.getA0() - 512.0) /512.0;
            dData[j]=tmp;
            tmp=Math.abs(tmp);
            average+=tmp;
        }
        average/=dData.length;

        double[] fft = new double[dData.length * 2];
        System.arraycopy(dData, 0, fft, 0, dData.length);
        DoubleFFT_1D fftDo = new DoubleFFT_1D(dData.length);
        fftDo.realForwardFull(fft);
        //BasicMLData data = new BasicMLData(3);
        return new double[]{getMagnitude(fft,50),getMagnitude(fft,150),getMagnitude(fft,250),getMagnitude(fft,350),average};
    }

    private void fillData(TimeClasifier stateType, DataType data) {
        if(data instanceof BasicMLData){
            fillDataSet((BasicMLData) data,stateType);
        }else if(data instanceof BasicMLDataPair){
            fillDataSet((BasicMLDataPair) data,stateType);
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
        /**
         * h = P*fs/N
         * P = h/ (FS/N) = h*N/FS
         */
        int pos = i*(fft.length/2)/1000;
        double re = fft[2*pos];
        double im = fft[2*pos+1];
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
