package com.team17.homeSwitchHomeUI;


import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.vaadin.grid.cellrenderers.view.BlobImageRenderer;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.NumberRenderer;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Propiedad;
import homeSwitchHome.Usuario;


public class ResidenciasUsuarioView extends Composite implements View {
	
	private Usuario u;
	private Label cabecera = new Label();
	private Label msjResultado = new Label("No hay residencias disponibles.");
	private Grid<Propiedad> tabla = new Grid<>(Propiedad.class);
	private ArrayList<Propiedad> propiedades;
		
	public ResidenciasUsuarioView() throws SQLException {
		
		ConnectionBD conectar = new ConnectionBD();
		u = conectar.buscarUsuario(HomeSwitchHome.getUsuarioActual());
		
		cabecera.setValue("<p style=\"text-align: center; font-size: "
				+ "200%;\">Bienvenido <strong>"+u.getNombre()+" "+
				u.getApellido()+"</strong>.</p>");
		cabecera.setContentMode(ContentMode.HTML);		
		
		msjResultado.setVisible(false);
		
		tabla.setVisible(false);
		tabla.setWidth("750");
		tabla.setBodyRowHeight(100);
				
		conectar = new ConnectionBD();
		propiedades = conectar.listaPropiedadesConFotos();		
		
		if ( propiedades.size() == 0 ) {
			tabla.setVisible(false);
			msjResultado.setVisible(true);
		} else {
			tabla.setVisible(true);
			msjResultado.setVisible(false);
			tabla.setItems(propiedades);
			tabla.setColumns("titulo", "provincia", "localidad", "domicilio");
			
			Column<Propiedad, Float> columnaMonto = tabla.addColumn(Propiedad::getMontoBase,
				      new NumberRenderer(new DecimalFormat("¤#######.##")));
			columnaMonto.setCaption("Monto base");
						
			BlobImageRenderer<Propiedad> blobRenderer1 = new BlobImageRenderer<>(-1, 100);			
			tabla.addColumn(Propiedad::getFoto1, blobRenderer1).setCaption("Foto 1");			       
			
			BlobImageRenderer<Propiedad> blobRenderer2 = new BlobImageRenderer<>(-1, 100);			
			tabla.addColumn(Propiedad::getFoto2, blobRenderer2).setCaption("Foto 2");	
			
			BlobImageRenderer<Propiedad> blobRenderer3 = new BlobImageRenderer<>(-1, 100);			
			tabla.addColumn(Propiedad::getFoto3, blobRenderer3).setCaption("Foto 3");	
			
			BlobImageRenderer<Propiedad> blobRenderer4 = new BlobImageRenderer<>(-1, 100);			
			tabla.addColumn(Propiedad::getFoto4, blobRenderer4).setCaption("Foto 4");	
			
			BlobImageRenderer<Propiedad> blobRenderer5 = new BlobImageRenderer<>(-1, 100);			
			tabla.addColumn(Propiedad::getFoto5, blobRenderer5).setCaption("Foto 5");
		}
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, msjResultado, tabla);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(tabla, Alignment.MIDDLE_LEFT);
		
		setCompositionRoot(mainLayout);
	}

		
	private String parseBoolean (boolean b) {
		return (b) ? "Si" : "No";
	}
}