package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.vaadin.ui.NumberField;

import com.vaadin.annotations.Title;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.validator.RegexpValidator;
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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.EstadoDeReserva;
import homeSwitchHome.Propiedad;
import homeSwitchHome.Reserva;
import homeSwitchHome.ReservaDirecta;
import homeSwitchHome.ReservaHotsale;
import homeSwitchHome.ReservaSubasta;
import homeSwitchHome.Usuario;

@Title("Semanas - HomeSwitchHome")
public class SemanasAdminView extends Composite implements View {
	
	private Label cabecera = new Label("Lista de semanas disponibles / en espera");
	private Label msjResultado = new Label("No se han encontrado semanas sin reservar en el sistema.");	
	
	private VerticalLayout semanasLayout = new VerticalLayout();
	private HorizontalLayout botonesLayout = new HorizontalLayout();
	private Panel panel = new Panel();
	private Button botonAbrirSubasta = new Button("Abrir Subastas");
	private Button botonCerrarSubasta = new Button("Cerrar Subastas");
	private Notification notifResultado = new Notification("");
	
	private Window ventanaConfirmacion = new Window("Confirmar monto");
	private Label info = new Label("Indique un monto:");
	private NumberField monto = new NumberField();
	private Button botonConfirmar = new Button("Enviar");
	private Button botonCancelar = new Button("Cancelar");
	private VerticalLayout ventanaLayout = new VerticalLayout();
	
