package view;


import Structure.Pair;
import evaluators.Differentiator;
import evaluators.Evaluator;
import evaluators.ExpressionEvaluator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.TreeMap;

import static evaluators.Evaluator.DEFAULT_STEP_SIZE;


public class Main {
    public static void main(String[] args) {
        Evaluator diff = new Differentiator(new ExpressionEvaluator("2.7^x", "x"));
        //System.out.println(diff.eval(5d));
        try1(diff, -15, 1000,-10, 20, DEFAULT_STEP_SIZE);
    }


    public static void try1(Evaluator eval, double ymin, double ymax, double start, double end, double stepSize) {
        TreeMap<Double, Double> functionTable = eval.adaptiveEval(start, end, stepSize);

        //PercentScaler scaler = new PercentScaler(0.99);
        //Scale s = scaler.getScaleRange(functionTable);
        //System.out.println(s.min +" "+s.max);

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
        FunctionFrame frame = new FunctionFrame(chart, new Pair<>(ymin, ymax));
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
