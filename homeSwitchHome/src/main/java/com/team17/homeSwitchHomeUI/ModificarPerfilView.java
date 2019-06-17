package com.team17.homeSwitchHomeUI;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;

import homeSwitchHome.HomeSwitchHome;

public class ModificarPerfilView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	public ModificarPerfilView() {
		Label label = new Label();
		label.setValue("Test Test Test Test Test" + HomeSwitchHome.getUsuarioActual());
			
		setCompositionRoot((Component) label);
    }
}