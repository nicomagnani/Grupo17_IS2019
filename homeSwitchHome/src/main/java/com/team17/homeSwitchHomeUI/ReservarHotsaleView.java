package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;

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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Propiedad;
import homeSwitchHome.ReservaHotsale;
import homeSwitchHome.Usuario;

@Title("Realizar reserva Hotsale - HomeSwitchHome")
public class ReservarHotsaleView extends Composite implements View {
	
	private Label cabeceraPrincipal = new Label("Realizar reserva Hotsale");
	private Label cabeceraDatos = new Label("Información");
	private Label titulo;
	private Label ubicacion;
	private Label montoBase;
	private Label fechaReserva;
	
	private Button botonReservar = new Button("Reservar");
	
	private Window ventanaConfirmacion = new Window("Confirmar reserva");
	private Label texto = new Label("¿Realizar reserva?");
	private Button botonConfirmar = new Button("Confirmar");
	private Button botonCancelar = new Button("Cancelar");
	
	private HtmlEmail email = new HtmlEmail();
	
	private Notification notifResultado;
	
	private FormLayout informacionLayout;
	private VerticalLayout ventanaLayout = new VerticalLayout();
	@SuppressWarnings("unused")
	private HorizontalLayout botonesLayout;
	private VerticalLayout mainLayout;
	
	private Usuario usuario;
	private Propiedad propiedad;
	private ReservaHotsale reserva;
	private ConnectionBD conexion;
	private MyUI interfaz;
	
	
	public ReservarHotsaleView(MyUI interfaz) {		
		
		this.interfaz = interfaz;		
		
		this.cargarDatosDeSesion();
		this.inicializarComponentes();
		this.inicializarLayouts();		
	}


	private void cargarDatosDeSesion() {
		
		this.usuario = HomeSwitchHome.getUsuarioActual();
		this.propiedad = HomeSwitchHome.getPropiedadActual();
		this.reserva = (ReservaHotsale) HomeSwitchHome.getReservaActual();		
	}


	private void inicializarComponentes() {
		
		cabeceraPrincipal.addStyleName(ValoTheme.MENU_TITLE);
		cabeceraDatos.addStyleName(ValoTheme.MENU_TITLE);
		
		titulo = new Label("<p><span style=\"text-align: left; font-weight: bold; font-size: 120%;\">Título:</span> <span style=\"font-size: 120%;\">"
				+ propiedad.getTitulo()+"</span></p>", ContentMode.HTML);
		
		ubicacion = new Label("<span style=\"font-weight: bold;\">Ubicación:</span> " + propiedad.getPais() + ", "
				+ propiedad.getProvincia() + ", " + propiedad.getLocalidad(), ContentMode.HTML);
		
		montoBase = new Label("<span style=\"font-weight: bold;\">Monto base:</span> "
				+ String.valueOf(reserva.getMonto()), ContentMode.HTML);
		
		fechaReserva = new Label("<span style=\"font-weight: bold;\">Fecha de reserva:</span> "
				+ reserva.getFechaReserva().toString(), ContentMode.HTML);
				
		botonReservar.addClickListener(e -> this.abrirVentanaConfirmacion() );
		botonConfirmar.addClickListener(e -> {
			try {
				this.reservar();
			} catch (SQLException | EmailException e1) {
				e1.printStackTrace();
			}
		} );		
		botonCancelar.addClickListener(e -> ventanaConfirmacion.close() );

		ventanaConfirmacion.center();
		ventanaConfirmacion.setWidth("300");
		ventanaConfirmacion.setResizable(false);
		ventanaConfirmacion.setModal(true);		
	}


	private void abrirVentanaConfirmacion() {
		UI.getCurrent().addWindow(ventanaConfirmacion);	
	}


