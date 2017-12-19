package controller;

import model.GraphModel;
import view.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Matthew on 12/17/2017.
 */
public class DerivativeListener extends InputListener {
    public static final String FIRST_DERIVATIVE = "FIRST_DERIVATIVE", SECOND_DERIVATIVE = "SECOND_DERIVATIVE";
    public DerivativeListener(GraphModel model, Window view) {
        super(model, view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractButton checkbox = (AbstractButton) e.getSource();
        boolean isEnabled = checkbox.isSelected();

        switch (e.getActionCommand()) {
            case FIRST_DERIVATIVE:
                model.showf1x(isEnabled);
                break;
            case SECOND_DERIVATIVE:
                model.showf2x(isEnabled);
                break;
        }

        view.updateAll();
    }
}
