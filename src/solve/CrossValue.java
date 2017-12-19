package solve;

import Structure.ComparablePair;
import evaluators.Differentiator;
import evaluators.Evaluator;
import evaluators.ExpressionEvaluator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static solve.DoubleUtil.fuzzyEqual;
import static solve.DoubleUtil.isEqual;
import static solve.DoubleUtil.compare;

/**
 * Created by Matthew on 11/6/2017.
 */
public class CrossValue {
    public static final double EPSILON = 2.22e-16d;

    private ExpressionEvaluator expression;
    private double value;

    /**
     * finds all values such f(x)=value
     * @param fx
     * @param lowerBound
     * @param upperBound
     * @param value
     * @return
     */
    public static Set<Double> equalXes(Evaluator fx, double lowerBound, double upperBound, double value) {
        HashSet<Double> equalLocation = new HashSet<>();
        equalLocation.addAll(crossXes(fx, lowerBound, upperBound, value));

        Differentiator dfxdx = new Differentiator(fx);
        Set<Double> potenialTangents = crossXes(dfxdx, lowerBound, upperBound, 0);

        for(double potenialTangent : potenialTangents) {
            if(fuzzyEqual(fx.eval(potenialTangent), value)) equalLocation.add(potenialTangent);
        }

        return equalLocation;
    }




    /**
     * looks for when f(x) crosses value
     * potentially finds tangents, but it is not guaranteed
     * @param fx
     * @param lowerBound
     * @param upperBound
     * @param value
     * @return
     */
    private static Set<Double> crossXes(Evaluator fx, double lowerBound, double upperBound, double value) {
        /* fxDiscrete must exist such that for:
                x1 is any x
                x2 is the smallest x greater than x1
                c in (x1, x2)
                dx = x2-x1
            f(c) for c in (x1, x2) is monotonically increasing or monotonically decreasing
            this is made likely but not guaranteed by sufficiently small dx
            functions with an infinite number of f(x) = value are not supported (ex sin(1/x) = 1 on [-1,1])
            functions that otherwise require dx < epsilon are also not supported
         */
        TreeMap<Double, Double> fxDiscrete = fx.eval(lowerBound, upperBound, 0.01);

        Map.Entry<Double, Double> lastEntry = null;
        HashSet<ComparablePair<Double, Double>> crossInterval = new HashSet<>(); //for ComparablePair p in minMaxXCrossValue: f(p.a) < value < f(p.b)
        HashSet<Double> crossLocation = new HashSet<>(); //for x in crossX: f(x) approximates value well
        boolean added = false;
        for(Map.Entry<Double, Double> entry : fxDiscrete.entrySet()) {
            if(lastEntry != null) {
                double low = Math.min(entry.getValue(), lastEntry.getValue());
                double high = Math.max(entry.getValue(), lastEntry.getValue());

                double lowx  = entry.getValue() == low  ? entry.getKey() : lastEntry.getKey();
                double highx = entry.getValue() == high ? entry.getKey() : lastEntry.getKey();

                if(!added && (low<value && high>value) ) {
                    crossInterval.add(new ComparablePair<>(lowx, highx));
                    added = true;

                }else if(!added && isEqual(low, value)) {
                    crossLocation.add(lowx);
                    added = true;

                }else if(!added && isEqual(high, value)) {
                    crossLocation.add(highx);
                    added = true;
                }else{
                    added = false;
                }
            }
            lastEntry = entry;
        }

        for(ComparablePair<Double, Double> interval : crossInterval) {

            crossLocation.add(crossX(fx, interval.a, interval.b, value));
        }

        return crossLocation;
    }

    /***
     * f(x) must be monotonically increasing or monotonically decreasing for [xlow, xhigh] or [xhigh, xlow] respectively
     * f(x_fxlow) < value < f(x_fxhigh)
     *
     * @param x_fxLow
     * @param x_fxHigh
     * @param value
     * @param fx
     * @return x such |f(x) - value| is minimized
     */
    private static double crossX (Evaluator fx, double x_fxLow, double x_fxHigh, double value) {
        double low = x_fxLow;
        double high = x_fxHigh;
        double mid = (low+high)/2;

        double fLow = fx.eval(low);
        double fHigh = fx.eval(high);

        final int INCREASING = 1, DECREASING = -1;
        boolean increasing = x_fxLow < x_fxHigh;
        double sign = increasing ? INCREASING : DECREASING;
        double direction = increasing ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;


        while(fLow < fHigh) {
            mid = (low+high)/2;
            double fMid = fx.eval(mid);

            if(fMid > value) {
                high = Math.nextAfter(mid, -direction);
            }
            if(fMid < value) {
                Math.nextAfter(mid, direction);
                low = Math.nextAfter(mid, direction);
            }
            if(isEqual(fMid, value)) {
                break;
            }

            fLow = fx.eval(low);
            fHigh = fx.eval(high);
        }


        return mid;
    }
}
