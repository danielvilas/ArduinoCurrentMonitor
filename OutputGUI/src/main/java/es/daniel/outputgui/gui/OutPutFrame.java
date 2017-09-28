package es.daniel.outputgui.gui;

import javax.swing.*;
import javax.xml.crypto.Data;

import es.daniel.outputgui.data.Bucket;
import es.daniel.outputgui.data.DataManagerListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OutPutFrame extends JFrame implements DataManagerListener {

    DetectedPanel detectedPanel;
    TimeDetectedPanel timeDetectedPanel;

    public OutPutFrame() throws HeadlessException {
        super("Appliances");
        detectedPanel = new DetectedPanel();
        timeDetectedPanel= new TimeDetectedPanel();

        GridLayout layout=new GridLayout(2,1);

        getContentPane().setLayout(layout);
        getContentPane().add(detectedPanel);
        getContentPane().add(timeDetectedPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    public void addOrUpdateBucket(Bucket bucket) {
        detectedPanel.addData(bucket);
        timeDetectedPanel.addData(bucket);
    }
}
