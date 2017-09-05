package es.daniel.somanalisys.data;

import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;

import java.util.Date;



public class PairDataManager extends DataManager<BasicMLDataPair> {
    protected BasicMLDataPair buildData(double[] in, TimeClasifier type, String file, Date time) {
        //return null;

        BasicMLData inData = new BasicMLData(in);
        BasicMLData outData=null;
        if(type!=null){
            outData=new BasicMLData(type.getData());
        }else{
            outData=new BasicMLData(new double[]{0.0,0.0,0.0});
        }
        return new CustomMLDataPair(inData,outData,file,time);
    }
}
