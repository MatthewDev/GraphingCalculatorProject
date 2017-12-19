package controller;

import model.GraphModel;
import view.Window;

import java.awt.event.ActionEvent;

/**
 * Created by Matthew on 12/14/2017.
 */
public class DomainListener extends InputListener {

    public DomainListener(GraphModel model, Window view) {
        super(model, view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.setDomain(view.getDomain());

        view.updateAll();
    }
}
