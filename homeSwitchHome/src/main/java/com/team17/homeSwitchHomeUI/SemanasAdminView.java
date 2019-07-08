package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.time.LocalDate;
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

import homeSwitchHome.EstadoDeReserva;
import homeSwitchHome.Reserva;
import homeSwitchHome.ReservaSubasta;

@Title("Residencias - HomeSwitchHome")
public class SemanasAdminView extends Composite implements View {
	
	private Label cabecera = new Label("Lista de semanas disponibles/sin reserva");
	private Label msjResultado = new Label("No se han encontrado semanas en el sistema.");	
	
	private VerticalLayout semanasLayout = new VerticalLayout();
	private HorizontalLayout botonesLayout = new HorizontalLayout();
	private Panel panel = new Panel();
	private Button botonAbrirSubasta = new Button("Abrir Subastas");
	private Button botonCerrarSubasta = new Button("Cerrar Subastas");
	private Notification notifResultado = new Notification("");
	
	private HtmlEmail email = new HtmlEmail();
	private ArrayList<Reserva> reservas = new ArrayList<>();
	

	private ConnectionBD conexion = new ConnectionBD();
	private MyUI interfaz;
	

	public SemanasAdminView(MyUI interfaz) throws SQLException {
		
		this.interfaz = interfaz;
		
		this.cargarSemanas();
		this.inicializarComponentes();
		this.inicializarLayouts();		
	}


	private void cargarSemanas() {
		
		reservas = conexion.listaReservasEnCursoSinDueño();
	}
	

	private void inicializarComponentes() {
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		panel.setVisible(false);
		botonesLayout.setVisible(false);
		msjResultado.setVisible(false);
		
		if (!reservas.isEmpty()) {
			panel.setVisible(true);
			botonesLayout.setVisible(true);
			for (Reserva r : reservas)
				this.añadirSemana(r);			
		} else
			msjResultado.setVisible(true);
		
		botonAbrirSubasta.addClickListener(e -> {
			try {
				this.abrirSubastas();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		});
		
		botonCerrarSubasta.addClickListener(e -> {
			this.cerrarSubastas();
		});
		
		try {
			this.inicializarEmail();
		} catch (EmailException e1) {
			e1.printStackTrace();
		}
	}


	private void inicializarLayouts() {
		
		semanasLayout.setSizeUndefined();
		
		//coloco el layout con las propiedades dentro del panel para poder scrollear
		panel.setContent(semanasLayout);
		panel.setHeight("600");
		panel.setWidth("750");
		panel.addStyleName("scrollable");
		
		botonesLayout.addComponent(botonAbrirSubasta);
		botonesLayout.addComponent(botonCerrarSubasta);		
		botonesLayout.setComponentAlignment(botonAbrirSubasta, Alignment.MIDDLE_CENTER);
		botonesLayout.setComponentAlignment(botonCerrarSubasta, Alignment.MIDDLE_CENTER);		
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, panel, botonesLayout, msjResultado);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(botonAbrirSubasta, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);
		
        setCompositionRoot(mainLayout);		
	}
	
	
	private void añadirSemana(Reserva r) {
		
		Label propiedad = new Label("<p><span style=\"text-align: left; font-weight: bold; font-size: 110%;\">Propiedad:</span> <span style=\"font-size: 110%;\">"
						+ r.getPropiedad()+"</span></p>", ContentMode.HTML);
		
		Label localidad = new Label("<span style=\"font-weight: bold;\">Localidad:</span> " + r.getLocalidad(), ContentMode.HTML);	
				
		Label estado = new Label("<span style=\"font-weight: bold;\">Monto base:</span> " + r.getEstadoComoString(), ContentMode.HTML);
		
		Label fechasInicioFin = new Label("<span style=\"font-weight: bold;\">Fechas de Inicio/Fin:</span> "
					+ r.getFechaInicio().toString() + " a "+r.getFechaFin().toString(), ContentMode.HTML);
		
		Label montoBase = new Label("<span style=\"font-weight: bold;\">Monto base:</span> " + String.valueOf(r.getMonto()), ContentMode.HTML);
		    	
    	/* aca irían los botones que tienen efecto sobre una semana en particular (abrir hotsale, cerrar hotsale, etc)
		HorizontalLayout botones2Layout = new HorizontalLayout(abrirHotsale, cerrarHotsale);
		*/
    	
    	FormLayout semanaLayout = new FormLayout(propiedad,localidad,estado,fechasInicioFin,montoBase);
		semanaLayout.setWidth("500");
		semanaLayout.setSizeFull();
		semanaLayout.addStyleName("layout-with-border");
		
		if (r instanceof ReservaSubasta) {
			
			String ofertasString = "";
			ArrayList<Float> montos = ((ReservaSubasta) r).getMontos();
			ArrayList<String> usuarios = ((ReservaSubasta) r).getUsuarios();
			int j = 0;
			
			if ( (usuarios != null) && (usuarios.isEmpty()) ) {				
				for (int i = 0; i < montos.size(); i++) {
					j++;
					ofertasString += j+". "+usuarios.get(i)+" (";
					ofertasString += String.valueOf(montos.get(j)) + ")\n";					
				}
			} else
				ofertasString = "No se han realizado ofertas"; 			
			
			Label ofertas = new Label("<span style=\"font-weight: bold;\">Monto base:</span> " + ofertasString, ContentMode.HTML);
			semanaLayout.addComponent(ofertas);
		}
		
		semanasLayout.addComponent(semanaLayout);
		semanasLayout.setComponentAlignment(semanaLayout, Alignment.MIDDLE_CENTER);		
	}
	
	
	private void abrirSubastas() throws SQLException {
		
		int n = 0; //subastas abiertas		
		LocalDate hace6meses = LocalDate.now().minusMonths(6);
		
		for (Reserva reserva : reservas) {
			
			if ( (reserva.getEstado() == EstadoDeReserva.DISPONIBLE_DIRECTA) && 
					(!reserva.getFechaInicio().isAfter(hace6meses)) &&
					(!reserva.getFechaInicio().isBefore(hace6meses.minusDays(3))) ) {					
				conexion.abrirSubasta(reserva);
				n++;
				//TODO: guardar informacion de la reserva/residencia) para mostrarla en la notificacion					
			}
		}
		
		if (n > 0) {
			mostrarNotificacion("Se abrieron "+n+" subastas.", Notification.Type.HUMANIZED_MESSAGE);
			interfaz.vistaAdmin("semanasAdmin");
		} else {
			mostrarNotificacion("No se abrió ninguna subasta.", Notification.Type.HUMANIZED_MESSAGE);
		}
	}
	

