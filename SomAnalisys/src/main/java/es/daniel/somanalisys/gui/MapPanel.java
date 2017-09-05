package es.daniel.somanalisys.gui;

import org.encog.mathutil.matrices.Matrix;
import org.encog.neural.som.SOM;
import org.encog.util.EngineArray;

import javax.swing.*;
import java.awt.*;

/**
 * Created by daniel on 6/8/17.
 */
public class MapPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 7528474872067939033L;
    public static final int CELL_SIZE = 8;
    public static final int CELL_U_DATA_SIZE = CELL_SIZE/2;
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public static final int TOTAL_SIZE =WIDTH*HEIGHT;

    //Dividers for normalize on Paint
    private static final int DIST_DIV = 2;
    private static final int I0_DIV = 40;
    private static final int I1_DIV = 25;
    private static final int I2_DIV = 17;
    private static final double I_TINTFACTOR=0.9;



    private Matrix weights;

	public MapPanel(SOM som)
    {
        this.weights = som.getWeights();
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
    public void paint(Graphics g)
    {
        double dist=0;
        for(int y = 0; y< HEIGHT; y++)
        {
            for(int x = 0; x< WIDTH; x++)
            {
                int index = (y*WIDTH)+x;

                double data[] =weights.getRow(index).getData()[0];

               PaintInputData(g,x,y,data);

                setColor(g, data[3],data[4],data[5]);
                g.fillRect(getX(x,1), getY(y,0), CELL_SIZE, CELL_SIZE);
                double tmp = PaintUMatrix(g, y, x, data);
                if(tmp>dist)
                    dist=tmp;

            }
        }
        System.out.println("Max Dist = "+dist);
    }

    private void PaintInputData(Graphics g, int x, int y, double[] data){
        setColor(g, data[0]/I0_DIV,data[1]/I1_DIV,data[2]/I2_DIV);
        g.fillRect(getX(x,0), getY(y,0), CELL_SIZE, CELL_SIZE);

        setColor(g, data[0]/I0_DIV,data[0]/I0_DIV*I_TINTFACTOR,data[0]/I0_DIV*I_TINTFACTOR);
        g.fillRect(getX(x,0), getY(y,1), CELL_SIZE, CELL_SIZE);

        setColor(g, data[1]/ I1_DIV*I_TINTFACTOR,data[1]/ I1_DIV,data[1]/ I1_DIV*I_TINTFACTOR);
        g.fillRect(getX(x,1), getY(y,1), CELL_SIZE, CELL_SIZE);

        setColor(g, data[2]/I2_DIV*I_TINTFACTOR,data[2]/I2_DIV*I_TINTFACTOR,data[2]/I2_DIV);
        g.fillRect(getX(x,2), getY(y,1), CELL_SIZE, CELL_SIZE);
    }

    private int getX(int x, int viewX){
        return viewX  * (WIDTH + 1) * CELL_SIZE + x * CELL_SIZE;
    }

    private int getY(int y, int viewY){
        return viewY  * (HEIGHT + 1) * CELL_SIZE + y * CELL_SIZE;
    }

    private Double PaintUMatrix(Graphics g, int y, int x, double[] data) {
        int index;
        index=(y*WIDTH)+x+1;
        boolean ret0=false;
        if(index >= TOTAL_SIZE) {
            index -=TOTAL_SIZE;
            ret0=true;
        }
        double left[] = weights.getRow(index).getData()[0];
        double dist = EngineArray.euclideanDistance(data, left);
        double medDist=dist/3;
        setColor(g, dist/ DIST_DIV, dist/ DIST_DIV, dist/ DIST_DIV);
        g.fillRect(getX(x,2)+ CELL_SIZE / 2, getY(y,0), CELL_SIZE / 2, CELL_SIZE / 2);


        index = ((y + 1) * WIDTH) + x;
        if (index >= TOTAL_SIZE) {
            index -= TOTAL_SIZE;
            ret0=true;
        }
        double down[] = weights.getRow(index).getData()[0];
        dist = EngineArray.euclideanDistance(data, down);
        medDist+=dist/3;
        setColor(g, dist/ DIST_DIV, dist/ DIST_DIV, dist/ DIST_DIV);
        g.fillRect(getX(x,2), getY(y,0) + CELL_SIZE / 2, CELL_SIZE / 2, CELL_SIZE / 2);


        index = ((y + 1) * WIDTH) + x + 1;
        if (index  >=  TOTAL_SIZE) {
            index -= TOTAL_SIZE;
            ret0=true;
        }
        double left_down[] = weights.getRow(index).getData()[0];
        dist = EngineArray.euclideanDistance(data, left_down);
        medDist+=dist/3;
        setColor(g, medDist/ DIST_DIV, medDist/ DIST_DIV, medDist/ DIST_DIV);
        g.fillRect(getX(x,2) + CELL_SIZE / 2, getY(y,0)+ CELL_SIZE / 2, CELL_SIZE / 2, CELL_SIZE / 2);
        setColor(g, dist/ DIST_DIV, dist/ DIST_DIV, dist/ DIST_DIV);
        g.fillRect(getX(x,2), getY(y,0), CELL_SIZE / 2, CELL_SIZE / 2);


        if(ret0 || x==WIDTH-1)return 0.0;
        return medDist;
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
