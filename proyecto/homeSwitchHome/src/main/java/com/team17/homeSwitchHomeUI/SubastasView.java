package com.team17.homeSwitchHomeUI;


import java.awt.TextField;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;


public class SubastasView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	public SubastasView() {
        setCompositionRoot(new Label("TODO: subastasView"));
    }
	
	public SubastasView(String str) {
        setCompositionRoot(new Label("TODO: subastasView " + str));

    }
}