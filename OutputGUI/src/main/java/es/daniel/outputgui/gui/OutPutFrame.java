package es.daniel.outputgui.gui;

import javax.swing.*;

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

public class OutPutFrame extends JFrame {

    List<Bucket> list;

    public OutPutFrame(List<Bucket> list) throws HeadlessException {
        super("Appliances");
        this.list=list;
        JPanel localJPanel = createAppliancesDeteced();
        JPanel appTimePanel = createTimeAppliances();

        localJPanel.setPreferredSize(new Dimension(500, 300));
        GridLayout layout=new GridLayout(2,1);

        getContentPane().setLayout(layout);
        getContentPane().add(localJPanel);
        getContentPane().add(appTimePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel createAppliancesDeteced()
    {
        return new ChartPanel(createChart(createDataset()));
    }


    public JPanel createTimeAppliances(){
        return new ChartPanel(createChart(createTimeDataset()));
    }

    private XYDataset createTimeDataset()
    {
        TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();

        TimeSeries tvSeries = new TimeSeries("TV");
        TimeSeries bluRaySeries = new TimeSeries("BluRay");
        TimeSeries appleTvSeries = new TimeSeries("AppleTv");
        TimeSeries ipTvSeries = new TimeSeries("IpTv");

        for(int i=0;i<list.size();i++){
            Bucket b = list.get(i);
            tvSeries.add(new Millisecond(b.getStart()),b.getTvSeconds());
            bluRaySeries.add(new Millisecond(b.getStart()),b.getBluraySeconds());
            appleTvSeries.add(new Millisecond(b.getStart()),b.getAppleTvSeconds());
            ipTvSeries.add(new Millisecond(b.getStart()),b.getIpTvSeconds());
        }

        localTimeSeriesCollection.addSeries(tvSeries);
        localTimeSeriesCollection.addSeries(bluRaySeries);
        localTimeSeriesCollection.addSeries(appleTvSeries);
        localTimeSeriesCollection.addSeries(ipTvSeries);
        return localTimeSeriesCollection;
    }

    private  JFreeChart createChart(XYDataset paramXYDataset)
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
        Object localObject = (DateAxis)localXYPlot.getDomainAxis();
        //((DateAxis)localObject).setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
        return ((JFreeChart)localJFreeChart);
    }


    private  JFreeChart createChart(IntervalXYDataset paramIntervalXYDataset)
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

    private IntervalXYDataset createDataset()
    {

        XYIntervalSeriesCollection localXYIntervalSeriesCollection = new XYIntervalSeriesCollection();
        XYIntervalSeries tvSeries = new XYIntervalSeries("TV");
        XYIntervalSeries bluRaySeries = new XYIntervalSeries("BluRay");
        XYIntervalSeries appleTvSeries = new XYIntervalSeries("AppleTv");
        XYIntervalSeries ipTvSeries = new XYIntervalSeries("IpTv");

        for(int i=0;i<list.size();i++){
            Bucket b=list.get(i);
            if(b.getTvSeconds()>6) {
                addItem(tvSeries, b.getStart(), b.getEnd(), 0);
            }
            if(b.getBluraySeconds()>6) {
                addItem(bluRaySeries, b.getStart(), b.getEnd(), 1);
            }
            if(b.getAppleTvSeconds()>6) {
                addItem(appleTvSeries, b.getStart(), b.getEnd(), 2);
            }
            if(b.getIpTvSeconds()>6) {
                addItem(ipTvSeries, b.getStart(), b.getEnd(), 3);
            }
        }

        localXYIntervalSeriesCollection.addSeries(tvSeries);
        localXYIntervalSeriesCollection.addSeries(bluRaySeries);
        localXYIntervalSeriesCollection.addSeries(appleTvSeries);
        localXYIntervalSeriesCollection.addSeries(ipTvSeries);
        return localXYIntervalSeriesCollection;
    }

    private void addItem(XYIntervalSeries serie, Date start, Date end, int pos)
    {
       serie.add(pos,pos-0.45,pos+0.45,start.getTime(),start.getTime(),end.getTime());
    }
}
