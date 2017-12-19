package solve;

import Structure.Pair;
import Structure.PointType.RelativeExtrema;
import evaluators.Differentiator;
import evaluators.Evaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Matthew on 12/18/2017.
 */
public class RelativeExtremaFinder {
    public static Map<Double, RelativeExtrema> find(Evaluator fx, Pair<Double, Double> domain) {
        Map<Double, RelativeExtrema> relativeExtremaMap = new HashMap<>();
        Evaluator f1x = new Differentiator(fx);
        Set<Double> potentialExtrema = CrossValue.equalXes(f1x, domain.a, domain.b, 0);
        for(double x : potentialExtrema) {
            if(extremaType(fx, x) != RelativeExtrema.NEITHER)
                relativeExtremaMap.put(x, extremaType(fx, x));
        }
        return relativeExtremaMap;
    }
    private static int REASONABLE_MOVEMENT = 1;
    public static RelativeExtrema extremaType(Evaluator fx, double x) {
        double y = fx.eval(x);
        double h = 0.001;

        double limitLeft = fx.eval(x-h);
        double limitRight = fx.eval(x+h);

        //if f(x) makes a more than reasonable movement in a 2h interval then it is probably not continuous and thus is not a relative extrema
        if(Math.abs(limitLeft-limitRight) > REASONABLE_MOVEMENT) return RelativeExtrema.NEITHER;

        if(limitLeft > y && limitRight > y) return RelativeExtrema.MIN;
        if(limitLeft < y && limitRight < y) return RelativeExtrema.MAX;
        return RelativeExtrema.NEITHER;
    }


}
