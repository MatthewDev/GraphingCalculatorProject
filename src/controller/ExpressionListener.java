package controller;

import model.GraphModel;
import view.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Matthew on 12/14/2017.
 */
public class ExpressionListener extends InputListener {

    public ExpressionListener(GraphModel model, Window view) {
        super(model, view);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String expression = e.getActionCommand();

        model.setExpression(expression);

        view.updateAll();
    }
}
