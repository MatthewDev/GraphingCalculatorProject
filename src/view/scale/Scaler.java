package view.scale;

import Structure.Pair;

import java.util.Map;

/**
 * Created by Matthew on 10/4/2017.
 */
public interface Scaler {
    Pair<Double, Double> getScaleRange(Map<Double, Double> function);
}
