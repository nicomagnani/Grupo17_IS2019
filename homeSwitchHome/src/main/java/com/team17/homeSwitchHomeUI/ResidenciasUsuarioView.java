package com.team17.homeSwitchHomeUI;


import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;

import homeSwitchHome.Propiedad;


public class ResidenciasUsuarioView extends Composite implements View {


		
	public ResidenciasUsuarioView() throws SQLException {		
			 ConnectionBD conectar = new ConnectionBD();
			 ArrayList<Propiedad> propiedades = conectar.listaPropiedadesSinFotos();
			
			 Grid<Propiedad> grid = new Grid<>(Propiedad.class);
			 grid.setItems(propiedades);
			 
			 for (int i=0; i < propiedades.size(); i++) {
				 Button login = new Button("Editar");
					login.addClickListener(e -> {
					 });					
			 }
		
		  if (propiedades.size() == 0) {
			  grid.setColumns("No hay residencias.");
		  }			
              
		  setCompositionRoot(grid);
    }
}
