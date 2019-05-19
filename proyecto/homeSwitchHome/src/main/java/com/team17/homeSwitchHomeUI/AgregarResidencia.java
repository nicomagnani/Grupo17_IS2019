package com.team17.homeSwitchHomeUI;

import javax.servlet.annotation.WebServlet;

import com.vaadin.navigator.View;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;

/**
 * @author Alejandro Duarte
 */
public class AgregarResidencia extends Composite implements View {

    public AgregarResidencia() {
        setCompositionRoot(new Label("This is view 1"));
    }
}
