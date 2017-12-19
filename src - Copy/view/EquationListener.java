package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Matthew on 10/16/2017.
 */
public class EquationListener implements ActionListener {
    FunctionFrame parent;
    public EquationListener(FunctionFrame parent) {
        this.parent = parent;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        parent.setFunction(e.getActionCommand());
    }
}
