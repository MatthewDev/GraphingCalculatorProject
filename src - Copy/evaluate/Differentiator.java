package evaluators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by Matthew on 9/18/2017.
 */
public class Differentiator extends Evaluator{

    public Differentiator(String expression, String variable) {
        super(expression, variable);
    }

    //must fix to have proper step size
    @Override
    public TreeMap<Double, Double> eval(double start, double end, int steps) {
        int fxSteps = steps*2;
        double fxStep = (end-start)/fxSteps;
        TreeMap<Double, Double> fx = super.eval(start-fxStep, end+fxStep, fxSteps);
        TreeMap<Double, Double> dfx = eval(fx);
        return dfx;
    }
    @Override
    public TreeMap<Double, Double> adaptiveEval(double start, double end, int steps) {
        int fxSteps = steps*2;
        double fxStep = (end-start)/fxSteps;
        TreeMap<Double, Double> fx = super.adaptiveEval(start-fxStep, end+fxStep, fxSteps);
        TreeMap<Double, Double> dfx = eval(fx);
        return dfx;
    }
    private TreeMap<Double, Double> eval(TreeMap<Double, Double> fx) {
        //System.out.println(fx.values());
        TreeMap<Double, Double> dfx = new TreeMap<>();
        Double[] x = fx.keySet().toArray(new Double[fx.size()]);
        Arrays.sort(x);
        for(int i = 0; i < x.length-1; i++) {
            double y1 = fx.get(x[i]);
            double y2 = fx.get(x[i+1]);
            double dy = (y2-y1)/(x[i+1]-x[i]);

            double dx = (x[i]+x[i+1])/2;

            dfx.put(dx, dy);
        }
        return dfx;
    }
}
