package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.util.ArrayList;

import org.vaadin.grid.cellrenderers.view.BlobImageRenderer;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import homeSwitchHome.Propiedad;

@Title("HomeSwitchHome")
public class ResidenciasVisitanteView extends Composite implements View {
		
	
	private Image img = new Image();
	private Label cabecera = new Label("<p style=\"text-align: center; font-weight: bold;"
			+ " text-decoration: underline; font-size: 150%;\">Bienvenido a HomeSwitchHome</p>", ContentMode.HTML);
	
	private Label descripcion = new Label("<p style=\"text-align: center; font-size: 120%;\">A través de este servicio"
			+ " los usuarios podrán realizar reservas a residencias.</p><p style=\"text-align: center; font-size: 120%;\">Para"
			+ " acceder al sitio web completo puede iniciar sesión o registrar una cuenta.</p>", ContentMode.HTML);
	
	private Label msj = new Label("No hay residencias disponibles en el sistema.");
	private Grid<Propiedad> tabla = new Grid<>(Propiedad.class);
	
	
	public ResidenciasVisitanteView() throws SQLException {
		
		img.setSource(new ThemeResource("logo.png"));
		
		ConnectionBD conectar = new ConnectionBD();
		ArrayList<Propiedad> propiedades = conectar.listaResidenciasVisitante();		
		
//		propiedades = new ArrayList<>();   <-- para chequear cuando no hay residencias cargadas
		if ( propiedades.size() == 0 )
			tabla.setVisible(false);
		else {
			msj.setVisible(false);
			tabla.setItems(propiedades);
			tabla.setWidth("700");
			tabla.setBodyRowHeight(75);
			tabla.setColumns("titulo", "localidad");
			
			BlobImageRenderer<Propiedad> blobRenderer = new BlobImageRenderer<>(-1, 75);
			tabla.addColumn(Propiedad::getFoto1, blobRenderer).setCaption("Foto");
			tabla.getColumn("titulo").setWidth(300);
			tabla.getColumn("localidad").setWidth(250);
		}
		
		img.setHeight("200");	
	
		VerticalLayout mainLayout = new VerticalLayout(img, cabecera, descripcion, tabla, msj);	
		mainLayout.setComponentAlignment(img, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(descripcion, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(tabla, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msj, Alignment.MIDDLE_CENTER);
		
		setCompositionRoot(mainLayout);
	}
		
}