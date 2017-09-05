package es.daniel.somanalisys.data;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;

import java.util.Date;

public class CustomMLData extends BasicMLData {
    private String file;
    private Date date;

    public CustomMLData(double[] d, String file, Date date) {
        super(d);
        this.file = file;
        this.date = date;
    }

    public CustomMLData(int size, String file, Date date) {
        super(size);
        this.file = file;
        this.date = date;
    }

    public CustomMLData(MLData d, String file, Date date) {
        super(d);
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
