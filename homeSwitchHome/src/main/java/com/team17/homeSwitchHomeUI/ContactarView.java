package com.team17.homeSwitchHomeUI;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Title("Contacto - HomeSwitchHome")
public class ContactarView extends Composite implements View {  //.necesita composite y view para funcionar correctamente
		
	public ContactarView() {
		
		Label cabecera = new Label("Comunicarse vía correo electrónico");
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
        
		String mail = "<a href=\"mailto:soporte@homeswitchhome.com\">Contacto</a>";
		Label texto1 = new Label("Soporte de la empresa: " + mail, ContentMode.HTML);
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera,texto1);
		
		setCompositionRoot(mainLayout);
    }
}