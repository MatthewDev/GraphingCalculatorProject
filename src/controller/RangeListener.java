package controller;

import model.GraphModel;
import view.Window;
import view.scale.RangeFinder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Matthew on 12/14/2017.
 */
public class RangeListener extends InputListener{

    public RangeListener(GraphModel model, Window view) {
        super(model, view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.setRange(view.getRange());

        view.updateAll();
    }
}
