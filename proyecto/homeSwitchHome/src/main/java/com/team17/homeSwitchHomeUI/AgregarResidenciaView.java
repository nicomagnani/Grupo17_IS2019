package com.team17.homeSwitchHomeUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
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
	boolean cumple1 = false;
	boolean cumple2 = false;
	boolean cumple3 = false;
	boolean cumple4 = false;
	boolean cumple5 = false;
	boolean cumple6 = false;
	boolean cumpleTodo = false;
	
	public AgregarResidenciaView() {
		
		cumple1 = cumple2 = cumple3 = cumple4 = cumple5 = cumple6 = false;
		
		titulo.addValueChangeListener(e1 -> esVacio1(titulo.getValue()));
		titulo.setValueChangeMode(ValueChangeMode.EAGER);
		
		descripcion.addValueChangeListener(e2 -> esVacio2(descripcion.getValue()));
		descripcion.setValueChangeMode(ValueChangeMode.EAGER);
		
		pais.addValueChangeListener(e3 -> esVacio3(pais.getValue()));
		pais.setValueChangeMode(ValueChangeMode.EAGER);
		
		provincia.addValueChangeListener(e4 -> esVacio4(provincia.getValue()));
		provincia.setValueChangeMode(ValueChangeMode.EAGER);
		
		localidad.addValueChangeListener(e5 -> esVacio5(localidad.getValue()));
		localidad.setValueChangeMode(ValueChangeMode.EAGER);
		
		domicilio.addValueChangeListener(e6 -> esVacio6(domicilio.getValue()));
		domicilio.setValueChangeMode(ValueChangeMode.EAGER);
		
		aceptarButton.addClickListener(e7 -> {
			try {
				aceptar();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});	
		
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
		
			
		
		VerticalLayout mainLayout = new VerticalLayout(formulario);
		
		setCompositionRoot(mainLayout);
    }
	
	private void esVacio1(String st) {
		if (st == "")
			cumple1 = false;
		else cumple1 = true;
	}

	private void esVacio2(String st) {
		if (st == "")
			cumple2 = false;
		else cumple2 = true;
	}
	
	private void esVacio3(String st) {
		if (st == "")
			cumple3 = false;
		else cumple3 = true;
	}
	
	private void esVacio4(String st) {
		if (st == "")
			cumple4 = false;
		else cumple4 = true;
	}
	
	private void esVacio5(String st) {
		if (st == "")
			cumple5 = false;
		else cumple5 = true;
	}
	
	private void esVacio6(String st) {
		if (st == "")
			cumple6 = false;
		else cumple6 = true;
	}
	
	private void aceptar() throws SQLException {
		cumpleTodo = true;
		
		if ((!cumple1) || (!cumple2) || (!cumple3) || (!cumple4) || (!cumple5) || (!cumple6)) {
			resultado1.setValue("Error: Deben completarse todos los campos.");
			cumpleTodo = false;
		}
		
		//TODO: chequear si se subieron max 5 imagenes
		//if () {
		//	resultado2.setValue("Error: Deben subirse a lo sumo 5 fotos o URLs.");
		//	noCumple = false;
		//}
		
		//si cumple todos los requisitos, cargo la residencia y borro el formulario
		if (cumpleTodo) {
			ConnectionBD con = new ConnectionBD();
			con.agregarResidencia(titulo.getValue(), descripcion.getValue(), pais.getValue(), provincia.getValue(), localidad.getValue(), domicilio.getValue());
			resultado1.setValue("Éxito.");
			for ( Component comp : formulario ) {
			    if (comp instanceof AbstractTextField)
			    	((AbstractTextField) comp).setValue("");
			    }
		}
		
	}
}