package model;

import structure.abstractsyntaxtree.AbstractSyntaxTree;
import structure.Pair;
import structure.pointtypes.Discontinuity;
import structure.pointtypes.RelativeExtrema;
import evaluators.ASTEvaluator;
import evaluators.Differentiator;
import evaluators.Evaluator;
import org.jfree.data.xy.XYSeries;
import solve.POIFinder;
import solve.RationalExprUtilities;
import solve.RelativeExtremaFinder;
import view.scale.PercentScaler;
import view.scale.RoundedPercentScaler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Matthew on 12/14/2017.
 */
public class GraphModel {
    //inputs
    private String expression, variableName;
    private Pair<Double, Double> domain, range;
    private boolean showHoles, showExtrema, showPOI;
    private boolean showf1x, showf2x;


    //internal state
    private AutoRange autoRange = new AutoRange(1.0d);
    private AbstractSyntaxTree ast;
    private Evaluator fx;
    private Evaluator f1x;
    private Evaluator f2x;

    private Map<Pair<Double, Double>, String> pointDescription = new HashMap<>();

    private FTCModel ftcModel;

    //outputs
    private XYSeries fxOutput;
    private XYSeries f1xOutput;
    private XYSeries f2xOutput;


    private GraphModel(String expression, String variableName, Pair<Double, Double> domain, Pair<Double, Double> range) {
        this.variableName = variableName;
        this.ftcModel = new FTCModel(expression, variableName, new Pair<Double, Double>(0d,0d));


        setDomain(domain);
        setExpression(expression);
        setRange(range);

    }


    public GraphModel(String expression, Pair<Double, Double> domain, Pair<Double, Double> range) {
        this(expression, "x", domain, range);
    }

    public String getExpression() {
        return expression;
    }


    public Map<Pair<Double, Double>, String> getPointDescription() {
        pointDescription = new HashMap<>();

        if(showHoles) {
            Map<Double, Discontinuity> discontinuityMap =  RationalExprUtilities.discontinuities(ast, domain.a, domain.b);
            for(double x : discontinuityMap.keySet()) {
                Discontinuity discontinuity = discontinuityMap.get(x);
                if(discontinuity == Discontinuity.Hole) {
                    double y = RationalExprUtilities.holeEval(fx, x);
                    fxOutput.addOrUpdate(x, y);
                    appendPointDescription(new Pair<>(x, y), discontinuity.toString());
                }
            }
        }
        if(showExtrema) {
            Map<Double, RelativeExtrema> relativeExtremaMap =  RelativeExtremaFinder.find(fx, domain);
            for(double x : relativeExtremaMap.keySet()) {
                RelativeExtrema extrema = relativeExtremaMap.get(x);
                double y = fx.eval(x);
                fxOutput.addOrUpdate(x, y);
                appendPointDescription(new Pair<>(x, y), extrema.toString());
            }
            //System.out.println(expression+" extrema "+relativeExtremaMap);
        }
        if(showPOI) {
            Set<Pair<Double, Double>> POImap =  POIFinder.find(fx, domain);
            for(Pair<Double, Double> point : POImap) {
                fxOutput.addOrUpdate(point.a, point.b);
                appendPointDescription(point, "point of inflection");
            }

        }

        return pointDescription;
    }

    private void appendPointDescription(Pair<Double, Double> point, String description) {
        if(!pointDescription.containsKey(point)) {
            pointDescription.put(point, description);
        } else {
            StringBuilder descriptionBuilder = new StringBuilder(pointDescription.get(point));
            descriptionBuilder.append(", ");
            descriptionBuilder.append(description);

            pointDescription.put(point, descriptionBuilder.toString());
        }
    }

    public boolean showHoles() {
        return showHoles;
    }

    public void showHoles(boolean showHoles) {
        this.showHoles = showHoles;
    }

    public boolean showExtrema() {
        return showExtrema;
    }

    public void showExtrema(boolean showExtrema) {
        this.showExtrema = showExtrema;
    }

    public boolean showPOI() {
        return showPOI;
    }

    public void showPOI(boolean showPOI) {
        this.showPOI = showPOI;
    }

    /**
     * updates auto range if it's enabled
     */
    private void updateAutoRange() {
        if(fx == null) return;
        if(!autoRange.isEnabled) return;

        range = autoRange.getRange(fx.eval(domain.a, domain.b));
    }
    public void setAutoRangeEnabled(boolean enabled) {
        autoRange.isEnabled = enabled;
        updateAutoRange();
    }
    public void setAutoRange(double rangeCoverage) {
        autoRange.setCoverageDecimal(rangeCoverage);
        autoRange.isEnabled = true;
        updateAutoRange();
    }
    public boolean isAutoRangeEnabled() {
        return autoRange.isEnabled;
    }
    public double getAutoRangeCoverage() {
        return autoRange.getCoverageDecimal();
    }

