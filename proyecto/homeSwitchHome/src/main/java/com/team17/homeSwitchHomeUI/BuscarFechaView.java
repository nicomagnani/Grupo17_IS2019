package com.team17.homeSwitchHomeUI;


import java.time.LocalDate;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class BuscarFechaView extends Composite implements View {  //necesita composite y view para funcionar correctamente

	public BuscarFechaView() {		
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
		buscar.addClickListener(e -> aceptar(fecha1.getValue(), fecha2.getValue(), msjError));
		
		HorizontalLayout fechas = new HorizontalLayout(fecha1, fecha2);
		VerticalLayout mainLayout = new VerticalLayout(fechas, buscar, msjError);
		
		setCompositionRoot(mainLayout);
    }
	
	private void aceptar(LocalDate fecha1, LocalDate fecha2, Label msjError) {
	     /* 1º chequea si la fecha de inicio es anterior o igual a la fecha de fin.
			2º chequea cuando ambas estan en el mismo año.
			3º chequea cuando la fecha de inicio es en diciembre y la de fin es en enero */
		
			if ((!fecha1.isAfter(fecha2)) &&
				    (((fecha1.getYear() == fecha2.getYear()) && (fecha1.getMonthValue() >= fecha2.getMonthValue()-1) && (fecha1.getMonthValue() <= fecha2.getMonthValue())) || //si la fecha 
					 ((fecha1.getYear() == fecha2.getYear()-1) && (fecha1.getMonthValue() == fecha2.getMonthValue()+11))))
			{
			// TODO: buscar reservas disponibles entre las fechas de inicio y fin (fecha1 <= fecha_reserva <= fecha2)
				msjError.setValue("Éxito");
			} else
				msjError.setValue("Error: El rango de meses es incorrecto");
		//	msjError.setValue(fecha1.toString()+" "+fecha2.toString()); <-- usado para probar que los calendarios funcionan correctamente
	}
	
}