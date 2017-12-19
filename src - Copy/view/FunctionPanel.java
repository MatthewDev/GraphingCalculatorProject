package view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

/**
 * Created by Matthew on 9/29/2017.
 */
public class FunctionPanel extends ChartPanel {
    private double ymin, ymax;

    public FunctionPanel(JFreeChart chart) {
        super(chart);
        restoreAutoRangeBounds();
    }
    @Override
    public void restoreAutoRangeBounds() {
        XYPlot plot = this.getChart().getXYPlot();

        plot.getDataset().getYValue(0,0);

        if(plot == null) return;
        boolean savedNotify = plot.isNotify();
        plot.setNotify(false);
        plot.getRangeAxis().setLowerBound(-10);
        plot.getRangeAxis().setUpperBound(10);
        plot.setNotify(savedNotify);
    }
    /*@Override
    public void restoreAutoBounds(){
        XYPlot plot = this.getChart().getXYPlot();
        if(plot == null) return;
        boolean savedNotify = plot.isNotify();
        plot.setNotify(false);
        this.restoreAutoDomainBounds();
        this.restoreAutoRangeBounds();
        //plot.setRangeAxis(null);
        ValueAxis va;
        plot.getRangeAxis().setLowerBound(-10);
        plot.getRangeAxis().setUpperBound(10);

        plot.setNotify(savedNotify);

        System.out.println(plot.getRangeAxis().getRange().getLowerBound());

    }*/
}
