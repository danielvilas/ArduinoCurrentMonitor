package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.BucketManager;
import es.daniel.outputgui.data.BucketManagerListener;

public class AbstractConsumer {
    protected  BucketManager bm = new BucketManager();

    public BucketManagerListener getOut() {
        return bm.getListener();
    }

    public void setOut(BucketManagerListener out) {
        bm.setListener(out);
    }


}
