package es.daniel.somanalisys.gui;

import es.daniel.somanalisys.Model.ModelManager;
import es.daniel.somanalisys.data.CustomMLData;
import es.daniel.somanalisys.data.CustomMLDataPair;
import es.daniel.somanalisys.data.DataManager;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class TimePanel  extends JPanel {

    public static final int CELL_HEIGH = 10;
    public static final int CELL_WIDTH = 1;

    private static final int DIST_DIV = 2;
    private static final int I0_DIV = 40;
    private static final int I1_DIV = 25;
    private static final int I2_DIV = 17;
    private static final double I_TINTFACTOR=0.9;

    DataManager dataMgr;
    ModelManager modelMgr;

    public TimePanel(DataManager dataMgr, ModelManager modelMgr) {
        this.dataMgr=dataMgr;
        this.modelMgr=modelMgr;
        this.setSize(60*60*TimePanel.CELL_WIDTH+20, TimePanel.CELL_HEIGH+120);
        this.setPreferredSize(getSize());
    }

    private int convertColor(double d)
    {
        //System.out.println(d);
        double result = 256*d;
        result = Math.min(result, 255);
        result = Math.max(result, 0);
        return (int)result;
    }

    @Override
    public void paint(Graphics g) {
        MLDataPair[][] toPaint= new MLDataPair[60*60][6];
        double[][][] result= new double[60*60][6][];
        for (MLDataPair mlDataPair:dataMgr.getAllData()) {
            MLData mlData = mlDataPair.getInput();


            Date date = null;
            if(mlData instanceof CustomMLData ) {
                CustomMLData customMLData = (CustomMLData) mlData;
                date = customMLData.getDate();
            }else if(mlDataPair instanceof CustomMLDataPair){
                CustomMLDataPair customMLData = (CustomMLDataPair) mlDataPair;
                date = customMLData.getDate();
            }
            int hour=date.getHours();
            hour -= 15;
            //double data[] = mlData.getData();
            int x = getX(date);
            toPaint[x][hour]=mlDataPair;
            result[x][hour] =modelMgr.execute(mlData);
        }
        MLDataPair last=null;
        double lastResult[]=null;

        for(int y = 0;y<6;y++) {
            for(int x=0;x<60*60;x++){
                if(toPaint[x][y]==null && last==null)continue;
                if(toPaint[x][y]!=null){
                    last=toPaint[x][y];
                    lastResult=result[x][y];
                }
                double data[] = last.getInput().getData();
                setColor(g, data[0] / I0_DIV, data[1] / I1_DIV, data[2] / I2_DIV);
                g.fillRect(x, getY(0, y), CELL_WIDTH, CELL_HEIGH);
                double out[]=(last.getIdeal()!=null)?last.getIdeal().getData():null;
                if(lastResult.length==3){
                    setColor(g, out[0], out[1], out[2]);
                    g.fillRect(x, getY(1, y), CELL_WIDTH, CELL_HEIGH);
                    setColor(g, lastResult[0], lastResult[1], lastResult[2]);
                    g.fillRect(x, getY(2, y), CELL_WIDTH, CELL_HEIGH);
                }else {

                    setColor(g, data[3], data[4], data[5]);
                    g.fillRect(x, getY(2, y), CELL_WIDTH, CELL_HEIGH);

                    setColor(g, lastResult[0] / I0_DIV, lastResult[1] / I1_DIV, lastResult[2] / I2_DIV);
                    g.fillRect(x, getY(1, y), CELL_WIDTH, CELL_HEIGH);
                    setColor(g, lastResult[3], lastResult[4], lastResult[5]);
                    g.fillRect(x, getY(3, y), CELL_WIDTH, CELL_HEIGH);
                }
            }
        }
    }

    private int getX(Date date){
        int m=date.getMinutes();
        int s= date.getSeconds();
        return m*60+s;
    }

    private int getY(int level, int view){
        return (view*5)*CELL_HEIGH+ level*CELL_HEIGH;
    }

    private void setColor(Graphics g, double red, double green, double blue ) {
        int ired;
        int igreen;
        int iblue;
        ired = convertColor(red);
        igreen = convertColor(green);
        iblue = convertColor(blue);
        g.setColor(new Color(ired,igreen,iblue));
    }
}
