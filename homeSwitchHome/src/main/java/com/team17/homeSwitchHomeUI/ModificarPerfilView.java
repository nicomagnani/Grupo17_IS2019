package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.vaadin.ui.NumberField;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.StringToShortConverter;
import homeSwitchHome.Tarjeta;
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
	ConnectionBD conectar ;
	public ModificarPerfilView(Navigator navigator) {

	ConnectionBD conectar = new ConnectionBD();		
		try {
			usuario = conectar.buscarUsuario(HomeSwitchHome.getUsuarioActual());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}				
		textoEmail.setValue(usuario.getMail());
		textoNombre.setValue(usuario.getNombre());
		textoApellido.setValue(usuario.getApellido());
		
		botonAceptar.addClickListener( e -> modificar(navigator) );
		botonCancelar.addClickListener(e-> cancelar(navigator));
		
		FormLayout layout1 = new FormLayout(textoEmail, textoNombre, textoApellido);			
		VerticalLayout mainLayout = new VerticalLayout(cabecera, labelParte1, layout1,botonAceptar,botonCancelar);
			
		setCompositionRoot(mainLayout);		
    }
	
	private void cancelar(Navigator navigator) {
		navigator.navigateTo("miPerfil");
	}
	
	private void modificar(Navigator navigator) {		
       ConnectionBD conectar = new ConnectionBD();
       ArrayList<Usuario>listaUsuarios=new ArrayList<Usuario>();
       try {
	       usuario = conectar.buscarUsuario(HomeSwitchHome.getUsuarioActual());
	       listaUsuarios= conectar.listaUsuarios();
       } catch (SQLException e) {
	    // TODO Auto-generated catch block
	   e.printStackTrace();
       }
       if (textoEmail.isEmpty()||textoNombre.isEmpty()|| textoApellido.isEmpty()) {
    	   Notification.show("hay campos vacios");
       }else {
    	   if(mailYaRegistrado()){
    		   Notification.show("el mail ya existe en el sistema, por favor ingrese otro mail");
    	   }else {	   	   
    	   conectar.ModificarPerfil(usuario.getMail(),textoEmail.getValue(),textoNombre.getValue(),textoApellido.getValue());
    	   Notification.show("modificando datos");
    	   navigator.navigateTo("miPerfil");
    	   
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
	
}