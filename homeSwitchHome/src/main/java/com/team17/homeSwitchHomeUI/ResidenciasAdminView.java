package com.team17.homeSwitchHomeUI;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.EstadoDeReserva;
import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Propiedad;
import homeSwitchHome.Reserva;
import homeSwitchHome.ReservaDirecta;
import homeSwitchHome.ReservaHotsale;
import homeSwitchHome.ReservaSubasta;

@Title("Residencias - HomeSwitchHome")
public class ResidenciasAdminView extends Composite implements View {
	
	private Label cabecera = new Label("Lista de residencias (administrador)");
	private Label msjResultado = new Label("No hay residencias cargadas.");
	
	private Notification notifResultado = new Notification("Residencia borrada con éxito.");
	private Window ventanaConfirmacion = new Window("Confirmar eliminación");
	private Label info = new Label("¿Está seguro que desea eliminar la residencia?");
	private Button botonConfirmar = new Button("Confirmar");
	private Button botonCancelar = new Button("Cancelar");
	private HtmlEmail email;
	
	private VerticalLayout propiedadesLayout = new VerticalLayout();
	private Panel panel = new Panel();
	private VerticalLayout ventanaLayout = new VerticalLayout();
	
	
	private ArrayList<Propiedad> propiedades = new ArrayList<>();	
	private Propiedad propActual;
	private FormLayout layoutActual;
	
	private ConnectionBD conexion = new ConnectionBD();	
	private MyUI interfaz;
	

	public ResidenciasAdminView(MyUI interfaz) {
		
		this.interfaz = interfaz;		

		this.cargarResidencias();
		this.inicializarComponentes();
		this.inicializarLayouts();
	}	


