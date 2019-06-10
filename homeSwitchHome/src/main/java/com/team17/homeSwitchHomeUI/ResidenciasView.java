package com.team17.homeSwitchHomeUI;


import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;

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
			 
			 for(int i=0;i<propiedades.size();i++) {
				 Button login = new Button("editar");
					login.addClickListener(e -> {
					 });
					
			 }
		
		  if(propiedades.size()==0) {
			  grid.setColumns("no hay residencias ");
		  }
		
			
              
              setCompositionRoot(grid);
    }
}
