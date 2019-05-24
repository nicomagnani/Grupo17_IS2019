package com.team17.homeSwitchHomeUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.vaadin.ui.NumberField;

import com.google.gwt.user.client.ui.Image;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
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
	
	private FormLayout formulario = new FormLayout();
	private TextField titulo = new TextField("Título");
	private TextArea descripcion = new TextArea("Descripción");
	private TextField pais = new TextField("País");
	private TextField provincia = new TextField("Provincia");
	private TextField localidad = new TextField("Localidad");
	private TextField domicilio = new TextField("Domicilio");
	private NumberField monto = new NumberField("Monto base");
	private Image[] fotos = new Image[5];
	private Button aceptarButton = new Button("Aceptar");
	private Label resultado1 = new Label();
	private Label resultado2 = new Label();
	private Binder<Propiedad> binder = new Binder<>(Propiedad.class);
	private Propiedad propiedad = new Propiedad();
	
	public AgregarResidenciaView() {
		
		//el binder asocia escrito en el formulario a los campos de un objeto Propiedad 
		binder.readBean(propiedad);
		
		binder.bind(titulo, Propiedad::getTitulo, Propiedad::setTitulo);
		binder.bind(descripcion, Propiedad::getDescripcion, Propiedad::setDescripcion);
		binder.bind(pais, Propiedad::getPais, Propiedad::setPais);
		binder.bind(provincia, Propiedad::getProvincia, Propiedad::setProvincia);
		binder.bind(localidad, Propiedad::getLocalidad, Propiedad::setLocalidad);
		binder.bind(domicilio, Propiedad::getDomicilio, Propiedad::setDomicilio);
		binder.forField(monto).withConverter(new StringToFloatConverter("")).
		bind(Propiedad::getMontoBase, Propiedad::setMontoBase);		
		
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
				
		//formulario.addComponent(componente donde se cargan imagenes);
		
		formulario.addComponent(aceptarButton);
		formulario.addComponent(resultado1);
		formulario.addComponent(resultado2);
		
		VerticalLayout mainLayout = new VerticalLayout(formulario);
		
		setCompositionRoot(mainLayout);
    }
	
	private boolean esVacio(String st) {
		return (st == "");
	}
	
	private void aceptar() throws SQLException {		
		boolean montoVacio = false, cumple = true;
		
		//si "monto base" está vacio, le asigna un valor (0) asi no tira error al hacer writeBean(propiedad)
		if (monto.getValue() == "") {
			monto.setValue("0");
			montoVacio = true;
		}
		
		try {
	        binder.writeBean(propiedad);
	      } catch (ValidationException e) {
	    	  e.printStackTrace();
	      }
		
		resultado1.setValue(Boolean.toString(esVacio(propiedad.getTitulo()))+" "+(Boolean.toString(esVacio(propiedad.getDescripcion())))+" "+ 
		(Boolean.toString(esVacio(propiedad.getPais())))+" "+(Boolean.toString(esVacio(propiedad.getProvincia())))+" "+
		(Boolean.toString(esVacio(propiedad.getLocalidad())))+" "+(Boolean.toString(esVacio(propiedad.getDomicilio())))+" "+(Boolean.toString(montoVacio)));
		
		if ((esVacio(propiedad.getTitulo())) || (esVacio(propiedad.getDescripcion())) || 
		(esVacio(propiedad.getPais())) || (esVacio(propiedad.getProvincia())) || 
		(esVacio(propiedad.getLocalidad())) || (esVacio(propiedad.getDomicilio())) || (montoVacio)) {
			resultado1.setValue("Error: Deben completarse todos los campos.");
			cumple = false;
		}
		
//		TODO: chequear si se subieron max 5 imagenes / o se puede chequear una a una mientras se suben 
//		if (true) {
//			resultado2.setValue("Error: Deben subirse a lo sumo 5 fotos o URLs.");
//			cumple = false;
//		}
		
		//si cumple todos los requisitos, cargo la residencia y borro el formulario
		if (cumple) {
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