package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Usuario;

public class ModificarPerfilView extends Composite implements View {  //.necesita composite y view para funcionar correctamente
	
	Label cabecera = new Label("Modificar datos");
	Label labelParte1 = new Label("<span style=\"text-align: left; font-weight: bold; text-decoration: underline; font-size: 120%;\">Datos generales</span>", ContentMode.HTML);
	TextField textoEmail = new TextField("Email:");	
	TextField textoNombre = new TextField("Nombre:");
	TextField textoApellido = new TextField("Apellido:");
	
	Button botonAceptar = new Button("Modificar");
	Button botonCancelar= new Button("Cancelar");
	Usuario usuario;
	ConnectionBD conectar;
	Notification notification = new Notification("sd");
	
	
	public ModificarPerfilView(Navigator navigator, MyUI interfaz) {

	ConnectionBD conectar = new ConnectionBD();		
		usuario = HomeSwitchHome.getUsuarioActual();				
		textoEmail.setValue(usuario.getMail());
		textoNombre.setValue(usuario.getNombre());
		textoApellido.setValue(usuario.getApellido());
		
		botonAceptar.addClickListener( e -> modificar(interfaz) );
		botonCancelar.addClickListener(e-> cancelar(navigator));
		
		FormLayout layout1 = new FormLayout(textoEmail, textoNombre, textoApellido);			
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
       ArrayList<Usuario>listaUsuarios = new ArrayList<Usuario>();
       usuario = HomeSwitchHome.getUsuarioActual();
       try {
	       listaUsuarios = conectar.listaUsuarios();
       } catch (SQLException e) {
	    // TODO Auto-generated catch block
	   e.printStackTrace();
       }
       if (textoEmail.isEmpty()||textoNombre.isEmpty()|| textoApellido.isEmpty()) {
    	   this.mostrarNotificacion("Error: Hay campos vacíos.", Notification.Type.ERROR_MESSAGE);
       }else {
    	   if( (!usuario.getMail().equals(textoEmail.getValue())) && mailYaRegistrado() ) {
    		   this.mostrarNotificacion("Error: El mail ya existe en el sistema, por favor ingrese otro mail.", Notification.Type.ERROR_MESSAGE);
    	   }else {	   	   
    	   conectar.ModificarPerfil(usuario.getMail(),textoEmail.getValue(),textoNombre.getValue(),textoApellido.getValue());
    	   
    	   try {
				HomeSwitchHome.setUsuarioActual(conectar.buscarUsuario(textoEmail.getValue()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	   
    	   this.mostrarNotificacion("Modificación exitosa", Notification.Type.HUMANIZED_MESSAGE);
    	   interfaz.vistaUsuario("miPerfil");
    	   
       }
       }
	}

	private Boolean mailYaRegistrado() {
		ConnectionBD conectar = new ConnectionBD();
	       ArrayList<Usuario>listaUsuarios=new ArrayList<Usuario>();
	       try {
		          listaUsuarios= conectar.listaUsuarios();
	       } catch (SQLException e) {
		 
		   e.printStackTrace();
	       }
	       for(int i=0;i<listaUsuarios.size();i++) {
	    	   if(listaUsuarios.get(i).getMail().equals(textoEmail.getValue())){
	    		   return true;
	    	   }
	       }
	       return false;
	       
	}
	
	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	notification = new Notification(st, tipo);
    	notification.setDelayMsec(5000);
		notification.show(Page.getCurrent());
    }
	
}