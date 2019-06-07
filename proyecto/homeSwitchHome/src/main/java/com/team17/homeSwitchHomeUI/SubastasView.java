package com.team17.homeSwitchHomeUI;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;

public class SubastasView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	public SubastasView() {
		Label label = new Label();
		label.setValue("Test Test Test Test Test");
			
		setCompositionRoot((Component) label);
    }
}