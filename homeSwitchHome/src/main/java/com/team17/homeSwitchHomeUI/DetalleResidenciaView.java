package com.team17.homeSwitchHomeUI;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.EstadoDeReserva;
import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Propiedad;
import homeSwitchHome.Reserva;
import homeSwitchHome.ReservaDirecta;
import homeSwitchHome.ReservaSubasta;
import homeSwitchHome.Usuario;
import homeSwitchHome.UsuarioPremium;

@Title("Detalle de residencia - HomeSwitchHome")
public class DetalleResidenciaView extends Composite implements View {
	
	private Label cabeceraPrincipal = new Label("Detalle de residencia");
	private Label cabeceraDatos = new Label("Datos de residencia");
	private Label cabeceraSemanas = new Label("Semanas");
	private Label cabeceraReservas = new Label("Reservas");
	
	private Label ayuda1 = new Label("Para realizar una reserva directa, haga click sobre la semana deseada.");
	private Label ayuda2 = new Label("Nota: Las reserva directas requieren estado de usuario premium.");
	private Label msjSemanas = new Label("Esta residencia no posee semanas disponibles.");
	private Label msjReservas = new Label("Esta residencia no posee reservas realizadas.");
	private Grid<Reserva> tablaSemanas = new Grid<>(Reserva.class);
	private Grid<Reserva> tablaReservas = new Grid<>(Reserva.class);
		
	private Label titulo = new Label();
	private Label descripcion = new Label();
	private Label ubicacion = new Label();
	private Button verFotos = new Button("Ver Fotos");
	
	private Image foto1 = new Image("Foto 1");
	private Image foto2 = new Image("Foto 2");
	private Image foto3 = new Image("Foto 3");
	private Image foto4 = new Image("Foto 4");
	private Image foto5 = new Image("Foto 5");   
	
	private Panel panel = new Panel();
	
	private ConnectionBD conexion = new ConnectionBD();
	
