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
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Propiedad;
import homeSwitchHome.Usuario;


public class ResidenciasUsuarioView extends Composite implements View {
	
	private Usuario u;
	private Label cabecera = new Label("Lista de residencias");;
	private Label msjBienvenida = new Label();
	private Label msjResultado = new Label("No hay residencias disponibles.");
	private Grid<Propiedad> tabla = new Grid<>(Propiedad.class);
	private ArrayList<Propiedad> propiedades;
	private ArrayList<Propiedad> propiedades2 = new ArrayList<>();	
	
		
	public ResidenciasUsuarioView(boolean mostrarCabecera, MyUI interfaz) throws SQLException {
				
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		ConnectionBD conectar = new ConnectionBD();
		u = HomeSwitchHome.getUsuarioActual();
		
		msjBienvenida.setValue("<p style=\"text-align: center; font-size: "
				+ "200%;\">Bienvenido, <strong>"+u.getNombre()+" "+
				u.getApellido()+"</strong>.</p>");
		msjBienvenida.setContentMode(ContentMode.HTML);
		msjBienvenida.setVisible(mostrarCabecera);
		cabecera.setVisible(!mostrarCabecera);		
		
		msjResultado.setVisible(false);
		
		tabla.setVisible(false);
		tabla.setWidth("750");
		tabla.setBodyRowHeight(100);
				
		conectar = new ConnectionBD();
		propiedades = conectar.listaResidenciasConFotos();		
		
		for (Propiedad p : propiedades) {
			p.setReservas(conectar.listaReservasPorPropiedad(p.getTitulo(), p.getLocalidad()));
			if (p.getReservas().size() > 0) {
				p.actualizarTiposDeReservasDisponibles();
				propiedades2.add(p);
			}				
		}	
		
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
			
			Column<Propiedad, String> columnaSubasta = tabla.addColumn( p -> parseBoolean(p.isDispSubasta()) );
			columnaSubasta.setCaption("En subasta");				
			
			Column<Propiedad, String> columnaHotsale = tabla.addColumn( p -> parseBoolean(p.isDispHotsale()) );
			columnaHotsale.setCaption("En Hotsale");
						
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
			
			tabla.addItemClickListener( event -> {
				HomeSwitchHome.setPropiedadActual(event.getItem());
				interfaz.vistaUsuarioConNuevaVista("detalleResidenciaNormal");
			});
			
		}
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, msjBienvenida, msjResultado, tabla);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjBienvenida, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(tabla, Alignment.MIDDLE_LEFT);
		
		setCompositionRoot(mainLayout);
	}

		
	private String parseBoolean (boolean b) {
		return (b) ? "Si" : "No";
	}
}
