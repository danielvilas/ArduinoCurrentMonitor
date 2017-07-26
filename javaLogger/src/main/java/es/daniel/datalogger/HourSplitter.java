package es.daniel.datalogger;

import es.daniel.datalogger.data.LogData;
import es.daniel.datalogger.data.LogPacket;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by daniel on 22/7/17.
 */
public class HourSplitter{
    public static void main(String[] args) throws Exception {
        FileInputStream fis =new FileInputStream("../MatlabAnalisys/data-20170716-153602.csv");
        InputStreamReader isr =new InputStreamReader(fis,"UTF-8");
        BufferedReader br = new BufferedReader(isr);
        int i=0;
        int line=0;
        String last;
        String file=null;
        FileOutputStream fos= null;
        SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd-HH");
        while ((last=br.readLine())!=null){
            line++;
            if(!last.startsWith( "#"))continue;
            LogPacket packet = readHeader(last);
            /*if(!last.equals(packet.toString())){
                System.err.println("error in line "+line+"\n\t"+last+" "+packet);
            }*/
            line = readPacket(br, line, last, packet);
            i++;
            String pD=dt.format(packet.getData(0).getDate());
            if(!pD.equals(file)){
                if(file!=null){
                    System.out.println(file+" writed "+i+" packets");
                    fos.close();
                    i=0;
                }
                file=pD;
                fos = new FileOutputStream("data-"+file+".csv");
            }
            fos.write(packet.toCsv().getBytes("UTF-8"));
            if (i % 10 == 0) {
                fos.flush();
            }
            //System.out.println(packet);
        }
        System.out.println(line+" lines> "+line/1025.0);
    }
    private static LogData firstPacket;
    private static int ov=0;
    private static LogData lastPacket;
    private static int readPacket(BufferedReader br, int line, String last, LogPacket packet) throws IOException {
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

    private static LogPacket readHeader(String last) {
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
