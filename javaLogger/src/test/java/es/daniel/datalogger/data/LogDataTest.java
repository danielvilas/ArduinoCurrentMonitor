package es.daniel.datalogger.data;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by daniel on 26/7/17.
 */
public class LogDataTest {

    @Test
    public void testSyncFrame(){
        byte[] data = new byte[8];
        data[0]= (byte) 0xff;
        data[1]= (byte) 0xff;
        data[2]= (byte) 0xff;
        data[3]= (byte) 0xff;

        data[4]= (byte) 0xff;
        data[5]= (byte) 0xff;

        data[6]= (byte) 0xff;
        data[7]= (byte) 0xff;

        LogData lg = new LogData(data);
        assertEquals(DataType.Sync,lg.getType());
        assertEquals(-1,lg.getMicros());
        assertEquals(-1,lg.getA0());
        assertEquals(-1,lg.getA1());

    }

    @Test
    public void testDataFrame(){
        byte[] data = new byte[8];
        data[0]= (byte) 0x00;
        data[1]= (byte) 0x00;
        data[2]= (byte) 0x00;
        data[3]= (byte) 0x10;

        data[4]= (byte) 0x00;
        data[5]= (byte) 0xFF;

        data[6]= (byte) 0x00;
        data[7]= (byte) 0xff;

        LogData lg = new LogData(data);
        assertEquals(DataType.Data,lg.getType());
        assertEquals(16,lg.getMicros());
        assertEquals(255,lg.getA0());
        assertEquals(255,lg.getA1());

    }

    @Test
    public void testDataFrameNegative(){
        byte[] data = new byte[8];
        data[0]= (byte) 0x80;
        data[1]= (byte) 0x70;
        data[2]= (byte) 0x60;
        data[3]= (byte) 0x10;

        data[4]= (byte) 0x00;
        data[5]= (byte) 0xFF;

        data[6]= (byte) 0x00;
        data[7]= (byte) 0xff;

        LogData lg = new LogData(data);
        assertEquals(DataType.Data,lg.getType());
        assertEquals(0x0080706010L,lg.getMicros());
        assertEquals(255,lg.getA0());
        assertEquals(255,lg.getA1());
        assertTrue(lg.getMicros()>=0);

    }
}
