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
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Propiedad;
import homeSwitchHome.ReservaSubasta;
import homeSwitchHome.Usuario;

@Title("Subastas - HomeSwitchHome")
public class SubastasUsuarioView extends Composite implements View { // necesita composite y view para funcionar correctamente

	private Label cabecera = new Label("Lista de subastas");
	private Label msjResultado = new Label("No hay subastas disponibles.");

	private Window ventanaConfirmacion = new Window("Confirmar oferta");
	private Label info = new Label("Indique un monto a ofertar:");
	private NumberField montoOferta = new NumberField();
	private Button botonConfirmar = new Button("Enviar");
	private Button botonCancelar = new Button("Cancelar");
	private Notification notifResultado;

	private VerticalLayout subastasLayout = new VerticalLayout();
	private Panel panel = new Panel();
	private VerticalLayout ventanaLayout = new VerticalLayout();

	private ArrayList<ReservaSubasta> subastas;
	private Usuario usuario;
	private ReservaSubasta rsActual;
	private Button botonActual;

	private ConnectionBD conexion = new ConnectionBD();
	private MyUI interfaz;




	public SubastasUsuarioView(MyUI interfaz) {

		this.interfaz = interfaz;

		this.cargarSubastas();
		this.inicializarComponentes();
		this.inicializarLayouts();
    }


	private void cargarSubastas() {

		try {
			subastas = conexion.listaSubastas();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private void inicializarComponentes() {

		usuario = HomeSwitchHome.getUsuarioActual();

		cabecera.addStyleName(ValoTheme.MENU_TITLE);

		panel.setVisible(false);
		msjResultado.setVisible(false);

		int n = 0;
		if (!subastas.isEmpty()) {
			for (ReservaSubasta subasta : subastas) {

				//muestra solo subastas en curso
				if (!subasta.getFechaFinSubastaString().equals("Finalizada")) {
					this.añadirSubasta(subasta);
					n++;
				}
			}
		}

		if (n > 0) {
			panel.setVisible(true);
		} else
			msjResultado.setVisible(true);

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

		botonConfirmar.addClickListener(e -> {
			this.ofertar(botonActual, rsActual);
		} );
		botonCancelar.addClickListener(e -> ventanaConfirmacion.close() );
	}


	private void añadirSubasta(ReservaSubasta rs) {

		//para mostrar tiempo restante
		//long tiempo1 = Duration.between(LocalDateTime.now(), unaSubasta.getFechaInicioSubasta().plusDays(3).atStartOfDay()).toMillis();
		//String tiempo2 = DurationFormatUtils.formatDuration(tiempo1, "H horas, mm minutos", true);

		Label titulo = new Label("<p><span style=\"text-align: left; font-weight: bold; font-size: 120%;\">Título:</span>"
				+ " <span style=\"font-size: 120%;\">"+rs.getPropiedad()+"</span></p>", ContentMode.HTML);
		Label localidad = new Label("<span style=\"font-weight: bold;\">Localidad:</span> " + rs.getLocalidad(), ContentMode.HTML);
		Label tiempoRestante = new Label("<span style=\"font-weight: bold;\">Finaliza:</span> " + rs.getFechaFinSubastaString(), ContentMode.HTML);
		Label monto = new Label("<span style=\"font-weight: bold;\">Monto actual:</span> " + rs.getMontos().get(0), ContentMode.HTML);

		Button botonOfertar = new Button("Ofertar");
		botonOfertar.addClickListener( e -> this.abrirVentanaConfirmacion(String.valueOf(rs.getMontos().get(0)), rs, botonOfertar) );

		// oculta el botón Ofertar si el usuario ya posee la máxima oferta (CONSULTAR)
//		if ( HomeSwitchHome.getUsuarioActual().getMail().equals(rs.getUsuario()) )
//			botonOfertar.setVisible(false);

		FormLayout subastaLayout = new FormLayout(titulo,localidad,tiempoRestante,monto,botonOfertar);
		subastaLayout.setSizeUndefined();
		subastaLayout.setWidth("500");
		subastaLayout.addStyleName("layout-with-border");
		subastaLayout.setComponentAlignment(titulo, Alignment.MIDDLE_CENTER);
		subastaLayout.setComponentAlignment(localidad, Alignment.MIDDLE_CENTER);
		subastaLayout.setComponentAlignment(tiempoRestante, Alignment.MIDDLE_CENTER);
		subastaLayout.setComponentAlignment(monto, Alignment.MIDDLE_CENTER);
		subastaLayout.setComponentAlignment(botonOfertar, Alignment.MIDDLE_CENTER);

		subastasLayout.addComponent(subastaLayout);
		subastasLayout.setComponentAlignment(subastaLayout, Alignment.MIDDLE_CENTER);
	}


	private void inicializarLayouts() {

		HorizontalLayout botones2Layout = new HorizontalLayout(botonConfirmar,botonCancelar);

		ventanaLayout = new VerticalLayout(info, montoOferta, botones2Layout);
		ventanaLayout.setComponentAlignment(info, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(montoOferta, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(botones2Layout, Alignment.MIDDLE_CENTER);

		ventanaConfirmacion.setContent(ventanaLayout);

		subastasLayout.setSizeUndefined();

		//coloco el layout con las propiedades dentro del panel para poder scrollear
		panel.setContent(subastasLayout);
		panel.setHeight("600");
		panel.setWidth("750");
		panel.addStyleName("scrollable");

		VerticalLayout mainLayout = new VerticalLayout(cabecera, panel, msjResultado);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);

        setCompositionRoot(mainLayout);
	}


	private void abrirVentanaConfirmacion(String montoString, ReservaSubasta rs, Button botonOfertar) {

		rsActual = rs;
		botonActual = botonOfertar;

		montoOferta.setValue(montoString);
		UI.getCurrent().addWindow(ventanaConfirmacion);


	}


	private void ofertar(Button botonOfertar, ReservaSubasta rs) {

		if (!montoOferta.isEmpty()) {

			float montoOfertaFloat = Float.parseFloat(montoOferta.getValue());
			float montoActualFloat = rs.getMontos().get(0);

			if (montoOfertaFloat > montoActualFloat) {

				if (usuario.getCreditos() > 0) {

					//modifico oferta y ofertante de la subasta (creo lista de usuarios si no hay ofertas)
					if (rs.getUsuarios() == null)
						rs.setUsuarios(new ArrayList<String>());

					rs.getMontos().add(0, Float.valueOf(montoOferta.getValue()));
					rs.getUsuarios().add(0, usuario.getMail());

					//actualizo subasta en la base de datos
					conexion = new ConnectionBD();
					try {
						conexion.modificarSubasta(rs);
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


	private void mostrarNotificacion(String st, Notification.Type tipo) {

		notifResultado = new Notification(st, tipo);
		notifResultado.setHtmlContentAllowed(true);
    	notifResultado.setDelayMsec(-1);;
    	notifResultado.show(Page.getCurrent());
    }

}