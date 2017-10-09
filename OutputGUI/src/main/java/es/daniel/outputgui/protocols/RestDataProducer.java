package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.Bucket;
import es.daniel.outputgui.data.DataManagerListener;
import org.springframework.web.client.RestTemplate;

public class RestDataProducer implements DataManagerListener {

    public void addOrUpdateBucket(Bucket bucket) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation("http://localhost:8080/api/addBucket",bucket);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
