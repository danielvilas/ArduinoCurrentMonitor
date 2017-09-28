package es.daniel.outputgui.data;

import java.util.Date;

public class Bucket implements Comparable<Bucket> {
    Date start;
    Date end;

    private float tvSeconds =0.0f;
    private float bluraySeconds=0.0f;
    private float appleTvSeconds=0.0f;
    private float ipTvSeconds=0.0f;

    public Bucket(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Bucket() {
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }


    public float getTvSeconds() {
        return tvSeconds;
    }

    public void setTvSeconds(float tvSeconds) {
        this.tvSeconds = tvSeconds;
    }

    public void appendTvSeconds(float seconds){
        tvSeconds+=seconds;
    }

    public float getBluraySeconds() {
        return bluraySeconds;
    }

    public void setBluraySeconds(float bluraySeconds) {
        this.bluraySeconds = bluraySeconds;
    }

    public void appendBluraySeconds(float seconds){
        bluraySeconds+=seconds;
    }

    public float getAppleTvSeconds() {
        return appleTvSeconds;
    }

    public void setAppleTvSeconds(float appleTvSeconds) {
        this.appleTvSeconds = appleTvSeconds;
    }

    public void appendAppleTvSeconds(float seconds){
        this.appleTvSeconds+=seconds;
    }

    public float getIpTvSeconds() {
        return ipTvSeconds;
    }

    public void setIpTvSeconds(float ipTvSeconds) {
        this.ipTvSeconds = ipTvSeconds;
    }

    public void appendIpTvSeconds(float seconds){
        this.ipTvSeconds+=seconds;
    }

    public int compareTo(Bucket o) {
        return start.compareTo(o.getStart());
    }

    @Override
    public String toString() {
        return  "Bucket@" + Integer.toHexString(hashCode())+": "+start.toString();
    }
}
