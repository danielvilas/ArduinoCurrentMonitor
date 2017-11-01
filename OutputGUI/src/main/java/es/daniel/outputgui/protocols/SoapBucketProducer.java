package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SoapBucketProducer extends WebServiceGatewaySupport implements DataManagerListener {

    private static final String URI = "http://server.local:9090/ws";
    //private static final String URI = "http://localhost:8080/ws";


    public SoapBucketProducer(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("es.daniel.outputgui.data");
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
    }

    public void addPacket(ParsedPacket p) {
        Packet packet = fromParsedPacket(p);
        AddPacketRequest req = new AddPacketRequest();
        req.setPacket(packet);

        AddPacketResponse res = (AddPacketResponse) getWebServiceTemplate().marshalSendAndReceive(URI,
                req, new SoapActionCallback(""));
        //client.addBucket(req);
    }

    private Packet fromParsedPacket(ParsedPacket p){
        Packet packet = new Packet();
        packet.setAppleTvSeconds((float)p.getAppleTv());
        packet.setBluraySeconds((float)p.getBluray());
        packet.setDate(getXmlGregorianCalendarFromDate(p.getDate()));
        packet.setIpTvSeconds((float)p.getIpTv());
        packet.setTvSeconds((float)p.getTv());
        return packet;
    }

    private XMLGregorianCalendar getXmlGregorianCalendarFromDate(final Date date)   {
        try {
            GregorianCalendar calendar = new GregorianCalendar();

            calendar.setTime(date);

            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        }catch (DatatypeConfigurationException e){
            throw new RuntimeException(e);
        }
    }
}
