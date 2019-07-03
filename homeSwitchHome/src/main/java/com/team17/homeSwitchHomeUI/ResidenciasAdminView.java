package com.team17.homeSwitchHomeUI;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractTextField;
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
import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Propiedad;
import homeSwitchHome.Reserva;
import homeSwitchHome.ReservaDirecta;
import homeSwitchHome.ReservaSubasta;

public class ResidenciasAdminView extends Composite implements View {
	
	private Label cabecera = new Label("Lista de residencias (administrador)");
	private Label msjResultado = new Label("No hay residencias cargadas.");	
	
	private VerticalLayout propiedadesLayout = new VerticalLayout();
	private Panel panel = new Panel();
	private Button botonSubastar = new Button("Abrir Subastas");	
	private Notification notifResultado = new Notification("Residencia borrada con éxito.");
	
	private ConnectionBD conexion = new ConnectionBD();

	private ArrayList<Propiedad> propiedades = new ArrayList<>();
	private ArrayList<Reserva> reservas = new ArrayList<>();
	
	private HtmlEmail email = new HtmlEmail();
	
	private MyUI interfaz;
	private Navigator navigator;

	

	public ResidenciasAdminView(MyUI interfaz, Navigator navigator) throws SQLException {
		
		this.interfaz = interfaz;
		this.navigator = navigator;
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		panel.setVisible(false);
		msjResultado.setVisible(false);
		
		propiedadesLayout.setSizeUndefined();
		
		cargarResidencias();
		
		//coloco el layout con las propiedades dentro del panel para poder scrollear
		panel.setContent(propiedadesLayout);
		panel.setHeight("600");
		panel.setWidth("550");
		panel.addStyleName("scrollable");

		botonSubastar.addClickListener(e -> {
			try {
				this.subastarTodo();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		});
		
		try {
			this.inicializarEmail();
		} catch (EmailException e1) {
			e1.printStackTrace();
		}
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, panel, botonSubastar, msjResultado);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(botonSubastar, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);
		
        setCompositionRoot(mainLayout);
	}
	

	private void inicializarEmail() throws EmailException {

		email.setHostName("localhost");
		email.setSmtpPort(9090);
		email.setAuthentication("homeswitchhome@outlook.com.ar", "1234");		
		email.setFrom("homeswitchhome@outlook.com.ar");
		email.setSubject("Propiedad eliminada");
		
		String mensaje = "<p>Estimado usuario, la propiedad que se encontraba en subasta"
				+ " y en la cual usted había ofertado ha sido eliminada. Sepa disculpar las molestias.</p><p>Atte. Staff"
				+ " de <span style=\"text-decoration: underline;\">HomeSwitchHome</span></p>";
		email.setHtmlMsg(mensaje);
		
	}


	private void cargarResidencias() throws SQLException {		
		
		propiedades = conexion.listaPropiedadesConFotos();
		
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
		
		Label titulo = new Label("<p><span style=\"text-align: left; font-weight: bold; font-size: 120%;\">Título:</span> <span style=\"font-size: 120%;\">"
						+propiedad.getTitulo()+"</span></p>", ContentMode.HTML);
		Label ubicacion = new Label("<span style=\"font-weight: bold;\">Ubicación:</span> " + propiedad.getPais() + ", " +
						propiedad.getProvincia() + ", " + propiedad.getLocalidad(), ContentMode.HTML);	
				
		Label descripcion = new Label("<span style=\"font-weight: bold;\">Descripción:</span> " + propiedad.getDescripcion(), ContentMode.HTML);
		Label montoBase = new Label("<span style=\"font-weight: bold;\">Monto base:</span> " + String.valueOf(propiedad.getMontoBase()), ContentMode.HTML);
		
		Button verFotos = new Button("Ver Fotos");
		Button verDetalle = new Button("Ver Detalle", e -> {
			HomeSwitchHome.setPropiedadActual(propiedad);			
			interfaz.vistaAdminConDetalle();
		});
		Button modificar = new Button("Modificar", e -> this.modificar(propiedad));
		Button eliminar = new Button("Eliminar");
	
    	Image foto1 = new Image("Foto 1");
    	Image foto2 = new Image("Foto 2");
    	Image foto3 = new Image("Foto 3");
    	Image foto4 = new Image("Foto 4");
    	Image foto5 = new Image("Foto 5");    	
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
    				foto1.setWidth(100, Unit.PIXELS);
    				foto1.setVisible(true);
    			}
    			
    			if (propiedad.getFoto2() != null) {
    				cargarFoto(foto2, propiedad.getFoto2());
    				foto2.setWidth(100, Unit.PIXELS);
    				foto2.setVisible(true);
    			}
    			
