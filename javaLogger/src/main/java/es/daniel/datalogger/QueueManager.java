package es.daniel.datalogger;

import es.daniel.datalogger.data.DataQueue;
import es.daniel.datalogger.data.LogData;
import es.daniel.datalogger.data.LogPacket;

/**
 * Created by daniel on 11/6/17.
 */
public class QueueManager {
    private DataQueue<LogData> dataQueue = new DataQueue<LogData>();
    private DataQueue<LogPacket> packetQueue = new DataQueue<LogPacket>();
    private DataQueue<LogPacket> outQueue = new DataQueue<LogPacket>();

    public DataQueue<LogData> getData(){
        return dataQueue;
    }

    public DataQueue<LogPacket> getPacket(){
        return packetQueue;
    }

    public DataQueue<LogPacket> getOutQueue() {
        return outQueue;
    }
}
