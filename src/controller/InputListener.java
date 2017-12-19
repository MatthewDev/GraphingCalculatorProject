package controller;

import model.GraphModel;
import view.Window;

import java.awt.event.ActionListener;

/**
 * Created by Matthew on 12/14/2017.
 */
public abstract class InputListener implements ActionListener{
    protected final GraphModel model;
    protected final Window view;

    public InputListener(GraphModel model, Window view) {
        this.model = model;
        this.view = view;
    }
}
