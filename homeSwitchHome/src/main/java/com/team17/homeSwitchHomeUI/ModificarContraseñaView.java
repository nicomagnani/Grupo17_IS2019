package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
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
	Notification notification = new Notification("sd");
	
	public ModificarContraseñaView(Navigator navigator, MyUI interfaz) {
		
		
		botonAceptar.addClickListener( e -> modificar(interfaz) );
		botonCancelar.addClickListener(e-> cancelar(navigator));
		
		FormLayout layout1 = new FormLayout(textoContraseña1, textoContraseña2, textoContraseña3);
		HorizontalLayout botonesLayout = new HorizontalLayout(botonAceptar,botonCancelar);
		VerticalLayout mainLayout = new VerticalLayout(layout1,botonesLayout);
		
		mainLayout.setComponentAlignment(botonesLayout, Alignment.MIDDLE_CENTER);
			
		setCompositionRoot(mainLayout);	
	}
	
	
	
	private void cancelar(Navigator navigator) {
		navigator.navigateTo("miPerfil");
	}
	
	private void modificar(MyUI interfaz) {
		
		ConnectionBD conectar = new ConnectionBD();	
		usuario = HomeSwitchHome.getUsuarioActual();
		if(textoContraseña1.isEmpty()||textoContraseña2.isEmpty()||textoContraseña3.isEmpty()) {
			this.mostrarNotificacion("Error: Hay campos vacíos.", Notification.Type.ERROR_MESSAGE);
		}else {
			
			if(textoContraseña1.getValue().equals(usuario.getContraseña())) {
				if(textoContraseña2.getValue().equals(textoContraseña3.getValue())) {
					conectar.cambiarContraseña(usuario.getMail(),textoContraseña2.getValue());
					this.mostrarNotificacion("Éxito. Regresando...", Notification.Type.HUMANIZED_MESSAGE);
					interfaz.vistaUsuario("miPerfil");
				}else {
					this.mostrarNotificacion("Error: Las nuevas contraseñas no coinciden.", Notification.Type.ERROR_MESSAGE);
				}
			}else{
				this.mostrarNotificacion("Error: Ingrese bien la contraseña anterior.", Notification.Type.ERROR_MESSAGE);
			}
		}
	}
	
    private void mostrarNotificacion(String st, Notification.Type tipo) {
    	notification = new Notification(st, tipo);
    	notification.setDelayMsec(5000);
		notification.show(Page.getCurrent());
    }
    
}
