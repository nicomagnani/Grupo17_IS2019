package com.team17.homeSwitchHomeUI;


import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.vaadin.grid.cellrenderers.view.BlobImageRenderer;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.Propiedad;


public class BuscarLugarView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	Label cabecera = new Label("Buscar residencias por lugar");
	TextField txtBusqueda = new TextField("Ingrese una localidad");
	Button botonBuscar = new Button("Buscar");
	Label msjError = new Label("Error: No se encontraron residencias disponibles en esa localidad");
	Grid<Propiedad> tabla = new Grid<>(Propiedad.class);

	ArrayList<Propiedad> propiedades;
	
	
	public BuscarLugarView() {
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);		
		
		botonBuscar.addClickListener(e -> buscar());
		msjError.setVisible(false);
		tabla.setVisible(false);
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, txtBusqueda, botonBuscar, msjError, tabla);
		
		setCompositionRoot(mainLayout);
    }
	

	private void buscar() {
		
		if (!txtBusqueda.isEmpty()) {
			
			ConnectionBD conectar = new ConnectionBD();
			propiedades = new ArrayList<Propiedad>();
			
			try {
				propiedades = conectar.listaPropiedadesPorLugar(txtBusqueda.getValue());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
//			propiedades = new ArrayList<>();   <-- para chequear cuando no hay residencias cargadas
			if ( propiedades.size() == 0 ) {
				tabla.setVisible(false);
				msjError.setVisible(true);
			} else {
				tabla.setVisible(true);
				msjError.setVisible(false);
				tabla.setItems(propiedades);
				tabla.setWidth("750");
				tabla.setBodyRowHeight(100);
				tabla.setColumns("titulo", "descripcion", "pais", "provincia", "localidad", "domicilio");
				
				Column<Propiedad, Float> columnaMonto = tabla.addColumn(Propiedad::getMontoBase,
					      new NumberRenderer(new DecimalFormat("¤#######.##")));
				columnaMonto.setCaption("Monto base");
				
				
				BlobImageRenderer<Propiedad> blobRenderer = new BlobImageRenderer<>(-1, 100);			
				tabla.addColumn(Propiedad::getFoto1, blobRenderer).setCaption("Foto");
			}
			
		}
	}
}