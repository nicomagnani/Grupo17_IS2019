package com.team17.homeSwitchHomeUI;
import java.sql.SQLException;
import java.util.ArrayList;

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
import homeSwitchHome.UsuarioAdministrador;
@Theme("hometheme")
public class IniciarSesionView extends Composite implements View {  //.necesita composite y view para funcionar correctamente
	
	public IniciarSesionView(HomeSwitchHome sistema,Navigator navigator, MyUI interfaz) {
		
		TextField textoEmail = new TextField("Email:");
		TextField textoContraseña = new TextField("Contraseña:");
		
		Button login = new Button("Iniciar Sesión");
		login.addClickListener(e -> {
			ConnectionBD conectar = new ConnectionBD();
			ArrayList<UsuarioAdministrador> usuarios= new ArrayList<UsuarioAdministrador>();
			try {
				usuarios= conectar.listaUsuarios();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i=0;i< usuarios.size();i++) {
				if(usuarios.get(i).getMail().equals(textoEmail.getValue())){
					if(usuarios.get(i).getContraseña().equals(textoContraseña.getValue())) {
						System.out.print("valido");
						this.iniciarSesion(interfaz);
					}else {
						System.out.print("contra");
						Notification.show("contraseña incorrecta");
					}
				}else {
					Notification.show("mail invalido");
				}
				
			}
			
        });
		VerticalLayout mainLayout = new VerticalLayout(textoEmail, textoContraseña, login);
        setCompositionRoot(mainLayout);
	
    }

	private void iniciarSesion(MyUI interfaz) {
		interfaz.cambiarAdmin();
	}
}
