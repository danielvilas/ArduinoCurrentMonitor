package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.ExtendedBucket;
import es.daniel.outputgui.data.BucketManagerListener;
import es.daniel.outputgui.data.Packet;
import es.daniel.outputgui.data.ParsedPacket;
import org.springframework.web.client.RestTemplate;

public class RestDataConsumer extends AbstractConsumer implements Runnable {
    public static final String URL = "http://server.local:9090/api/getPackets";
    //public static final String URL = "http://localhost:8080/api/getPackets";

    private boolean running=true;


    public void run(){
        try {
            while (running) {
                RestTemplate restTemplate = new RestTemplate();
                Packet[] list = restTemplate.getForObject(URL, Packet[].class);
                if (list != null && list.length > 0) {
                    for (Packet b : list) {
                        bm.addPacket(fromPacket(b));
                    }
                } else {
                    Thread.sleep(1000);//1sec
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ParsedPacket fromPacket(Packet packet){
        ParsedPacket p = new ParsedPacket();

        p.setAppleTv(packet.getAppleTvSeconds());
        p.setBluray(packet.getBluraySeconds());
        p.setDate(packet.getDate().toGregorianCalendar().getTime());
        p.setIpTv(packet.getIpTvSeconds());
        p.setTv(packet.getTvSeconds());

        return p;
    }


    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