	private void cargarResidencias() {		
		
		try {
			propiedades = conexion.listaResidenciasConFotos();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private void inicializarComponentes() {

		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		panel.setVisible(false);
		msjResultado.setVisible(false);
				
		if (!propiedades.isEmpty()) {
			panel.setVisible(true);			
			for (Propiedad p : propiedades)
				this.añadirResidencia(p);
		} else
			msjResultado.setVisible(true);
		
		botonConfirmar.addClickListener(e -> {
			try {
				this.eliminar(propActual, layoutActual);
			} catch (SQLException | EmailException e1) {
				e1.printStackTrace();
			}
		} );
		
		botonCancelar.addClickListener(e -> ventanaConfirmacion.close() );		
					
		ventanaConfirmacion.center();
		ventanaConfirmacion.setWidth("500");
		ventanaConfirmacion.setResizable(true);
		ventanaConfirmacion.setModal(true);
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
			interfaz.vistaAdminConNuevaVista("detalleResidencia");
		});
		
		Button botonModificar = new Button("Modificar", e -> {
			HomeSwitchHome.setPropiedadActual(propiedad);			
			interfaz.vistaAdminConNuevaVista("modificarResidencia");
		});
		
		Button botonEliminar = new Button("Eliminar");
	
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

    	HorizontalLayout botonesLayout = new HorizontalLayout(verFotos, verDetalle, botonModificar, botonEliminar);
    	
    	HorizontalLayout fotosLayout = new HorizontalLayout(foto1,foto2,foto3,foto4,foto5);
    	fotosLayout.setWidth("650");
    	fotosLayout.addStyleName("scrollable");
		
		FormLayout propiedadLayout = new FormLayout(titulo,ubicacion,descripcion,montoBase,botonesLayout,fotosLayout);
		propiedadLayout.setWidth("500");
		propiedadLayout.setSizeFull();
		propiedadLayout.setComponentAlignment(fotosLayout, Alignment.MIDDLE_CENTER);
		propiedadLayout.addStyleName("layout-with-border");
		
		propiedadesLayout.addComponent(propiedadLayout);
		propiedadesLayout.setComponentAlignment(propiedadLayout, Alignment.MIDDLE_CENTER);		
		
		botonEliminar.addClickListener(e -> this.abrirVentanaConfirmacion(propiedad, propiedadLayout) );
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
	

	private void inicializarLayouts() {

		propiedadesLayout.setSizeUndefined();
		
		panel.setContent(propiedadesLayout);
		panel.setHeight("600");
		panel.setWidth("750");
		panel.addStyleName("scrollable");
		
		HorizontalLayout botones2Layout = new HorizontalLayout(botonConfirmar,botonCancelar);

		ventanaLayout = new VerticalLayout(info, botones2Layout);
		ventanaLayout.setComponentAlignment(info, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(botones2Layout, Alignment.MIDDLE_CENTER);

		ventanaConfirmacion.setContent(ventanaLayout);
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, panel, msjResultado);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);
		
        setCompositionRoot(mainLayout);
		
	}

	
	private void abrirVentanaConfirmacion(Propiedad propiedad, FormLayout propiedadLayout) {

		propActual = propiedad;
		layoutActual = propiedadLayout;
		
		UI.getCurrent().addWindow(ventanaConfirmacion);
	}
		
	
	private void eliminar(Propiedad propiedad, FormLayout propiedadLayout) throws SQLException, EmailException {		
		
		/* #PASOS#
		1) verificar si tenia subasta en curso, enviar mails y agregar al informe
		2a) verificar si tenia reservas directas, devolver creditos, enviar mails y agregar al informe
		2a) verificar si tenia reservas hotsale, enviar mails y agregar al informe
		3) informar todo junto en 1 notificacion: si tenia subasta en curso, si habia ofertantes y cuantas reservas
		4) eliminar residencia de la BD
		5) actualizar sesion de admin
		*/
		
		int n = 0; //cantidad de ofertantes informados / cantidad de reservas eliminadas (parcial)
		int m = 0; //cantidad de reservas eliminadas (total)
		String msjNotificacion = "Residencia borrada con éxito.<br/>";
		propiedad.setReservas( conexion.listaReservasPorPropiedad(propiedad.getTitulo(), propiedad.getLocalidad()) );
		
		if (propiedad.haySubastasEncurso()) {				
			
			this.inicializarEmailSubasta(propiedad.getTitulo(), propiedad.getLocalidad());
			
			for (Reserva r : propiedad.getReservas()) {
				if ( (r.getEstado() == EstadoDeReserva.DISPONIBLE) && (r instanceof ReservaSubasta) ) {
					ReservaSubasta r2 = conexion.buscarSubasta(r.getPropiedad(), r.getLocalidad(), r.getFechaInicio(),
							r.getEstado(), r.getMontoOriginal());
					if (r2.getUsuarios() != null)
						for (String usuario : r2.getUsuarios()) {
							this.agregarReceptorDeEmailSubasta(usuario);
							n++;
						}
				}
			}
			
			if (n > 1) {
				email.send();
				msjNotificacion += "Se encontraba en subasta y "+n+" ofertantes fueron informados vía email.<br/>";
			} else
				if (n > 0) {
					email.send();
					msjNotificacion += "Se encontraba en subasta y "+n+" ofertante fue informado vía email.<br/>";
				} else
					msjNotificacion += "Se se encontraba en en subasta y sin ofertas.<br/>";
		} else
			msjNotificacion += "No se encontraba en subasta.<br/>";

		if (propiedad.hayReservasRealizadas()) {
			
			n = 0;
			this.inicializarEmailReservaDirectaSubasta(propiedad.getTitulo(), propiedad.getLocalidad());
			
			// debe preparar un mail distinto según si devuelve créditos o no, y no puede preparar ambos a la vez,
			//por lo tanto recorre un vez por cada tipo
			for (Reserva r : propiedad.getReservas())
				if ( (r.getEstado() == EstadoDeReserva.RESERVADA)
						&& ((r instanceof ReservaDirecta) || (r instanceof ReservaSubasta)) ) {
					n++;					
					conexion.modificarUsuarioCreditos(r.getUsuario(), "+", 1);
					this.agregarReceptorDeEmailReservaDirectaSubasta(r.getUsuario());
				}
			
			if (n > 0) {
				email.send();				
				m += n;
				n = 0;
			}
			
			this.inicializarEmailReservaHotsale(propiedad.getTitulo(), propiedad.getLocalidad());
			
			for (Reserva r : propiedad.getReservas())
				if ( (r.getEstado() == EstadoDeReserva.RESERVADA) && (r instanceof ReservaHotsale) ) {
					n++;
					this.agregarReceptorDeEmailReservaHotsale(r.getUsuario());
				}
			
			
			if (n > 0) {
				email.send();
				m += n;
			}
			
			if (m > 1) {
				msjNotificacion += "Tenía "+m+" reservas hechas, por lo que sus usuarios fueron informados vía email.";
			} else
				msjNotificacion += "Tenía "+m+" reserva hecha, por lo que su usuario fue informado vía email.";
			
		} else
			msjNotificacion += "No tenía reservas hechas.";
		
		ventanaConfirmacion.close();
		mostrarNotificacion(msjNotificacion, Notification.Type.HUMANIZED_MESSAGE);
		conexion.eliminarResidencia(propiedad);
		interfaz.vistaAdmin("residenciasAdmin");
	}
	

