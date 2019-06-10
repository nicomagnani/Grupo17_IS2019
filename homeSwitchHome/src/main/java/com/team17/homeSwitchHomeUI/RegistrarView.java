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

import homeSwitchHome.Usuario;

public class RegistrarView extends Composite implements View {

	TextField textoEmail = new TextField("Email:");
	TextField textoContraseña = new TextField("Contraseña:");
	TextField textoTarjeta = new TextField("arjeta:");
	Button login = new Button("Registrarse");
	Label mensaje = new Label();
	
	public RegistrarView() {
		
		
		
		login.addClickListener(e -> {
			ConnectionBD conectar = new ConnectionBD();
			ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
			try {
				usuarios = conectar.listaUsuarios();
				for(int i=0;i< usuarios.size();i++) {
					
					if(usuarios.get(i).getMail().equals(textoEmail.getValue())) {
						mensaje.setValue("El Mail ya esta registrado en el sistema");
					}
					if (textoEmail.isEmpty() || textoContraseña.isEmpty() || textoTarjeta.isEmpty()) {
						mensaje.setValue("Campos vacios");							
						}else {
							 if (textoTarjeta.getValue().length()!=7) {
								 mensaje.setValue("tarjeta Invalida");		
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