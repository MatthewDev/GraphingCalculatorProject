package structure;

/**
 * Created by Matthew on 10/16/2017.
 */
public class RangeSum { //only works with double because Java generics are trash
    public double[] prefixSum;
    public RangeSum (double[] num){
        prefixSum = new double[num.length+1];
        for(int i = 0; i<num.length; i++){
            prefixSum[i+1] = prefixSum[i]+num[i];
        }
    }

    /**
     * sum of original num[] from a to b inclusive [a, b]
     * a and b must be valid indices for the original number array
     * @param a start of range
     * @param b end of range
     * @return
     */
    public double sum(int a, int b) {
        return prefixSum[b+1]-prefixSum[a];
    }
}