	private Propiedad propiedad;
	private Usuario usuario;
	private ArrayList<Reserva> reservas = new ArrayList<>();
	private ArrayList<Reserva> resSinReservar = new ArrayList<>();
	private ArrayList<Reserva> resReservadas = new ArrayList<>();
	private String tipo;
	private MyUI interfaz;
	
	
	public DetalleResidenciaView(String tipo, MyUI interfaz) {		
		
		this.interfaz = interfaz;
		this.tipo = tipo;
		this.propiedad = HomeSwitchHome.getPropiedadActual();
		this.usuario = HomeSwitchHome.getUsuarioActual();
		
		cabeceraPrincipal.addStyleName(ValoTheme.MENU_TITLE);
		cabeceraDatos.addStyleName(ValoTheme.MENU_TITLE);
		cabeceraSemanas.addStyleName(ValoTheme.MENU_TITLE);
		cabeceraReservas.addStyleName(ValoTheme.MENU_TITLE);		
		
		
		this.inicializarDatosResidencia();		
		
		this.inicializarFotos();

		ayuda1.setVisible(false);
		ayuda2.setVisible(false);
		msjSemanas.setVisible(false);
		msjReservas.setVisible(false);
		tablaSemanas.setVisible(false);
		tablaReservas.setVisible(false);		
				
		HorizontalLayout fotosLayout = new HorizontalLayout(foto1,foto2,foto3,foto4,foto5);		
		fotosLayout.setWidth("650");
		fotosLayout.addStyleName("scrollable");
		
		FormLayout propiedadLayout = new FormLayout(cabeceraDatos,titulo,ubicacion,descripcion,verFotos,fotosLayout);
		propiedadLayout.setWidth("750");
		propiedadLayout.setSizeFull();
		propiedadLayout.setComponentAlignment(verFotos, Alignment.MIDDLE_CENTER);
		propiedadLayout.setComponentAlignment(fotosLayout, Alignment.MIDDLE_CENTER);
		propiedadLayout.addStyleName("layout-with-border");
		
		VerticalLayout semanasLayout = new VerticalLayout (cabeceraSemanas, ayuda1, ayuda2, tablaSemanas, msjSemanas);
		semanasLayout.setComponentAlignment(cabeceraSemanas, Alignment.MIDDLE_CENTER);
		semanasLayout.setComponentAlignment(ayuda1, Alignment.MIDDLE_CENTER);
		semanasLayout.setComponentAlignment(ayuda2, Alignment.MIDDLE_CENTER);
		semanasLayout.setComponentAlignment(tablaSemanas, Alignment.MIDDLE_CENTER);
		semanasLayout.setComponentAlignment(msjSemanas, Alignment.MIDDLE_CENTER);
		semanasLayout.addStyleName("layout-with-border");		
		
		VerticalLayout reservasLayout = new VerticalLayout (cabeceraReservas, tablaReservas, msjReservas);
		reservasLayout.setComponentAlignment(tablaReservas, Alignment.MIDDLE_CENTER);
		reservasLayout.setComponentAlignment(msjReservas, Alignment.MIDDLE_CENTER);		
		reservasLayout.addStyleName("layout-with-border");
		
		VerticalLayout contentLayout = new VerticalLayout(propiedadLayout, semanasLayout, reservasLayout);
		contentLayout.setComponentAlignment(propiedadLayout, Alignment.MIDDLE_CENTER);
		contentLayout.setComponentAlignment(semanasLayout, Alignment.MIDDLE_CENTER);
		contentLayout.setComponentAlignment(reservasLayout, Alignment.MIDDLE_CENTER);
		reservasLayout.setVisible(false);
		
		contentLayout.setSizeUndefined();
		
		//coloco el layout con los datos de la propiedad y las tablas dentro del panel para poder scrollear
		panel.setContent(contentLayout);
		panel.setHeight("600");
		panel.setWidth("750");
		panel.addStyleName("scrollable");
		
		VerticalLayout mainLayout = new VerticalLayout(cabeceraPrincipal, panel);
		mainLayout.setComponentAlignment(cabeceraPrincipal, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		
		setCompositionRoot(mainLayout);
		

		try {
			this.cargarReservas(reservasLayout);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private void inicializarDatosResidencia() {
		
		//configura la cabecera dependiendo de donde proviene
		if (tipo.equals("admin")) {
			cabeceraPrincipal.setValue("Detalle de residencia (administrador)");
			//
		} else
			if (tipo.equals("usuario")) {
				cabeceraPrincipal.setValue("Detalle de residencia (usuario)");
				//	
			} else
				if (tipo.equals("busqueda")) {
					cabeceraPrincipal.setValue("Detalle de residencia (búsqueda por fecha)");
					//	
				}
		
		titulo = new Label("<p><span style=\"text-align: left; font-weight: bold; font-size: 110%;\">Título:</span> <span style=\"font-size: 110%;\">"
				+propiedad.getTitulo()+"</span></p>", ContentMode.HTML);
		
		ubicacion = new Label("<span style=\"font-weight: bold;\">Ubicación:</span> " + propiedad.getPais() + ", " +
				propiedad.getProvincia() + ", " + propiedad.getLocalidad(), ContentMode.HTML);
		
		descripcion = new Label("<span style=\"font-weight: bold;\">Descripción:</span> " + propiedad.getDescripcion(), ContentMode.HTML);
	}


	private void inicializarFotos() {		

		foto1.setVisible(false);
		foto2.setVisible(false);
		foto3.setVisible(false);
		foto4.setVisible(false);
		foto5.setVisible(false);
		
		if ( (propiedad.getFoto1() == null) && (propiedad.getFoto2() == null) && (propiedad.getFoto3() == null)
				&& (propiedad.getFoto4() == null) && (propiedad.getFoto5() == null) ) {
			verFotos.setVisible(false);
		} else {
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
	}


	private void cargarReservas(VerticalLayout reservasLayout) throws SQLException {

		ReservaSubasta r2;
		
		reservas = conexion.listaReservasPorPropiedad(propiedad.getTitulo(), propiedad.getLocalidad());
		
		//crea la lista de semanas de acuerdo al tipo
		if (tipo.equals("admin")) {
			for (Reserva r : reservas) {
				//chequea que la semana no lleve un año publicada ni esté reservada
				if ( (r.getEstado() != EstadoDeReserva.FINALIZADA) && (r.getEstado() != EstadoDeReserva.RESERVADA) ) {					
					//si es una subasta, carga sus datos desde la tabla 'subastas'
					if (r instanceof ReservaSubasta) {
						r2 = conexion.buscarSubasta(r.getPropiedad(), r.getLocalidad(), r.getFechaInicio(), r.getEstado());
						resSinReservar.add(r2);
					} else
						resSinReservar.add(r);
				}
			}			
		} else if (tipo.equals("usuario")) {
				for (Reserva r : reservas) {
					//chequea que la semana esté disponible
					if ( (r.getEstado() == EstadoDeReserva.DISPONIBLE_DIRECTA) || (r.getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA)
							|| (r.getEstado() == EstadoDeReserva.DISPONIBLE_HOTSALE) ) {						
						//si es una subasta, carga sus datos desde la tabla 'subastas'
						if (r instanceof ReservaSubasta) {
							r2 = conexion.buscarSubasta(r.getPropiedad(), r.getLocalidad(), r.getFechaInicio(), r.getEstado());
							resSinReservar.add(r2);
						} else
							resSinReservar.add(r);
					}
				}				
			} else if (tipo.equals("busqueda")) {
					for (Reserva r : reservas) {
						//chequea que la semana esté disponible y se encuentre entre las fechas buscadas
						if ( ((r.getEstado() == EstadoDeReserva.DISPONIBLE_DIRECTA) || (r.getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA)
								|| (r.getEstado() == EstadoDeReserva.DISPONIBLE_HOTSALE)) 
								&& (r.reservadaEntreFechas(HomeSwitchHome.getFechaInicioBuscada(), HomeSwitchHome.getFechaFinBuscada())) ) {
							//si es una subasta, carga sus datos desde la tabla 'subastas'
							if (r instanceof ReservaSubasta) {
								r2 = conexion.buscarSubasta(r.getPropiedad(), r.getLocalidad(), r.getFechaInicio(), r.getEstado());
								resSinReservar.add(r2);
							} else
								resSinReservar.add(r);
						}
					}	
				}

		//habilito y configuro la tabla de semanas
		if (resSinReservar.isEmpty()) {
			msjSemanas.setVisible(true);
		} else {
			if (!tipo.equals("admin")) {
				ayuda1.setVisible(true);
				ayuda2.setVisible(true);
			}
			tablaSemanas.setItems(resSinReservar);
			tablaSemanas.setVisible(true);
			tablaSemanas.setColumns("fechaFin", "fechaReserva");
			
			tablaSemanas.addColumn(Reserva::getEstadoComoString)
			.setCaption("Estado (Tipo)");
			
			tablaSemanas.addColumn(Reserva::getMonto,
					new NumberRenderer(new DecimalFormat("¤#######.##")))
					.setCaption("Precio actual");

			tablaSemanas.addItemClickListener( event -> {
				if ( (event.getItem() instanceof ReservaDirecta) && (usuario instanceof UsuarioPremium) ) {
						HomeSwitchHome.setReservaActual(event.getItem());
						interfaz.vistaUsuarioConNuevaVista("reservarDirecta");
				}
			});
			
			
		}
		
		if (tipo.equals("admin")) {
			
			//creo lista de reservas (admin)
			for (Reserva r : reservas) {
				//chequeo que esté reservada (haya finalizado o no)
				if ( ((r.getEstado() == EstadoDeReserva.FINALIZADA) && (r.getUsuario() != null))
						|| (r.getEstado() == EstadoDeReserva.RESERVADA) ) {
					resReservadas.add(r);
				}
			}
			
			reservasLayout.setVisible(true);
			
			//habilito y configuro la tabla de reservas (admin)
			if (resReservadas.isEmpty()) {			
				msjReservas.setVisible(true);
			} else {				
				tablaReservas.setItems(reservas);
				tablaReservas.setVisible(true);
				tablaReservas.setColumns("fechaInicio", "fechaFin", "fechaReserva", "usuario");
				
				tablaReservas.addColumn(Reserva::getMonto,
						new NumberRenderer(new DecimalFormat("¤#######.##")))
						.setCaption("Monto");
				
				tablaReservas.addColumn(Reserva::getTipo)
						.setCaption("Tipo");
			}
		}
		
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
	

}