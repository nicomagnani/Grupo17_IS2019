package com.team17.homeSwitchHomeUI;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;

public class ResidenciasVisitanteView extends Composite implements View {
		
	public ResidenciasVisitanteView() {
		Label label = new Label();
		label.setValue("123 proban234567");
			
		setCompositionRoot((Component) label);
	}

}