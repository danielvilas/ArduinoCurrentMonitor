package es.daniel.datalogger.data;

import sun.rmi.runtime.Log;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Created by daniel on 11/6/17.
 */
public class LogData {
    private byte[] rawData;
    private long micros;
    private int A0;
    private int A1;

    private DataType type;
    private Date date=null;
    private long deltaMicros=-1;


    public LogData(byte[] rawData){
        long t0=0xFFFFFFFF & ByteBuffer.wrap(rawData,0,4).getInt();
        int a0 = 0x03FF & ByteBuffer.wrap(rawData,4,2).getShort();
        int a1 = 0x03FF & ByteBuffer.wrap(rawData,6,2).getShort();

        this.rawData=rawData;

        this.micros=t0;
        this.A0 =0x03FF & a0;
        this.A1 = 0x03FF & a1;
        type=DataType.Data;
    }

    private LogData(DataType type){
        this.type=type;
    }

    @Override
    public String toString() {
        if(this.type==DataType.Data) {
            return "(" + bytesToHex(rawData,0,4) + ", " + A0 + ", " + A1+ ", " +date+ ", "+deltaMicros + ")";
        }else //if(type==DataType.Sync)
            {
            return "Sync";
        }
    }

    public String toCsvString(){
        if(this.type==DataType.Data) {
            if(date==null){
                return bytesToHex(rawData,0,4) + ", " + A0 + ", " + A1+ ", 0, 0";
            }
            return bytesToHex(rawData,0,4) + ", " + A0 + ", " + A1+ ", " +date.getTime()+ ", "+deltaMicros;
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

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes, int offset, int size) {
        char[] hexChars = new char[size * 2];
        for ( int j = 0; j < size; j++ ) {
            int v = bytes[offset+j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
