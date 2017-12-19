package model;

import Structure.AbstractSyntaxTree.AbstractSyntaxTree;
import Structure.Pair;
import evaluators.*;
import org.jfree.data.xy.XYSeries;

import java.util.Map;

/**
 * Created by Matthew on 12/17/2017.
 */
class FTCModel {
    private Pair<Double, Double> ab;
    private String expression, variableName = "x";

    Evaluator ddxfx;

    //outputs
    private XYSeries derivativeOutput;


    public FTCModel (String expression, String variableName, Pair <Double, Double> ab) {
        this.variableName = variableName;
        this.expression = expression;
        this.ab = ab;

        AbstractSyntaxTree ast = new AbstractSyntaxTree(expression, variableName);
        Evaluator fx = new ASTEvaluator(ast);
        ddxfx = new Differentiator(fx);
    }

    public void setAB(Pair<Double, Double> ab) {
        this.ab = ab;

        updateFunction();
    }

    public void setExpression(String expression) {
        this.expression = expression;

        updateFunction();
    }
    public Pair<Double, Double> getAB() {
        return ab;
    }


    public double getIntegral(){
        Evaluator integral = new Integrator(ddxfx, ab.a);
        return integral.eval(ab.b);
    }
    public XYSeries getDerivativeOutput() {
        return derivativeOutput;
    }


    private void updateFunction() {
        if(expression == null || variableName == null || ab == null) return;

        AbstractSyntaxTree ast = new AbstractSyntaxTree(expression, variableName);
        Evaluator fx = new ASTEvaluator(ast);
        ddxfx = new Differentiator(fx);

        //System.out.println(expression);
        //System.out.println(fx.eval(10));

        derivativeOutput = new XYSeries("f'(x)");

        Map<Double, Double> functionTable = ddxfx.eval(ab.a, ab.b);


        for(Map.Entry<Double, Double> point : functionTable.entrySet()) {
            Double x = point.getKey();
            Double y = point.getValue();
            if(y != null && !Double.isFinite(y)) y = Double.NaN;
            derivativeOutput.add(x, y);
            //System.out.println(x+" "+y);
        }
    }

}
