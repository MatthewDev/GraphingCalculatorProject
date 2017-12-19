package Structure;

import java.text.DecimalFormat;

/**
 * Created by Matthew on 12/12/2017.
 */
public class Pair<A, B> {
    public A a;
    public B b;
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (a != null ? !a.equals(pair.a) : pair.a != null) return false;
        return b != null ? b.equals(pair.b) : pair.b == null;
    }

    @Override
    public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        return result;
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.####");
    @Override
    public String toString() {
        return "("+ decimalFormat.format(a) + ", " + decimalFormat.format(b) + ")";
    }
}
