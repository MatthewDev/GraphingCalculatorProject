package view;

import Structure.AbstractSyntaxTree.AbstractSyntaxTree;
import Structure.Pair;
import Structure.PointType.Discontinuity;
import controller.EquationListener;
import evaluators.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import solve.RationalExprUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * Created by Matthew on 9/26/2017.
 */
public class FunctionFrame extends JFrame {
    private JFreeChart chart;
    private FunctionPanel graph;
    public FunctionFrame(JFreeChart chart, Pair<Double, Double> range) {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.chart = chart;

        graph = new FunctionPanel(chart, range);
        this.getContentPane().add(graph);


        JPanel sideBar = new JPanel(new BorderLayout());

        JTextField input1 = new JTextField();
        input1.setPreferredSize(new Dimension(500, 20));
        input1.addActionListener(new EquationListener(this));
        sideBar.add(input1, BorderLayout.LINE_START);
        this.getContentPane().add(sideBar, BorderLayout.LINE_START);
    }

    private void test(String function) {
        AbstractSyntaxTree AST = new AbstractSyntaxTree(function, "x");
        System.out.println(AST.eval(1)+"AST");



        /*PercentScaler scaler = new PercentScaler(0.99);
        Scale s = scaler.getScaleRange(functionTable);
        System.out.println(s.max);*/


        //test
        //InfixToPostfix postfix = new InfixToPostfix(function, "x");
        //Denominator d = new Denominator(postfix.getPostfix(), "x");
        //d.getPostfix()
        //end test
    }
    public void setFunction(String function) {
        //Evaluator fx = new ExpressionEvaluator(function, "x");

        AbstractSyntaxTree ast = new AbstractSyntaxTree(function, "x");
        Evaluator fx = new ASTEvaluator(ast);

        //test
        RationalExprUtilities.discontinuities(ast, -10,10);
        test(function);

        TreeMap<Double, Double> functionTable = fx.eval(-10 , 10, 0.001);

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries xyseries = new XYSeries("");

        Map<Double, Discontinuity> discontinuities = RationalExprUtilities.discontinuities(ast, -10, 10);


        GraphRenderer renderer = new GraphRenderer(graph);

        for(Map.Entry<Double, Discontinuity> discontinuity : discontinuities.entrySet()) {
            double x = discontinuity.getKey();
            Discontinuity type = discontinuity.getValue();
            if(type == Discontinuity.Hole) {
                double y = RationalExprUtilities.holeEval(fx, x);
                functionTable.put(x, y);
                renderer.addLabel(x, y, "removable discontinuity");
            }
            if(type == Discontinuity.Asymptote) {
                functionTable.put(x, null);
            }
        }


        for(Double key : functionTable.navigableKeySet()) {
            Double val = functionTable.get(key);
            if(val != null && !Double.isFinite(val)) val=null;
            xyseries.add(key, val);
            //System.out.println("("+key+", "+val+")");
        }

        dataset.addSeries(xyseries);
        JFreeChart chart = ChartFactory.createXYLineChart(
                null, null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
        //chart = ChartFactory.createScatterPlot("","","",dataset);
        //XYSplineRenderer renderer = new XYSplineRenderer();
        chart.getXYPlot().setRenderer(renderer);
        double size = 0;
        double delta = size / 2.0;
        Shape shape1 = new Rectangle2D.Double(-delta, -delta, size, size);
        renderer.setSeriesShape(0, shape1);


        //label test
        renderer.setBaseItemLabelsVisible(true);


        this.chart = chart;


        //Scale scale = new PercentScaler(0.99).getScaleRange(functionTable);

        this.getContentPane().remove(graph);
        //graph = new FunctionPanel(chart, scale.min, scale.max);
        graph = new FunctionPanel(chart);

        this.getContentPane().add(graph);
        //chart.getXYPlot().setDataset(dataset);

        this.revalidate();
        this.repaint();

        /*
        //more tests
        Differentiator dfxdx = new Differentiator(fx);
        Set<Double> relativeExtrema = CrossValue.equalXes(dfxdx,-10,10,0);
        for(double extrema : relativeExtrema) {
            double x = extrema;
            double y = fx.eval(extrema);
            xyseries.add(x,y);
            renderer.addSpecialPoint(x,y, ShapeUtilities.createDiamond(5));
            System.out.println(extrema + " " + extremaType(fx, extrema));
        }*/

    }

}
