package com.team17.homeSwitchHomeUI;

import java.awt.TextField;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;

public class SubastasAdminView extends Composite implements View {
	
	public SubastasAdminView() {
		TextField textField = new TextField();
		textField.setText("Text field label");
		
        setCompositionRoot((Component) textField);
    }

}
