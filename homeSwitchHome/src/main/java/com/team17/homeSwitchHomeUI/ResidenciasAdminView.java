package com.team17.homeSwitchHomeUI;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.Propiedad;

public class ResidenciasAdminView extends Composite implements View {
	
	private Label cabecera = new Label("Lista de residencias (administrador)");
	private Label msjResultado = new Label("No hay residencias cargadas.");	
	
	private VerticalLayout propiedadesLayout = new VerticalLayout();
	private Panel panel = new Panel();
	private Notification notifResultado = new Notification("Residencia borraada con éito.");
		
	private ConnectionBD conexion = new ConnectionBD();
	


	
		
	public ResidenciasAdminView() {
				
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		panel.setVisible(false);
		msjResultado.setVisible(false);		
		
		propiedadesLayout = new VerticalLayout();
		propiedadesLayout.setSizeUndefined();
		
		cargarResidencias();
		
		//coloco el layout con las propiedades dentro del panel para poder scrollear
		panel.setContent(propiedadesLayout);
		panel.setHeight("750");
		panel.setWidth("550");
		panel.addStyleName("scrollable");
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, panel, msjResultado);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);
		
        setCompositionRoot(mainLayout);
	}
	

	private void cargarResidencias() {
		
		Iterator<Propiedad> propiedades = null;
		
		try {
			propiedades = conexion.listaPropiedadesConFotos().iterator();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (propiedades != null) {
			panel.setVisible(true);
			while (propiedades.hasNext()) {
				this.añadirResidencia( (Propiedad)propiedades.next() );
			}
		} else
			msjResultado.setVisible(true);
		
	}
	
	
	private void añadirResidencia(Propiedad propiedad) {
		
    	Label titulo = new Label("Título: "+propiedad.getTitulo());
		Label ubicacion = new Label("Ubicación: "+ propiedad.getPais() + ", " +
				propiedad.getProvincia() + ", " + propiedad.getLocalidad() +
				", " + propiedad.getDomicilio());
		Label descripcion = new Label ("Descripción: "+propiedad.getDescripcion());
		Label montoBase = new Label ("Monto base: " + String.valueOf(propiedad.getMontoBase()));
		
		Button verFotos = new Button("Ver Fotos");
		Button subastar = new Button("Abrir Subasta", e -> this.subastar(propiedad));		
		Button modificar = new Button("Modificar", e -> this.modificar(propiedad));
		Button eliminar = new Button("Eliminar");		
	
    	Image foto1 = new Image("Foto 1");
    	Image foto2 = new Image("Foto 2");
    	Image foto3 = new Image("Foto 3");
    	Image foto4 = new Image("Foto 4");
    	Image foto5 = new Image("Foto 5");   
    	foto1.setWidth(100, Unit.PIXELS);
    	foto2.setWidth(100, Unit.PIXELS);
    	foto3.setWidth(100, Unit.PIXELS);
    	foto4.setWidth(100, Unit.PIXELS);
    	foto5.setWidth(100, Unit.PIXELS);
		foto1.setVisible(false);
		foto2.setVisible(false);
		foto3.setVisible(false);
		foto4.setVisible(false);
		foto5.setVisible(false);
    	
    	if ( (propiedad.getFoto1() == null) && (propiedad.getFoto2() == null) && (propiedad.getFoto3() == null)
    			&& (propiedad.getFoto4() == null) && (propiedad.getFoto5() == null) ) {
    		verFotos.setVisible(false);
    	}
    	else {
    		verFotos.addClickListener(e -> {
    			if (propiedad.getFoto1() != null) {
    				cargarFoto(foto1, propiedad.getFoto1());
    				foto1.setVisible(true);
    			}
    			
    			if (propiedad.getFoto2() != null) {
    				cargarFoto(foto2, propiedad.getFoto2());
    				foto2.setVisible(true);
    			}
    			
    			if (propiedad.getFoto3() != null) {
					cargarFoto(foto3, propiedad.getFoto3());
					foto3.setVisible(true);
    			}
    			
    			if (propiedad.getFoto4() != null) {
    				cargarFoto(foto4, propiedad.getFoto4());
    				foto4.setVisible(true);
    			}
    			
    			if (propiedad.getFoto5() != null) {
    				cargarFoto(foto5, propiedad.getFoto5());
    				foto5.setVisible(true);
    			}
    		});
    	}

    	HorizontalLayout botonesLayout = new HorizontalLayout(verFotos, subastar, modificar, eliminar);
    	
    	HorizontalLayout fotosLayout = new HorizontalLayout(foto1,foto2,foto3,foto4,foto5);
    	fotosLayout.setWidth("650");
    	fotosLayout.addStyleName("scrollable");

		Label separador = new Label("____________________________________________________________________");
		
		FormLayout propiedadLayout = new FormLayout(titulo,ubicacion,descripcion,montoBase,botonesLayout,fotosLayout,separador);
		propiedadLayout.setWidth("500");
		propiedadLayout.setSizeFull();
		
		propiedadesLayout.addComponent(propiedadLayout);
		
		
		eliminar.addClickListener(e -> {
			try {
				this.eliminar(propiedad, propiedadLayout);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
	}
	
	
	private void cargarFoto(Image image, byte[] foto) {
		StreamResource resource = new StreamResource(
	            new StreamResource.StreamSource() {
	                @Override
	                public InputStream getStream() {
	                    return new ByteArrayInputStream(foto);
	                }
	            }, "filename.png");
	    image.setSource(resource);
	}
	
	//devuelve true si la subasta fue un exito
	private boolean subastar(Propiedad propiedad) {
		//TODO
		return true;
	}
	
	private void modificar(Propiedad propiedad) {
		//TODO
	}
	
	private void eliminar(Propiedad propiedad, FormLayout propiedadLayout) throws SQLException {
		//TODO
		conexion.eliminarResidencia(propiedad);
		propiedadLayout.setVisible(false);
		mostrarNotificacion("Residencia borrada con éxito.", Notification.Type.HUMANIZED_MESSAGE);
	}
	
	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	notifResultado = new Notification(st, tipo);
    	notifResultado.setDelayMsec(5000);
    	notifResultado.show(Page.getCurrent());
    }
	
}