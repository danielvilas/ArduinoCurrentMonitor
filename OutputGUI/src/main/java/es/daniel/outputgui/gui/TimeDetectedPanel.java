package es.daniel.outputgui.gui;

import es.daniel.outputgui.data.ExtendedBucket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;

public class TimeDetectedPanel extends JPanel {
    TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();

    TimeSeries tvSeries = new TimeSeries("TV");
    TimeSeries bluRaySeries = new TimeSeries("BluRay");
    TimeSeries appleTvSeries = new TimeSeries("AppleTv");
    TimeSeries ipTvSeries = new TimeSeries("IpTv");


    public TimeDetectedPanel() {
        super();
        setLayout(new GridLayout(1,1));
        JPanel panel=  new ChartPanel(createChart(createDataset()));
        add(panel);
        setPreferredSize(new Dimension(500, 300));
    }

    private XYDataset createDataset()
    {
        localTimeSeriesCollection = new TimeSeriesCollection();

        tvSeries = new TimeSeries("TV");
        bluRaySeries = new TimeSeries("BluRay");
        appleTvSeries = new TimeSeries("AppleTv");
        ipTvSeries = new TimeSeries("IpTv");

        localTimeSeriesCollection.addSeries(tvSeries);
        localTimeSeriesCollection.addSeries(bluRaySeries);
        localTimeSeriesCollection.addSeries(appleTvSeries);
        localTimeSeriesCollection.addSeries(ipTvSeries);

        return localTimeSeriesCollection;
    }

    public void addData(ExtendedBucket b) {
        tvSeries.addOrUpdate(new Millisecond(b.getStartDate()),b.getTvSeconds());
        bluRaySeries.addOrUpdate(new Millisecond(b.getStartDate()),b.getBluraySeconds());
        appleTvSeries.addOrUpdate(new Millisecond(b.getStartDate()),b.getAppleTvSeconds());
        ipTvSeries.addOrUpdate(new Millisecond(b.getStartDate()),b.getIpTvSeconds());
    }

    private JFreeChart createChart(XYDataset paramXYDataset)
    {
        JFreeChart localJFreeChart = ChartFactory.createTimeSeriesChart("Detection Time", "Date", "Detected Time", paramXYDataset, true, true, false);
        XYPlot localXYPlot = (XYPlot)localJFreeChart.getPlot();
        localXYPlot.setDomainPannable(true);
        localXYPlot.setRangePannable(false);
        localXYPlot.setDomainCrosshairVisible(true);
        localXYPlot.setRangeCrosshairVisible(true);
        XYItemRenderer localXYItemRenderer = localXYPlot.getRenderer();
        if (localXYItemRenderer instanceof XYLineAndShapeRenderer)
        {
            XYLineAndShapeRenderer localObject = (XYLineAndShapeRenderer)localXYItemRenderer;
            ((XYLineAndShapeRenderer)localObject).setBaseShapesVisible(false);
        }
        return ((JFreeChart)localJFreeChart);
    }

    public void clearAllData() {
        tvSeries.clear();
        bluRaySeries.clear();
        appleTvSeries.clear();
        ipTvSeries.clear();
    }
}
