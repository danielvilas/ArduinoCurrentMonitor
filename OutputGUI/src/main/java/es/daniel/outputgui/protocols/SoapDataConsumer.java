package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.util.List;

public class SoapDataConsumer extends WebServiceGatewaySupport implements Runnable{
    private BucketManagerListener out;
    private boolean running=true;

    private static final String URI = "http://server.local:9090/ws";
    //private static final String URI = "http://localhost:8080/ws";

    public SoapDataConsumer(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("es.daniel.outputgui.data");
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
    }

    public void run(){


        try {
            while (running) {
                 GetPacketsResponse res =  (GetPacketsResponse) getWebServiceTemplate().marshalSendAndReceive(URI,
                        new GetPacketsRequest(), new SoapActionCallback(""));

                List<Packet> list = res.getPackets().getPacket();

                if (list != null && list.size() > 0) {
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

    protected  BucketManager bm = new BucketManager();

    public BucketManagerListener getOut() {
        return bm.getListener();
    }

    public void setOut(BucketManagerListener out) {
        bm.setListener(out);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
