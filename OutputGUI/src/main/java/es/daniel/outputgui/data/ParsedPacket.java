package es.daniel.outputgui.data;

import java.io.Serializable;
import java.util.Date;

public class ParsedPacket implements Serializable {

    private Date date;
    private double tv;
    private double bluray;
    private double appleTv;
    private double ipTv;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTv() {
        return tv;
    }

    public void setTv(double tv) {
        this.tv = tv;
    }

    public double getBluray() {
        return bluray;
    }

    public void setBluray(double bluray) {
        this.bluray = bluray;
    }

    public double getAppleTv() {
        return appleTv;
    }

    public void setAppleTv(double appleTv) {
        this.appleTv = appleTv;
    }

    public double getIpTv() {
        return ipTv;
    }

    public void setIpTv(double ipTv) {
        this.ipTv = ipTv;
    }

    public ParsedPacket() {
    }
}