	private void inicializarEmailSubasta(String titulo, String localidad) throws EmailException {

		email = new HtmlEmail();
		email.setHostName("localhost");
		email.setSmtpPort(9090);
		email.setAuthentication("homeswitchhome@outlook.com.ar", "1234");		
		email.setFrom("homeswitchhome@outlook.com.ar");
		email.setSubject("Propiedad en subasta eliminada");
		
		String mensaje = "<p>Estimado usuario:<br/><br/> La propiedad de título "+titulo+" ubicada en"
				+ " la localidad de "+localidad+", con una subasta en la que usted se encontraba"
				+ " participando, ha sido eliminada. Sepa disculpar las molestias.</p><p>Atte. Staff"
				+ " de <span style=\"text-decoration: underline;\">HomeSwitchHome</span></p>";
		email.setHtmlMsg(mensaje);
	}
	
	
	private void inicializarEmailReservaDirectaSubasta(String titulo, String localidad) throws EmailException {
		
		email = new HtmlEmail();
		email.setHostName("localhost");
		email.setSmtpPort(9090);
		email.setAuthentication("homeswitchhome@outlook.com.ar", "1234");		
		email.setFrom("homeswitchhome@outlook.com.ar");
		email.setSubject("Propiedad reservada eliminada");
		
		String mensaje = "<p>Estimado usuario:<br/><br/> La propiedad de título "+titulo+" ubicada en"
				+ " la localidad de "+localidad+" y que usted había reservado ha sido eliminada."
				+ " Se le ha devuelto 1 crédito. Sepa disculpar las molestias.</p><p>Atte. Staff"
				+ " de <span style=\"text-decoration: underline;\">HomeSwitchHome</span></p>";
		email.setHtmlMsg(mensaje);
	}
	
	
	private void inicializarEmailReservaHotsale(String titulo, String localidad) throws EmailException {

		email = new HtmlEmail();
		email.setHostName("localhost");
		email.setSmtpPort(9090);
		email.setAuthentication("homeswitchhome@outlook.com.ar", "1234");		
		email.setFrom("homeswitchhome@outlook.com.ar");
		email.setSubject("Propiedad reservada eliminada");
		
		String mensaje = "<p>Estimado usuario:<br/><br/> La propiedad de título "+titulo+" ubicada en"
				+ " la localidad de "+localidad+" y que usted había reservado en Hotsale ha sido eliminada."
				+ " Sepa disculpar las molestias.</p><p>Atte. Staff"
				+ " de <span style=\"text-decoration: underline;\">HomeSwitchHome</span></p>";
		email.setHtmlMsg(mensaje);
	}
	
	
	private void agregarReceptorDeEmailSubasta (String mail) {
		
		try {
			email.addTo(mail);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	private void agregarReceptorDeEmailReservaDirectaSubasta (String mail) {
		
		try {
			email.addTo(mail);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	private void agregarReceptorDeEmailReservaHotsale (String mail) {
		
		try {
			email.addTo(mail);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}


	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	
		notifResultado = new Notification(st, tipo);
    	notifResultado.setDelayMsec(-1);
    	notifResultado.setHtmlContentAllowed(true);
    	notifResultado.show(Page.getCurrent());
    }
	
}