package view.scale;

import Structure.ComparablePair;
import Structure.RangeSum;

import java.util.Arrays;

/**
 * Created by Matthew on 10/19/2017.
 */
public class RangeFinder implements Runnable {
    int yMini, yMiniLimit, points, minRangei = 0, maxRangei = 0;
    ComparablePair<Double, Double>[] yx;
    RangeSum domainArea;
    double absDomainCoverage, minRangeSize;

    /**
     *
     * @param yMini inclusive start
     * @param yMiniLimit exclusive end
     * @param yx
     * @param domainArea
     * @param absDomainCoverage
     */
    @Deprecated
    public RangeFinder(int yMini, int yMiniLimit, ComparablePair<Double, Double>[] yx, RangeSum domainArea, double absDomainCoverage) {
        points = yx.length;
        minRangeSize = Double.MAX_VALUE;

        this.yMini = yMini;
        this.yMiniLimit = yMiniLimit;
        this.yx = yx;
        this.domainArea = domainArea;
        this.absDomainCoverage = absDomainCoverage;

    }
    private int binarySearch(double[] arr, double val) {
        int i = Arrays.binarySearch(arr, val);
        if(i<0) {
            i = -(i+1);
        }
        return i;
    }

    @Override
    public void run() {
        for(yMini=yMini; yMini < yMiniLimit; yMini++) {
            double yMin = yx[yMini].a;
            double totalCoverage = domainArea.sum(0, yMini) + absDomainCoverage; //coverage from 0 to ymax
            int yMaxi = binarySearch(domainArea.prefixSum, totalCoverage)-1;
            if(yMaxi >= points) break;
            double yMax = yx[yMaxi].a;
            double ySize = yMax - yMin;
            if(ySize < minRangeSize) {
                minRangeSize = ySize;
                minRangei = yMini;
                maxRangei = yMaxi;
            }
        }
    }
}
