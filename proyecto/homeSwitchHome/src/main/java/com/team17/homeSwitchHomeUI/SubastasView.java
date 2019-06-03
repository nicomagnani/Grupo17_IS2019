package com.team17.homeSwitchHomeUI;


import java.awt.TextField;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;


public class SubastasView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	public SubastasView() {
		TextField textField = new TextField();
		textField.setText("Text field label");
		
        setCompositionRoot((Component) textField);
    }
}