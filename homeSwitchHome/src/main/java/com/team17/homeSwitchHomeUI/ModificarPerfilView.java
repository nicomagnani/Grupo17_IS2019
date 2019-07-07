package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Usuario;

@Title("Modificar cuenta - HomeSwitchHome")
public class ModificarPerfilView extends Composite implements View {  //.necesita composite y view para funcionar correctamente
	
	Label cabecera = new Label("Modificar datos generales");
	Label labelParte1 = new Label("<span style=\"text-align: left; font-weight: bold; text-decoration: underline; font-size: 120%;\">Datos generales</span>", ContentMode.HTML);
	TextField textoEmail = new TextField("Email:");	
	TextField textoNombre = new TextField("Nombre:");
	TextField textoApellido = new TextField("Apellido:");
	DateField fechaNac = new DateField("Fecha de nacimiento:");
	
	Button botonAceptar = new Button("Modificar");
	Button botonCancelar= new Button("Cancelar");
	Usuario usuario;
	ConnectionBD conectar;
	Notification notification = new Notification("sd");
	
	
	public ModificarPerfilView(Navigator navigator, MyUI interfaz) {

		usuario = HomeSwitchHome.getUsuarioActual();
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
	   
		textoEmail.setValue(usuario.getMail());
		textoNombre.setValue(usuario.getNombre());
		textoApellido.setValue(usuario.getApellido());
		fechaNac.setValue(usuario.getfNac());
		fechaNac.setRangeEnd(LocalDate.now().minusYears(18));		
		
		botonAceptar.addClickListener( e -> modificar(interfaz) );
		botonCancelar.addClickListener(e-> cancelar(navigator));
		
		FormLayout layout1 = new FormLayout(textoEmail, textoNombre, textoApellido, fechaNac);			
		HorizontalLayout botonesLayout = new HorizontalLayout(botonAceptar, botonCancelar);
		VerticalLayout mainLayout = new VerticalLayout(cabecera, labelParte1, layout1, botonesLayout);
		
		mainLayout.setComponentAlignment(botonesLayout, Alignment.MIDDLE_CENTER);
			
		setCompositionRoot(mainLayout);		
    }
	
	
	private void cancelar(Navigator navigator) {
		navigator.navigateTo("miPerfil");
	}
	
	
	private void modificar(MyUI interfaz) {
		
		ConnectionBD conectar = new ConnectionBD();
		
		@SuppressWarnings("unused") // <- listaUsuarios se usa para conectar a la base de datos
		ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
		usuario = HomeSwitchHome.getUsuarioActual();
		
		try {
			listaUsuarios = conectar.listaUsuarios();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//si los campos están vacíos, informa error
		if (textoEmail.isEmpty() || textoNombre.isEmpty() || textoApellido.isEmpty() || fechaNac.isEmpty()) {
			this.mostrarNotificacion("Error: Hay campos vacíos.", Notification.Type.ERROR_MESSAGE);
		} else {
			//si el mail cambió y ya está registrado, informa error 
			if( (!usuario.getMail().equals(textoEmail.getValue())) && mailYaRegistrado() ) {
				this.mostrarNotificacion("Error: El mail ya existe en el sistema, por favor ingrese otro mail.", Notification.Type.ERROR_MESSAGE);
			} else {
				
				//modificar datos y actualizar usuario actual
				try {
					conectar.ModificarPerfil(usuario.getMail(),textoEmail.getValue(),textoNombre.getValue(),textoApellido.getValue(),fechaNac.getValue());
					HomeSwitchHome.setUsuarioActual(conectar.buscarUsuario(textoEmail.getValue()));
					
					//informa resultado y actualiza la vista solo si no ocurre excepción
					this.mostrarNotificacion("Modificación exitosa", Notification.Type.HUMANIZED_MESSAGE);
					interfaz.vistaUsuario("miPerfil");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
       }
	}
	

	private boolean mailYaRegistrado() {
		ConnectionBD conectar = new ConnectionBD();
		ArrayList<Usuario>listaUsuarios=new ArrayList<Usuario>();
	       
		try {
			listaUsuarios= conectar.listaUsuarios();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for	( Usuario usuario : listaUsuarios) {
			if ( usuario.getMail().equals(textoEmail.getValue()) )
				return true;
		}
		
		return false;	       
	}
	
	
	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	notification = new Notification(st, tipo);
    	notification.setDelayMsec(5000);
		notification.show(Page.getCurrent());
    }
	
}