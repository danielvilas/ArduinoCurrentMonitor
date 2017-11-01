package es.daniel.outputgui.protocols;

import es.daniel.outputgui.data.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RestBucketProducer implements DataManagerListener {
    public static final String URL = "http://server.local:9090/api/addPacket";
    //public static final String URL = "http://localhost:8080/api/addPacket";

    public void addPacket(ParsedPacket p) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(URL,fromParsedPacket(p));
        }catch (Exception e){
            e.printStackTrace();
        }
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

