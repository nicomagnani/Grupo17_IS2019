package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.Solicitud;

@Title("Solicitudes de Premium - HomeSwitchHome")
public class SolicitudesPremiumView extends Composite implements View {
	
	private Label cabecera = new Label("Solicitudes de alta/baja de premium");
	private Label msjResultado = new Label("No existen solicitudes pendientes.");
	private Notification notifResultado = new Notification("");
	
	private VerticalLayout solicitudesLayout = new VerticalLayout();
	private Panel panel = new Panel();	
	
	private ConnectionBD conexion = new ConnectionBD();
	private HtmlEmail email;
	private ArrayList<Solicitud> solicitudes = new ArrayList<>();
	private int cantAgregadas = 0;
	private int cantOcultadas = 0;
	
	
		
	
	public SolicitudesPremiumView() {
		
		this.cargarSolicitudes();
		this.inicializarComponentes();
		this.inicializarLayouts();
	}

	
	private void cargarSolicitudes() {

		try {
			solicitudes = conexion.listaSolicitudes();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}


	private void inicializarComponentes() {

		cabecera.addStyleName(ValoTheme.MENU_TITLE);		

		msjResultado.setVisible(false);
		panel.setVisible(false);
				
		//configuro la tabla de reservas (admin)
		if (solicitudes.isEmpty()) {			
			msjResultado.setVisible(true);
		} else {
			panel.setVisible(true);
			
			for ( Solicitud s : solicitudes ) {
				this.agregarSolicitud(s);
				cantAgregadas++;
			}
		}		
	}


	private void agregarSolicitud(Solicitud s) {
			
			Label labelMail = new Label("<p><span style=\"text-align: left; font-weight: bold; font-size: 100%;\">Usuario:</span> <span style=\"font-size: 100%;\">"
							+s.getMail()+"</span></p>", ContentMode.HTML);
			
			Label labelTipoActual = new Label("<span style=\"font-weight: bold;\">Estado actual:</span> " + this.obtenerTipoUsuario(s.getTipo()), ContentMode.HTML);	
					
			Label labelTipoSolicitud = new Label("<span style=\"font-weight: bold;\">Tipo de solicitud:</span> " + this.obtenerTipoSolicitud(s.getTipo()), ContentMode.HTML);
			
			Label labelMotivo = new Label("<span style=\"font-weight: bold;\">Motivo:</span> " + s.getMotivo(), ContentMode.HTML);
			
			Button botonAceptar = new Button("Aceptar solicitud");
			Button botonRechazar = new Button("Rechazar");
			
			HorizontalLayout botonesLayout = new HorizontalLayout(botonAceptar, botonRechazar);
						
			FormLayout solicitudLayout = new FormLayout(labelMail,labelTipoActual,labelTipoSolicitud,labelMotivo,botonesLayout);
			solicitudLayout.setWidth("600");
			solicitudLayout.setComponentAlignment(botonesLayout, Alignment.MIDDLE_CENTER);
			solicitudLayout.addStyleName("layout-with-border");
			
			solicitudesLayout.addComponent(solicitudLayout);
			solicitudesLayout.setComponentAlignment(solicitudLayout, Alignment.MIDDLE_CENTER);			
			
			botonAceptar.addClickListener( e -> {
				try {
					this.aceptar(s,solicitudLayout);
				} catch (EmailException | SQLException e1) {
					e1.printStackTrace();
				}
			} );			
			
			botonRechazar.addClickListener( e -> {
				try {
					this.rechazar(s,solicitudLayout);
				} catch (EmailException | SQLException e2) {
					e2.printStackTrace();
				}
			} );			
	}

	
	private void inicializarLayouts() {
		
		solicitudesLayout.setSizeUndefined();
		
		//coloco el layout con las propiedades dentro del panel para poder scrollear
		panel.setContent(solicitudesLayout);
		panel.setHeight("600");
		panel.setWidth("700");
		panel.addStyleName("scrollable");

		VerticalLayout mainLayout = new VerticalLayout(cabecera, panel, msjResultado);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);
		
		setCompositionRoot(mainLayout);
	}

	
	private void aceptar(Solicitud s, FormLayout sl) throws EmailException, SQLException {
		
		conexion.modificarDeUsuarioTipo(s.getMail(),s.getTipo());
		conexion.eliminarSolicitud(s.getMail());
		this.enviarEmail(s.getMail(), "aceptada", s.getTipo());
		mostrarNotificacion("Solicitud aceptada y usuario informado vía mail.", Notification.Type.HUMANIZED_MESSAGE);
		this.ocultarLayout(sl);		
	}
	

	private void rechazar(Solicitud s, FormLayout sl) throws EmailException, SQLException {

		conexion.eliminarSolicitud(s.getMail());
		this.enviarEmail(s.getMail(), "rechazada", s.getTipo());
		mostrarNotificacion("Solicitud rechazada y usuario informado vía mail.", Notification.Type.HUMANIZED_MESSAGE);
		this.ocultarLayout(sl);		
	}
	

	private void ocultarLayout(FormLayout sl) {
		
		sl.setVisible(false);
		cantOcultadas++;
		if (cantOcultadas == cantAgregadas) {
			panel.setVisible(false);
			msjResultado.setVisible(true);
		}
	}

	
	private void enviarEmail(String mail, String accion, String tipo) throws EmailException {
		
		email = new HtmlEmail();
		this.inicializarEmail(tipo, accion);
		email.addTo(mail);
		email.send();
	}
	
	
	private void inicializarEmail(String tipo, String accion) throws EmailException {

		email.setHostName("localhost");
		email.setSmtpPort(9090);
		email.setAuthentication("homeswitchhome@outlook.com.ar", "1234");		
		email.setFrom("homeswitchhome@outlook.com.ar");
		email.setSubject("Solicitud de "+tipo+" "+accion);
		
		String mensaje = "<p>Estimado usuario, su solicitud de "+tipo+" de premium ha sido "+accion+".</p>"
					+ "<p>Atte. Staff de <span style=\"text-decoration: underline;\">HomeSwitchHome</span></p>";
		
		email.setHtmlMsg(mensaje);		
	}
	
	
	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	
		notifResultado = new Notification(st, tipo);
    	notifResultado.setDelayMsec(5000);
    	notifResultado.show(Page.getCurrent());
    }
	
	
	private String obtenerTipoUsuario(String tipo) {
		return (tipo.equals("alta")) ? "Usuario Común" : "Usuario Premium";
	}
	
	
	private String obtenerTipoSolicitud(String tipo) {
		return (tipo.equals("alta")) ? "Alta" : "Baja";
	}
	
	
}