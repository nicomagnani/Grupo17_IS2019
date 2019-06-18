package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.EstadoDeReserva;
import homeSwitchHome.Propiedad;
import homeSwitchHome.Reserva;
import homeSwitchHome.ReservaSubasta;

public class SubastasView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	private ArrayList<Reserva> subastas;
	private ConnectionBD conexion = new ConnectionBD();
	
	private Label cabecera = new Label("Lista de subastas");
	private Label msjResultado = new Label("No hay subastas disponibles.");
	
	private VerticalLayout subastasLayout = new VerticalLayout();
	private Panel panel = new Panel();
	
	public SubastasView() {
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		panel.setVisible(false);
		msjResultado.setVisible(false);	
		
		subastasLayout = new VerticalLayout();
		subastasLayout.setSizeUndefined();
		
		//coloco el layout con las propiedades dentro del panel para poder scrollear
		panel.setContent(subastasLayout);
		panel.setHeight("750");
		panel.setWidth("550");
		panel.addStyleName("scrollable");	
		
		this.cargarSubastas();

		VerticalLayout mainLayout = new VerticalLayout(cabecera, panel, msjResultado);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(msjResultado, Alignment.MIDDLE_CENTER);
		
        setCompositionRoot(mainLayout);
    }
	
	private void cargarSubastas() {
		
		try {
			subastas = conexion.listaReservasPorEstado(EstadoDeReserva.DISPONIBLE_SUBASTA);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!subastas.isEmpty()) {
			panel.setVisible(true);
			for (int i=0; i < subastas.size(); i++) {
				this.añadirSubastas(subastas.get(i));
			}
		} else {
			msjResultado.setVisible(true);		
		}
		
	}

	private void añadirSubastas(Reserva unaSubasta) {
		
		Label titulo = new Label("Título: "+unaSubasta.getPropiedad());
		Label localidad = new Label("Localidad: "+unaSubasta.getLocalidad());
		//Label tiempoRestante = new Label ...
		Label monto = new Label ("Monto actual: " + String.valueOf(unaSubasta.getMonto()));
		
		Button pujar = new Button("Pujar", e -> this.pujar());
		
		Label separador = MyUI.separador();
		
		FormLayout subastaLayout = new FormLayout(titulo,localidad,monto,pujar,separador);
		subastaLayout.setWidth("500");
		subastaLayout.setSizeFull();
		subastaLayout.setComponentAlignment(separador, Alignment.MIDDLE_CENTER);		
		
		subastasLayout.addComponent(subastaLayout);
		subastasLayout.setComponentAlignment(subastaLayout, Alignment.MIDDLE_CENTER);		
	}
	
	private void pujar() {} //TODO
}