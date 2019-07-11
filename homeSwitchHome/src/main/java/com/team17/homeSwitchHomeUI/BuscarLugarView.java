package com.team17.homeSwitchHomeUI;


import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.vaadin.grid.cellrenderers.view.BlobImageRenderer;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Propiedad;

@Title("Búsqueda por lugar - HomeSwitchHome")
public class BuscarLugarView extends Composite implements View {  //.necesita composite y view para funcionar correctamente
		
	Label cabecera = new Label("Buscar residencias por lugar");
	TextField textoBuscar = new TextField("Ingrese una localidad");
	Button botonBuscar = new Button("Buscar");
	Label msjAyuda = new Label("Seleccione una residencia para ver sus semanas disponibles.");
	Label msjResultado = new Label();
	Grid<Propiedad> tabla = new Grid<>(Propiedad.class);

	ArrayList<Propiedad> propiedades;

	private MyUI interfaz;
	
	
	public BuscarLugarView(MyUI interfaz) {
		
		this.interfaz = interfaz;
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);		
		
		botonBuscar.addClickListener(e -> buscar());
		
		msjResultado.setVisible(false);
		msjAyuda.setVisible(false);
		tabla.setVisible(false);
		tabla.setWidth("750");
		tabla.setBodyRowHeight(100);		
		
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, textoBuscar, botonBuscar, msjAyuda, msjResultado, tabla);
		mainLayout.setComponentAlignment(textoBuscar, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(botonBuscar, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjAyuda, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(tabla, Alignment.MIDDLE_LEFT);
		
		setCompositionRoot(mainLayout);
    }
	

	private void buscar() {
		
		if (!textoBuscar.isEmpty()) {
			
			ConnectionBD conectar = new ConnectionBD();
			propiedades = new ArrayList<Propiedad>();
			
			try {
				propiedades = conectar.listaResidenciasPorLugar(textoBuscar.getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			//propiedades = new ArrayList<>();  // <-- para chequear cuando no hay residencias cargadas
			if ( propiedades.size() == 0 ) {
				msjAyuda.setVisible(false);
				tabla.setVisible(false);
				msjResultado.setVisible(true);
				msjResultado.setValue("No se encontraron residencias disponibles en esa localidad.");
			} else {
				msjResultado.setVisible(false);
				msjAyuda.setVisible(true);
				tabla.setVisible(true);
				tabla.setItems(propiedades);				
				tabla.setColumns("titulo", "domicilio");
				
				Column<Propiedad, Float> columnaMonto = tabla.addColumn(Propiedad::getMontoBase,
					      new NumberRenderer(new DecimalFormat("¤#######.##")));
				columnaMonto.setCaption("Monto base");
				
				Column<Propiedad, String> columnaDirecta = tabla.addColumn( p -> parseBoolean(p.isDispDirecta()) );
				columnaDirecta.setCaption("Res. directa");
				
				Column<Propiedad, String> columnaSubasta = tabla.addColumn( p -> parseBoolean(p.isDispSubasta()) );
				columnaSubasta.setCaption("En subasta");				
				
				Column<Propiedad, String> columnaHotsale = tabla.addColumn( p -> parseBoolean(p.isDispHotsale()) );
				columnaHotsale.setCaption("En Hotsale");								
				
				BlobImageRenderer<Propiedad> blobRenderer = new BlobImageRenderer<>(-1, 100);			
				tabla.addColumn(Propiedad::getFoto1, blobRenderer).setCaption("Foto");
				
				tabla.addItemClickListener( event -> {
					HomeSwitchHome.setPropiedadActual(event.getItem());
					interfaz.vistaUsuarioConNuevaVista("detalleResidenciaNormal");
				});
			}
		} else {
			msjAyuda.setVisible(false);
			tabla.setVisible(false);
			msjResultado.setVisible(true);
			msjResultado.setValue("Error: Ingrese una localidad.");
		}		
	}
		

	private String parseBoolean (boolean b) {
		return (b) ? "Si" : "No";
	}
		
	
}