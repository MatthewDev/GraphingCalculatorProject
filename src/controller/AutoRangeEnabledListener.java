package controller;

import model.GraphModel;
import view.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Matthew on 12/15/2017.
 */
public class AutoRangeEnabledListener extends InputListener {
    public AutoRangeEnabledListener(GraphModel model, Window view) {
        super(model, view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractButton checkbox = (AbstractButton) e.getSource();

        model.setAutoRangeEnabled(checkbox.isSelected());

        view.updateAll();
    }
}
