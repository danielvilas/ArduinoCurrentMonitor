package es.daniel.fann.gui;

import es.daniel.datalogger.data.Appliance;
import es.daniel.fann.model.ModelManager;
import es.daniel.fann.data.CustomMLDataPair;
import es.daniel.fann.data.DataManager;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

@SuppressWarnings("deprecation")
public class TimePanel extends JPanel {

    public static final int CELL_HEIGH = 10;
    public static final int CELL_WIDTH = 1;

    private static final int DIST_DIV = 2;
    private static final int I0_DIV = 12;
    private static final int I1_DIV = 8;
    private static final int I2_DIV = 6;
    private static final double I_TINTFACTOR=0.9;

    DataManager dataMgr;
    ModelManager modelMgr;
    Color[] outColors;

    public TimePanel(DataManager dataMgr, ModelManager modelMgr) {
        this.dataMgr=dataMgr;
        this.modelMgr=modelMgr;
        this.setSize(60*60*TimePanel.CELL_WIDTH+20, TimePanel.CELL_HEIGH+120);
        this.setPreferredSize(getSize());
        outColors=new Color[Appliance.values().length];
        for(Appliance app:Appliance.values()){
            outColors[app.getPosition()]=app.getColor();
        }
    }

    private int convertColor(double d)
    {
        //System.out.println(d);
        double result = 256*d;
        return fixColor((int)result);
    }

    private int fixColor(int result) {
        result = Math.min(result, 255);
        result = Math.max(result, 0);
        return result;
    }

    @Override
    public void paint(Graphics g) {
        MLDataPair[][] toPaint= new MLDataPair[60*60][6];
        double[][][] result= new double[60*60][6][];
        for (MLDataPair mlDataPair:dataMgr.getAllData()) {
            MLData mlData = mlDataPair.getInput();


            Date date = null;
            /*if(mlData instanceof CustomMLData ) {
                CustomMLData customMLData = (CustomMLData) mlData;
                date = customMLData.getDate();
            }else */if(mlDataPair instanceof CustomMLDataPair){
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
                //if(toPaint[x][y]==null)continue;
                if(toPaint[x][y]==null && last==null)continue;
                if(toPaint[x][y]!=null){
                    last=toPaint[x][y];
                    lastResult=result[x][y];
                }
                double data[] = last.getInput().getData();
                setColor(g, data[0] / I0_DIV, data[1] / I1_DIV, data[2] / I2_DIV);
                g.fillRect(x, getY(0, y), CELL_WIDTH, CELL_HEIGH);

                double out[]=(last.getIdeal()!=null)?last.getIdeal().getData():null;
                setColorData(g, out);
                g.fillRect(x, getY(1, y), CELL_WIDTH, CELL_HEIGH);

                setColorData(g,lastResult);
                g.fillRect(x, getY(2, y), CELL_WIDTH, CELL_HEIGH);

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

    private void setColorData(Graphics g, double[] data){

        int ired=0;
        int igreen=0;
        int iblue=0;

        for(int i=0;i<data.length;i++){
            ired+=data[i]*outColors[i].getRed();
            igreen+=data[i]*outColors[i].getGreen();
            iblue+=data[i]*outColors[i].getBlue();
        }
        ired=fixColor(ired);
        igreen=fixColor(igreen);
        iblue=fixColor(iblue);

        g.setColor(new Color(ired,igreen,iblue));
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
