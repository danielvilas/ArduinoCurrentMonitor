package es.daniel.fann.data;

import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;

import java.util.Arrays;
import java.util.Date;

public class FannDataManager extends DataManager<BasicMLDataPair> {
    protected BasicMLDataPair buildData(double[] in, TimeClasifier type, String file, Date time) {
        BasicMLData inData = new BasicMLData(in);
        BasicMLData outData=null;
        if(type!=null){
            outData=new BasicMLData(type.getData());
        }else{
            double[] ret = new double[Appliance.values().length];
            Arrays.fill(ret, 0.0);
            outData=new BasicMLData(ret);
        }
        return new CustomMLDataPair(inData,outData,file,time);
    }
}
