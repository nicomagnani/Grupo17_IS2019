package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.ReservaSubasta;

@Title("Subastas - HomeSwitchHome")
public class SubastasUsuarioView extends Composite implements View { // necesita composite y view para funcionar correctamente

	private Label cabecera = new Label("Lista de subastas");
	private Label msjResultado = new Label("No hay subastas disponibles.");
	
	private VerticalLayout subastasLayout = new VerticalLayout();
	private Panel panel = new Panel();
	
	private ArrayList<ReservaSubasta> subastas;
	private ConnectionBD conexion = new ConnectionBD();
	
	
	public SubastasUsuarioView() {
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		panel.setVisible(false);
		msjResultado.setVisible(false);	
		
		subastasLayout.setSizeUndefined();
		
		//coloco el layout con las propiedades dentro del panel para poder scrollear	
		panel.setContent(subastasLayout);
		panel.setHeight("600");
		panel.setWidth("750");
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
			subastas = conexion.listaSubastas();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
	}
	

	private void añadirSubasta(ReservaSubasta unaSubasta) {
		
		// para mostrar tiempo restante
		//long tiempo1 = Duration.between(LocalDateTime.now(), unaSubasta.getFechaInicioSubasta().plusDays(3).atStartOfDay()).toMillis();
		//String tiempo2 = DurationFormatUtils.formatDuration(tiempo1, "H horas, mm minutos", true);
				
		Label titulo = new Label("<p><span style=\"text-align: left; font-weight: bold; font-size: 120%;\">Título:</span>"
				+ " <span style=\"font-size: 120%;\">"+unaSubasta.getPropiedad()+"</span></p>", ContentMode.HTML);
		Label localidad = new Label("<span style=\"font-weight: bold;\">Localidad:</span> " + unaSubasta.getLocalidad(), ContentMode.HTML);	
		Label tiempoRestante = new Label("<span style=\"font-weight: bold;\">Finaliza:</span> " + unaSubasta.getFechaFinSubastaString(), ContentMode.HTML);	
		Label monto = new Label("<span style=\"font-weight: bold;\">Monto actual:</span> " + unaSubasta.getMontos().get(0), ContentMode.HTML);
		
		Button pujar = new Button("Pujar", e -> this.pujar());		
				
		FormLayout subastaLayout = new FormLayout(titulo,localidad,tiempoRestante,monto,pujar);
		subastaLayout.setSizeUndefined();
		subastaLayout.setWidth("500");
		subastaLayout.addStyleName("layout-with-border");
		subastaLayout.setComponentAlignment(titulo, Alignment.MIDDLE_CENTER);
		subastaLayout.setComponentAlignment(localidad, Alignment.MIDDLE_CENTER);
		subastaLayout.setComponentAlignment(tiempoRestante, Alignment.MIDDLE_CENTER);
		subastaLayout.setComponentAlignment(monto, Alignment.MIDDLE_CENTER);
		subastaLayout.setComponentAlignment(pujar, Alignment.MIDDLE_CENTER);
		
		subastasLayout.addComponent(subastaLayout);
		subastasLayout.setComponentAlignment(subastaLayout, Alignment.MIDDLE_CENTER);
	}
	
	
	private void pujar() {
		//TODO
	}
	
}