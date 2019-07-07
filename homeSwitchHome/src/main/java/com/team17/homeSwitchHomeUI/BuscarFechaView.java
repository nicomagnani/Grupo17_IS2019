package com.team17.homeSwitchHomeUI;


import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import org.vaadin.grid.cellrenderers.view.BlobImageRenderer;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Propiedad;

@Title("Búsqueda por fecha - HomeSwitchHome")
public class BuscarFechaView extends Composite implements View {  //necesita composite y view para funcionar correctamente

	private Label cabecera = new Label("Buscar residencias por fecha");
	private DateField campoFechaIncio = new DateField("Fecha inicio",LocalDate.now().plusMonths(6));
	private DateField campoFechaFin = new DateField("Fecha fin",LocalDate.now().plusMonths(8));
	private Label msjAyuda = new Label("Seleccione una residencia para ver sus semanas disponibles.");
	private Label msjResultado = new Label();
	private Button botonBuscar = new Button("Buscar");	
	private Grid<Propiedad> tabla = new Grid<>(Propiedad.class);
	
	ArrayList<Propiedad> propiedades = new ArrayList<>();

	private MyUI interfaz;
	
	
	public BuscarFechaView(MyUI interfaz) {				
		
		this.interfaz = interfaz;
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		campoFechaIncio.setTextFieldEnabled(false);		
		campoFechaIncio.setResolution(DateResolution.DAY);
		campoFechaIncio.setRangeStart(LocalDate.now().plusMonths(6));
				
		campoFechaFin.setTextFieldEnabled(false);
		campoFechaFin.setResolution(DateResolution.DAY);
		campoFechaFin.setRangeStart(LocalDate.now().plusMonths(6));
				
		botonBuscar.addClickListener(e -> buscar());
		
		msjResultado.setVisible(false);
		msjAyuda.setVisible(false);
		tabla.setVisible(false);		
		tabla.setWidth("750");
		tabla.setBodyRowHeight(100);
		
		
		HorizontalLayout fechas = new HorizontalLayout(campoFechaIncio, campoFechaFin);
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, fechas, botonBuscar, msjAyuda, msjResultado, tabla);
		mainLayout.setComponentAlignment(fechas, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(botonBuscar, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjAyuda, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(tabla, Alignment.MIDDLE_LEFT);
		
		setCompositionRoot(mainLayout);
    }
	
	
	private void buscar() {
		
		LocalDate fechaInicio = campoFechaIncio.getValue();
		LocalDate fechaFin = campoFechaFin.getValue();
		HomeSwitchHome.setFechaInicioBuscada(fechaInicio);
		HomeSwitchHome.setFechaFinBuscada(fechaFin);
		
		/* 1º parte: chequea si la fecha de inicio es mayor a la fecha de fin
		   2º parte: chequea si la fecha de fin es más de 2 meses mayor a la fecha de inicio
		*/		
		if ( (!fechaInicio.isAfter(fechaFin)) && (!fechaFin.isAfter(fechaInicio.plusMonths(2))) ) {
			
			ConnectionBD conectar = new ConnectionBD();
			propiedades = new ArrayList<Propiedad>();
			
			try {
				propiedades = conectar.listaResidenciasPorFecha(fechaInicio, fechaFin);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			//propiedades = new ArrayList<>();   <-- para chequear cuando no hay residencias cargadas
			if ( propiedades.size() == 0 ) {			
				tabla.setVisible(false);
				msjResultado.setValue("No se encontraron residencias en ese rango de fechas.");
			} else {
				msjAyuda.setVisible(true);
				tabla.setVisible(true);
				tabla.setItems(propiedades);
				tabla.setColumns("titulo", "provincia", "localidad", "domicilio");
				
				Column<Propiedad, Float> columnaMonto = tabla.addColumn(Propiedad::getMontoBase,
					      new NumberRenderer(new DecimalFormat("¤#######.##")));
				columnaMonto.setCaption("Monto base");
				
				Column<Propiedad, String> columnaSubasta = tabla.addColumn( p -> parseBoolean(p.isDispSubasta()) );
				columnaSubasta.setCaption("En subasta");				
				
				Column<Propiedad, String> columnaHotsale = tabla.addColumn( p -> parseBoolean(p.isDispHotsale()) );
				columnaHotsale.setCaption("En Hotsale");
				
				BlobImageRenderer<Propiedad> blobRenderer = new BlobImageRenderer<>(-1, 100);			
				tabla.addColumn(Propiedad::getFoto1, blobRenderer).setCaption("Foto");
				
				tabla.addItemClickListener( event -> {
					HomeSwitchHome.setPropiedadActual(event.getItem());
					interfaz.vistaUsuarioConNuevaVista("detalleResidenciaPorFechas");
				});
			}
		} else {
			tabla.setVisible(false);
			msjResultado.setValue("Error: El rango de meses es incorrecto.");
		}		
	}
	
	
	private String parseBoolean (boolean b) {
		return (b) ? "Si" : "No";
	}
	
}
