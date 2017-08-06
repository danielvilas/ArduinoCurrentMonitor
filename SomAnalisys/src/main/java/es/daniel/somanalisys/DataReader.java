package es.daniel.somanalisys;

import es.daniel.datalogger.data.LogData;
import es.daniel.datalogger.data.LogPacket;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by daniel on 6/8/17.
 */
public class DataReader {
    private File dataIn;

    public DataReader (String file){
        this.dataIn = new File(file);
    }

    public List<LogPacket> readFile() throws Exception{
        ArrayList<LogPacket> ret = new ArrayList<LogPacket>();

        FileInputStream fis =new FileInputStream(dataIn);
        InputStreamReader isr =new InputStreamReader(fis,"UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String last;
        int line=0;
        while ((last=br.readLine())!=null){
            line++;
            if(!last.startsWith( "#"))continue;
            LogPacket packet = readHeader(last);
            /*if(!last.equals(packet.toString())){
                System.err.println("error in line "+line+"\n\t"+last+" "+packet);
            }*/
            line = readPacket(br, line, last, packet);
            ret.add(packet);
            //System.out.println(packet);
        }
        return ret;
    }

    private static LogData firstPacket;
    private static int ov=0;
    private static LogData lastPacket;

    private int readPacket(BufferedReader br, int line, String last, LogPacket packet) throws IOException {
        for(int j=0;j< LogPacket.PACKET_SIZE;j++){
            String tmp=br.readLine();
            line++;
            LogData lg= new LogData(tmp);

            if(firstPacket==null){
                firstPacket= lg;
                lastPacket=lg;
            }else if (lg.getMicros()>0){

                if(ov!=0)lg.overFlowMicros(ov);
                lg.getDate().setTime(firstPacket.getDate().getTime()+(lg.getMicros()-firstPacket.getMicros())/1000);
                if(lastPacket.getDate().after(lg.getDate())){
                    System.out.println("OverFlow Detected");
                    ov++;
                    lg.overFlowMicros(1);
                    lg.getDate().setTime(firstPacket.getDate().getTime()+(lg.getMicros()-firstPacket.getMicros())/1000);
                }
                lastPacket=lg;
            }

            packet.pushData(lg);

        }
        return line;
    }

    private  LogPacket readHeader(String last) {
        LogPacket lp = new LogPacket();
        String clean =last.substring(2,last.lastIndexOf(')'));
        StringTokenizer st = new StringTokenizer(clean,",");

        lp.getA0Data().setMax(Integer.parseInt(st.nextToken().trim()));
        lp.getA0Data().setMin(Integer.parseInt(st.nextToken().trim()));
        lp.getA0Data().setAverage(Float.parseFloat(st.nextToken().trim()));
        lp.getA0Data().setPeaks(Integer.parseInt(st.nextToken().trim()));

        lp.getA1Data().setMax(Integer.parseInt(st.nextToken().trim()));
        lp.getA1Data().setMin(Integer.parseInt(st.nextToken().trim()));
        lp.getA1Data().setAverage(Float.parseFloat(st.nextToken().trim()));
        lp.getA1Data().setPeaks(Integer.parseInt(st.nextToken().trim()));

        return lp;
    }

}
