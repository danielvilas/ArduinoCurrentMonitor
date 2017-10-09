package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.Bucket;
import es.daniel.outputgui.data.DataManagerListener;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RestDataConsumer implements Runnable {
    private DataManagerListener out;
    private boolean running=true;


    public void run(){
        try {
            while (running) {
                RestTemplate restTemplate = new RestTemplate();
                Bucket[] list = restTemplate.getForObject("http://localhost:8080/api/getBuckets", Bucket[].class);
                if (list != null && list.length > 0) {
                    for (Bucket b : list) {
                        out.addOrUpdateBucket(b);
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