	private HtmlEmail email = new HtmlEmail();
	private ArrayList<Reserva> reservas = new ArrayList<>();	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private ReservaHotsale rhActual;

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
			if ( ((r.getEstado() == EstadoDeReserva.DISPONIBLE) || (r.getEstado() == EstadoDeReserva.EN_ESPERA)) &&
					(LocalDate.now().isBefore(r.getFechaFin())) ) {
				reservas.add(r);
			}
		}
		
		for (int i=0; i < reservas.size(); i++) {
			if ( (reservas.get(i).getEstado() == EstadoDeReserva.DISPONIBLE)
					&& (reservas.get(i) instanceof ReservaSubasta) ) 
				try {
					reservas.set(i, conexion.buscarSubasta(reservas.get(i).getPropiedad(), reservas.get(i).getLocalidad(),
							reservas.get(i).getFechaInicio(), reservas.get(i).getEstado(), reservas.get(i).getMontoOriginal()));
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
		
		monto.setDecimalPrecision(2);
		monto.setDecimalSeparator('.');
		monto.setGroupingUsed(false);

		new Binder<Propiedad>().forField(monto)
			    .withValidator(new RegexpValidator("", "[-+]?[0-9]*\\.?[0-9]+"))
			    .withConverter(new StringToFloatConverter(""))
			    .bind(Propiedad::getMontoBase, Propiedad::setMontoBase);

		ventanaConfirmacion.center();
		ventanaConfirmacion.setWidth("500");
		ventanaConfirmacion.setResizable(true);
		ventanaConfirmacion.setModal(true);

		botonConfirmar.addClickListener(e -> {
			this.abrirHotsale(rhActual);
		} );
		botonCancelar.addClickListener(e -> ventanaConfirmacion.close() );
	}


	private void inicializarLayouts() {		
		
		HorizontalLayout botones3Layout = new HorizontalLayout(botonConfirmar,botonCancelar);
		
		ventanaLayout = new VerticalLayout(info, monto, botones3Layout);
		ventanaLayout.setComponentAlignment(info, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(monto, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(botones3Layout, Alignment.MIDDLE_CENTER);

		ventanaConfirmacion.setContent(ventanaLayout);
		
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
				
		Label estado = new Label("<span style=\"font-weight: bold;\">Estado (Tipo):</span> " + r.getEstadoComoString(), ContentMode.HTML);		
		
		String tipo = "";
		String fecha = "";
		if (r instanceof ReservaDirecta) {
			tipo = "reserva directa";
			fecha = r.getFechaInicio().plusMonths(6).format(formatter);
		} else
			if (r instanceof ReservaSubasta) {
				tipo = "subasta";
				fecha = ((ReservaSubasta) r).getFechaFinSubasta().format(formatter);
			} else
				if (r instanceof ReservaHotsale) {
					tipo = "Hotsale";
					fecha = r.getFechaFin().format(formatter);
					
				}
		
		Label fechaFinParcial = new Label("<span style=\"font-weight: bold;\">Fin de "+tipo+":</span> "
				+ fecha, ContentMode.HTML);
				
		Label fechasInicioFin = new Label("<span style=\"font-weight: bold;\">Período de publicación [1 año]:</span> "
				+ r.getFechaInicio().format(formatter) + " a "+r.getFechaFin().format(formatter), ContentMode.HTML);
		
		Label montoBase = new Label("<span style=\"font-weight: bold;\">Precio:</span> " + r.getMonto(), ContentMode.HTML);
		
    	
    	FormLayout semanaLayout = new FormLayout(propiedad,localidad,estado,fechaFinParcial,fechasInicioFin,montoBase);
		semanaLayout.setWidth("500");
		semanaLayout.setSizeFull();
		semanaLayout.addStyleName("layout-with-border");
		
		
		if (r instanceof ReservaSubasta) {			
			
			String ofertasString = "<span style=\"font-weight: bold;\">Ofertas en subasta:</span><br/>";
			ArrayList<Float> montos = ((ReservaSubasta) r).getMontos();
			ArrayList<String> usuarios = ((ReservaSubasta) r).getUsuarios();
						
			if ( (usuarios != null) && (!usuarios.isEmpty()) ) {				
				for (int i = 0; i < usuarios.size(); i++) {
					ofertasString += (i+1)+". "+usuarios.get(i)+" ($";
					ofertasString += String.valueOf(montos.get(i)) + ")<br/>";
				}
			} else
				ofertasString = "No se han realizado ofertas"; 			
			
			Label ofertas = new Label("" + ofertasString, ContentMode.HTML);
			semanaLayout.addComponent(ofertas);
		} else 
			if (r instanceof ReservaHotsale ) {
				
				Button botonAbrirHotsale = new Button("Abrir Hotsale");
				Button botonCerrarHotsale = new Button("Cerrar Hotsale");
				botonAbrirHotsale.setVisible(false);
				botonCerrarHotsale.setVisible(false);				
				
				if (r.getEstado() == EstadoDeReserva.EN_ESPERA) {
					if ( ((ReservaHotsale) r).puedeAbrirHotsale() )
						botonAbrirHotsale.setVisible(true);
				} else
					if (r.getEstado() == EstadoDeReserva.DISPONIBLE) {
						botonCerrarHotsale.setVisible(true);
					}					

				botonAbrirHotsale.addClickListener( e -> this.abrirVentanaConfirmacion((ReservaHotsale) r) );
				botonCerrarHotsale.addClickListener( e -> this.cerrarHotsale((ReservaHotsale) r) );
				    	
				HorizontalLayout botones2Layout = new HorizontalLayout(botonAbrirHotsale, botonCerrarHotsale);
				
				semanaLayout.addComponent(botones2Layout);
			}				
		
		semanasLayout.addComponent(semanaLayout);
		semanasLayout.setComponentAlignment(semanaLayout, Alignment.MIDDLE_CENTER);		
	}
	
	
	private void abrirSubastas() throws SQLException {
		
		int n = 0; //subastas abiertas		
		LocalDate hace6meses = LocalDate.now().minusMonths(6);
		String subastasAbiertas = "<strong>Propiedad - Localidad - Fecha de inicio</strong><br/>";
		
		for (Reserva r : reservas) {
			
			if ( ((r.getEstado() == EstadoDeReserva.DISPONIBLE) && (r instanceof ReservaDirecta)) && 
					(!r.getFechaInicio().isAfter(hace6meses)) &&
					(!r.getFechaInicio().isBefore(hace6meses.minusDays(3))) ) {
				conexion.abrirSubasta(r);
				n++;
				subastasAbiertas += r.getPropiedad()+" - "+r.getLocalidad()+" - "+r.getFechaInicio().format(formatter)+"<br/>";
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
		ReservaSubasta rs;
		String subastasCerradas = "<strong>Propiedad - Localidad - Fecha de inicio - Ganador</strong><br/>";		
		
		// para cada reserva:
		// > si es una subasta -> si está disponible y se alcanzó la fecha del cierre ->
		// >> si hay ganador -> enviar mail + restar credito + cerrar subasta con ganador 
		// >> si no hay ganador -> cerrar subasta sin ganador
		// >> sumar subasta cerrada (n++)
		// >> agregar info de subasta
		
		for (Reserva r : reservas) {			
			
			if (r instanceof ReservaSubasta) {
				rs = (ReservaSubasta) r;				
				
				if ( (r.getEstado() == EstadoDeReserva.DISPONIBLE) && (r instanceof ReservaSubasta) && (!hoy.isBefore(rs.getFechaFinSubasta())) ) {					
					
					if (this.hayOfertaGanadora(rs)) {
						this.enviarEmail(rs);
						conexion.modificarUsuarioCreditos(rs.getUsuarios().get(0), "-", 1);
						conexion.cerrarSubastaConGanador(rs);
					} else
						conexion.cerrarSubastaSinGanador(rs);
					
					n++;
					subastasCerradas += rs.getPropiedad()+" - "+rs.getLocalidad()+" - "+rs.getFechaInicio().format(formatter)+" - "+rs.getOfertaGanadora()+"<br/>";
				}
			}
		}
		
		if (n > 0) {
			mostrarNotificacion("Se cerraron "+n+" subastas. Detalle:<br/><br/>" + subastasCerradas, Notification.Type.HUMANIZED_MESSAGE);
			interfaz.vistaAdmin("semanasAdmin");
		} else {
			mostrarNotificacion("No se cerró ninguna subasta.", Notification.Type.HUMANIZED_MESSAGE);
		}
	}
	
	
	
	private boolean hayOfertaGanadora(ReservaSubasta rs) throws SQLException {
		
		//verifica si:
		// 1) existen al menos una oferta Y
		// 2) el ofertante posee créditos disponibles Y
		// 3) no posee reservas en la misma semana
		
		Usuario u;
		ArrayList<Float> montos = rs.getMontos();
		ArrayList<String> usuarios = rs.getUsuarios();
				
		if (montos != null) {
			while (!montos.isEmpty()) {			
				u = conexion.buscarUsuario(usuarios.get(0));
				if ( (u.getCreditos() > 0) && (!poseeReservaEnMismaSemana(u.getMail(), rs.getFechaReserva())) ) {
					return true;
				} else
					rs.eliminarOferta(0);
			}
		}
		
		return false;
	}


	private boolean poseeReservaEnMismaSemana(String mail, LocalDate fecha) throws SQLException {
		
		ArrayList<Reserva> reservas;
		reservas = conexion.listaReservasPorUsuario(mail);
		
		//verifica si alguna reserva de la BD pertenece al usuario ganador Y coincide en la fecha de reserva con la semana subastada
		for (Reserva r : reservas) {				
			if ( r.getUsuario().equals(mail) && (r.getFechaReserva().isEqual(fecha)) )
				return true;
		}
		
		return false;
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
					+ rs.getPropiedad()+" - "+rs.getLocalidad()+" - "+rs.getFechaReserva().format(formatter)+"<br/>"					
					+ "<p>Se descontará un crédito de su cuenta junto con el cobro del pago.</p>"
					+ "<p>Atte. Staff de <span style=\"text-decoration: underline;\">HomeSwitchHome</span></p>";
		
		email.setHtmlMsg(mensaje);
	}
	
	
	private void abrirVentanaConfirmacion(ReservaHotsale rh) {

		rhActual = rh;

		// completa el campo doferta automáticamente con la oferta actual; deshabilitado ya que no se indica en la historia
//		montoOferta.setValue(montoString);
		
		UI.getCurrent().addWindow(ventanaConfirmacion);
	}


	private void abrirHotsale(ReservaHotsale rh) {

		if (!monto.isEmpty()) {
			
			Float montoFinal = Float.valueOf(monto.getValue());
			
			if (montoFinal > 0) {
				
				//actualizo hotsale en la base de datos
				try {
					conexion.abrirHotsale(rh, montoFinal);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				//cierro ventana de confirmacion e indico éxito
				ventanaConfirmacion.close();
				this.mostrarNotificacion("Hotsale iniciado exitosamente.", Notification.Type.HUMANIZED_MESSAGE);

				//actualizo sesión
				interfaz.vistaAdmin("semanasAdmin");

			} else
				this.mostrarNotificacion("Error: El monto debe ser mayor a 0.", Notification.Type.ERROR_MESSAGE);				
		} else
			this.mostrarNotificacion("Error: Ingrese un monto.", Notification.Type.ERROR_MESSAGE);
	}
	
	private void cerrarHotsale(ReservaHotsale rh) {

		//actualizo hotsale en la base de datos
		try {
			conexion.cerrarHotsale(rh);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.mostrarNotificacion("Hotsale cerrado exitosamente.", Notification.Type.HUMANIZED_MESSAGE);

		//actualizo sesión
		interfaz.vistaAdmin("semanasAdmin");
	}
	
	

	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	
		notifResultado = new Notification(st, tipo);
    	notifResultado.setDelayMsec(5000);
    	notifResultado.setHtmlContentAllowed(true);
    	notifResultado.show(Page.getCurrent());
    }
	
}