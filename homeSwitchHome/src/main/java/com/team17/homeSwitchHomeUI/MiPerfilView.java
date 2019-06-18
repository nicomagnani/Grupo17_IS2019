package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Tarjeta;
import homeSwitchHome.Usuario;
import homeSwitchHome.UsuarioPremium;

@Theme("hometheme")
public class MiPerfilView extends Composite implements View {  //.necesita composite y view para funcionar correctamente
	
	Label cabecera = new Label("Perfil de usuario");
	
	Button botonModificarDatos = new Button("Modificar mis datos");
	Button botonModificarContraseña= new Button("Modificar Contraseña");
	Button botonModificarTarjeta= new Button("Modificar Tarjeta");
	Usuario usuario;
	Tarjeta tarjeta;
		
	public MiPerfilView(Navigator navigator) {		
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
						
		ConnectionBD conectar = new ConnectionBD();
		
		try {
			usuario = conectar.buscarUsuario(HomeSwitchHome.getUsuarioActual());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Label labelParte1 = new Label("<span style=\"text-align: left; font-weight: bold; text-decoration: underline; font-size: 120%;\">Datos generales</span>", ContentMode.HTML);
		Label labelMail = new Label("<span style=\"font-weight: bold;\">Email:</span> " + usuario.getMail(), ContentMode.HTML);	
		Label labelNombre = new Label("<span style=\"font-weight: bold;\">Nombre:</span> " + usuario.getNombre(), ContentMode.HTML);	
		Label labelApellido = new Label("<span style=\"font-weight: bold;\">Apellido:</span> " + usuario.getApellido(), ContentMode.HTML);
		Label labelFechaNac = new Label("<span style=\"font-weight: bold;\">Fecha de nac.:</span> " + usuario.getfNac().toString(), ContentMode.HTML);
		
		tarjeta = usuario.getTarjeta();
		String nuevoNro = ofuscarNro(tarjeta.getNumero());
		String tipoUsuario = obtenerTipoUsuario();
		
		Label labelParte2 = new Label("<span style=\"text-align: left; font-weight: bold; text-decoration: underline; font-size: 120%;\">Datos de tarjeta</span>", ContentMode.HTML);
		Label labelNroTarj = new Label("<span style=\"font-weight: bold;\">Número de tarjeta:</span> " + nuevoNro, ContentMode.HTML);
		Label labelMarcaTarj = new Label("<span style=\"font-weight: bold;\">Marca:</span> " + tarjeta.getMarca(), ContentMode.HTML);
		Label labelTitTarj = new Label("<span style=\"font-weight: bold;\">Titular:</span> " + tarjeta.getTitular(), ContentMode.HTML);
		Label labelFVencTarj = new Label("<span style=\"font-weight: bold;\">Fecha de venc.:</span> " + tarjeta.getfVenc().toString(), ContentMode.HTML);		
		
		Label labelParte3 = new Label("<span style=\"text-align: left; font-weight: bold; text-decoration: underline; font-size: 120%;\">Otros datos</span>", ContentMode.HTML);
		Label labelCreditos = new Label("<span style=\"font-weight: bold;\">Créditos disponibles:</span> " + usuario.getCreditos(), ContentMode.HTML);
		Label labelFVencCred = new Label("<span style=\"font-weight: bold;\">Vencimiento de los créditos:</span> " + usuario.getfVencCred(), ContentMode.HTML);
		Label labelTipoUsuario = new Label("<span style=\"font-weight: bold;\">Tipo de usuario:</span> " + tipoUsuario, ContentMode.HTML);		
		
		botonModificarDatos.addClickListener(e -> this.modificarUsuario(navigator));
		botonModificarContraseña.addClickListener(e -> this.modificarContraseña(navigator));
		botonModificarTarjeta.addClickListener(e -> this.mofificarTarjeta(navigator));
		
		FormLayout formLayout = new FormLayout(labelParte1, labelMail, labelNombre, labelApellido, labelFechaNac,
				labelParte2, labelNroTarj, labelMarcaTarj, labelTitTarj , labelFVencTarj,
				labelParte3, labelCreditos, labelFVencCred, labelTipoUsuario);
		
		HorizontalLayout botonesLayout = new HorizontalLayout(botonModificarDatos,botonModificarContraseña,botonModificarTarjeta);
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, formLayout, botonesLayout);
		
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(botonesLayout, Alignment.MIDDLE_CENTER);
		
        setCompositionRoot(mainLayout);
    
	}		
	

	private void mofificarTarjeta(Navigator navigator) {
		navigator.navigateTo("modificarTarjeta");
	
	}


	private void modificarContraseña(Navigator navigator) {
		navigator.navigateTo("modificarContraseña");
	}







	private String ofuscarNro(long n) {
		StringBuilder stb = new StringBuilder(String.valueOf(n));
		if (stb.length() > 3) {
			for (int i = 0; i < stb.length()-3; i++) {
				stb.setCharAt(i, '*');
			}
		} else
			stb = new StringBuilder("***");
		return stb.toString();
	}

	
	private String obtenerTipoUsuario() {
		return (usuario instanceof UsuarioPremium) ? "Usuario Premium" : "Usuario Común";
	}
	

	private void modificarUsuario(Navigator navigator) {
		navigator.navigateTo("modificarPerfil");
	}
	
}
