package view.scale;

import structure.ComparablePair;
import structure.Pair;
import structure.RangeSum;

import java.util.*;

/**
 * Created by Matthew on 10/4/2017.
 */
public class PercentScaler implements Scaler {
    protected double coverageDecimal;

    public PercentScaler(double scaleDecimal) {
        this.coverageDecimal = scaleDecimal;
    }

    public void setScale(double scale) {
        this.coverageDecimal = scale;
    }
    /**
     * overly complex due to support for varying x distance between points that is deprecated within Evaluator
     *
     * @param function
     * @return
     */
    public Pair<Double, Double> getScaleRange(Map<Double, Double> function) {
        TreeMap<Double, Double> fx = new TreeMap<>(function);

        //remove non numbers that mess up the algo
        for(Map.Entry<Double, Double> point : function.entrySet()) {
            if(!Double.isFinite(fx.get(point.getKey()))) fx.remove(point.getKey());
        }

        int points = fx.size();
        ComparablePair<Double, Double>[] yx = (ComparablePair<Double, Double>[]) new ComparablePair[points];

        int i = 0;
        for(Double x : fx.keySet()) {
            Double y = fx.get(x);
            yx[i] = new ComparablePair<>(y, x); //we need to sort by y
            i++;
        }

        Arrays.sort(yx);
        i = 0;
        double[] domainAreai = new double[points]; //half of both line segments ending at the point
        for(ComparablePair<Double, Double> yxpoint : yx) {
            double x = yxpoint.b;
            double xlow = fx.lowerKey(x) == null ? x : fx.lowerKey(x);
            double xhigh = fx.higherKey(x) == null ? x : fx.higherKey(x);
            double halfSecSizei = (x-xlow)/2 + (xhigh-x)/2;
            //System.out.println(halfSecSizei);
            domainAreai[i] = halfSecSizei;
            i++;
        }
        RangeSum domainArea = new RangeSum(domainAreai);
        //domainArea.sum(0,points-1) is used to account for accumulated floating point error
        final double domainSize = domainArea.sum(0,points-1);
        double absDomainCoverage =  domainSize * coverageDecimal;

        int bestymini = 0 , bestymaxi = points-1;
        double bestysize = Double.MAX_VALUE;
        for(int ymini = 0; ymini < points; ymini++) {
            double offset = domainArea.sum(0, ymini);
            //System.out.println("offset: "+offset);
            double targetCoverage = offset + absDomainCoverage; //target coverage from 0 to ymax
            int ymaxi = binarySearch(domainArea.prefixSum, targetCoverage)-1;//-1 for prefix array indexing

            //if the location is outside of the array then range is greater than 100%
            if(ymaxi >= points) break;

            double ymax = yx[ymaxi].a;
            double ymin = yx[ymini].a;
            double ysize = ymax - ymin;
            if(ysize < bestysize) {
                bestysize = ysize;
                bestymaxi = ymaxi;
                bestymini = ymini;
            }
        }

        return new Pair<>(yx[bestymini].a, yx[bestymaxi].a);
    }



    private int binarySearch(double[] arr, double val) {
        int i = Arrays.binarySearch(arr, val);
        if(i<0) {
            i = -(i+1);
        }
        return i;
    }
}


