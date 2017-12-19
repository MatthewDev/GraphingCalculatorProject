package evaluators;

import java.util.TreeMap;

/**
 * Created by Matthew on 9/18/2017.
 */
public interface Evaluator {
    double DEFAULT_STEP_SIZE = 0.0001d;


    double eval(double x);

    TreeMap<Double, Double> eval(double start, double end, double stepSize) ;

    default TreeMap<Double, Double> eval(double start, double end) {
        return eval(start, end, DEFAULT_STEP_SIZE);
    }


    @Deprecated
    default TreeMap<Double, Double> adaptiveEval(double start, double end, double stepSize) {
        return eval(start, end, stepSize);
    }

}
