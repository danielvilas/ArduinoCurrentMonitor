package es.daniel.datalogger.threads;

import es.daniel.datalogger.data.DataQueue;
import es.daniel.datalogger.data.LogPacket;
import es.daniel.datalogger.data.PacketAnalysData;

/**
 * Created by daniel on 11/6/17.
 */
public class WindowAnalisys implements Runnable {
    private DataQueue<LogPacket> inData;
    private DataQueue<LogPacket> outData;
    private static final double MAX_MARGIN=0.01;

    public WindowAnalisys(DataQueue<LogPacket> inData, DataQueue<LogPacket> outData) {
        this.inData = inData;
        this.outData = outData;
    }

    @Override
    public void run() {
        LogPacket packet = null;
        LogPacket lasPacket = null;
        int i=0;
        try {
            while (true) {
                i=(1+i)%60;
                packet = inData.pull();
                fillMaxMinData(packet, packet.getA0Data(), packet.getA1Data());
                fillPeaks(packet, packet.getA0Data(), packet.getA1Data());
                if (lasPacket == null || isDifferent(lasPacket, packet)) {
                    System.out.println("(" + packet.getA0Data() + ", " + packet.getA1Data() + ") ****");
                    lasPacket = packet;
                    i=0;
                    outData.push(lasPacket);
                }else if(i==0){
                    System.out.println("(" + packet.getA0Data() + ", " + packet.getA1Data() + ") ----");
                    lasPacket=packet;
                    outData.push(lasPacket);
                }else {
                    System.out.println("(" + packet.getA0Data() + ", " + packet.getA1Data() + ")");
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void fillMaxMinData(LogPacket packet, PacketAnalysData a0Data, PacketAnalysData a1Data) {
        float a0Average=0;
        float a1Average=0;
        for(int i=0;i<LogPacket.PACKET_SIZE;i++){
            int a0=packet.getData(i).getA0();
            if(a0>a0Data.getMax())a0Data.setMax(a0);
            if(a0<a0Data.getMin())a0Data.setMin(a0);
            a0Average+=(float)a0/LogPacket.PACKET_SIZE;

            int a1=packet.getData(i).getA1();
            if(a1>a1Data.getMax())a1Data.setMax(a1);
            if(a1<a1Data.getMin())a1Data.setMin(a1);
            a1Average+=(float)a1/LogPacket.PACKET_SIZE;

        }
        a0Data.setAverage(a0Average);
        a1Data.setAverage(a1Average);
    }


    private void fillPeaks(LogPacket packet, PacketAnalysData a0Data, PacketAnalysData a1Data){
        int a0Max= (int)(a0Data.getMax()*0.9);
        int a0Min =(int)(a0Data.getMin()*1.1);

        int a1Max= (int)(a1Data.getMax()*0.9);
        int a1Min =(int)(a1Data.getMin()*1.1);

        int a0Peaks=0;
        int a1Peaks=0;

        for(int i=0;i<LogPacket.PACKET_SIZE;i++){
            int a0=packet.getData(i).getA0();
            if(a0>a0Max)a0Peaks++;
            if(a0<a0Min)a0Peaks++;

            int a1=packet.getData(i).getA1();
            if(a1>a1Max)a1Peaks++;
            if(a1<a1Min)a1Peaks++;
        }
        a0Data.setPeaks(a0Peaks);
        a1Data.setPeaks(a1Peaks);
    }

    private float diff(int a, int b){
        return (float)(Math.max(a,b)/Math.min(a, b)-1);
    }

    private float diff(float a, float b){
        return (float)(Math.max(a,b)/Math.min(a, b)-1);
    }

    private boolean isDifferent(PacketAnalysData lastData, PacketAnalysData currData){
        if(diff(lastData.getMax(),currData.getMax())>MAX_MARGIN)return true;
        if(diff(lastData.getMin(),currData.getMin())>MAX_MARGIN)return true;
        if(diff(lastData.getPeaks(),currData.getPeaks())>MAX_MARGIN)return true;
        if(diff(lastData.getAverage(),currData.getAverage())>MAX_MARGIN)return true;

        return false;
    }

    private boolean isDifferent(LogPacket lasPacket, LogPacket packet) {
        return isDifferent(lasPacket.getA0Data(),packet.getA0Data()) || isDifferent(lasPacket.getA1Data(),packet.getA1Data());
    }
}
