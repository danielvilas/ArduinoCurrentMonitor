package es.daniel.outputgui.data;

import java.util.*;

public class BucketManager implements DataManagerListener {
    Map<Date, ExtendedBucket> allBuckets;
    List<ParsedPacket> allPackets;
    BucketManagerListener listener;

    public static final int BUCKET_SIZE_MINUTES=5;
    public static final double THRESOLD = 0.75;

    public BucketManager (){
        allBuckets = new HashMap<Date, ExtendedBucket>();
        allPackets = new ArrayList<ParsedPacket>();
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

    public List<ExtendedBucket> getAllBuckets(){
        ArrayList<ExtendedBucket> ret =new ArrayList<ExtendedBucket>();
        ret.addAll(allBuckets.values());

        Collections.sort(ret);

        return ret;
    }


    public BucketManagerListener getListener() {
        return listener;
    }

    public void setListener(BucketManagerListener listener) {
        this.listener = listener;
    }

}