	private void cerrarSubastas() {
		
		int n = 0; //subastas cerradas
		LocalDate hoy = LocalDate.now();
		ReservaSubasta r2; 
		
		for (Reserva r : reservas) {
			
			if (r instanceof ReservaSubasta) {
				r2 = (ReservaSubasta) r;
				if ( (r2.getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA)		
						&& (hoy.isBefore(r2.getFechaFinSubasta())) ) {						
					conexion.cerrarSubasta((ReservaSubasta) r);
					n++;
					//TODO: guardar informacion de la reserva/residencia) para mostrarla en la notificacion
					//TODO: también enviar notificaciones, cobrar, etc
				}
			}
		}
		
		if (n > 0) {
			mostrarNotificacion("Se abrieron "+n+" subastas.", Notification.Type.HUMANIZED_MESSAGE);
			interfaz.vistaAdmin("semanasAdmin");
		} else {
			mostrarNotificacion("No se abrió ninguna subasta.", Notification.Type.HUMANIZED_MESSAGE);
		}
		
	}
	
	
	private void enviarEmail(String mail) throws EmailException {
		
		email = new HtmlEmail();
		this.inicializarEmail();
		email.addTo(mail);
		email.send();
	}
	
	
	private void inicializarEmail() throws EmailException {

		email.setHostName("localhost");
		email.setSmtpPort(9090);
		email.setAuthentication("homeswitchhome@outlook.com.ar", "1234");		
		email.setFrom("homeswitchhome@outlook.com.ar");
		email.setSubject("Ganador de subasta");
		
		String mensaje = "<p>Estimado usuario, usted ha sido el ganador de la siguiente subasta:</p>"
					//TODO: mostrar detalles
					+ "<p>detalles</p>"
					+ "<p>Se descontará un crédito de su cuenta junto con el cobro del pago.</p>"
					+ "<p>Atte. Staff de <span style=\"text-decoration: underline;\">HomeSwitchHome</span></p>";
		
		email.setHtmlMsg(mensaje);		
	}	
	
	
	//devuelve true si tuvo exito

	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	
		notifResultado = new Notification(st, tipo);
    	notifResultado.setDelayMsec(5000);
    	notifResultado.show(Page.getCurrent());
    }
	
}