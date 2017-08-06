package es.daniel.somanalisys;

import org.encog.mathutil.matrices.Matrix;
import org.encog.neural.som.SOM;

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
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;

    private Matrix weights;

	public MapPanel(SOM som)
    {
        this.weights = som.getWeights();
    }

    private int convertColor(double d)
    {
        //System.out.println(d);
        double result = 128*d;
        result+=128;
        result = Math.min(result, 255);
        result = Math.max(result, 0);
        return (int)result;
    }

    @Override
    public void paint(Graphics g)
    {
        for(int y = 0; y< HEIGHT; y++)
        {
            for(int x = 0; x< WIDTH; x++)
            {
                int index = (y*WIDTH)+x;
                int red = convertColor(weights.get(index,0));
                int green = convertColor(weights.get(index,1));
                int blue = convertColor(weights.get(index,2));
                g.setColor(new Color(red,green,blue));
                g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}
