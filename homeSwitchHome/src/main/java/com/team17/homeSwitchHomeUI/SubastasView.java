package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.vaadin.navigator.View;
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

public class SubastasView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	private ArrayList<ReservaSubasta> subastas;
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
			subastas = conexion.listaSubastas();
		} catch (SQLException e) {
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

	private void añadirSubastas(ReservaSubasta unaSubasta) {
		
		// para mostrar tiempo restante
		//long tiempo1 = Duration.between(LocalDateTime.now(), unaSubasta.getFechaInicioSubasta().plusDays(3).atStartOfDay()).toMillis();
		//String tiempo2 = DurationFormatUtils.formatDuration(tiempo1, "H horas, mm minutos", true);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		String fechaFinSubasta = unaSubasta.getFechaInicioSubasta().plusDays(3).atStartOfDay().format(formatter);
		
		Label titulo = new Label("<p><span style=\"text-align: left; font-weight: bold; font-size: 120%;\">Título:</span> <span style=\"font-size: 120%;\">"+unaSubasta.getPropiedad()+"</span></p>", ContentMode.HTML);
		Label localidad = new Label("<span style=\"font-weight: bold;\">Localidad:</span> " + unaSubasta.getLocalidad(), ContentMode.HTML);	
		Label tiempoRestante = new Label("<span style=\"font-weight: bold;\">Tiempo restante:</span> " + fechaFinSubasta + " hs.", ContentMode.HTML);	
		Label monto = new Label("<span style=\"font-weight: bold;\">Monto actual:</span> " + unaSubasta.getMontos().get(0), ContentMode.HTML);
		
		Button pujar = new Button("Pujar", e -> this.pujar());
		
		Label separador = MyUI.separador();
		
		FormLayout subastaLayout = new FormLayout(titulo,localidad,tiempoRestante,monto,pujar,separador);
		subastaLayout.setWidth("600");
		subastaLayout.setSizeFull();
		subastaLayout.setComponentAlignment(separador, Alignment.MIDDLE_CENTER);		
		
		subastasLayout.addComponent(subastaLayout);
		subastasLayout.setComponentAlignment(subastaLayout, Alignment.MIDDLE_CENTER);		
	}
	
	private void pujar() {} //TODO
}