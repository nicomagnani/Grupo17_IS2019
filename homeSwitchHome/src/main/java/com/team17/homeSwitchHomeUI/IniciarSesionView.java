package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Usuario;
import homeSwitchHome.UsuarioAdministrador;


@Theme("hometheme")
public class IniciarSesionView extends Composite implements View {  //.necesita composite y view para funcionar correctamente
	
	Label cabecera = new Label("Iniciar Sesión");
	TextField textoEmail = new TextField("Email:");
	PasswordField textoContraseña = new PasswordField("Contraseña:");
	Button login = new Button("Iniciar Sesión");
	Label msj = new Label();
		
	public IniciarSesionView(MyUI interfaz) {		
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
						
		login.addClickListener(e -> {
			
			//si los campos no estan vacios
			if ( (!textoEmail.isEmpty()) && (!textoContraseña.isEmpty()) ) {
								
				ConnectionBD conectar = new ConnectionBD();
				
				//me conecto a la tabla usuarios
				ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
				try {
					usuarios = conectar.listaUsuarios();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				boolean ok = false;
				
				//busco en la tabla usuarios
				for (int i = 0; ( (i < usuarios.size()) && !ok ); i++) {
					if ( (usuarios.get(i).getMail().equals(textoEmail.getValue())) && 
							(usuarios.get(i).getContraseña().equals(textoContraseña.getValue())) ) {
						msj.setValue("Éxito. Iniciando sesión de usuario...");
						this.iniciarSesionUsuario(usuarios.get(i).getMail(), interfaz);
						ok = true;
					}
				}
				
				//si no encuentra en la tabla usuarios, busca en la tabla admins
				if (!ok) {
					ArrayList<UsuarioAdministrador> admins = new ArrayList<UsuarioAdministrador>();
					try {
						admins = conectar.listaAdmins();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					for (int i = 0; ( (i < admins.size()) && !ok ); i++) {
						if ( (admins.get(i).getMail().equals(textoEmail.getValue())) && 
								(admins.get(i).getContraseña().equals(textoContraseña.getValue())) ) {
							msj.setValue("Éxito. Iniciando sesión de usuario...");
							this.iniciarSesionAdmin(admins.get(i).getMail(), interfaz);
							ok = true;
						}
					}
					
					////si no encuentra en ninguna tabla, muestra error
					msj.setValue("Error: Email y/o contraseña inválidos");
				}
			} else
				msj.setValue("Error: Al menos un campo se encuentra vacío.");
			
        });
		
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, textoEmail, textoContraseña, login, msj);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(textoEmail, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(textoContraseña, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(login, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msj, Alignment.MIDDLE_CENTER);
		
        setCompositionRoot(mainLayout);	
        
        
    }
	

	private void iniciarSesionUsuario(String usuarioActual, MyUI interfaz) {
		HomeSwitchHome.setUsuarioActual(usuarioActual);
		interfaz.vistaUsuario("residencias");
	}
	
	private void iniciarSesionAdmin(String usuarioActual, MyUI interfaz) {
		HomeSwitchHome.setUsuarioActual(usuarioActual);
		interfaz.vistaAdmin("residenciasAdmin");
	}
}
