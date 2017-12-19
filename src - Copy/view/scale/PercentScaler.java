package view.scale;

import structure.RangeSum;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Matthew on 10/4/2017.
 */
public class PercentScaler implements Scaler{
    double percentDecimal = 1.0;
    public PercentScaler(double percent) {
        this.percentDecimal = percent; //add compat jazz
    }
    @Override
    public Scale getScaleRange(Map<Double, Double> function) {
        TreeMap<Double, Double> fx = new TreeMap<>(function);
        double domainSize = fx.lastKey()-fx.firstKey();
        int points = function.size();
        Pair<Double, Double>[] yx = (Pair<Double, Double>[]) new Pair[points];

        int i = 0;
        for(Double x : function.keySet()) {
            Double y = function.get(x);
            yx[i] = new Pair<>(y, x); //we need to sort by y
            i++;
        }
        Arrays.sort(yx);
        i = 0;
        double[] domainAreai = new double[points]; //half of both line segments ending at the point
        for(Pair<Double, Double> yxpoint : yx) {
            double x = yxpoint.b;
            double xlow = fx.lowerKey(x) == null ? x : fx.lowerKey(x);
            double xhigh = fx.higherKey(x) == null ? x : fx.higherKey(x);
            double halfSecSizei = (x-xlow)/2 + (xhigh-x)/2;
            //System.out.println(halfSecSizei);
            domainAreai[i] = halfSecSizei;
            i++;
        }
        RangeSum domainArea = new RangeSum(domainAreai);
        double absDomainCoverage = domainSize * percentDecimal;

        int bestymini = 0 , bestymaxi = 0;
        double bestysize = Double.MAX_VALUE;
        for(int ymini = 0; ymini < points; ymini++) {
            double ymin = yx[ymini].a;
            double totalCoverage = domainArea.sum(0, ymini) + absDomainCoverage; //coverage from 0 to ymax
            int ymaxi = binarySearch(domainArea.prefixSum, totalCoverage)-1;
            if(ymaxi >= points) break;
            double ymax = yx[ymaxi].a;
            double ysize = ymax - ymin;
            if(ysize < bestysize) {
                bestysize = ysize;
                bestymaxi = ymaxi;
                bestymini = ymini;
            }
        }

        return new Scale(yx[bestymini].a, yx[bestymaxi].a);
    }

    /**
     * utility function for adding domain sizes of items with the same a value with an index of start+k*incr where k is an arbitrary integer >= 0
     *
     * @param start index to start looking at
     * @param ab ab is the array it looks in
     * @param asize is the size for a at an index
     * @param incr +1 or -1 to up up or down the ab array
     * @return Integer index of last item location, Double is size of segment
     */
    private Pair<Integer, Double> sameASize(int start, Pair<Double, Double>[] ab, double[] asize, int incr) {
        incr = Integer.signum(incr);
        int i = start;
        double segmentSize = 0;
        while( (i>=0 && i<asize.length) && ab[i].a == ab[start].a) {
            segmentSize += asize[i];
            i+=incr;
        }
        return new Pair<>(i, segmentSize);
    }
    public int binarySearch(double[] arr, double val) {
        int i = Arrays.binarySearch(arr, val);
        if(i<0) {
            i = -(i+1);
        }
        return i;
    }
}


class Pair<A extends Comparable<A>, B extends Comparable<B>> implements Comparable<Pair<A, B>>{
    public A a;
    public B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }
    @Override
    public int compareTo(Pair<A, B> o) {
        if(a.compareTo(o.a) != 0) return a.compareTo(o.a);
        return b.compareTo(b);
    }


}