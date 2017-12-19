package structure.pointtypes;

/**
 * Created by Matthew on 12/12/2017.
 */
public enum RelativeExtrema {
    MIN("relative minimum"),
    MAX("relative maximum"),
    NEITHER("");

    String description;
    RelativeExtrema(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return description;
    }
}