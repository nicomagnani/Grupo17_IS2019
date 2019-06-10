package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.UsuarioAdministrador;

public class RegistrarView extends Composite implements View {

	public RegistrarView() {
		
		
		TextField textoEmail = new TextField("Email:");
		TextField textoContraseña = new TextField("Contraseña:");
		TextField textoTarjeta = new TextField("tarjeta:");
		Button login = new Button("Registrarse");
		login.addClickListener(e -> {
			ConnectionBD conectar = new ConnectionBD();
			ArrayList<UsuarioAdministrador> usuarios= new ArrayList<UsuarioAdministrador>();
			try {
				usuarios= conectar.listaUsuarios();
				for(int i=0;i< usuarios.size();i++) {
					
					if(usuarios.get(i).getMail().equals(textoEmail.getValue())) {
						Notification.show("El Mail ya esta registrado en el sistema");
					}
					if (textoEmail.isEmpty() || textoContraseña.isEmpty() || textoTarjeta.isEmpty()) {
							Notification.show("Campos vacios");							
						}else {
							 if (textoTarjeta.getValue().length()!=7) {
								 Notification.show("tarjeta Invalida");		
							 }else {
								conectar.AgregarUsuario(textoEmail.getValue(), textoContraseña.getValue(),Integer.parseInt(textoTarjeta.getValue()));
							 }
							
						}
					}
					
				}
			 catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

		  });
		
		VerticalLayout mainLayout = new VerticalLayout( textoEmail, textoContraseña,textoTarjeta, login);
        setCompositionRoot(mainLayout);
}
}