package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.ExtendedBucket;
import es.daniel.outputgui.data.DataManagerListener;
import org.springframework.web.client.RestTemplate;

public class RestDataConsumer implements Runnable {
    private DataManagerListener out;
    private boolean running=true;


    public void run(){
        try {
            while (running) {
                RestTemplate restTemplate = new RestTemplate();
                ExtendedBucket[] list = restTemplate.getForObject("http://localhost:8080/api/getBuckets", ExtendedBucket[].class);
                if (list != null && list.length > 0) {
                    for (ExtendedBucket b : list) {
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
