package com.team17.homeSwitchHomeUI;


import java.time.LocalDate;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.Propiedad;

public class BuscarFechaView extends Composite implements View {  //necesita composite y view para funcionar correctamente

	public BuscarFechaView() {		
		Label cabecera = new Label("Buscar residencias por fecha");
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		DateField fecha1 = new DateField("Fecha inicio",LocalDate.now());
		fecha1.setTextFieldEnabled(false);		
		fecha1.setResolution(DateResolution.DAY);
		fecha1.setRangeStart(LocalDate.now());
		
		DateField fecha2 = new DateField("Fecha fin",LocalDate.now());
		fecha2.setTextFieldEnabled(false);
		fecha2.setResolution(DateResolution.DAY);
		fecha1.setRangeStart(LocalDate.now());
		
		Label msjError = new Label();
		
		Button buscar = new Button("Buscar");		
		
		Grid<Propiedad> tabla = new Grid<>(Propiedad.class);
		tabla.setVisible(false);
		
		buscar.addClickListener(e -> aceptar(fecha1.getValue(), fecha2.getValue(), msjError, tabla));
		
		HorizontalLayout fechas = new HorizontalLayout(fecha1, fecha2);
		VerticalLayout mainLayout = new VerticalLayout(cabecera, fechas, buscar, msjError, tabla);
		
		setCompositionRoot(mainLayout);
    }
	
	private void aceptar(LocalDate fecha1, LocalDate fecha2, Label msjError, Grid<Propiedad> tabla) {
		boolean encontro = false;
		
		/* 1º parte: chequea si la fecha de inicio es anterior o igual a la fecha de fin.
		   2º parte: chequea cuando ambas estan en el mismo año.
		   3º parte: chequea cuando la fecha de inicio es en diciembre y la de fin es en enero
		*/		
		if ((!fecha1.isAfter(fecha2)) &&
				(((fecha1.getYear() == fecha2.getYear()) && (fecha1.getMonthValue() >= fecha2.getMonthValue()-1) && (fecha1.getMonthValue() <= fecha2.getMonthValue())) || 
				((fecha1.getYear() == fecha2.getYear()-1) && (fecha1.getMonthValue() == fecha2.getMonthValue()+11))))
		{
			// TODO: buscar reservas disponibles entre las fechas de inicio y fin (fecha1 <= fecha_reserva <= fecha2)
			// si existen residencias con reservas disponibles, entonces (encontro = true)
			
			encontro = true;
			if (encontro == true) {
				msjError.setValue("Éxito"); //usado de prueba; sacar
				tabla.setVisible(true);
				// TODO: volcar propiedades en la tabla
			} else {
				tabla.setVisible(false);
				msjError.setValue("Error: No se encontraron residencias en ese rango de fechas");
			}
		} else {
			tabla.setVisible(false);
			msjError.setValue("Error: El rango de meses es incorrecto");
		}
		//	msjError.setValue(fecha1.toString()+" "+fecha2.toString()); <-- usado para probar que los calendarios funcionan correctamente
	}
	
}