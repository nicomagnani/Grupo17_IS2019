package com.team17.homeSwitchHomeUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.vaadin.navigator.View;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AgregarResidenciaView extends Composite implements View {  //.necesita composite y view para funcionar correctamente
	
	FormLayout formulario = new FormLayout();;
	TextField titulo = new TextField("Título");
	TextArea descripcion = new TextArea("Descripción");
	TextField pais = new TextField("País");
	TextField provincia = new TextField("Provincia");
	TextField localidad = new TextField("Localidad");
	TextField domicilio = new TextField("Domicilio");
	//TODO: añadir campo para cargar imagenes		
	Button aceptarButton = new Button("Aceptar");
	Label resultado1 = new Label();
	Label resultado2 = new Label();
	
	public AgregarResidenciaView() {
		//formulario.setMargin(true) controla margenes/formato
		
		formulario.addComponent(titulo);
		formulario.addComponent(descripcion);
		formulario.addComponent(pais);
		formulario.addComponent(provincia);
		formulario.addComponent(localidad);
		formulario.addComponent(domicilio);
		//formulario.addComponent(campo donde se cargan imagenes);
		formulario.addComponent(aceptarButton);
		formulario.addComponent(resultado1);
		formulario.addComponent(resultado2);
		
		aceptarButton.addClickListener(e -> aceptar());		
		
		VerticalLayout mainLayout = new VerticalLayout(formulario);
		
		setCompositionRoot(mainLayout);
    }
	
	private void aceptar() {
		boolean cumple = true;
		
		//chequeo si hay algún campo vacío
		if ((titulo.getValue() == "") || (descripcion.getValue() == "") || (pais.getValue() == "") || 
				(provincia.getValue() == "") || (localidad.getValue() == "") || 
				(domicilio.getValue() == "")) {
			resultado1.setValue("Error: Deben completarse todos los campos.");
			cumple = false;
		}
		
		//TODO: chequear si se subieron max 5 imagenes
		//if () {
		//	resultado2.setValue("Error: Deben subirse a lo sumo 5 fotos o URLs.");
		//	noCumple = false;
		//}
		
		//si cumple todos los requisitos, cargo la residencia y borro el formulario
		if (cumple) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("Error al registrar el driver de MySQL: " + e);
				e.printStackTrace();
			}
			
			try {
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost/homeswitchhome","root","");
			Statement s = con.createStatement();
			s.executeUpdate("INSERT INTO propiedades (titulo,descripcion,pais,localidad,domicilio) VALUES ('"+titulo.getValue()+"','"+descripcion.getValue()+"','"+pais.getValue()+"','"+provincia.getValue()+"','"+localidad.getValue()+"','"+domicilio.getValue()+"')");
			} catch (SQLException e1) {
				System.out.println("Error: " + e1);
				e1.printStackTrace();
			}
			
			resultado1.setValue("Éxito.");
			for ( Component comp : formulario ) {
			    if (comp instanceof AbstractTextField)
			    	((AbstractTextField) comp).setValue("");
			    }
		}
		
	}
}