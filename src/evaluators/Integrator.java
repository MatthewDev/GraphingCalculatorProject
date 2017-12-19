package evaluators;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Matthew on 11/16/2017.
 */
public class Integrator implements Evaluator{
    private Evaluator fx;
    private double start, dx;

    private static final double DEFAULT_DX = 0.00001;

    /**
     * defines the definite integral
     * x
     * ∫f(x)dx
     * start
     * @param fx
     * @param start
     * @param dx
     */
    public Integrator(Evaluator fx, double start, double dx) {
        this.fx = fx;
        this.start = start;
        if(dx <= 0) {
            this.dx = DEFAULT_DX;
        }else{
            this.dx = dx;
        }
    }
    public Integrator(Evaluator fx, double start) {
        this(fx, start, DEFAULT_DX);
    }

    /**
     * default lower limit of integration is 0
     * x
     * ∫f(x)dx
     * 0
     * @param fx
     */
    public Integrator(Evaluator fx) {
        this(fx, 0, DEFAULT_DX);
    }


    /**
     * @param x
     * @return
     * x
     * ∫f(x)dx
     * start
     */
    private TreeMap<Double, Double> cachedValues = new TreeMap<>();
    @Override
    public double eval(double x) {
        return simpsons(x);

    }
    public double simpsons(double x) {
        //don't manually compute the integral if it is from a to a
        if(start==x) return 0;

        double trapezoidIntegral = 0;
        double midpointIntegral = 0;

        trapezoidIntegral += fx.eval(start)/2;
        double i;
        for(i = start+dx; i < x-dx; i += dx) {
            trapezoidIntegral += fx.eval(i);

            midpointIntegral += fx.eval(i-(dx/2));
        }
        trapezoidIntegral += fx.eval(i)/2;
        trapezoidIntegral *= dx;

        midpointIntegral += fx.eval(i-(dx/2));
        midpointIntegral *= dx;
        double simpsonsIntegral = (2*midpointIntegral+trapezoidIntegral)/3;

        return simpsonsIntegral;
    }

    /**
     * unused but implements interface
     *
     * @param start first x value evaluated
     * @param end last x value evaluated
     * @param stepSize changes steps in output, doesn't change dx
     * @return
     */
    @Override
    public TreeMap<Double, Double> eval(double start, double end, double stepSize) {
        TreeMap<Double, Double> integrals = new TreeMap<>();
        eval(start);
        eval(end);
        for(double i = start; i <= end; i+= stepSize) {
            Map.Entry<Double, Double> entry = cachedValues.floorEntry(i);
            double x0 = entry.getKey();
            double integral = entry.getValue();
            integral += trapezoidSum(x0, i);
            integrals.put(i, integral);
        }
        return null;
    }
    private double trapezoidSum(double a, double b) {
        return (b-a)*(fx.eval(a)+fx.eval(b))/2;
    }
}
