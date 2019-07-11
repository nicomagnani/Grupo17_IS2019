package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.EstadoDeReserva;
import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Reserva;
import homeSwitchHome.Usuario;

@Title("Mis reservas - HomeSwitchHome")
public class MisReservasView extends Composite implements View {
	
	private Label cabecera = new Label("Mi historial de reservas");
	private Label msj = new Label("No se han realizado reservas.");
	private Grid<Reserva> tabla = new Grid<>(Reserva.class);
	
	private ArrayList<Reserva> reservas = new ArrayList<>();
	private ArrayList<Reserva> canceladas = new ArrayList<>();
	private ArrayList<Reserva> reservasUsuario = new ArrayList<>();
	
	private ConnectionBD conexion = new ConnectionBD();
	
	private Usuario usuario = HomeSwitchHome.getUsuarioActual();
	
	
	public MisReservasView() {
		
		
		this.cargarReservas();
		this.inicializarComponentes();
		this.inicializarLayouts();
	}

	
	private void cargarReservas() {

		try {
			reservas = conexion.listaReservasPorUsuario(usuario.getMail());
			canceladas = conexion.listaCanceladasPorUsuario(usuario.getMail());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//creo lista de reservas (admin)
		for (Reserva r : reservas) {
			//chequeo que esté reservada (haya finalizado o no)
			if ( ((r.getEstado() == EstadoDeReserva.FINALIZADA) && (r.getUsuario() != null))
					|| (r.getEstado() == EstadoDeReserva.RESERVADA) ) {
				reservasUsuario.add(r);
			}
		}
		
		for (Reserva r : canceladas) {
			reservasUsuario.add(r);
		}		
	}


	private void inicializarComponentes() {

		cabecera.addStyleName(ValoTheme.MENU_TITLE);		

		msj.setVisible(false);
		tabla.setVisible(false);
				
		//configuro la tabla de reservas (admin)
		if (reservasUsuario.isEmpty()) {			
			msj.setVisible(true);
		} else {				
			tabla.setItems(reservasUsuario);
			tabla.setVisible(true);
			tabla.setColumns("propiedad", "localidad", "fechaReserva");
			
			tabla.addColumn(Reserva::getMonto,
					new NumberRenderer(new DecimalFormat("¤#######.##")))
					.setCaption("Monto");
			
			tabla.addColumn(Reserva::getTipo)
					.setCaption("Tipo");
			
			tabla.addColumn(Reserva::getEstadoComoString)
					.setCaption("Estado (Tipo)");
			
			tabla.setWidth("650");
		}
	}


	private void inicializarLayouts() {
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, tabla, msj);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(tabla, Alignment.MIDDLE_CENTER);

		setCompositionRoot(mainLayout);
	}

}