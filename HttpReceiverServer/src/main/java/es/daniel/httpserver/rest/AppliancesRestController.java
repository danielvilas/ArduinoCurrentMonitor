package es.daniel.httpserver.rest;

import es.daniel.outputgui.data.Packet;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/api/")
public class AppliancesRestController {
    List<Packet> list;

    public AppliancesRestController(){
        list=new ArrayList<Packet>();
    }

    @RequestMapping(value = "/addPacket", method = RequestMethod.POST)
    public void addBubcket(@RequestBody() Packet bucket){
        synchronized (list){
            list.add(bucket);
        }
    }

    @RequestMapping(value = "/getPackets", method = RequestMethod.GET)
    public List<Packet> getAllPendingsBucket(){
        ArrayList<Packet> ret = new ArrayList<Packet>();
        synchronized (list){
            ret.addAll(list);
            list.clear();
        }
        return ret;
    }

    @RequestMapping(value = "/debugPackets", method = RequestMethod.GET)
    public List<Packet> debugBucketList(){
        ArrayList<Packet> ret = new ArrayList<Packet>();
        synchronized (list){
            ret.addAll(list);
        }
        return ret;
    }

}
