package view;

import Structure.ComparablePair;
import Structure.Pair;
import controller.*;
import model.GraphModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * Created by Matthew on 12/13/2017.
 */
public class Window {
    private JPanel sideBar;
    private JPanel equationContainer;
    private JTextField domainLower;
    private JTextField domainUpper;
    private JTextField expression;
    private JPanel domainContainer;
    private JCheckBox showExtrema;
    private JCheckBox showPOIs;
    private JCheckBox showHoles;
    private JPanel pointLabelContainer;
    private JPanel functionContainer;
    private JPanel container;
    private JPanel rangeContainer;
    private JTextField rangeLower;
    private JTextField rangeUpper;
    private JPanel autoRangeContainer;
    private JPanel textContainer;
    private JTextField autoRangeCoverage;
    private JCheckBox autoRangeCheckBox;
    private JPanel FTCPanel;
    private JPanel FTCContainer;
    private JTextField bField;
    private JTextField aField;
    private JPanel abContainer;
    private JCheckBox showf1x;
    private JPanel fbfaContainer;
    private JPanel integralContainer;
    private JCheckBox showf2x;
    private JTable POITable;
    private JScrollPane POITableContainer;

    private FunctionPanel functionPanel;
    private GraphRenderer graphRenderer;

    private GraphModel model;

    private final String columnNames[] = {"Point", "Type"};


    private static DecimalFormat percentFormat = new DecimalFormat("0.#");
    private static DecimalFormat coordinateFormat = new DecimalFormat("0.####");

    private final String FTCLatex = "\\int_{a}^{b} f'(x)dx = f(b) - f(a)";
    private final String fafbLatexBase = "f(b) - f(a) = ";
    private final String integralLatexBase = "\\int_{a}^{b} f'(x)dx = ";


    public Window() {
        //set default function to graph
        model = new GraphModel("x", new Pair<Double, Double>(-10.0, 10.0), new Pair<Double, Double>(-10.0, 10.0));

        updateAll();


        //action listeners
        expression.addActionListener(new ExpressionListener(model, this));

        domainLower.addActionListener(new DomainListener(model, this));
        domainUpper.addActionListener(new DomainListener(model, this));

        rangeLower.addActionListener(new RangeListener(model, this));
        rangeUpper.addActionListener(new RangeListener(model, this));

        autoRangeCheckBox.addActionListener(new AutoRangeEnabledListener(model, this));
        autoRangeCoverage.addActionListener(new AutoRangeListener(model, this));


        showExtrema.setActionCommand(PointOfInterestListener.RELATIVE_EXTREMA);
        showExtrema.addActionListener(new PointOfInterestListener(model, this));
        showHoles.setActionCommand(PointOfInterestListener.HOLE);
        showHoles.addActionListener  (new PointOfInterestListener(model, this));
        showPOIs.setActionCommand(PointOfInterestListener.POINT_OF_INFLECTION);
        showPOIs.addActionListener   (new PointOfInterestListener(model, this));


        showf1x.setActionCommand(DerivativeListener.FIRST_DERIVATIVE);
        showf1x.addActionListener(new DerivativeListener(model, this));
        showf2x.setActionCommand(DerivativeListener.SECOND_DERIVATIVE);
        showf2x.addActionListener(new DerivativeListener(model, this));


        aField.addActionListener(new ABListener(model, this));
        bField.addActionListener(new ABListener(model, this));

        FTCPanel.add(buildLatexLabel(FTCLatex));

    }
    private static JLabel buildLatexLabel(String latex) {
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20).build();

