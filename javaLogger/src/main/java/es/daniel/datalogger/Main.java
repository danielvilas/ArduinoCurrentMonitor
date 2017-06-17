package es.daniel.datalogger;

import com.fazecast.jSerialComm.SerialPort;
import es.daniel.datalogger.threads.FileSaver;
import es.daniel.datalogger.threads.SerialReader;
import es.daniel.datalogger.threads.TimeWindowzer;
import es.daniel.datalogger.threads.WindowAnalisys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by daniel on 8/6/17.
 */
public class Main {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));



    public static void main(String args[]){

        SerialPort port = null;
        while(port==null)port=choosePort();

        System.out.println("Port Choosed: "+port.getSystemPortName());
        port.setBaudRate(115200);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING,100,0);
        port.openPort();

        QueueManager qMgr = new QueueManager();
        SerialReader sr;
        sr = new SerialReader(port, qMgr.getData());

        Thread t = new Thread(sr);

        sr.sync();
        System.out.println("Sync");
        t.start();

        t = new Thread(new TimeWindowzer(qMgr.getData(),qMgr.getPacket()));
        t.start();
        t= new Thread(new WindowAnalisys(qMgr.getPacket(), qMgr.getOutQueue()));
        t.start();
        t= new Thread(new FileSaver( qMgr.getOutQueue()));
        t.start();
;    }

    private static SerialPort choosePort(){
        System.out.println("Choose port");
        SerialPort[] ports = SerialPort.getCommPorts();
        for(int i=0;i<ports.length;i++){
            SerialPort s = ports[i];
            System.out.println("  "+i+": "+s.getSystemPortName());
        }
        try{
            int i = Integer.parseInt(br.readLine());
            if(1<0)System.exit(0);
            if(i<ports.length)return ports[i];
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }catch (IOException nfe){

        }
        return null;
    }
}
