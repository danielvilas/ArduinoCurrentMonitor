package es.daniel.datalogger.data;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * Created by daniel on 11/6/17.
 */
public class DataQueue <DATA>{

    private Queue<DATA> dataList;

    public DataQueue(){
        dataList = new LinkedTransferQueue<DATA>();
    }

    public void push(DATA in){
        synchronized (dataList) {
            dataList.add(in);
            dataList.notify();
        }
    }

    public DATA pull() throws InterruptedException{
        DATA ret;
        synchronized (dataList) {
            while (dataList.isEmpty()) {
                dataList.wait();
            }
            ret = dataList.poll();
            dataList.notify();
        }
        return ret;
    }
}
