package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import homeSwitchHome.Reserva;

public class SubastasAdminView extends Composite implements View {
	
	VerticalLayout main;
	ConnectionBD conexion;
	ArrayList<Reserva> reservas;
	
	public SubastasAdminView() {
		
		Label label = new Label("Test");
		main = new VerticalLayout(label);
		setCompositionRoot((Component) main);
    
	}
}
