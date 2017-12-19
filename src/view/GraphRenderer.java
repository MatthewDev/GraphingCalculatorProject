package view;

import org.jfree.util.ShapeUtilities;
import structure.ComparablePair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import structure.pointtypes.PointLabel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew on 11/13/2017.
 */
//public class GraphRenderer extends XYSplineRenderer {
public class GraphRenderer extends XYLineAndShapeRenderer{
    private FunctionPanel chartPanel;


    public GraphRenderer(FunctionPanel chartPanel) {
        super();
        this.chartPanel = chartPanel;


        this.setUseFillPaint(true);
        //this.setBaseShapesVisible(false);
        //this.setBaseItemLabelsVisible(true);
    }



    private Map<ComparablePair<Double, Double>, PointLabel> specialPoints = new HashMap<>();

    public void addSpecialPoint(double x, double y, PointLabel label) {
        ComparablePair<Double, Double> point = new ComparablePair<>(x, y);
        specialPoints.put(point, label);

        XYSeries fx = ((XYSeriesCollection) this.getPlot().getDataset()).getSeries(0);
        fx.addOrUpdate(x, y);
    }
    public static final int SHAPE_SIZE = 4;
    public static final Shape HOLE_SHAPE = createCircle(SHAPE_SIZE),
            POI_SHAPE = ShapeUtilities.createDiagonalCross(SHAPE_SIZE/2, SHAPE_SIZE/2),
            MIN_SHAPE = ShapeUtilities.createDownTriangle(SHAPE_SIZE),
            MAX_SHAPE = ShapeUtilities.createUpTriangle(SHAPE_SIZE),
            NO_SHAPE = createCircle(0);

    @Override
    public boolean getItemShapeVisible(int row, int column){
        XYSeriesCollection dataset = (XYSeriesCollection) this.chartPanel.getChart().getXYPlot().getDataset();
        double xVal = dataset.getXValue(row, column);
        double yVal = dataset.getYValue(row, column);
        ComparablePair<Double, Double> point = new ComparablePair<>(xVal, yVal);

        if(specialPoints.containsKey(point)) return true;

        return false;
    }
    @Override
    public Shape getItemShape(int row, int column) {
        XYSeriesCollection dataset = (XYSeriesCollection) this.chartPanel.getChart().getXYPlot().getDataset();
        double xVal = dataset.getXValue(row, column);
        double yVal = dataset.getYValue(row, column);
        ComparablePair<Double, Double> point = new ComparablePair<>(xVal, yVal);

        if(!specialPoints.containsKey(point)) return NO_SHAPE;

        switch(specialPoints.get(point)) {
            case HOLE:
                return HOLE_SHAPE;
            case POI:
                return POI_SHAPE;
            case MIN :
                return MIN_SHAPE;
            case MAX:
                return MAX_SHAPE;
        }

        return NO_SHAPE;
    }

    @Override
    public boolean getItemShapeFilled(int row, int column) {
        XYSeriesCollection dataset = (XYSeriesCollection) this.chartPanel.getChart().getXYPlot().getDataset();
        double xVal = dataset.getXValue(row, column);
        double yVal = dataset.getYValue(row, column);
        ComparablePair<Double, Double> point = new ComparablePair<>(xVal, yVal);

        if(!specialPoints.containsKey(point)) return this.getBaseShapesFilled();

        switch(specialPoints.get(point)) {
            case HOLE:
                return true;
        }

        return this.getBaseShapesFilled();
    }

    public static final Color HOLE_FILL_COLOR = Color.WHITE;

    @Override
    public Paint getItemFillPaint(int row, int column) {
        XYSeriesCollection dataset = (XYSeriesCollection) this.chartPanel.getChart().getXYPlot().getDataset();
        double xVal = dataset.getXValue(row, column);
        double yVal = dataset.getYValue(row, column);
        ComparablePair<Double, Double> point = new ComparablePair<>(xVal, yVal);

        Paint normalPaint = this.getSeriesPaint(row);

        if(!specialPoints.containsKey(point)) return normalPaint;

        switch(specialPoints.get(point)) {
            case MIN:
                break;
            case MAX:
                break;
            case HOLE:
                return HOLE_FILL_COLOR;
            case POI:
                break;
            case NONE:
                break;
        }
        return normalPaint;
    }






    private static Shape createCircle(float radius) {
        return new Ellipse2D.Double(-radius, -radius, radius*2, radius*2);
    }

    @Override
    protected void drawItemLabel(Graphics2D g2, PlotOrientation orientation, XYDataset dataset, int series, int item, double x, double y, boolean negative) {
        double xVal = dataset.getXValue(series, item);
        double yVal = dataset.getYValue(series, item);
        ComparablePair<Double, Double> point = new ComparablePair<>(xVal, yVal);

        if(specialPoints.containsKey(point)){
            JTextArea label = createLabel(xVal, yVal, specialPoints.get(point).toString() );
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
