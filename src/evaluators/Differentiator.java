package evaluators;

import java.util.Arrays;
import java.util.TreeMap;

import static solve.CrossValue.EPSILON;

/**
 * Created by Matthew on 9/18/2017.
 */
public class Differentiator implements Evaluator {
    private static final double DEFAULT_DX = 0.0001;
    private Evaluator fx;

    public Differentiator(Evaluator fx) {
        this.fx = fx;
    }


    @Override
    public double eval(double x) {
        return evalSymmDifferenceQuotient(x, DEFAULT_DX);
    }

    private double evalSymmDifferenceQuotient(double x, double stepSize) {
        double dxStep = stepSize;

        double xLeft = x-dxStep;
        double xRight = x+dxStep;

        double dxLeft = Math.abs(xLeft - x); //due to floating point error dxLeft + dxStep =  dxStep is not guaranteed
        double dxRight = xRight - x;

        double yLeft = fx.eval(xLeft);
        double yRight = fx.eval(xRight);

        double dydx =  (yRight-yLeft)/(dxLeft + dxRight);

        return dydx;
    }


    /**
     * 5 point stencil based first derivative, marginally better than symmetrical difference quotient
     * f'(x) = f(x-2h)-8f(x-h)+8f(x+h)-f(x+2h)/12h
     * error on the order of h^4 purely from the equation, but small h exaggerate floating point errors, so h should be as large as possible while maintaining sufficient accuracy
     * @param x
     * @param h
     * @return approximate derivative of f(x) at x
     */
    private double eval5pt(double x, double h) {

        double x0 = x-2*h;
        double x1 = x-h;
        double x2 = x+h;
        double x3 = x+2*h;

        double h0x2 = Math.abs(x0 - x); //due to floating point error x0 - x =  -2*h is not guaranteed
        double h1 = Math.abs(x1 - x);
        double h2 = Math.abs(x2 - x);
        double h3x2 = Math.abs(x3 - x);

        double _12h = (8*h1)+(8*h2)-(h0x2)-(h3x2);
        //12h = 8h1+8h2-2h0-2h3 when accounting for floating point error

        double y0 = fx.eval(x0);
        double y1 = fx.eval(x1);
        double y2 = fx.eval(x2);
        double y3 = fx.eval(x3);

        double dydx =  (y0-(8*y1)+(8*y2)-(y3)) / (_12h);

        return dydx;
    }

    @Override
    public TreeMap<Double, Double> eval(double start, double end, double stepSize) {
        TreeMap<Double, Double> ans = new TreeMap<>();

        int steps = (int) Math.ceil((end-start)/stepSize);

        for(int i = 0; i < steps; i++) {
            double x = start + (i*stepSize);
            ans.put(x, eval(x));
        }

        return ans;
    }

}
