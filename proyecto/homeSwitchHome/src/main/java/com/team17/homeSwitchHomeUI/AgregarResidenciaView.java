package com.team17.homeSwitchHomeUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.vaadin.ui.NumberField;

import com.vaadin.data.Binder;
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

import homeSwitchHome.Propiedad;

public class AgregarResidenciaView extends Composite implements View {  //.necesita composite y view para funcionar correctamente
	
	FormLayout formulario = new FormLayout();
	TextField titulo = new TextField("Título");
	TextArea descripcion = new TextArea("Descripción");
	TextField pais = new TextField("País");
	TextField provincia = new TextField("Provincia");
	TextField localidad = new TextField("Localidad");
	TextField domicilio = new TextField("Domicilio");
	NumberField monto = new NumberField("Monto base");
	//TODO: añadir campo para cargar imagenes		
	Button aceptarButton = new Button("Aceptar");
	Label resultado1 = new Label();
	Label resultado2 = new Label();
	
	
	boolean[] cumple = {false,false,false,false,false,false,false,false}; //el ultimo elemento es true cuando se completen todos los campos
	
	public AgregarResidenciaView() {
		titulo.addValueChangeListener(e1 -> esVacio(titulo.getValue(),0));
		titulo.setValueChangeMode(ValueChangeMode.EAGER);
		
		descripcion.addValueChangeListener(e2 -> esVacio(descripcion.getValue(),1));
		descripcion.setValueChangeMode(ValueChangeMode.EAGER);
		
		pais.addValueChangeListener(e3 -> esVacio(pais.getValue(),2));
		pais.setValueChangeMode(ValueChangeMode.EAGER);
		
		provincia.addValueChangeListener(e4 -> esVacio(provincia.getValue(),3));
		provincia.setValueChangeMode(ValueChangeMode.EAGER);
		
		localidad.addValueChangeListener(e5 -> esVacio(localidad.getValue(),4));
		localidad.setValueChangeMode(ValueChangeMode.EAGER);
		
		domicilio.addValueChangeListener(e6 -> esVacio(domicilio.getValue(),5));
		domicilio.setValueChangeMode(ValueChangeMode.EAGER);		

		monto.addValueChangeListener(e7 -> esVacio(monto.getValue(),6));
		monto.setValueChangeMode(ValueChangeMode.EAGER);
		
		aceptarButton.addClickListener(e -> {
			try {
				aceptar();
			} catch (SQLException e8) {
				// TODO Auto-generated catch block
				e8.printStackTrace();
			}
		});
		
		formulario.addComponent(titulo);
		formulario.addComponent(descripcion);
		formulario.addComponent(pais);
		formulario.addComponent(provincia);
		formulario.addComponent(localidad);
		formulario.addComponent(domicilio);
		formulario.addComponent(monto);		
		//formulario.addComponent(campo donde se cargan imagenes);
		formulario.addComponent(aceptarButton);
		formulario.addComponent(resultado1);
		formulario.addComponent(resultado2);
		
		VerticalLayout mainLayout = new VerticalLayout(formulario);
		
		setCompositionRoot(mainLayout);
    }
	
	private void esVacio(String st, int i) {
		if (st == null)
			cumple[i] = false;
		else cumple[i] = true;
	}
	
	private void aceptar() throws SQLException {
		cumple[7] = true;
		
		if ((!cumple[0]) || (!cumple[1]) || (!cumple[1]) || (!cumple[3]) || (!cumple[4]) || (!cumple[5]) || (!cumple[6])) {
			resultado1.setValue("Error: Deben completarse todos los campos.");
			cumple[7] = false;
		}
		
		//TODO: chequear si se subieron max 5 imagenes
		//if () {
		//	resultado2.setValue("Error: Deben subirse a lo sumo 5 fotos o URLs.");
		//	noCumple = false;
		//}
		
		//si cumple todos los requisitos, cargo la residencia y borro el formulario
		if (cumple[7] = true) {
			ConnectionBD con = new ConnectionBD();
			con.agregarResidencia(titulo.getValue(), descripcion.getValue(), pais.getValue(), provincia.getValue(), localidad.getValue(),
					domicilio.getValue(),Integer.parseInt(monto.getValue()));
			resultado1.setValue("Éxito.");
			for ( Component comp : formulario ) {
			    if (comp instanceof AbstractTextField)
			    	((AbstractTextField) comp).setValue("");
			    }
		}
		
	}
}