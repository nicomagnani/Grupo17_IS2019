package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Oferta;
import homeSwitchHome.Reserva;
import homeSwitchHome.ReservaSubasta;

@Title("Mis subastas - HomeSwitchHome")
public class MisSubastasView extends Composite implements View {	
	
	private Label cabecera = new Label("Subastas en que participé");
	private Label msjResultado = new Label("No se han hecho ofertas en subastas.");
	private Grid<Oferta> tabla = new Grid<>(Oferta.class);
	
	private VerticalLayout ofertasLayout = new VerticalLayout();
	private Panel panel = new Panel();
	
	private ArrayList<Reserva> reservas;
	private ArrayList<ReservaSubasta> subastas = new ArrayList<>();
	private ArrayList<Oferta> ofertas = new ArrayList<>();
	private String mailActual;
	
	private ConnectionBD conexion = new ConnectionBD();
	
	
	public MisSubastasView() {
		
		mailActual = HomeSwitchHome.getUsuarioActual().getMail();
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		panel.setVisible(false);
		msjResultado.setVisible(false);	

		try {
			this.cargarSubastas();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.cargarTabla();
		
		ofertasLayout = new VerticalLayout(tabla);
		ofertasLayout.setSizeUndefined();
		
		//coloco el layout con la tabla dentro del panel para poder scrollear
		panel.setContent(ofertasLayout);
		panel.setHeight("500");
		panel.setWidth("700");
		panel.addStyleName("scrollable");		

		VerticalLayout mainLayout = new VerticalLayout(cabecera, panel, msjResultado);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);
		
        setCompositionRoot(mainLayout);
        
    }

	
	private void cargarSubastas() throws SQLException {
		
		reservas = conexion.listaReservasPorUsuario(mailActual);
		
		for (Reserva r : reservas) {
			if (r instanceof ReservaSubasta)
				subastas.add( conexion.buscarSubasta(r.getPropiedad(), r.getLocalidad(), r.getFechaInicio(),
						r.getEstado(), r.getMontoOriginal()) );
		}
		
		for (ReservaSubasta rs : subastas) {
			if ( (rs.getUsuarios() != null) && (rs.getUsuarios().indexOf(mailActual) != -1) ) {				
				ofertas.add( new Oferta(rs.getPropiedad(), rs.getLocalidad(), rs.getFechaReserva(),
						rs.getFechaFinSubastaString(), rs.getMonto(), rs.getMontos().get(rs.getUsuarios().indexOf(mailActual))) );
			}
		}		
	}
	
		
	private void cargarTabla() {
			
		if ( ofertas.isEmpty() )
			msjResultado.setVisible(true);
		else {
			panel.setVisible(true);
			tabla.setItems(ofertas);
			tabla.setWidth("700");
			tabla.setColumns("propiedad", "localidad", "fechaReserva", "fechaFinSubasta");
						
			tabla.addColumn(Oferta::getPrecioString)
			.setCaption("Precio actual");
			
			tabla.addColumn(Oferta::getMontoString)
			.setCaption("Mi oferta");
		}		
	}
}
