package es.daniel.somanalisys.data;

public enum TimeClasifier {

    // 15:36: TV + BluRay + AppleTV (Yellow)
    TRAIN_TV_BDR_APPLE(15,36,DataManager.YELLOW),
    TEST_TV_BDR_APPLE(15,36,DataManager.YELLOW, true),
    TRAIN_TV_BDR_APPLE_2(16,00,DataManager.YELLOW), //Added
    TEST_TV_BDR_APPLE_2(16,00,DataManager.YELLOW, true),
    //17:00 TV + BluRay (red)
    TRAIN_TV_BDR(17,0,DataManager.RED),
    TEST_TV_BDR(17,0,DataManager.RED, true),
    //17:21 TV + IpTV (green)
    TRAIN_TV_IpTV(17,21,DataManager.GREEN),
    TEST_TV_IpTV(17,21,DataManager.GREEN, true),
    //17:40 OFF (blue)
    TRAIN_OFF(17,40,DataManager.BLUE),
    TEST_OFF(17,40,DataManager.BLUE, true),
    TRAIN_OFF_2(17,45,DataManager.BLUE),
    TEST_OFF_2(17,45,DataManager.BLUE, true),
    //18:40 TV + IpTV (green)
    TRAIN_TV_IpTV_2(18,40,DataManager.GREEN),
    TEST_TV_IpTV_2(18,40,DataManager.GREEN, true),
    //19:00 OFF (blue)
    TRAIN_OFF_3(19,0,DataManager.BLUE),
    TEST_OFF_3(19,0,DataManager.BLUE, true),
    TRAIN_OFF_4(19,5,DataManager.BLUE),
    TEST_OFF_4(19,5,DataManager.BLUE, true),
    // 20:00  TV + BluRay (red) added
    TRAIN_TV_BDR_2(20,2,DataManager.RED),
    TEST_TV_BDR_2(20,2,DataManager.RED, true),
    ;

    private int hour;
    private int minute;
    private double[] data;
    private boolean isTest;

    private TimeClasifier(int hour, int m, double[] data){
        this(hour,m,data,false);
    }

    private TimeClasifier(int hour, int m, double[] data, boolean isTest){
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
        return data;
    }

    public boolean isTest() {
        return isTest;
    }
}
