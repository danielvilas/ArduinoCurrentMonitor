package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.ExtendedBucket;
import es.daniel.outputgui.data.DataManagerListener;
import org.springframework.web.client.RestTemplate;

public class RestDataProducer implements DataManagerListener {

    public void addOrUpdateBucket(ExtendedBucket bucket) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation("http://server.local:9090/api/addBucket",bucket);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
