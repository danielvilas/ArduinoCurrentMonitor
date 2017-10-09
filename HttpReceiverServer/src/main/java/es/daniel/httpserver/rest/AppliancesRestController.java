package es.daniel.httpserver.rest;

import es.daniel.outputgui.data.Bucket;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/api/")
public class AppliancesRestController {
    List<Bucket> list;

    public AppliancesRestController(){
        list=new ArrayList<Bucket>();
    }

    @RequestMapping(value = "/addBucket", method = RequestMethod.POST)
    public void addBubcket(@RequestBody() Bucket bucket){
        synchronized (list){
            list.add(bucket);
        }
    }

    @RequestMapping(value = "/getBuckets", method = RequestMethod.GET)
    public List<Bucket> getAllPendingsBucket(){
        ArrayList<Bucket> ret = new ArrayList<Bucket>();
        synchronized (list){
            ret.addAll(list);
            list.clear();
        }
        return ret;
    }

    @RequestMapping(value = "/debugBuckets", method = RequestMethod.GET)
    public List<Bucket> debugBucketList(){
        ArrayList<Bucket> ret = new ArrayList<Bucket>();
        synchronized (list){
            ret.addAll(list);
        }
        return ret;
    }

}
