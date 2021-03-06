package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Usuario;

@Title("Modificar cuenta - HomeSwitchHome")
public class ModificarContraseñaView extends Composite implements View {
	
	Label cabecera = new Label("Modificar contraseña");
	PasswordField textoContraseña1 = new PasswordField("Contraseña anterior:");
	PasswordField textoContraseña2 = new PasswordField("Contraseña nueva:");
	PasswordField textoContraseña3 = new PasswordField("Repetir contraseña nueva:");
	Button botonAceptar = new Button("Modificar");
	Button botonCancelar= new Button("Cancelar");
	Usuario usuario;
	Notification notification = new Notification("sd");
	
	
	public ModificarContraseñaView(Navigator navigator, MyUI interfaz) {		
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		botonAceptar.addClickListener( e -> modificar(interfaz) );
		botonCancelar.addClickListener(e-> cancelar(navigator));
		
		FormLayout layout1 = new FormLayout(textoContraseña1, textoContraseña2, textoContraseña3);
		HorizontalLayout botonesLayout = new HorizontalLayout(botonAceptar,botonCancelar);
		VerticalLayout mainLayout = new VerticalLayout(cabecera,layout1,botonesLayout);
		
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
		} else {			
			if ( textoContraseña1.getValue().equals(usuario.getContraseña()) ) {
				if ( textoContraseña2.getValue().equals(textoContraseña3.getValue()) ) {					
					try {
						conectar.modificarUsuarioContraseña(usuario.getMail(),textoContraseña2.getValue());

						//informa resultado y actualiza la vista solo si no ocurre excepción
						this.mostrarNotificacion("Éxito. Regresando...", Notification.Type.HUMANIZED_MESSAGE);
						interfaz.vistaUsuario("miPerfil");
					} catch (SQLException e) {
						e.printStackTrace();
					}					
				} else {
					this.mostrarNotificacion("Error: Las nuevas contraseñas no coinciden.", Notification.Type.ERROR_MESSAGE);
				}
			} else {
				this.mostrarNotificacion("Error: La contraseña actual es incorrecta.", Notification.Type.ERROR_MESSAGE);
			}
		}
	}
	
    private void mostrarNotificacion(String st, Notification.Type tipo) {
    	notification = new Notification(st, tipo);
    	notification.setDelayMsec(5000);
		notification.show(Page.getCurrent());
    }
    
}
