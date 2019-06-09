package com.team17.homeSwitchHomeUI;


import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.Propiedad;

public class BuscarLugarView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	public BuscarLugarView() {
		Label cabecera = new Label("Buscar residencias por lugar");
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		
		TextField txtBusqueda = new TextField("Ingrese una localidad");
		Button botonBuscar = new Button("Buscar");
		Label msjError = new Label();
		Grid<Propiedad> tabla = new Grid<>(Propiedad.class);
		
		botonBuscar.addClickListener(e -> buscar(txtBusqueda, msjError, tabla));	
		tabla.setVisible(false);
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, txtBusqueda, botonBuscar, msjError, tabla);
		
		setCompositionRoot(mainLayout);
    }

	private void buscar(TextField txtBusqueda, Label msjError, Grid<Propiedad> tabla) {
		boolean encontro = false;
		if (!txtBusqueda.isEmpty()) {
			encontro = true;
			//buscar localidad "st" en la base de datos
			//si encuentra al menos una, entonces (encontro = true)
		}
		if (encontro) {
			tabla.setVisible(true);
			msjError.setValue("");
			
		} else {
			tabla.setVisible(false);
			msjError.setValue("Error: No se encontraron residencias en esa localidad");
		}
	}
}