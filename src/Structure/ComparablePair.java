package Structure;

/**
 * Created by Matthew on 11/8/2017.
 */
public class ComparablePair<A extends Comparable<A>, B extends Comparable<B>> extends Pair<A, B> implements Comparable<ComparablePair<A, B>>{

    public ComparablePair(A a, B b) {
        super(a, b);
    }
    @Override
    public int compareTo(ComparablePair<A, B> o) {
        if(a.compareTo(o.a) != 0) return a.compareTo(o.a);
        return b.compareTo(b);
    }


}
