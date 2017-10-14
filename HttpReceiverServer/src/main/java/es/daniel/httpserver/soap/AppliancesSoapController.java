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

    List<Bucket> list;

    public AppliancesSoapController(){
        list=new ArrayList<Bucket>();
    }

    public static final String NAMESPACE_URI = "http://daniel.es/outputgui/data";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddBucketRequest")
    @ResponsePayload
    public AddBucketResponse addBubcket(@RequestPayload AddBucketRequest bucket){
        synchronized (list){
            list.add(bucket.getBucket());
        }
        AddBucketResponse ret = new AddBucketResponse();
        ret.setBucket(bucket.getBucket());
        return ret;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetBucketsRequest")
    @ResponsePayload
    public GetBucketsResponse getAllPendingsBucket(){
        GetBucketsResponse ret  = new GetBucketsResponse();
        synchronized (list){
            ret.setBuckets(new ListOfBucket());
            ret.getBuckets().getBucket().addAll(list);
            list.clear();
        }
        return ret;
    }

   @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DebugBucketsRequest")
   @ResponsePayload
   public DebugBucketsResponse debugBucketList(){
       DebugBucketsResponse ret = new DebugBucketsResponse();
        synchronized (list){
            ret.getBuckets().getBucket().addAll(list);
        }
        return ret;
    }


}
