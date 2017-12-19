package solve;

import static solve.CrossValue.EPSILON;

/**
 * Created by Matthew on 12/12/2017.
 */
public class DoubleUtil {

    /**
     * allow some reasonable error such that |f(a)-f(b)| < error
     * for most continuous functions when
     * a = x-dx
     * b = x+dx
     * where dx = Math.ulp(x)
     * @param a
     * @param b
     * @return error
     */
    public static double reasonableError(double a, double b) {
        final double minError = Math.sqrt(EPSILON);
        double error = maxUlp(a, b);

        //square root or square the output resolution to halve the number of LSB that must match
        //"close enough"
        if(error<1) error = Math.sqrt(error);
        if(error>1) error *= error;

        //when a/b are 0 ULP can be unreasonably small, so use an arbitrary smallish number, in this case sqrt(epsilon)
        error = Math.max(error, minError);
        return error;
    }

    private static final double BASICALLY_INFINITY = 1<<53;

    /**
     * if a is basically infinity, return actual positive/negative infinity, otherwise return a
     * @param a
     * @return
     */
    public static double fixBasicallyInfinity(double a) {
        if(Math.abs(a) > BASICALLY_INFINITY) return Math.signum(a) * Double.POSITIVE_INFINITY;

        return a;
    }

    public static boolean isBasicallyFinite(double a) {
        if(!Double.isFinite(a)) return false;
        if(!Double.isFinite(fixBasicallyInfinity(a))) return false;
        return true;
    }

    public static double maxUlp(double a, double b) {
        return Math.max(Math.ulp(a), Math.ulp(b));
    }

    public static boolean fuzzyEqual(double a, double b) {
        return Math.abs(a-b) < reasonableError(a , b);
    }

    public static boolean isEqual (double a, double b) {
        return Math.abs(a-b) <= 2*maxUlp(a, b);
    }

    public static boolean compare(double a, double b, boolean greater) {
        if (greater) return a>b;
        else return a<b;
    }

}
