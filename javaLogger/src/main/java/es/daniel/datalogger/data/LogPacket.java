package es.daniel.datalogger.data;

/**
 * Created by daniel on 11/6/17.
 */
public class LogPacket{
    public static final int PACKET_SIZE=1024;
    private LogData[] data=new LogData[PACKET_SIZE];
    private PacketAnalysData a0Data = new PacketAnalysData();
    private PacketAnalysData a1Data = new PacketAnalysData();

    public PacketAnalysData getA0Data() {
        return a0Data;
    }

    public PacketAnalysData getA1Data() {
        return a1Data;
    }

    public LogData getData(int i){
        return data[i];
    }

    private int i= 0;

    public int pushData(LogData in){
        int ret = i++;
        data[ret]=in;
        return ret;
    }

    public String toCsv() {
        StringBuffer sb = new StringBuffer();
        sb.append("#(" + getA0Data() + ", " + getA1Data() + ")").append('\n');
        for(int i=0;i<PACKET_SIZE;i++){
            sb.append(data[i].toCsvString()).append('\n');
        }
        return sb.toString();
    }
}
