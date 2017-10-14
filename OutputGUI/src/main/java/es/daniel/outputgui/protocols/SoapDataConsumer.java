package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.ws.BindingProvider;
import java.util.List;

public class SoapDataConsumer extends WebServiceGatewaySupport implements Runnable{
    private DataManagerListener out;
    private boolean running=true;

    public SoapDataConsumer(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("es.daniel.outputgui.data");
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
    }

    public void run(){


        try {
            while (running) {
                 GetBucketsResponse res =  (GetBucketsResponse) getWebServiceTemplate().marshalSendAndReceive("http://localhost:8080/ws",
                        new GetBucketsRequest(), new SoapActionCallback(""));

                List<Bucket> list = res.getBuckets().getBucket();

                if (list != null && list.size() > 0) {
                    for (Bucket b : list) {

                        out.addOrUpdateBucket(new ExtendedBucket(b));
                    }
                } else {
                    Thread.sleep(1000);//1sec
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public DataManagerListener getOut() {
        return out;
    }

    public void setOut(DataManagerListener out) {
        this.out = out;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
