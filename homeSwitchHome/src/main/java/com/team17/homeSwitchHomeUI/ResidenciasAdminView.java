package com.team17.homeSwitchHomeUI;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.EstadoDeReserva;
import homeSwitchHome.Propiedad;
import homeSwitchHome.Reserva;

public class ResidenciasAdminView extends Composite implements View {
	
	private Label cabecera = new Label("Lista de residencias (administrador)");
	private Label msjResultado = new Label("No hay residencias cargadas.");	
	
	private VerticalLayout propiedadesLayout = new VerticalLayout();
	private Panel panel = new Panel();
	private Button botonSubastar = new Button("Abrir Subastas");	
	private Notification notifResultado = new Notification("Residencia borraada con éito.");
	
	private ConnectionBD conexion = new ConnectionBD();

	private ArrayList<Propiedad> propiedades = new ArrayList();
	private ArrayList<Reserva> reservas = new ArrayList();
	private Propiedad propiedad;
	private Reserva reserva;
	
		
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

		botonSubastar.addClickListener(e -> this.subastarTodo());
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, panel, botonSubastar, msjResultado);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(botonSubastar, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);
		
        setCompositionRoot(mainLayout);
	}
	

	private void cargarResidencias() {
		
		
		try {
			propiedades = conexion.listaPropiedadesConFotos();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!propiedades.isEmpty()) {
			panel.setVisible(true);
			for (int i=0; i < propiedades.size(); i++) {
				this.añadirResidencia(propiedades.get(i));
			}
		} else {
			msjResultado.setVisible(true);
			botonSubastar.setVisible(false);			
		}
		
	}
	
	
	private void añadirResidencia(Propiedad propiedad) {
		
    	Label titulo = new Label("Título: "+propiedad.getTitulo());
		Label ubicacion = new Label("Ubicación: "+ propiedad.getPais() + ", " +
				propiedad.getProvincia() + ", " + propiedad.getLocalidad() +
				", " + propiedad.getDomicilio());
		Label descripcion = new Label ("Descripción: "+propiedad.getDescripcion());
		Label montoBase = new Label ("Monto base: " + String.valueOf(propiedad.getMontoBase()));
		
		Button verFotos = new Button("Ver Fotos");	
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

    	HorizontalLayout botonesLayout = new HorizontalLayout(verFotos, modificar, eliminar);
    	
    	HorizontalLayout fotosLayout = new HorizontalLayout(foto1,foto2,foto3,foto4,foto5);
    	fotosLayout.setWidth("650");
    	fotosLayout.addStyleName("scrollable");

		Label separador = new Label("____________________________________________________________________");
		
		FormLayout propiedadLayout = new FormLayout(titulo,ubicacion,descripcion,montoBase,botonesLayout,fotosLayout,separador);
		propiedadLayout.setWidth("500");
		propiedadLayout.setSizeFull();
		propiedadLayout.setComponentAlignment(fotosLayout, Alignment.MIDDLE_CENTER);
		propiedadLayout.setComponentAlignment(separador, Alignment.MIDDLE_CENTER);		
		
		propiedadesLayout.addComponent(propiedadLayout);
		propiedadesLayout.setComponentAlignment(propiedadLayout, Alignment.MIDDLE_CENTER);		
		
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
	private void subastarTodo() {
		int n1 = 0, n2 = 0;
		LocalDate lunesSemanaActual = LocalDate.now().with(DayOfWeek.MONDAY);
		
		try {
			reservas = conexion.listaReservas();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (!reservas.isEmpty()) {			
			//recorre la lista de reservas
			for (Reserva reserva : reservas) {
				
				n2++;
				//chequea si cumple requisitos para iniciar subasta
				if ( (reserva.getEstado() == EstadoDeReserva.DISPONIBLE_DIRECTA) && 
						(reserva.getFechaInicio().isBefore(lunesSemanaActual.minusMonths(6))) ) {
					
					try {
						conexion.comenzarReservaSubasta(reserva);
						n1++;						
					} catch (SQLException e) {
						e.printStackTrace();						
					}
					
				} //fin chequeo
			}
			if (n1 > 0) {
				mostrarNotificacion("Se abrieron "+n1+" subastas de un total de "+n2+" reservas.", Notification.Type.HUMANIZED_MESSAGE);
			} else {
				mostrarNotificacion("No se abrió ninguna subasta.", Notification.Type.HUMANIZED_MESSAGE);
			}
		} else 
			mostrarNotificacion("No hay reservas cargadas.", Notification.Type.HUMANIZED_MESSAGE);
		
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