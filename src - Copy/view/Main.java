package view;


import evaluators.Differentiator;
import evaluators.Evaluator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import view.scale.PercentScaler;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Evaluator diff = new Evaluator("-1/x^2", "x");
        try1(diff, -15, 15,-10, 10, 1000);
    }
    public static XYSeries continuousSection(Evaluator eval, double ymin, double ymax, double start, double end, int steps, String name) {
        XYSeries function = new XYSeries(name);

        TreeMap<Double, Double> functionTable = eval.eval(start, end, steps);
        Double[] keys = functionTable.navigableKeySet().toArray(new Double[functionTable.size()]);

        for(int i = 0; i < keys.length; i++) {
            double x = keys[i];
            double y = functionTable.get(x);
            double m = 0;
            if(i+1 < keys.length) { //if there is another item ahead
                double x2 = keys[i+1];
                double y2 = functionTable.get(x2);
                m = (y2-y)/(x2-x);
            }

            //System.out.println(key+", "+val);
            function.add(x, y);
        }

        return function;
    }

    public static void try1(Evaluator eval, double ymin, double ymax, double start, double end, int steps) {
        TreeMap<Double, Double> functionTable = eval.adaptiveEval(start, end, steps);

        PercentScaler scaler = new PercentScaler(0.99);
        Scale s = scaler.getScaleRange(functionTable);
        System.out.println(s.min +" "+s.max);

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries xyseries = new XYSeries("");

        for(Double key : functionTable.navigableKeySet()) {
            Double val = functionTable.get(key);
            //System.out.println(key+", "+val);
            xyseries.add(key, val);
        }
        dataset.addSeries(xyseries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                null, null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
        //chart = ChartFactory.createScatterPlot("","","",dataset);
        FunctionFrame frame = new FunctionFrame(chart);
        //chart.getXYPlot().
        frame.pack();
        frame.setVisible(true);
        chart.getXYPlot().getRenderer();
        chart.getXYPlot().setDomainZeroBaselineVisible(true);
        chart.getXYPlot().setRangeZeroBaselineVisible(true);
        //chart.getXYPlot().setRenderer(new XYSplineRenderer(100));
        NumberAxis yaxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        //yaxis.setRange(ymin, ymax);
        yaxis.setAutoRangeIncludesZero(true);
        //yaxis.setFixedAutoRange(100);
        //yaxis.setAutoRangeIncludesZero(true);
        yaxis.setAutoRangeStickyZero(true);

        //yaxis.setAutoRange(true);

        //yaxis.setFixedAutoRange(100.0);


        XYItemRenderer renderer = chart.getXYPlot().getRenderer();
        double size = 2;
        double delta = size / 2.0;
        Shape shape1 = new Rectangle2D.Double(-delta, -delta, size, size);
        renderer.setSeriesShape(0, shape1);
    }

}
