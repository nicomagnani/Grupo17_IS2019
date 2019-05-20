package com.team17.homeSwitchHomeUI;
import java.sql.SQLException;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
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
			int result = sistema.iniciarSesion(textoEmail.getValue(),textoContraseña.getValue());
			if (result == HomeSwitchHome.LOGIN_SUCCESS) {
				iniciarSesion();
			}else if (result == HomeSwitchHome.WRONG_USERNAME) {
				Notification.show("Usuario incorrecto");
			}else if (result == HomeSwitchHome.WRONG_PASSWORD) {
				Notification.show("Contraseña incorrecta");
			}else if (result == HomeSwitchHome.INVALID_EMAIL) {
				Notification.show("Ingrese un email válido");
			}
        });
		
		VerticalLayout mainLayout = new VerticalLayout(textoEmail, textoContraseña, login);
        setCompositionRoot(mainLayout);
    }

	private void iniciarSesion() {
		//TODO: mostrar AdminUI
	}
}
