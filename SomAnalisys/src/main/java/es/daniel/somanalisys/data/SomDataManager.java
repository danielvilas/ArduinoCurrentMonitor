package es.daniel.somanalisys.data;

import org.encog.ml.data.basic.BasicMLData;

import java.util.Date;

public class SomDataManager extends DataManager<BasicMLData> {


    protected BasicMLData buildData(double[] in, TimeClasifier type, String file, Date time) {
        BasicMLData data = new CustomMLData(6, file, time);
        data.setData(0, in[0]);//getMagnitude(fft,50)
        data.setData(1, in[1]);//getMagnitude(fft,150)
        data.setData(2, in[2]);//getMagnitude(fft,250)

        if(type !=null) {
            double[] state = type.getData();
            for (int i = 0; i < 3; i++) {
                data.setData(3 + i, state[i]);
            }
        }
        return data;
    }
}


