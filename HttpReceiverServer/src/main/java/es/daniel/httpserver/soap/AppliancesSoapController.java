package es.daniel.httpserver.soap;

import es.daniel.outputgui.data.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.List;

@Endpoint()
public class AppliancesSoapController {

    List<Packet> list;

    public AppliancesSoapController(){
        list=new ArrayList<Packet>();
    }

    public static final String NAMESPACE_URI = "http://daniel.es/outputgui/data";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddPacketRequest")
    @ResponsePayload
    public AddPacketResponse addBubcket(@RequestPayload AddPacketRequest Packet){
        synchronized (list){
            list.add(Packet.getPacket());
        }
        AddPacketResponse ret = new AddPacketResponse();
        ret.setPacket(Packet.getPacket());
        return ret;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetPacketsRequest")
    @ResponsePayload
    public GetPacketsResponse getAllPendingsPacket(){
        GetPacketsResponse ret  = new GetPacketsResponse();
        synchronized (list){
            ret.setPackets(new ListOfPackets());
            ret.getPackets().getPacket().addAll(list);
            list.clear();
        }
        return ret;
    }

   @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DebugPacketsRequest")
   @ResponsePayload
   public DebugPacketsResponse debugPacketList(){
       DebugPacketsResponse ret = new DebugPacketsResponse();
        synchronized (list){
            ret.getPackets().getPacket().addAll(list);
        }
        return ret;
    }


}
