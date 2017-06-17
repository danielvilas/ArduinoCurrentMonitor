package es.daniel.datalogger.threads;

import com.fazecast.jSerialComm.SerialPort;
import es.daniel.datalogger.data.DataQueue;
import es.daniel.datalogger.data.LogData;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.nio.ByteBuffer;

/**
 * Created by daniel on 8/6/17.
 */
public class SerialReader implements Runnable{
    private SerialPort port;
    private DataQueue<LogData> queue;

    public SerialReader(SerialPort port,DataQueue<LogData> queue) {
        this.port = port;
        this.queue=queue;
    }

    @Override
    public void run() {
        byte buff[] =new byte[8];
        int cnt=0;
        while(true){
            while(port.bytesAvailable()<8);

            port.readBytes(buff, buff.length);
            LogData data = new LogData(buff);
            //System.out.println(data);
            queue.push(data);
            cnt=(cnt+1)%1024;
            if(cnt==0){
                sync();
                System.out.println("Sync");
            }

        }
    }

    public void sync() {
        boolean sync=false;
        while(!sync){
            byte buff[] = new byte[8];
            int r = port.readBytes(buff,8);
            //System.out.println(Base64.getEncoder().encodeToString(buff));
            int ff=0;
            inner: for(int i=7;i>=0;i--){
                if(buff[i]!=(byte)0xFF)break inner;
                ff++;
            }
            if(ff==8){
                sync=true;
            }else if(ff>0){
                port.readBytes(buff,8-ff);
                inner: for(int i=7-ff;i>=0;i--){
                    if(buff[i]!=0xFF)break inner;
                    ff++;
                }
                if(ff==8){
                    sync=true;
                }
            }
        }
        queue.push(LogData.syncData());
    }
}
