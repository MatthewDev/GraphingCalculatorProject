package view.scale;

import structure.Pair;

import java.util.Map;

/**
 * Created by Matthew on 12/15/2017.
 */
public class RoundedPercentScaler extends PercentScaler {

    public RoundedPercentScaler(double percent) {
        super(percent);
    }

    @Override
    public Pair<Double, Double> getScaleRange(Map<Double, Double> function) {
        Pair<Double, Double> range = super.getScaleRange(function);
        //percentscaler will never return a range greater than the function's range
        //thus multiply distance from center by coverage%
        if(coverageDecimal > 1) {
            double mid = (range.a+range.b)/2;
            double distance = range.b - mid;
            range.a = mid - (distance*coverageDecimal);
            range.b = mid + (distance*coverageDecimal);
        }

        //rounds to 2 significant figures of the larger endpoint
        double maxAbs = Math.max(Math.abs(range.a), Math.abs(range.b));
        double base = getBaseValue(getOOM(maxAbs)-1);
        //if base is 0 no rounding should be done
        if(base == 0) return range;
        double a = floorMultiple(range.a, base);
        double b =  ceilMultiple(range.b, base);

        return new Pair<>(a, b);
    }
    private static double ceilMultiple(double number, double multiple) {
        double n = number/multiple;
        n = Math.ceil(n);
        return Math.ceil(multiple*n);
    }
    private static double floorMultiple(double number, double multiple) {
        double n = number/multiple;
        n = Math.floor(n);
        return Math.floor(multiple*n);
    }

    private static double getOOM(double number) {
        return Math.floor(Math.log10(Math.abs(number)));
    }
    private static double getBaseValue(double OOM) {
        return Math.pow(10, OOM);
    }

}
