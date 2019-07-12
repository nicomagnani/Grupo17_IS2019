package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.util.ArrayList;

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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Propiedad;
import homeSwitchHome.ReservaSubasta;
import homeSwitchHome.Usuario;

@Title("Ofertar en subasta - HomeSwitchHome")
public class ReservarSubastaView extends Composite implements View {
	
	private Label cabeceraPrincipal = new Label("Ofertar en subasta");
	private Label cabeceraDatos = new Label("Información");
	private Label titulo;
	private Label ubicacion;
	private Label montoBase;
	private Label fechaReserva;
	private Label tiempoRestante;
	private Label creditosDisponibles;
	
	private Button botonOfertar = new Button("Ofertar");	

	private Window ventanaConfirmacion = new Window("Confirmar oferta");
	private Label info = new Label("Indique un monto a ofertar:");
	private NumberField montoOferta = new NumberField();
	private Button botonConfirmar = new Button("Confirmar");
	private Button botonCancelar = new Button("Cancelar");
		
	private Notification notifResultado;
	
	private FormLayout informacionLayout;
	private VerticalLayout ventanaLayout = new VerticalLayout();	
	@SuppressWarnings("unused")
	private HorizontalLayout botonesLayout;
	private VerticalLayout mainLayout;
	
	private Usuario usuario;
	private Propiedad propiedad;
	private ReservaSubasta reserva;
	private ConnectionBD conexion;
	private MyUI interfaz;
	
	
	public ReservarSubastaView(MyUI interfaz) {		
		
		this.interfaz = interfaz;		
		
		this.cargarDatosDeSesion();
		this.inicializarComponentes();
		this.inicializarLayouts();		
	}


	private void cargarDatosDeSesion() {
		
		this.usuario = HomeSwitchHome.getUsuarioActual();
		this.propiedad = HomeSwitchHome.getPropiedadActual();
		this.reserva = (ReservaSubasta) HomeSwitchHome.getReservaActual();		
	}


	private void inicializarComponentes() {
		
		cabeceraPrincipal.addStyleName(ValoTheme.MENU_TITLE);
		cabeceraDatos.addStyleName(ValoTheme.MENU_TITLE);
		
		titulo = new Label("<p><span style=\"text-align: left; font-weight: bold; font-size: 120%;\">Título:</span> <span style=\"font-size: 120%;\">"
				+ propiedad.getTitulo()+"</span></p>", ContentMode.HTML);
		
		ubicacion = new Label("<span style=\"font-weight: bold;\">Ubicación:</span> " + propiedad.getPais() + ", "
				+ propiedad.getProvincia() + ", " + propiedad.getLocalidad(), ContentMode.HTML);
		
		montoBase = new Label("<span style=\"font-weight: bold;\">Precio:</span> "
				+ String.valueOf(reserva.getMonto()), ContentMode.HTML);
		
		fechaReserva = new Label("<span style=\"font-weight: bold;\">Fecha de reserva:</span> "
				+ reserva.getFechaReserva().toString(), ContentMode.HTML);

		tiempoRestante = new Label("<span style=\"font-weight: bold;\">Subasta finaliza:</span> "
				+ reserva.getFechaFinSubastaString(), ContentMode.HTML);
		
		creditosDisponibles = new Label("<span style=\"font-weight: bold;\">Créditos disponibles:</span> "
				+ String.valueOf(usuario.getCreditos()), ContentMode.HTML);
		
		botonOfertar.addClickListener(e -> this.abrirVentanaConfirmacion() );
		botonConfirmar.addClickListener( e -> this.ofertar() );
		botonCancelar.addClickListener( e -> ventanaConfirmacion.close() );
		
		montoOferta.setDecimalPrecision(2);
		montoOferta.setDecimalSeparator('.');
		montoOferta.setGroupingUsed(false);

		new Binder<Propiedad>().forField(montoOferta)
			    .withValidator(new RegexpValidator("", "[-+]?[0-9]*\\.?[0-9]+"))
			    .withConverter(new StringToFloatConverter(""))
			    .bind(Propiedad::getMontoBase, Propiedad::setMontoBase);

		ventanaConfirmacion.center();
		ventanaConfirmacion.setWidth("500");
		ventanaConfirmacion.setResizable(true);
		ventanaConfirmacion.setModal(true);
	}