    public Pair<Double, Double> getDomain() {
        return domain;
    }

    public void setDomain(Pair<Double, Double> domain) {
        this.domain = domain;

        updateFunction();
        updateAutoRange();
    }

    public void setExpression(String expression) {
        this.expression = expression;
        ftcModel.setExpression(expression);

        updateFunction();
        updateAutoRange();
    }

    public void setAB (Pair<Double, Double> ab) {
        ftcModel.setAB(ab);
    }
    public Pair<Double, Double> getAB () {
        return ftcModel.getAB();
    }


    public XYSeries getFTCArea () {
        return ftcModel.getDerivativeOutput();
    }
    public double integraldfx() {
        return ftcModel.getIntegral();
    }
    public double evalfx(double x) {
        return fx.eval(x);
    }

    /**
     * sets visible range, doesn't affect function output
     * @param range
     */
    public void setRange(Pair<Double, Double> range) {
        this.range = range;

        autoRange.isEnabled = false;
    }

    public Pair<Double, Double> getRange() {
        //System.out.println(range);
        return range;
    }

    public XYSeries getFxOutput() {
        return fxOutput;
    }

    public void showf1x(boolean showf1x) {
        this.showf1x = showf1x;
    }
    public void showf2x(boolean showf2x) {
        this.showf2x = showf2x;
    }

    public XYSeries getF1xOutput() {
        f1x = new Differentiator(fx);
        f1xOutput = new XYSeries("f'(x)", true,false);

        if(!showf1x) return f1xOutput;

        Map<Double, Double> functionTable = f1x.eval(domain.a, domain.b);


        for(Map.Entry<Double, Double> point : functionTable.entrySet()) {
            Double x = point.getKey();
            Double y = point.getValue();
            if(y == null) continue;
            if(y < range.a || y > range.b) y = Double.NaN;
            f1xOutput.add(x, y);
            //System.out.println(x+" "+y);
        }

        return f1xOutput;
    }
    public XYSeries getF2xOutput() {
        f1x = new Differentiator(fx);
        f2x = new Differentiator(f1x);
        f2xOutput = new XYSeries("f''(x)", true, false);

        if(!showf2x) return f2xOutput;

        Map<Double, Double> functionTable = f2x.eval(domain.a, domain.b);


        for(Map.Entry<Double, Double> point : functionTable.entrySet()) {
            Double x = point.getKey();
            Double y = point.getValue();
            if(y == null) continue;
            if(y < range.a || y > range.b) y = Double.NaN;
            f2xOutput.add(x, y);
            //System.out.println(x+" "+y);
        }

        return f2xOutput;
    }


    private void updateFunction() {
        if(expression == null || variableName == null || domain == null) return;

        ast = new AbstractSyntaxTree(expression, variableName);
        fx = new ASTEvaluator(ast);

        //System.out.println(expression);
        //System.out.println(fx.eval(10));

        fxOutput = new XYSeries("f(x)", true, false);

        Map<Double, Double> functionTable = fx.eval(domain.a, domain.b);
        Map<Double, Discontinuity> discontinuities = RationalExprUtilities.discontinuities(ast, domain.a, domain.b);

        for(Map.Entry<Double, Discontinuity> discontinuity : discontinuities.entrySet()) {
            double x = discontinuity.getKey();
            Discontinuity type = discontinuity.getValue();
            if(type == Discontinuity.Hole) {
                double y = RationalExprUtilities.holeEval(fx, x);
                functionTable.put(x, y);
            }
            if(type == Discontinuity.Asymptote) {
                fxOutput.add(x, Double.NaN);
            }
        }

        for(Map.Entry<Double, Double> point : functionTable.entrySet()) {
            Double x = point.getKey();
            Double y = point.getValue();
            if(y != null && !Double.isFinite(y)) continue; //don't insert NaN because jfree sucks and it gets really slow for whatever reason
            fxOutput.add(x, y);
            //System.out.println(x+" "+y);
        }
    }

    private class AutoRange {
        private double coverageDecimal;
        private PercentScaler scaler;

        public boolean isEnabled; //not enforced, used for flag outside

        public AutoRange(double coverageDecimal) {
            this.coverageDecimal = coverageDecimal;
            scaler = new RoundedPercentScaler(coverageDecimal);
            isEnabled = true;
        }

        public double getCoverageDecimal() {
            return coverageDecimal;
        }

        public void setCoverageDecimal(double coverageDecimal) {
            this.coverageDecimal = coverageDecimal;
            scaler.setScale(coverageDecimal);
        }
        public Pair<Double, Double> getRange(Map<Double, Double> functionTable) {
            return scaler.getScaleRange(functionTable);
        }
    }
}
