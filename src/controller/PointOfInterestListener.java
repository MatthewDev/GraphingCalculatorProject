package controller;

import model.GraphModel;
import view.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Matthew on 12/15/2017.
 */
public class PointOfInterestListener extends InputListener {
    public static final String HOLE = "HOLE", POINT_OF_INFLECTION = "POI", RELATIVE_EXTREMA = "EXTREMA";

    public PointOfInterestListener(GraphModel model, Window view) {
        super(model, view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractButton checkbox = (AbstractButton) e.getSource();
        boolean isEnabled = checkbox.isSelected();

        switch (e.getActionCommand()) {
            case HOLE:
                model.showHoles(isEnabled);
                break;
            case POINT_OF_INFLECTION:
                model.showPOI(isEnabled);
                break;
            case RELATIVE_EXTREMA:
                model.showExtrema(isEnabled);
                break;
        }

        view.updateAll();
    }
}
