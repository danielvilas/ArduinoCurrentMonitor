package es.daniel.outputgui.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ExtendedBucket implements Comparable<ExtendedBucket> {


    protected Date start;
    protected Date end;
    protected float tvSeconds;
    protected float bluraySeconds;
    protected float appleTvSeconds;
    protected float ipTvSeconds;


    public ExtendedBucket(Date start, Date end) {
        this.start =start;
        this.end = end;
    }

    public ExtendedBucket() {
    }

    public Date getStart(){
        return start;
    }

    public Date getEnd(){
        return end;
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
        return start.compareTo(o.getStart());
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

    /**
     * Obtiene el valor de la propiedad tvSeconds.
     *
     */
    public float getTvSeconds() {
        return tvSeconds;
    }

    /**
     * Define el valor de la propiedad tvSeconds.
     *
     */
    public void setTvSeconds(float value) {
        this.tvSeconds = value;
    }

    /**
     * Obtiene el valor de la propiedad bluraySeconds.
     *
     */
    public float getBluraySeconds() {
        return bluraySeconds;
    }

    /**
     * Define el valor de la propiedad bluraySeconds.
     *
     */
    public void setBluraySeconds(float value) {
        this.bluraySeconds = value;
    }

    /**
     * Obtiene el valor de la propiedad appleTvSeconds.
     *
     */
    public float getAppleTvSeconds() {
        return appleTvSeconds;
    }

    /**
     * Define el valor de la propiedad appleTvSeconds.
     *
     */
    public void setAppleTvSeconds(float value) {
        this.appleTvSeconds = value;
    }

    /**
     * Obtiene el valor de la propiedad ipTvSeconds.
     *
     */
    public float getIpTvSeconds() {
        return ipTvSeconds;
    }

    /**
     * Define el valor de la propiedad ipTvSeconds.
     *
     */
    public void setIpTvSeconds(float value) {
        this.ipTvSeconds = value;
    }

}
