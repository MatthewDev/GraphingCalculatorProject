package view;

import evaluators.Evaluator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import view.scale.PercentScaler;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by Matthew on 9/26/2017.
 */
public class FunctionFrame extends JFrame {
    private JFreeChart chart;
    private FunctionPanel graph;
    public FunctionFrame(JFreeChart chart) {
        this(chart, "");
    }
    public FunctionFrame(JFreeChart chart, String title) {
        this(chart, title, EXIT_ON_CLOSE);
    }
    public FunctionFrame(JFreeChart chart, String title, int closeOperation) {
        super();

        this.chart = chart;
        this.setTitle(title);
        this.setDefaultCloseOperation(closeOperation);

        graph = new FunctionPanel(chart);
        this.getContentPane().add(graph);

        JPanel sideBar = new JPanel();
        JTextField input1 = new JTextField();
        input1.setPreferredSize(new Dimension(500, 20));
        input1.addActionListener(new EquationListener(this));
        sideBar.add(input1);
        this.getContentPane().add(sideBar, BorderLayout.LINE_START);
        //this.add(sideBar);
    }
    public void setFunction(String function) {
        Evaluator eval = new Evaluator(function, "x");
        TreeMap<Double, Double> functionTable = eval.adaptiveEval(-10 , 10, 1000);

        PercentScaler scaler = new PercentScaler(0.99);
        Scale s = scaler.getScaleRange(functionTable);

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries xyseries = new XYSeries("");

        Set<Double> breaks = findBreaks(functionTable);

        for(Double key : functionTable.navigableKeySet()) {
            Double val = functionTable.get(key);
            //System.out.println(key+", "+val);
            xyseries.add(key, val);
            if(breaks.contains(key)) xyseries.add(key, null);
        }

        dataset.addSeries(xyseries);
        JFreeChart chart = ChartFactory.createXYLineChart(
                null, null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
        //chart = ChartFactory.createScatterPlot("","","",dataset);
        this.chart = chart;
        this.getContentPane().remove(this.graph);
        graph = new FunctionPanel(chart);
        this.getContentPane().add(graph);
        //chart.getXYPlot().setDataset(dataset);
        this.revalidate();
        this.repaint();

    }
    public static Set<Double> findBreaks(TreeMap<Double, Double> functionTable) {
        Map.Entry<Double, Double> lastEntry = functionTable.firstEntry();
        Set<Double> breaks = new TreeSet<>();
        for(Map.Entry<Double, Double> entry : functionTable.entrySet()) {
            if(Math.abs(lastEntry.getValue() - entry.getValue()) > 10) breaks.add(lastEntry.getKey());
            lastEntry = entry;
            //System.out.println(key+", "+val);
        }
        return breaks;
    }

}