	private void reservar() throws SQLException, EmailException {
		
		String mail = usuario.getMail();
		
		//se chequea: 1) reserva ya hecha en misma semana (no se consumen créditos)
		conexion = new ConnectionBD();
		usuario.setReservas( conexion.listaReservasPorUsuario(mail) );
		
		if (!usuario.tieneReservaEnFecha(reserva.getFechaReserva()) ) {
			
			//cierro ventana de confirmacion e indico éxito
			ventanaConfirmacion.close();
			mostrarNotificacion("Éxito. Redirigiendo...", Notification.Type.HUMANIZED_MESSAGE);
							
			//modifico reserva			
			reserva.setUsuario(mail);
			conexion.realizarReservaHotsale(reserva);
			
			//envio mail
			this.inicializarEmail();
			this.agregarReceptorDeEmail(mail);
			email.send();
			
			//actualizo sesión y redirijo a vista de reservas
			HomeSwitchHome.setUsuarioActual( conexion.buscarUsuario(mail) );
			interfaz.vistaUsuario("misReservas");
			
		} else
			this.mostrarNotificacion("Error: Solo puede realizarse una reserva para la misma semana.", Notification.Type.ERROR_MESSAGE);
	}


	private void inicializarLayouts() {
		
		informacionLayout = new FormLayout(cabeceraDatos, titulo, ubicacion, montoBase, fechaReserva);
		informacionLayout.setComponentAlignment(cabeceraDatos, Alignment.MIDDLE_CENTER);
		informacionLayout.setComponentAlignment(titulo, Alignment.MIDDLE_CENTER);
		informacionLayout.setComponentAlignment(ubicacion, Alignment.MIDDLE_CENTER);
		informacionLayout.setComponentAlignment(montoBase, Alignment.MIDDLE_CENTER);
		informacionLayout.setComponentAlignment(fechaReserva, Alignment.MIDDLE_CENTER);
		informacionLayout.setWidth("600");
		informacionLayout.addStyleName("layout-with-border");
		
		botonesLayout = new HorizontalLayout(botonConfirmar, botonCancelar);
		
		ventanaLayout = new VerticalLayout(texto, botonConfirmar, botonCancelar);
		ventanaLayout.setComponentAlignment(texto, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(botonConfirmar, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(botonCancelar, Alignment.MIDDLE_CENTER);
		
		ventanaConfirmacion.setContent(ventanaLayout);
		
		mainLayout = new VerticalLayout(cabeceraPrincipal, informacionLayout, botonReservar);
		mainLayout.setComponentAlignment(cabeceraPrincipal, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(informacionLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(botonReservar, Alignment.MIDDLE_CENTER);

		setCompositionRoot(mainLayout);
	}
	
	
	private void inicializarEmail() throws EmailException {

		email.setHostName("localhost");
		email.setSmtpPort(9090);
		email.setAuthentication("homeswitchhome@outlook.com.ar", "1234");		
		email.setFrom("homeswitchhome@outlook.com.ar");
		email.setSubject("Reserva realizada");
		
		String mensaje = "<p>Estimado usuario, su reserva tipo Hotsale ha sido realizada con éxito. Detalle:</p>"
				
				+"<p><span style=\"text-align: left; font-weight: bold;\">Título:</span> "
				+propiedad.getTitulo()+"</p>"
				
				+"<p><span style=\"font-weight: bold;\">Ubicación:</span> "+propiedad.getPais()+", "
				+propiedad.getProvincia()+", "+propiedad.getLocalidad()+"</p>"
				
				+"<p><span style=\"font-weight: bold;\">Monto base:</span> "				
				+String.valueOf(reserva.getMonto())+"</p>"		
				
				+"<p><span style=\"font-weight: bold;\">Fecha de reserva:</span> "
				+reserva.getFechaReserva().toString()+"</p>"
				
				+ "<p>Atte. Staff de <span style=\"text-decoration: underline;\">HomeSwitchHome</span></p>";
		
		email.setHtmlMsg(mensaje);		
	}


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
