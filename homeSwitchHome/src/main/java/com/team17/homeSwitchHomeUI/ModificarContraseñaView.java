package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Usuario;

public class ModificarContraseñaView extends Composite implements View {
	PasswordField textoContraseña1 = new PasswordField("Contraseña anterior:");
	PasswordField textoContraseña2 = new PasswordField("Contraseña nueva:");
	PasswordField textoContraseña3 = new PasswordField("Repetir contraseña nueva:");
	Button botonAceptar = new Button("Modificar");
	Button botonCancelar= new Button("Cancelar");
	Usuario usuario;
	public ModificarContraseñaView(Navigator navigator) {
		
		
		botonAceptar.addClickListener( e -> modificar(navigator) );
		botonCancelar.addClickListener(e-> cancelar(navigator));
		
		FormLayout layout1 = new FormLayout(textoContraseña1, textoContraseña2, textoContraseña3);			
		VerticalLayout mainLayout = new VerticalLayout(layout1,botonAceptar,botonCancelar);
			
		setCompositionRoot(mainLayout);	
	}
	
	
	
	private void cancelar(Navigator navigator) {
		navigator.navigateTo("miPerfil");
	}
	private void modificar(Navigator navigator) {
		
		ConnectionBD conectar = new ConnectionBD();	
		try {
			usuario = conectar.buscarUsuario(HomeSwitchHome.getUsuarioActual());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
		if(textoContraseña1.isEmpty()||textoContraseña1.isEmpty()||textoContraseña1.isEmpty()) {
			Notification.show("hay campos vacios");
		}else {
		
		if(textoContraseña1.getValue().equals(usuario.getContraseña())) {
			if(textoContraseña2.getValue().equals(textoContraseña3.getValue())) {
				conectar.cambiarContraseña(usuario.getMail(),textoContraseña2.getValue());
				Notification.show("cambiando contraseña");
				navigator.navigateTo("miPerfil");
			}else {
				Notification.show("las nuevas contraseñas no coinciden");
			}
		}else{
			Notification.show("por favor ingrese bien la contraseña anterior");
		}
		}
	}
}
