package controller;

import model.GraphModel;
import view.Window;

import java.awt.event.ActionEvent;

/**
 * Created by Matthew on 12/15/2017.
 */
public class AutoRangeListener extends InputListener {
    public AutoRangeListener(GraphModel model, Window view) {
        super(model, view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double coveragePercent = Double.parseDouble(e.getActionCommand());
        double coverageDecimal = coveragePercent/100;

        //System.out.println(coverageDecimal);
        model.setAutoRange(coverageDecimal);

        view.updateAll();
    }
}
