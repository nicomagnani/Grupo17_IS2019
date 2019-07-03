package com.team17.homeSwitchHomeUI;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
import homeSwitchHome.ReservaSubasta;

public class DetalleResidenciaView extends Composite implements View {
	
	private Label cabecera = new Label("Detalle de residencia");
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
	private ArrayList<Reserva> reservas = new ArrayList<>();
	private ArrayList<Reserva> resSinReservar = new ArrayList<>();
	private ArrayList<Reserva> resReservadas = new ArrayList<>();
	private String tipo;
	
	
	public DetalleResidenciaView(String tipo) {		
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		this.tipo = tipo;
		this.propiedad = HomeSwitchHome.getPropiedadActual();
		
		//configura la cabecera dependiendo de donde proviene
		if (tipo.equals("admin")) {
			cabecera.setValue("Detalle de residencia (administrador)");
			//
		} else
			if (tipo.equals("usuario")) {
				cabecera.setValue("Detalle de residencia (usuario)");
				//	
			} else
				if (tipo.equals("busqueda")) {
					cabecera.setValue("Detalle de residencia (búsqueda por fecha)");
					//	
				}
		
		titulo = new Label("<p><span style=\"text-align: left; font-weight: bold; font-size: 100%;\">Título:</span> <span style=\"font-size: 100%;\">"
				+propiedad.getTitulo()+"</span></p>", ContentMode.HTML);
		ubicacion = new Label("<span style=\"font-weight: bold;\">Ubicación:</span> " + propiedad.getPais() + ", " +
				propiedad.getProvincia() + ", " + propiedad.getLocalidad(), ContentMode.HTML);			
		descripcion = new Label("<span style=\"font-weight: bold;\">Descripción:</span> " + propiedad.getDescripcion(), ContentMode.HTML);
		
		
		
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

		msjSemanas.setVisible(false);
		msjReservas.setVisible(false);
		tablaSemanas.setVisible(false);
		tablaReservas.setVisible(false);		

		try {
			this.cargarReservas();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
		HorizontalLayout fotosLayout = new HorizontalLayout(foto1,foto2,foto3,foto4,foto5);		
		fotosLayout.setWidth("650");
		fotosLayout.addStyleName("scrollable");
		
		FormLayout propiedadLayout = new FormLayout(titulo,ubicacion,descripcion,verFotos,fotosLayout);
		propiedadLayout.setWidth("750");
		propiedadLayout.setSizeFull();
		propiedadLayout.setComponentAlignment(verFotos, Alignment.MIDDLE_CENTER);
		propiedadLayout.setComponentAlignment(fotosLayout, Alignment.MIDDLE_CENTER);
		
		VerticalLayout contentLayout = new VerticalLayout(propiedadLayout, tablaSemanas, msjSemanas, tablaReservas, msjReservas);
		
		contentLayout.setSizeUndefined();
		
		//coloco el layout con las tablas dentro del panel para poder scrollear
		panel.setContent(contentLayout);
		panel.setHeight("600");
		panel.setWidth("750");
		panel.addStyleName("scrollable");
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, panel);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);		
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		
		setCompositionRoot(mainLayout);
	}



	private void cargarReservas() throws SQLException {

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
			tablaSemanas.setItems(resSinReservar);
			tablaSemanas.setVisible(true);
			tablaSemanas.setColumns("fechaInicio", "fechaFin");
			
			tablaSemanas.addColumn(Reserva::getEstadoComoString)
			.setCaption("Estado (Tipo)");
			
			tablaSemanas.addColumn(Reserva::getMonto,
					new NumberRenderer(new DecimalFormat("¤#######.##")))
					.setCaption("Precio actual");
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
			
			//habilito y configuro la tabla de reservas (admin)
			if (resReservadas.isEmpty()) {			
				msjReservas.setVisible(true);
			} else {				
				tablaReservas.setItems(reservas);
				tablaReservas.setVisible(true);
				tablaReservas.setColumns("fechaInicio", "usuario");
				
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