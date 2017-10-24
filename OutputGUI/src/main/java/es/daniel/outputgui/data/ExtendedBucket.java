package es.daniel.outputgui.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ExtendedBucket extends Bucket implements Comparable<ExtendedBucket> {

    public ExtendedBucket(Date start, Date end) {
        super();
        try {
            this.start = getXmlGregorianCalendarFromDate(start);
            this.end = getXmlGregorianCalendarFromDate(end);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExtendedBucket() {
    }

    public ExtendedBucket(Bucket b){
        this.start=b.getStart();
        this.end=b.getEnd();

        this.bluraySeconds=b.getBluraySeconds();
        this.tvSeconds=b.getTvSeconds();
        this.appleTvSeconds=b.getAppleTvSeconds();
        this.ipTvSeconds=b.getIpTvSeconds();
    }

    @JsonIgnore
    public Date getStartDate(){
        return start.toGregorianCalendar().getTime();
    }
    @JsonIgnore
    public Date getEndDate(){
        return end.toGregorianCalendar().getTime();
    }

    public void appendTvSeconds(float seconds){
        tvSeconds+=seconds;
    }

    public void appendBluraySeconds(float seconds){
        bluraySeconds+=seconds;
    }

    public void appendAppleTvSeconds(float seconds){
        this.appleTvSeconds+=seconds;
    }

    public void appendIpTvSeconds(float seconds){
        this.ipTvSeconds+=seconds;
    }

    public int compareTo(ExtendedBucket o) {
        return start.toGregorianCalendar().getTime().compareTo(o.getStart().toGregorianCalendar().getTime());
    }

    @Override
    public String toString() {
        return  "ExtendedBucket@" + Integer.toHexString(hashCode())+": "+start.toString();
    }


    public String toJsonString() throws Exception {
        /*return "{" +
                "\n\tstart:" + start +
                ",\n\tend:" + end +
                ",\n\ttvSeconds:" + tvSeconds +
                ",\n\tbluraySeconds:" + bluraySeconds +
                ",\n\tappleTvSeconds:" + appleTvSeconds +
                ",\n\tipTvSeconds:" + ipTvSeconds +
                "\n}";*/

        ObjectMapper om= new ObjectMapper();
        return om.writeValueAsString(this);
    }

    public static XMLGregorianCalendar getXmlGregorianCalendarFromDate(final Date date) throws DatatypeConfigurationException {

        GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(date);

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);

    }
}