    			if (propiedad.getFoto3() != null) {
					cargarFoto(foto3, propiedad.getFoto3());
					foto3.setWidth(100, Unit.PIXELS);
					foto3.setVisible(true);
    			}
    			
    			if (propiedad.getFoto4() != null) {
    				cargarFoto(foto4, propiedad.getFoto4());
    				foto4.setWidth(100, Unit.PIXELS);
    				foto4.setVisible(true);
    			}
    			
    			if (propiedad.getFoto5() != null) {
    				cargarFoto(foto5, propiedad.getFoto5());
    				foto5.setWidth(100, Unit.PIXELS);
    				foto5.setVisible(true);
    			}
    		});
    	}

    	HorizontalLayout botonesLayout = new HorizontalLayout(verFotos, verDetalle, modificar, eliminar);
    	
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
			} catch (SQLException | EmailException e1) {
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
	private void subastarTodo() throws SQLException {
		
		int n1 = 0;
		LocalDate hace6meses = LocalDate.now().minusMonths(6);
		
		reservas = conexion.listaReservas();
		
		if (!reservas.isEmpty()) {			
			//recorre la lista de reservas
			for (Reserva reserva : reservas) {
				
				//chequea si cumple requisitos para iniciar subasta
				if ( (reserva.getEstado() == EstadoDeReserva.DISPONIBLE_DIRECTA) && 
						(!reserva.getFechaInicio().isAfter(hace6meses)) &&
						(!reserva.getFechaInicio().isBefore(hace6meses.minusDays(3))) ) {					
					conexion.comenzarReservaSubasta(reserva);
					n1++;
					//TODO: guardar informacion de la reserva/residencia) para mostrarla en la notifiacion					
				} //fin chequeo
			}
			if (n1 > 0) {
				//TODO: mostrar informacion de las subastas abiertas
				mostrarNotificacion("Se abrieron "+n1+" subastas.", Notification.Type.HUMANIZED_MESSAGE);
				interfaz.vistaAdmin("residenciasAdmin");
			} else {
				mostrarNotificacion("No se abrió ninguna subasta.", Notification.Type.HUMANIZED_MESSAGE);
			}
		} else 
			mostrarNotificacion("No hay reservas cargadas.", Notification.Type.HUMANIZED_MESSAGE);
		
	}
	
	
	private void modificar(Propiedad propiedad) {
		//TODO
	}
	
	
	private void eliminar(Propiedad propiedad, FormLayout propiedadLayout) throws SQLException, EmailException {		
		
		int n = 0; //cantidad de ofertantes informados o reservas eliminadas 
		
		propiedad.setReservas( conexion.listaReservasPorPropiedad(propiedad.getTitulo(), propiedad.getLocalidad()) );
		
		if (!propiedad.hayReservasRealizadas()) {
			
			if (propiedad.haySubastasEncurso()) {				
				for (Reserva reserva : propiedad.getReservas()) {
					if (reserva.getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA) {
						ReservaSubasta reserva2 = conexion.buscarSubasta(reserva.getPropiedad(), reserva.getLocalidad(), reserva.getFechaInicio(), reserva.getEstado());
						for (String usuario : reserva2.getUsuarios() )  {
							if (this.agregarReceptorDeEmail(usuario))
								n++;
						}
						email.send();
					}
				}
				if (n > 0) {
					mostrarNotificacion("Residencia en subasta borrada con éxito y "+n+" ofertantes informados vía email.", Notification.Type.HUMANIZED_MESSAGE);
				} else
					mostrarNotificacion("Residencia en subasta y sin ofertas borrada con éxito.", Notification.Type.HUMANIZED_MESSAGE);interfaz.vistaAdmin("residenciasAdmin");
				
			} else
				mostrarNotificacion("Residencia sin reservas borrada con éxito.", Notification.Type.HUMANIZED_MESSAGE);			
			 	
		} else {			
			//devolver creditos de reservas directas
			for (Reserva r : propiedad.getReservas()) { 
				if ( (r instanceof ReservaDirecta) && (r.getEstado() == EstadoDeReserva.RESERVADA) )
					conexion.agregarCredito(r.getUsuario());
			}
			
			mostrarNotificacion("Residencia con "+propiedad.getReservas().size()+" reservas borrada con éxito.", Notification.Type.ERROR_MESSAGE);			
		}
		
		conexion.eliminarResidencia(propiedad);
		interfaz.vistaAdmin("residenciasAdmin");
		
	}
	
	
	//devuelve true si tuvo exito
	private boolean agregarReceptorDeEmail (String mail) {
		
		try {
			email.addTo(mail);
			return true;
		} catch (EmailException e) {
			e.printStackTrace();
		}
		
		return false;
	}


	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	
		notifResultado = new Notification(st, tipo);
    	notifResultado.setDelayMsec(5000);
    	notifResultado.show(Page.getCurrent());
    }
	
}