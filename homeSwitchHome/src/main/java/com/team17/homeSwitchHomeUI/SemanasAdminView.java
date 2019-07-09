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
import homeSwitchHome.Usuario;

@Title("Residencias - HomeSwitchHome")
public class SemanasAdminView extends Composite implements View {
	
	private Label cabecera = new Label("Lista de semanas disponibles / en espera");
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
	

	public SemanasAdminView(MyUI interfaz) {
		
		this.interfaz = interfaz;
		
		this.cargarSemanas();
		this.inicializarComponentes();
		this.inicializarLayouts();		
	}


	private void cargarSemanas() {
		
		ArrayList<Reserva> reservasTodas = null;
		
		try {
			reservasTodas = conexion.listaReservas();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (Reserva r : reservasTodas) {
			if ( ((r.getEstado() == EstadoDeReserva.DISPONIBLE_DIRECTA) || (r.getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA)
					|| (r.getEstado() == EstadoDeReserva.DISPONIBLE_HOTSALE) || (r.getEstado() == EstadoDeReserva.EN_ESPERA)) &&
					(LocalDate.now().isBefore(r.getFechaFin())) ) {
				reservas.add(r);
			}
		}
		
		for (int i=0; i < reservas.size(); i++) {
			if ( reservas.get(i).getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA )
				try {
					reservas.set(i, conexion.buscarSubasta(reservas.get(i).getPropiedad(), reservas.get(i).getLocalidad(),
							reservas.get(i).getFechaInicio(), reservas.get(i).getEstado()) );
				} catch (SQLException e) {
					e.printStackTrace();
				}		
		}		
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
			try {
				this.cerrarSubastas();
			} catch (EmailException | SQLException e1) {
				e1.printStackTrace();
			}
		});
	}


	private void inicializarLayouts() {
		
		semanasLayout.setSizeUndefined();
		semanasLayout.setWidth("650");
		
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
		mainLayout.setComponentAlignment(botonesLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);
		
        setCompositionRoot(mainLayout);		
	}
	
	
	private void añadirSemana(Reserva r) {
		
		Label propiedad = new Label("<p><span style=\"text-align: left; font-weight: bold; font-size: 120%;\">Propiedad:</span> <span style=\"font-size: 120%;\">"
						+ r.getPropiedad()+"</span></p>", ContentMode.HTML);
		
		Label localidad = new Label("<span style=\"font-weight: bold;\">Localidad:</span> " + r.getLocalidad(), ContentMode.HTML);	
				
		Label estado = new Label("<span style=\"font-weight: bold;\">Estado:</span> " + r.getEstadoComoString(), ContentMode.HTML);
		
		Label fechasInicioFin = new Label("<span style=\"font-weight: bold;\">Fecha de publicación (inicio a fin):</span> "
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
			
			String ofertasString = "<span style=\"font-weight: bold;\">Ofertas en subasta:</span><br/>";
			ArrayList<Float> montos = ((ReservaSubasta) r).getMontos();
			ArrayList<String> usuarios = ((ReservaSubasta) r).getUsuarios();
			int j = 0;
			
			if ( (usuarios != null) && (!usuarios.isEmpty()) ) {				
				for (int i = 0; i < usuarios.size(); i++) {
					j++;
					ofertasString += j+". "+usuarios.get(i)+" ($";
					ofertasString += String.valueOf(montos.get(j)) + ")<br/>";
				}
			} else
				ofertasString = "No se han realizado ofertas"; 			
			
			Label ofertas = new Label("" + ofertasString, ContentMode.HTML);
			semanaLayout.addComponent(ofertas);
		}
		
		semanasLayout.addComponent(semanaLayout);
		semanasLayout.setComponentAlignment(semanaLayout, Alignment.MIDDLE_CENTER);		
	}
	
	
	private void abrirSubastas() throws SQLException {
		
		int n = 0; //subastas abiertas		
		LocalDate hace6meses = LocalDate.now().minusMonths(6);
		String subastasAbiertas = "<strong>Propiedad - Localidad - Fecha de inicio</strong><br/>";
		
		for (Reserva r : reservas) {
			
			if ( (r.getEstado() == EstadoDeReserva.DISPONIBLE_DIRECTA) && 
					(!r.getFechaInicio().isAfter(hace6meses)) &&
					(!r.getFechaInicio().isBefore(hace6meses.minusDays(3))) ) {					
				conexion.abrirSubasta(r);
				n++;
				subastasAbiertas += r.getPropiedad()+" - "+r.getLocalidad()+" - "+r.getFechaInicio()+"<br/>";
			}
		}
		
		if (n > 0) {
			mostrarNotificacion("Se abrieron "+n+" subastas. Detalle:<br/>" + subastasAbiertas, Notification.Type.HUMANIZED_MESSAGE);
			interfaz.vistaAdmin("semanasAdmin");
		} else {
			mostrarNotificacion("No se abrió ninguna subasta.", Notification.Type.HUMANIZED_MESSAGE);
		}
	}
	

	private void cerrarSubastas() throws EmailException, SQLException {
		
		int n = 0; //subastas cerradas
		LocalDate hoy = LocalDate.now();
		ReservaSubasta r2;
		String subastasCerradas = "<strong>Propiedad - Localidad - Fecha de inicio - Ganador</strong><br/>";
		
		for (Reserva r : reservas) {
			
			if (r instanceof ReservaSubasta) {
				r2 = (ReservaSubasta) r;
				if ( (r2.getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA)		
						&& (hoy.isBefore(r2.getFechaFinSubasta())) ) {						
					this.definirOfertaGanadora(r2);
					this.enviarEmail(r2);
					conexion.modificarCreditos(r2.getUsuarios().get(0), "-", 1);	
					conexion.cerrarSubasta(r2);
					n++;
					subastasCerradas += r2.getPropiedad()+" - "+r2.getLocalidad()+" - "+r2.getFechaInicio()+" - "+r2.getOfertaGanadora()+"<br/>";
				}
			}
		}
		
		if (n > 0) {
			mostrarNotificacion("Se cerraron "+n+" subastas. Detalle:<br/>" + subastasCerradas, Notification.Type.HUMANIZED_MESSAGE);
			interfaz.vistaAdmin("semanasAdmin");
		} else {
			mostrarNotificacion("No se abrió ninguna subasta.", Notification.Type.HUMANIZED_MESSAGE);
		}		
	}
	
	
	
	private void definirOfertaGanadora(ReservaSubasta rs) throws SQLException {
		
		//verifica:
		// 1) créditos disponibles
		// 2) que no posea reservas en la misma semana
		
		Usuario u;
		boolean ok = false;
		ArrayList<Float> montos = rs.getMontos();
		ArrayList<String> usuarios = rs.getUsuarios();		
		
		while ( (montos.size() > 1) && (!ok) ) {			
			u = conexion.buscarUsuario(usuarios.get(0));
			if ( (u.getCreditos() > 1) && (!poseeReservaEnMismaSemana(u.getMail(), rs.getFechaReserva())) ) {
				ok = true;
			} else
				rs.eliminarOferta(0);
		}
	}


	private boolean poseeReservaEnMismaSemana(String mail, LocalDate fecha) throws SQLException {
		
		ArrayList<Reserva> reservas;
		reservas = conexion.listaReservasPorUsuario(mail);
		
		//verifica si alguna reserva de la BD pertenece al usuario ganador Y coincide en la fecha de reserva con la semana subastada
		for (Reserva r : reservas) {				
			if ( r.getUsuario().equals(mail) && (r.getFechaReserva().isEqual(fecha)) )
				return false;
		}
		return true;
	}


	private void enviarEmail(ReservaSubasta rs) throws EmailException {
		
		email = new HtmlEmail();
		this.inicializarEmail(rs);
		email.addTo(rs.getUsuarios().get(0));
		email.send();
	}
	
	
	private void inicializarEmail(ReservaSubasta rs) throws EmailException {

		email.setHostName("localhost");
		email.setSmtpPort(9090);
		email.setAuthentication("homeswitchhome@outlook.com.ar", "1234");		
		email.setFrom("homeswitchhome@outlook.com.ar");
		email.setSubject("Ganador de subasta");
		
		String mensaje = "Estimado usuario, usted ha sido el ganador de la siguiente subasta:<br/>"
					+ "<br/>"
					+ "<strong>Propiedad - Localidad - Fecha de Reserva</strong><br/>"
					+ rs.getPropiedad()+" - "+rs.getLocalidad()+" - "+rs.getFechaReserva()+"<br/>"					
					+ "<p>Se descontará un crédito de su cuenta junto con el cobro del pago.</p>"
					+ "<p>Atte. Staff de <span style=\"text-decoration: underline;\">HomeSwitchHome</span></p>";
		
		email.setHtmlMsg(mensaje);
	}	
	

	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	
		notifResultado = new Notification(st, tipo);
    	notifResultado.setDelayMsec(5000);
    	notifResultado.setHtmlContentAllowed(true);
    	notifResultado.show(Page.getCurrent());
    }
	
}