        return new JLabel(icon);
    }


    public Pair<Double, Double> getRange() {
        double lower = Double.parseDouble(rangeLower.getText());
        double upper = Double.parseDouble(rangeUpper.getText());
        return new Pair<>(lower, upper);
    }

    public Pair<Double, Double> getDomain() {
        double lower = Double.parseDouble(domainLower.getText());
        double upper = Double.parseDouble(domainUpper.getText());
        return new Pair<>(lower, upper);
    }
    public Pair<Double, Double> getAB() {
        double a = Double.parseDouble(aField.getText());
        double b = Double.parseDouble(bField.getText());
        return new Pair<>(a, b);
    }


    private JFreeChart createChart(XYSeries series) {
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        //fixDiscontinuities(dataset);

        JFreeChart chart = ChartFactory.createXYLineChart(null, null, null, dataset);

        return chart;
    }


    public void drawGraph() {
        JFreeChart chart = createChart(model.getFxOutput());

        functionContainer.removeAll();

        functionPanel = new FunctionPanel(chart, model.getRange());
        functionContainer.add(functionPanel);

        graphRenderer = new GraphRenderer(functionPanel);
        functionPanel.getChart().getXYPlot().setRenderer(graphRenderer);

        //unnecessary see jdoc
        //colorFunctions();


        container.revalidate();
        container.repaint();
    }

    static final int FX_DATASET_INDEX = 0, F1X_DATASET_INDEX = 1, F2X_DATASET_INDEX = 2, FTC_DATASET_INDEX = 3;
    static final Color FX_COLOR = Color.RED, FTC_COLOR = Color.BLUE, F1X_COLOR = Color.BLUE, F2X_COLOR = Color.GREEN;
    public void updateFTCVisual() {
        XYSeriesCollection dataset = new XYSeriesCollection(model.getFTCArea());


        functionPanel.getChart().getXYPlot().setDataset(FTC_DATASET_INDEX, dataset);

        functionPanel.getChart().getXYPlot().setRenderer(FTC_DATASET_INDEX, new XYAreaRenderer());
        functionPanel.getChart().getXYPlot().getRenderer(FTC_DATASET_INDEX).setSeriesPaint(0, FTC_COLOR);


        container.revalidate();
        container.repaint();
    }
    public void drawf1x() {
        XYSeriesCollection dataset = new XYSeriesCollection(model.getF1xOutput());

        functionPanel.getChart().getXYPlot().setDataset(F1X_DATASET_INDEX, dataset);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, F1X_COLOR);
        renderer.setBaseShapesVisible(false);
        functionPanel.getChart().getXYPlot().setRenderer(F1X_DATASET_INDEX, renderer);

        container.revalidate();
        container.repaint();
    }
    public void drawf2x() {
        XYSeriesCollection dataset = new XYSeriesCollection(model.getF2xOutput());

        functionPanel.getChart().getXYPlot().setDataset(F2X_DATASET_INDEX, dataset);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, F2X_COLOR);
        renderer.setBaseShapesVisible(false);
        functionPanel.getChart().getXYPlot().setRenderer(F2X_DATASET_INDEX, renderer);

        container.revalidate();
        container.repaint();
    }



    private void updateFTCText () {
        double a = model.getAB().a;
        double b = model.getAB().b;

        double fa = model.evalfx(a);
        double fb = model.evalfx(b);
        double fbfa = fb-fa;

        double integral = model.integraldfx();

        String fbfaLatex = fafbLatexBase+coordinateFormat.format(fb)+" - "+coordinateFormat.format(fa)+" = "+coordinateFormat.format(fbfa);
        String integralLatex = integralLatexBase+coordinateFormat.format(integral);

        fbfaContainer.removeAll();
        integralContainer.removeAll();

        fbfaContainer.add(buildLatexLabel(fbfaLatex));
        integralContainer.add(buildLatexLabel(integralLatex));

    }

    public void updateFields() {
        expression.setText(model.getExpression());

        domainLower.setText(coordinateFormat.format(model.getDomain().a));
        domainUpper.setText(coordinateFormat.format(model.getDomain().b));

        rangeLower.setText(coordinateFormat.format(model.getRange().a));
        rangeUpper.setText(coordinateFormat.format(model.getRange().b));

        autoRangeCheckBox.setSelected(model.isAutoRangeEnabled());
        autoRangeCoverage.setEnabled(model.isAutoRangeEnabled());

        double coverageAsPercent = model.getAutoRangeCoverage()*100;
        autoRangeCoverage.setText(percentFormat.format(coverageAsPercent));

        double a = model.getAB().a;
        double b = model.getAB().b;
        aField.setText(coordinateFormat.format(a));
        bField.setText(coordinateFormat.format(b));

        container.revalidate();
        container.repaint();
    }

    /**
     * only necessary if dataset contains multiple series, which only occurs when fixDiscontinuities is used, which is only necessary when XYSPlineRenderer is used
     * (which it is not currently used)
     */
    private void colorFunctions() {
        final Color[] color = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW};

        XYPlot plot = functionPanel.getChart().getXYPlot();
        XYSeriesCollection dataset = (XYSeriesCollection) plot.getDataset();
        XYItemRenderer renderer = plot.getRenderer();

        Map<String, Color> functionToColor = new HashMap<>();
        Map<Comparable, String> keyToFunction = new HashMap<>();

        int curColor = 0;
        List<XYSeries> seriesList = dataset.getSeries();
        for(XYSeries series : seriesList) {
            String function = series.getDescription();
            if(!functionToColor.containsKey(function)) {
                functionToColor.put(function, color[curColor]);
                curColor = (curColor + 1) % color.length;
            }
            keyToFunction.put(series.getKey(), function);
        }

        for(Comparable key : keyToFunction.keySet()) {
            Color lineColor = functionToColor.get(keyToFunction.get(key));
            renderer.setSeriesPaint(dataset.getSeriesIndex(key), lineColor);
        }
    }

    private void addPOI() {
        Map<Pair<Double, Double>, String> pointDescription = model.getPointDescription();

        for(Pair<Double, Double> point : pointDescription.keySet()) {
            final String description = pointDescription.get(point);

            graphRenderer.addLabel(point.a, point.b, description);
        }

    }

    public void updateAll() {
        updateFields();
        drawGraph();

        addPOI();
        updatePOTTable();

        drawf1x();
        drawf2x();

        updateFTCVisual();
        updateFTCText();

        container.repaint();

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Window");
        frame.setContentPane(new Window().container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    /**
     * only necessary for XYSplineRenderer
     */
    @Deprecated
    private void fixDiscontinuities(XYSeriesCollection dataset){
        List<XYSeries> seriesList = dataset.getSeries();
        List<XYSeries> splitSeriesList = new LinkedList<>();

        for(XYSeries series : seriesList) {
            splitSeriesList.addAll(splitSeries(series));
        }

        dataset.removeAllSeries();

        for(XYSeries series : splitSeriesList) {
            dataset.addSeries(series);
        }
    }



    /**
     * split series at all discontinuities (when y value is not finite)
     * @param series
     * @return
     */
    private List<XYSeries> splitSeries(XYSeries series) {
        List<XYSeries> seriesList = new LinkedList<>();
        List<XYDataItem> xyDataItems = series.getItems();

        int i = 0;
        String name = series.getKey().toString();

        XYSeries currentSeries = new XYSeries(name+i++);
        currentSeries.setDescription(name);

        for(XYDataItem dataItem : xyDataItems) {
            if(!Double.isFinite(dataItem.getYValue())) {
                seriesList.add(currentSeries);

                currentSeries = new XYSeries(name+i++);
                currentSeries.setDescription(name);
            } else {
                currentSeries.add(dataItem);
            }
        }

        if(!currentSeries.isEmpty()) {
            seriesList.add(currentSeries);
        }

        //System.out.println(currentSeries.getItems());
        //System.out.println(seriesList);
        return seriesList;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        //creates table backed by rowData and column name arrays
        POITable = new JTable();
        POITable.setAutoCreateRowSorter(true);
        POITable.setModel(new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
    }
    private void updatePOTTable() {
        Map<Pair<Double, Double>, String> pointDescription = model.getPointDescription();



        DefaultTableModel POITableModel = (DefaultTableModel) POITable.getModel();

        //remove all rows to begin refresh
        while(POITableModel.getRowCount() > 0) {
            POITableModel.removeRow(0);
        }

        //add new rows from the special poitns found
        for(Pair<Double, Double> point : pointDescription.keySet()) {
            String description = pointDescription.get(point);
            POITableModel.addRow(new Object[]{point, description});
        }

        POITableModel.fireTableDataChanged();

    }
}
