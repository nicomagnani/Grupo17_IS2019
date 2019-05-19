package com.team17.homeSwitchHomeUI;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import homeSwitchHome.HomeSwitchHome;

@Theme("hometheme")
public class IniciarSesionView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	public IniciarSesionView(HomeSwitchHome sistema) {
		
		TextField textoEmail = new TextField("Email:");
		TextField textoContraseña = new TextField("Contraseña:");
		
		Button login = new Button("Iniciar Sesión");
		login.addClickListener(e -> {
			int result = sistema.iniciarSesion(textoEmail.getValue(), textoContraseña.getValue()); 
            if (result == sistema.LOGIN_SUCCESS) {
            	cambiarAdminUI(sistema);
            }else if (result == sistema.WRONG_USERNAME){
            	//TODO
            } else if (result == sistema.WRONG_PASSWORD) {
            	//TODO
            }
        });
		
		VerticalLayout mainLayout = new VerticalLayout(textoEmail, textoContraseña, login);
        setCompositionRoot(mainLayout);
    }

	private void cambiarAdminUI(HomeSwitchHome sistema) {
		AdminUI adminUI = AdminUI new(sistema);	
	}
}
