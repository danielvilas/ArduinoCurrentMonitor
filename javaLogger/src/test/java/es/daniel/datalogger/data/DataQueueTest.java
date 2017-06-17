package es.daniel.datalogger.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by daniel on 11/6/17.
 */
public class DataQueueTest {
    DataQueue<Integer> queue = new DataQueue<Integer>();

    @Test
    public void testOnNonThread() throws  InterruptedException{
        for(int i=0;i<10;i++){
            queue.push(i);
        }
        for(int i=0;i<10;i++){
            Integer I = queue.pull();
            assertEquals(i,I.intValue());
        }
    }

    @Test
    public void testOnProductorThread() throws  InterruptedException{
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    queue.push(i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(r).start();
        for(int i=0;i<10;i++){
            Integer I = queue.pull();
            assertEquals(i,I.intValue());
        }
    }

}