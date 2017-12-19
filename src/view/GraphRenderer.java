package view;

import Structure.ComparablePair;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Matthew on 11/13/2017.
 */
//public class GraphRenderer extends XYSplineRenderer {
public class GraphRenderer extends XYLineAndShapeRenderer{
    private FunctionPanel chartPanel;


    public GraphRenderer(FunctionPanel chartPanel) {
        super();
        this.chartPanel = chartPanel;

        this.setBaseShapesVisible(false);
        this.setBaseItemLabelsVisible(true);
    }



    private Map<ComparablePair<Double, Double>, String> labels = new HashMap<>();

    public void addLabel(double x, double y, String label) {
        ComparablePair<Double, Double> point = new ComparablePair<>(x, y);
        labels.put(point, label);

        XYSeries fx = ((XYSeriesCollection) this.getPlot().getDataset()).getSeries(0);
        fx.addOrUpdate(x, y);
    }

    @Override
    protected void drawItemLabel(Graphics2D g2, PlotOrientation orientation, XYDataset dataset, int series, int item, double x, double y, boolean negative) {
        double xVal = dataset.getXValue(series, item);
        double yVal = dataset.getYValue(series, item);
        ComparablePair<Double, Double> point = new ComparablePair<>(xVal, yVal);

        if(labels.containsKey(point)){
            JTextArea label = createLabel(xVal, yVal, labels.get(point) );
            drawJComponent(g2, label, ceil(x), ceil(y) );
        }

    }
    private static final Border border = BorderFactory.createLineBorder(Color.BLACK, 1, true);
    private JTextArea createLabel(double x, double y, String description) {
        JTextArea label = new JTextArea();

        label.setFont(this.chartPanel.getFont());

        //25% gray background
        final double opacity = 0.25;
        Color gray = Color.GRAY;
        Color lowOpacityGray = new Color(gray.getRed(), gray.getGreen(), gray.getBlue(), (int)(255*opacity));
        label.setBackground(lowOpacityGray);

        label.setBorder(border);

        //display point with 4 decimal places of accuracy and description
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        label.append("("+decimalFormat.format(x)+", "+decimalFormat.format(y)+")");
        label.append("\n");
        label.append(description);

        return label;
    }

    private void drawJComponent(Graphics2D g, JComponent component, int x, int y) {
        Dimension d = component.getPreferredSize();
        component.setSize(d);
        chartPanel.add(component);
        CellRendererPane intermediatePane = new CellRendererPane();
        SwingUtilities.paintComponent(g, component, intermediatePane, x, y, ceil(d.getWidth()), ceil(d.getHeight()) );
    }


    private int ceil(double d) {
        return (int)Math.ceil(d);
    }
}
