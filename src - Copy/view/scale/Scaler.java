package view.scale;

import java.util.Map;

/**
 * Created by Matthew on 10/4/2017.
 */
public interface Scaler {
    Scale getScaleRange(Map<Double, Double> function);
}
