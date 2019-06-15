package com.team17.homeSwitchHomeUI;


import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import org.vaadin.grid.cellrenderers.view.BlobImageRenderer;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.datefield.DateResolution;
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

import homeSwitchHome.Propiedad;

public class BuscarFechaView extends Composite implements View {  //necesita composite y view para funcionar correctamente

	private Label cabecera = new Label("Buscar residencias por fecha");
	private DateField campoFecha1 = new DateField("Fecha inicio",LocalDate.now().plusMonths(6));
	private DateField campoFecha2 = new DateField("Fecha fin",LocalDate.now().plusMonths(8));
	private Label msjResultado = new Label();	
	private Button botonBuscar = new Button("Buscar");	
	private Grid<Propiedad> tabla = new Grid<>(Propiedad.class);
	
	ArrayList<Propiedad> propiedades = new ArrayList<>();
	
	
	public BuscarFechaView() {		
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		campoFecha1.setTextFieldEnabled(false);		
		campoFecha1.setResolution(DateResolution.DAY);
		campoFecha1.setRangeStart(LocalDate.now().plusMonths(6));
				
		campoFecha2.setTextFieldEnabled(false);
		campoFecha2.setResolution(DateResolution.DAY);
		campoFecha2.setRangeStart(LocalDate.now().plusMonths(8));
				
		tabla.setVisible(false);
		
		botonBuscar.addClickListener(e -> buscar());
		
		tabla.setWidth("750");
		tabla.setBodyRowHeight(100);
		
		HorizontalLayout fechas = new HorizontalLayout(campoFecha1, campoFecha2);
		VerticalLayout mainLayout = new VerticalLayout(cabecera, fechas, botonBuscar, msjResultado, tabla);
		
		setCompositionRoot(mainLayout);
    }
	
	
	private void buscar() {
		
		LocalDate fecha1 = campoFecha1.getValue();
		LocalDate fecha2 = campoFecha2.getValue();
		
		/* 1º parte: chequea si la fecha de inicio es mayor a la fecha de fin
		   2º parte: chequea si la fecha de fin es más de 2 meses mayor a la fecha de inicio
		*/		
		if ( (!fecha1.isAfter(fecha2)) && (!fecha2.isAfter(fecha1.plusMonths(2))) ) {
			
			ConnectionBD conectar = new ConnectionBD();
			propiedades = new ArrayList<Propiedad>();
			
			try {
				propiedades = conectar.listaPropiedadesPorFecha(fecha1, fecha2);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			//propiedades = new ArrayList<>();   <-- para chequear cuando no hay residencias cargadas
			if ( propiedades.size() == 0 ) {			
				tabla.setVisible(false);
//				msjResultado.setValue("No se encontraron residencias en ese rango de fechas.");
				msjResultado.setValue(campoFecha1.getValue()+" "+campoFecha2.getValue());
			} else {
				tabla.setVisible(true);
				msjResultado.setValue("Éxito.");				
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
