package es.daniel.datalogger.threads;

import es.daniel.datalogger.data.DataQueue;
import es.daniel.datalogger.data.DataType;
import es.daniel.datalogger.data.LogData;
import es.daniel.datalogger.data.LogPacket;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * Created by daniel on 11/6/17.
 */
public class TimeWindowzer implements Runnable {
    private DataQueue<LogData> inData;
    private DataQueue<LogPacket> outData;

    public TimeWindowzer(DataQueue<LogData> inData, DataQueue<LogPacket> outData) {
        this.inData = inData;
        this.outData = outData;
    }

    public void run() {
        Date baseDate = null;
        long baseMicros = -1;
        long previousMicros = 0;
        LogPacket packet = null;
        int overflow=0;
        try {
            while (true) {
                LogData data = inData.pull();
                if (data.getType() == DataType.Sync) {
                    if (packet != null) {
                        outData.push(packet);
                    }
                    packet = new LogPacket();
                } else {//data.getType()== DataType.DATA){
                    if (baseDate == null) {
                        baseDate = new Date();
                        baseMicros = data.getMicros();
                    }
                    data.overFlowMicros(overflow);
                    if(data.getMicros()<previousMicros){
                        System.out.println("\tOverflow detected");
                        data.overFlowMicros(1);
                        overflow++;
                    }
                    Date currDate = new Date();
                    currDate.setTime(baseDate.getTime() + (data.getMicros() - baseMicros) / 1000);
                    data.fillDate(currDate);
                    data.fillDeltaMicros(data.getMicros() - previousMicros);
                    previousMicros = data.getMicros();

                    packet.pushData(data);
                    //System.out.println(data);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
