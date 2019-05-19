package com.team17.homeSwitchHomeUI;

import com.vaadin.navigator.View;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ContactarView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	public ContactarView() {
		Label titulo = new Label("Comunicarse vía correo electrónico");
		titulo.addStyleName(ValoTheme.MENU_TITLE);
        
		String mail = "<a href=\"mailto:soporte@homeswitchhome.com\">Contacto</a>";
		Label texto1 = new Label("Soporte de la empresa: " + mail);
		texto1.setContentMode(com.vaadin.shared.ui.ContentMode.HTML);
		
		VerticalLayout mainLayout = new VerticalLayout(titulo,texto1);
		
		setCompositionRoot(mainLayout);
    }
}