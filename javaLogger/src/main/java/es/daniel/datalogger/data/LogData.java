package es.daniel.datalogger.data;

import sun.rmi.runtime.Log;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by daniel on 11/6/17.
 */
public class LogData {
    private byte[] rawData;
    private long micros;
    private int A0;
    private int A1;

    private DataType type;

    public Date getDate() {
        return date;
    }

    private Date date=null;
    private long deltaMicros=-1;


    public LogData(byte[] rawData){
        long t0= ByteBuffer.wrap(rawData,0,4).getInt();
        int a0 = ByteBuffer.wrap(rawData,4,2).getShort();
        int a1 = ByteBuffer.wrap(rawData,6,2).getShort();

        this.rawData=rawData;

        if(t0 == -1L && a0==-1 && a1==-1){
            this.micros=this.A0 =this.A1 = -1;
            type=DataType.Sync;
        }else{
            this.micros=0x00FFFFFFFFL &t0;
            this.A0 =0x03FF & a0;
            this.A1 = 0x03FF & a1;
            type=DataType.Data;
        }

    }

    public LogData(String data){
        StringTokenizer st = new StringTokenizer(data,",");
        String stMicros = st.nextToken().trim();
        String stA0 =st.nextToken().trim();
        String stA1=st.nextToken().trim();
        String stDate=st.nextToken().trim();
        String stDelta=st.nextToken().trim();

        if(stMicros.startsWith("-")){
            long tmp = Long.parseLong(stMicros,16);
            long tmp2 = 0x00FFFFFFFFL & tmp;
            this.micros=(tmp==-1)?tmp:tmp2;
        }else{
            this.micros=Long.parseLong(stMicros,16);
        }

        this.A0=Integer.parseInt(stA0);
        this.A1=Integer.parseInt(stA1);
        this.date=new Date();
        this.date.setTime(Long.parseLong(stDate));
        this.deltaMicros=Long.parseLong(stDelta);
        type=DataType.Data;


    }
    private LogData(DataType type){
        this.type=type;
    }
    public void overFlowMicros(int i){

       // this.micros+=i*0x00FFFFFFFFL;
        this.micros+=i*0x0100000000L;

    }
    @Override
    public String toString() {
        if(this.type==DataType.Data) {
            return "(" + Long.toString(micros,16) + ", " + A0 + ", " + A1+ ", " +date+ ", "+deltaMicros + ")";
        }else //if(type==DataType.Sync)
            {
            return "Sync";
        }
    }

    public String toCsvString(){
        if(this.type==DataType.Data) {
            if(date==null){
                return Long.toString(micros,16) + ", " + A0 + ", " + A1+ ", 0, 0";
            }
            return Long.toString(micros,16) + ", " + A0 + ", " + A1+ ", " +date.getTime()+ ", "+deltaMicros;
        }
        return "";
    }

    public byte[] getRawData() {
        return rawData;
    }

    public long getMicros() {
        return micros;
    }

    public int getA0() {
        return A0;
    }

    public int getA1() {
        return A1;
    }

    public DataType getType() {
        return type;
    }


    public static LogData syncData(){
        return new LogData(DataType.Sync);
    }

    public void fillDate(Date currDate) {
        if(date==null)
            date=currDate;
    }


    public void fillDeltaMicros(long delta) {
        if(deltaMicros==-1)
            deltaMicros=delta;
    }

}
