package es.daniel.somanalisys.data;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLDataPair;

import java.util.Date;

public class CustomMLDataPair extends BasicMLDataPair implements CustomData {
    private String file;
    private Date date;

    public CustomMLDataPair(MLData theInput, String file, Date date) {
        super(theInput);
        this.file = file;
        this.date = date;
    }

    public CustomMLDataPair(MLData theInput, MLData theIdeal, String file, Date date) {
        super(theInput, theIdeal);
        this.file = file;
        this.date = date;
    }


    public String getFile() {
        return file;
    }
    public Date getDate() {
        return date;
    }
}
