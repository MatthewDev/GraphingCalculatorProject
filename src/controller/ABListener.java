package controller;

import model.GraphModel;
import view.Window;

import java.awt.event.ActionEvent;

/**
 * Created by Matthew on 12/17/2017.
 */
public class ABListener extends InputListener {
    public ABListener(GraphModel model, Window view) {
        super(model, view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.setAB(view.getAB());

        view.updateAll();
    }

}
