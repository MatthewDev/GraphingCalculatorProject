package view;

import structure.Pair;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;

import java.awt.*;

/**
 * Created by Matthew on 9/29/2017.
 */
public class FunctionPanel extends ChartPanel {
    private Pair<Double, Double> range;

    public FunctionPanel(JFreeChart chart) {
        this(chart, new Pair<>(-10d, 10d));
    }

    public FunctionPanel(JFreeChart chart, Pair<Double, Double> range) {
        super(chart);
        this.range = range;
        restoreAutoRangeBounds();

        chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.BLACK);

        LegendItemCollection chartLegend = new LegendItemCollection();
        chartLegend.add(new LegendItem("f(x)", Window.FX_COLOR));
        chartLegend.add(new LegendItem("f'(x)", Window.F1X_COLOR));
        chartLegend.add(new LegendItem("f''(x)", Window.F2X_COLOR));

        chartLegend.add(new LegendItem("Relative Minimum", null, null, null, GraphRenderer.MIN_SHAPE, Window.FX_COLOR));
        chartLegend.add(new LegendItem("Relative Maximum", null, null, null, GraphRenderer.MAX_SHAPE, Window.FX_COLOR));
        chartLegend.add(new LegendItem("Point of Inflection", null, null, null, GraphRenderer.POI_SHAPE, Window.FX_COLOR));

        chartLegend.add(new LegendItem("Hole", null, null, null, GraphRenderer.HOLE_SHAPE, GraphRenderer.HOLE_FILL_COLOR, chart.getXYPlot().getRenderer().getBaseStroke(), Window.FX_COLOR));


        chart.getXYPlot().setFixedLegendItems(chartLegend);

        restoreAutoBounds();
    }


    public void setRange(Pair<Double, Double> range) {
        this.range = range;
        restoreAutoRangeBounds();
    }

    @Override
    public void restoreAutoRangeBounds() {
        if(this.getChart() == null) return;

        XYPlot plot = this.getChart().getXYPlot();

        if(plot == null) return;

        boolean savedNotify = plot.isNotify();
        plot.setNotify(false);

        plot.getRangeAxis().setLowerBound(range.a);
        plot.getRangeAxis().setUpperBound(range.b);

        plot.setNotify(savedNotify);
    }

    @Override
    public void restoreAutoDomainBounds() {
        if(this.getChart() == null) return;

        XYPlot plot = this.getChart().getXYPlot();

        if(plot == null) return;

        Range xAxis = plot.getDataRange(plot.getDomainAxis());
        if(xAxis == null) return;

        boolean savedNotify = plot.isNotify();
        plot.setNotify(false);

        plot.getDomainAxis().setLowerBound(xAxis.getLowerBound());
        plot.getDomainAxis().setUpperBound(xAxis.getUpperBound());

        plot.setNotify(savedNotify);
    }

}


