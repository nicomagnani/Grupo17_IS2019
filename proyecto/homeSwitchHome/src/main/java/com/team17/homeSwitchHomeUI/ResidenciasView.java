package com.team17.homeSwitchHomeUI;


import java.awt.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import homeSwitchHome.Propiedad;

public class ResidenciasView extends Composite implements View {  //.necesita composite y view para funcionar correctamente
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Statement stmt;
	public ResidenciasView() throws SQLException {		
			 ConnectionBD conectar= new ConnectionBD();
			 ArrayList<Propiedad> propiedades= new ArrayList<Propiedad>();
			 propiedades= conectar.listaPropiedades();
			
			 Grid<Propiedad> grid = new Grid<>(Propiedad.class);
			 grid.setItems(propiedades);
		
		  if(propiedades.size()==0) {
			  grid.setColumns("no hay residencias ");
		  }
		
			
              
              setCompositionRoot(grid);
    }
}
