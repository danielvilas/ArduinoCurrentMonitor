package es.daniel.outputgui.data;

import com.googlecode.fannj.Fann;
import es.daniel.datalogger.data.Appliance;
import es.daniel.datalogger.data.DataReader;
import es.daniel.datalogger.data.LogData;
import es.daniel.datalogger.data.LogPacket;
import org.jtransforms.fft.DoubleFFT_1D;

import java.util.*;

public class DataManager {
    public static final double THRESOLD = 0.75;
    List<ParsedPacket> allPackets;
    Map<Date, ExtendedBucket> allBuckets;
    Fann network;
    DataManagerListener listener;

    public static final int BUCKET_SIZE_MINUTES=5;

    public DataManager() {
        allPackets = new ArrayList<ParsedPacket>();
        allBuckets = new HashMap<Date, ExtendedBucket>();
        network = new Fann("net_16000.net");
    }

    public void readData(String file) throws Exception{
        DataReader dt = new DataReader(file);
        List<LogPacket> list= dt.readFile();
        for(LogPacket lp : list) {
            Date tmp = lp.getData(0).getDate();

            double[] in0=createData(lp, 0);//[0,300)
            double[] in3=createData(lp, 300);//[300,600)
            double[] in6=createData(lp, 600);//[600,900)
            double[] in9=createData(lp, 1023-300);//[724,1024)

            ParsedPacket p=createData(tmp,in0,in3,in6,in9);

            addPacket(p);
        }
        System.out.println("Readed: "+file);
    }

    public void addPacket(ParsedPacket p) {
        Date tmp = p.getDate();
        allPackets.add(p);
        ExtendedBucket b =getOrCreateBucket(tmp);

        //Each Packet represent 1 second,
        b.appendTvSeconds(getSeconds(p.getTv()));
        b.appendBluraySeconds(getSeconds(p.getBluray()));
        b.appendAppleTvSeconds(getSeconds(p.getAppleTv()));
        b.appendIpTvSeconds(getSeconds(p.getIpTv()));

        if(listener!=null)listener.addOrUpdateBucket(b);
    }

    private ExtendedBucket getOrCreateBucket(Date tmp) {
        long milliseconds = tmp.getTime();
        long bucketNum = milliseconds/ (1000 * 60 * BUCKET_SIZE_MINUTES );
        Date bucketDate = new Date(bucketNum*BUCKET_SIZE_MINUTES*60*1000);
        if(allBuckets.get(bucketDate)!=null){
            return allBuckets.get(bucketDate);
        }
        Date bucketDateEnd = new Date((1+bucketNum)*BUCKET_SIZE_MINUTES*60*1000);
        ExtendedBucket b = new ExtendedBucket(bucketDate,bucketDateEnd);
        allBuckets.put(bucketDate,b);
        return b;
    }

    private float getSeconds(double tv) {
        //A packet represents a second, this method
        //return (float)tv;
        if(tv>= THRESOLD)return 1.0f;
        return 0.0f;
    }


    private ParsedPacket createData(Date tmp, double[] in0, double[]in3, double[]in6,double[]in9){
        ParsedPacket ret = new ParsedPacket();
        double[] out = new double[in0.length];
        for(int i=0;i<out.length;i++){
            out[i]=(in0[i]+in3[i]+in6[i]+in9[i])/4.0;
        }

        ret.setDate(tmp);
        ret.setTv(out[Appliance.TV.getPosition()]);
        ret.setBluray(out[Appliance.BluRay.getPosition()]);
        ret.setAppleTv(out[Appliance.AppleTV.getPosition()]);
        ret.setIpTv(out[Appliance.IpTV.getPosition()]);

        return ret;
    }

    private double[] createData(LogPacket lp, int offset) {
        double[] in = getFftData(lp,offset);
        //network is trained to return
        //TV, BluRay, AppleTV, IpTV
        double out[] = execute(in);
        return out;


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


    public double[] execute(double[] data) {
        float[] in = new float[5];
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

    public List<ExtendedBucket> getAllBuckets(){
        ArrayList<ExtendedBucket> ret =new ArrayList<ExtendedBucket>();
        ret.addAll(allBuckets.values());

        Collections.sort(ret);

        return ret;
    }

    public DataManagerListener getListener() {
        return listener;
    }

    public void setListener(DataManagerListener listener) {
        this.listener = listener;
    }
}
