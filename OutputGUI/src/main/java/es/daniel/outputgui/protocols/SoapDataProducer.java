package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

public class SoapDataProducer extends WebServiceGatewaySupport implements DataManagerListener {

    public SoapDataProducer(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("es.daniel.outputgui.data");
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
    }



    public void addOrUpdateBucket(ExtendedBucket extendedBucket) {
        AddBucketRequest req = new AddBucketRequest();
        req.setBucket(extendedBucket);

        AddBucketResponse res = (AddBucketResponse) getWebServiceTemplate().marshalSendAndReceive("http://server.local:9090/ws",
                req, new SoapActionCallback(""));
        //client.addBucket(req);
    }



}
