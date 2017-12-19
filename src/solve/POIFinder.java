package solve;

import structure.ComparablePair;
import structure.Pair;
import evaluators.Differentiator;
import evaluators.Evaluator;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matthew on 12/18/2017.
 */
public class POIFinder {
    /**
     *
     * @param fx
     * @param domain
     * @return set of points (x,y) that are points of inflection on the domain of f(x)
     */
    public static Set<ComparablePair<Double, Double>> find(Evaluator fx, Pair<Double, Double> domain) {
        Set<ComparablePair<Double, Double>> relativeExtremaMap = new HashSet<>();
        Differentiator f1x = new Differentiator(fx);
        Differentiator f2x = new Differentiator(f1x);
        Set<Double> potentialPOI = CrossValue.equalXes(f2x, domain.a, domain.b, 0);
        for(double x : potentialPOI) {
            if(isPOI(fx, f2x, x)) {
                double y = fx.eval(x);
                relativeExtremaMap.add(new ComparablePair<>(x, y));
            }
        }
        return relativeExtremaMap;
    }
    public static boolean isPOI(Evaluator fx, Differentiator f2x, double x) {
        //can't be a POI if f(x) is undefined/infinite
        if(!DoubleUtil.isBasicallyFinite(fx.eval(x))) return false;

        //for POI it's better than dx is bigger than smaller within reason so that it can actually reach the x crossing if there is one
        final double dx = Differentiator.DEFAULT_STEP_SIZE;

        double limitLeft = f2x.eval(x-dx);
        double limitRight = f2x.eval(x+dx);

        limitLeft = DoubleUtil.fixBasicallyInfinity(limitLeft);
        limitRight = DoubleUtil.fixBasicallyInfinity(limitRight);


        if(fuzzySignum(limitLeft) != fuzzySignum(limitRight)) return true;

        return false;

    }

    private static double fuzzySignum(double a) {
        final double fuzzyFactor = 1E-7;
        if(Math.abs(a) < fuzzyFactor) return 0;
        return Math.signum(a);
    }


}
