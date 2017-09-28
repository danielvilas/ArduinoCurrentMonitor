package es.daniel.outputgui.gui;

import es.daniel.outputgui.data.Bucket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.awt.*;
import java.util.Date;


public class DetectedPanel extends JPanel {

    XYIntervalSeriesCollection localXYIntervalSeriesCollection = new XYIntervalSeriesCollection();
    XYIntervalSeries tvSeries = new XYIntervalSeries("TV");
    XYIntervalSeries bluRaySeries = new XYIntervalSeries("BluRay");
    XYIntervalSeries appleTvSeries = new XYIntervalSeries("AppleTv");
    XYIntervalSeries ipTvSeries = new XYIntervalSeries("IpTv");

    public DetectedPanel() {
        super();
        setLayout(new GridLayout(1,1));
        JPanel panel=  new ChartPanel(createChart(createDataset()));
        add(panel);
        setPreferredSize(new Dimension(500, 300));
    }
    private IntervalXYDataset createDataset()
    {

        localXYIntervalSeriesCollection = new XYIntervalSeriesCollection();
        tvSeries = new XYIntervalSeries("TV");
        bluRaySeries = new XYIntervalSeries("BluRay");
        appleTvSeries = new XYIntervalSeries("AppleTv");
        ipTvSeries = new XYIntervalSeries("IpTv");

        localXYIntervalSeriesCollection.addSeries(tvSeries);
        localXYIntervalSeriesCollection.addSeries(bluRaySeries);
        localXYIntervalSeriesCollection.addSeries(appleTvSeries);
        localXYIntervalSeriesCollection.addSeries(ipTvSeries);

        return localXYIntervalSeriesCollection;
    }

    public void addData(Bucket bucket){
        if(bucket.getTvSeconds()>6) {
            addItem(tvSeries, bucket.getStart(), bucket.getEnd(), 0);
        }
        if(bucket.getBluraySeconds()>6) {
            addItem(bluRaySeries, bucket.getStart(), bucket.getEnd(), 1);
        }
        if(bucket.getAppleTvSeconds()>6) {
            addItem(appleTvSeries, bucket.getStart(), bucket.getEnd(), 2);
        }
        if(bucket.getIpTvSeconds()>6) {
            addItem(ipTvSeries, bucket.getStart(), bucket.getEnd(), 3);
        }
    }

    private void addItem(XYIntervalSeries serie, Date start, Date end, int pos)
    {
        XYIntervalDataItem item =  new XYIntervalDataItem(pos, pos-0.45,pos+0.45,start.getTime(),start.getTime(),end.getTime());
        serie.add(item,true);
    }

    private JFreeChart createChart(IntervalXYDataset paramIntervalXYDataset)
    {
        JFreeChart localJFreeChart = ChartFactory.createXYBarChart("Appliances", "Date", true, "Y", paramIntervalXYDataset, PlotOrientation.HORIZONTAL, true, false, false);
        XYPlot localXYPlot = (XYPlot)localJFreeChart.getPlot();
        localXYPlot.setRangePannable(true);
        localXYPlot.setRangeAxis(new DateAxis("Date"));
        SymbolAxis localSymbolAxis = new SymbolAxis("Series", new String[] { "TV", "BluRay", "AppleTv","IpTV" });
        localSymbolAxis.setGridBandsVisible(false);
        localXYPlot.setDomainAxis(localSymbolAxis);
        XYBarRenderer localXYBarRenderer = (XYBarRenderer)localXYPlot.getRenderer();
        localXYBarRenderer.setUseYInterval(true);
        localXYPlot.setRenderer(localXYBarRenderer);
        localXYPlot.setBackgroundPaint(Color.lightGray);
        localXYPlot.setDomainGridlinePaint(Color.white);
        localXYPlot.setRangeGridlinePaint(Color.white);
        ChartUtilities.applyCurrentTheme(localJFreeChart);
        return localJFreeChart;
    }
}
