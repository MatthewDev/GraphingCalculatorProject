package structure.pointtypes;

/**
 * Created by Matthew on 12/18/2017.
 */
public enum PointLabel {
    MIN("relative minimum"),
    MAX("relative maximum"),
    HOLE("hole"),
    POI("point of inflection"),
    NONE("");

    String description;
    PointLabel(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return description;
    }

}