	private void abrirVentanaConfirmacion() {
		UI.getCurrent().addWindow(ventanaConfirmacion);	
	}
	
	
	private void ofertar() {

		if (!montoOferta.isEmpty()) {

			float montoOfertaFloat = Float.parseFloat(montoOferta.getValue());
			float montoActualFloat = reserva.getMonto();

			if (montoOfertaFloat > montoActualFloat) {

				if (usuario.getCreditos() > 0) {

					//modifico oferta y ofertante de la subasta (creo lista de usuarios si no hay ofertas)
					if (reserva.getMontos() == null)
						reserva.setMontos(new ArrayList<Float>());					
					
					if (reserva.getUsuarios() == null)
						reserva.setUsuarios(new ArrayList<String>());					

					reserva.getMontos().add(0, Float.valueOf(montoOferta.getValue()));
					reserva.getUsuarios().add(0, usuario.getMail());

					//actualizo subasta en la base de datos
					conexion = new ConnectionBD();
					try {
						conexion.modificarSubasta(reserva);
					} catch (SQLException e) {
						e.printStackTrace();
					}

					//cierro ventana de confirmacion e indico éxito
					ventanaConfirmacion.close();
					this.mostrarNotificacion("Oferta hecha exitosamente. Redirigiendo...", Notification.Type.HUMANIZED_MESSAGE);

					//actualizo sesión y redirijo a vista "mis subastas"
					interfaz.vistaUsuario("misSubastas");

				} else {
					this.mostrarNotificacion("Error: No dispone de créditos para enviar la oferta.", Notification.Type.ERROR_MESSAGE);
				}
			} else {
				this.mostrarNotificacion("Error: La oferta enviada debe ser mayor al monto actual.", Notification.Type.ERROR_MESSAGE);
			}
		} else {
			this.mostrarNotificacion("Error: Ingrese un monto.", Notification.Type.ERROR_MESSAGE);
		}
	}


	private void inicializarLayouts() {
		
		informacionLayout = new FormLayout(cabeceraDatos, titulo, ubicacion, montoBase, fechaReserva, tiempoRestante, creditosDisponibles);
		informacionLayout.setComponentAlignment(cabeceraDatos, Alignment.MIDDLE_CENTER);
		informacionLayout.setComponentAlignment(titulo, Alignment.MIDDLE_CENTER);
		informacionLayout.setComponentAlignment(ubicacion, Alignment.MIDDLE_CENTER);
		informacionLayout.setComponentAlignment(montoBase, Alignment.MIDDLE_CENTER);
		informacionLayout.setComponentAlignment(fechaReserva, Alignment.MIDDLE_CENTER);
		informacionLayout.setComponentAlignment(tiempoRestante, Alignment.MIDDLE_CENTER);
		informacionLayout.setComponentAlignment(creditosDisponibles, Alignment.MIDDLE_CENTER);
		informacionLayout.setWidth("600");
		informacionLayout.addStyleName("layout-with-border");
		
		botonesLayout = new HorizontalLayout(botonConfirmar, botonCancelar);
		
		ventanaLayout = new VerticalLayout(info, montoOferta, botonConfirmar, botonCancelar);
		ventanaLayout.setComponentAlignment(info, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(montoOferta, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(botonConfirmar, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(botonCancelar, Alignment.MIDDLE_CENTER);
		
		ventanaConfirmacion.setContent(ventanaLayout);
		
		mainLayout = new VerticalLayout(cabeceraPrincipal, informacionLayout, botonOfertar);
		mainLayout.setComponentAlignment(cabeceraPrincipal, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(informacionLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(botonOfertar, Alignment.MIDDLE_CENTER);

		setCompositionRoot(mainLayout);
	}
	

	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	
		notifResultado = new Notification(st, tipo);
    	notifResultado.setDelayMsec(5000);
    	notifResultado.show(Page.getCurrent());
    }	
}
