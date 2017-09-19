package es.daniel.fann.gui;

import es.daniel.fann.data.Appliance;
import es.daniel.fann.data.CustomMLDataPair;
import es.daniel.fann.data.DataManager;
import es.daniel.fann.model.ModelManager;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class TimePanelContinous extends JPanel {

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

    public TimePanelContinous(DataManager dataMgr, ModelManager modelMgr) {
        this.dataMgr=dataMgr;
        this.modelMgr=modelMgr;
        this.setSize(60*60* TimePanelContinous.CELL_WIDTH+20, TimePanelContinous.CELL_HEIGH+120);
        this.setPreferredSize(getSize());
        outColors=new Color[Appliance.values().length];
        for(Appliance app:Appliance.values()){
            outColors[app.getPosition()]=app.getColor();
        }
        this.setDoubleBuffered(true);
        this.setIgnoreRepaint(true);
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
        int x=0;
        for (MLDataPair mlDataPair:dataMgr.getAllData()) {
            MLData mlData = mlDataPair.getInput();

            double calcResult[]=modelMgr.execute(mlData);

            double data[] = mlData.getData();
            double out[]=(mlDataPair.getIdeal()!=null)?mlDataPair.getIdeal().getData():null;
            //Paint InData
            setColor(g, data[0] / I0_DIV, data[1] / I1_DIV, data[2] / I2_DIV);
            g.fillRect(x*CELL_WIDTH, getY(0,0), CELL_WIDTH, CELL_HEIGH);

            //Paint general Data
            setColorData(g, out);
            g.fillRect(x*CELL_WIDTH, getY(1, 0), CELL_WIDTH, CELL_HEIGH);
            setColorData(g,calcResult);
            g.fillRect(x*CELL_WIDTH, getY(2, 0), CELL_WIDTH, CELL_HEIGH);

            for(int i=0;i<outColors.length;i++){
                setColor(g,outColors[i],out[i]);
                g.fillRect(x*CELL_WIDTH, getY(i*2, 1), CELL_WIDTH, CELL_HEIGH);
                setColor(g,outColors[i],calcResult[i]);
                g.fillRect(x*CELL_WIDTH, getY(i*2+1, 1), CELL_WIDTH, CELL_HEIGH);
            }

            x++;
        }
        this.setSize(x*CELL_WIDTH, TimePanelContinous.CELL_HEIGH+120);
        this.setPreferredSize(getSize());
    }

    private void setColor(Graphics g, Color outColor, double v) {
        double dRed=v*outColor.getRed();
        double dGreen=v*outColor.getGreen();
        double dBlue=v*outColor.getBlue();

        int iRed=fixColor((int)dRed);
        int iGreen=fixColor((int)dGreen);
        int iBlue=fixColor((int)dBlue);

        g.setColor(new Color(iRed,iGreen,iBlue));
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
