package es.daniel.fann.data;

import es.daniel.datalogger.data.Appliance;

import java.util.Arrays;

import static es.daniel.datalogger.data.Appliance.*;

public enum TimeClasifier {

    // 15:36: TV + BluRay + AppleTV (Yellow)
    TRAIN_TV_BDR_APPLE(15,36,TV,BluRay,AppleTV),
    TEST_TV_BDR_APPLE(15,36,true,TV,BluRay,AppleTV),
    TRAIN_TV_BDR_APPLE_2(16,00,TV,BluRay,AppleTV), //Added
    TEST_TV_BDR_APPLE_2(16,00,true,TV,BluRay,AppleTV),
    //17:00 TV + BluRay (red)
    TRAIN_TV_BDR(17,0,TV,BluRay),
    TEST_TV_BDR(17,0,true,TV,BluRay),
    //17:21 TV + IpTV (green)
    TRAIN_TV_IpTV(17,21,TV,IpTV),
    TEST_TV_IpTV(17,21,true,TV,IpTV),
    //17:40 OFF (blue)
    TRAIN_OFF(17,40),
    TEST_OFF(17,40,true),
    TRAIN_OFF_2(17,45),
    TEST_OFF_2(17,45,true),
    //18:40 TV + IpTV (green)
    TRAIN_TV_IpTV_2(18,40,TV,IpTV),
    TEST_TV_IpTV_2(18,40,true,TV,IpTV),
    //19:00 OFF (blue)
    TRAIN_OFF_3(19,0),
    TEST_OFF_3(19,0, true),
    TRAIN_OFF_4(19,5),
    TEST_OFF_4(19,5, true),
    // 20:00  TV + BluRay (red) added
    TRAIN_TV_BDR_2(20,2,TV,BluRay),
    TEST_TV_BDR_2(20,2,true,TV,BluRay),
    ;

    private int hour;
    private int minute;
    private Appliance[] data;
    private boolean isTest;

    private TimeClasifier(int hour, int m, Appliance... data ){
        this(hour,m,false, data);
    }

    private TimeClasifier(int hour, int m, boolean isTest, Appliance... data){
        this.hour=hour;
        this.minute=m;
        this.data=data;
        this.isTest=isTest;
        if(isTest)
            this.minute += DataManager.TIME_SIZE;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public double[] getData() {
        double[] ret = new double[Appliance.values().length];
        Arrays.fill(ret, 0.0);

        if(data!=null && data.length>0 ) {
            for(Appliance app: data){
               ret[app.getPosition()]=1.0;
            }
        }
        return ret;
    }

    public boolean isTest() {
        return isTest;
    }
}
