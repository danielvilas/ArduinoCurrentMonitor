package es.daniel.datalogger.threads;

import es.daniel.datalogger.data.DataQueue;
import es.daniel.datalogger.data.LogData;
import es.daniel.datalogger.data.LogPacket;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by daniel on 12/6/17.
 */
public class FileSaver implements Runnable {
    private DataQueue<LogPacket> inData;

    public FileSaver(DataQueue<LogPacket> inData) {
        this.inData = inData;
    }

    public void run() {
        LogPacket packet;
        int i = 0;
        FileOutputStream fos = null;
        try {
            String name="data-";
            SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd-HHmmss");
            name+=dt.format(new Date());
            fos = new FileOutputStream(name+".csv");
            while (true) {
                packet = inData.pull();
                i = (i + 1) % 10;
                fos.write(packet.toCsv().getBytes("UTF-8"));
                if (i == 0) {
                    fos.flush();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
