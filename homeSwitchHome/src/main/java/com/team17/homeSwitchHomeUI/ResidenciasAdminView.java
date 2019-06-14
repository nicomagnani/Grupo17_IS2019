package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.util.Iterator;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import homeSwitchHome.Propiedad;

public class ResidenciasAdminView extends Composite implements View {
	VerticalLayout mainLayout;
		
	public ResidenciasAdminView() {
		
		mainLayout = new VerticalLayout();
		this.cargarResidencias();
        setCompositionRoot((Component) mainLayout);
	}

	private void cargarResidencias() {
		
		ConnectionBD conexion = new ConnectionBD(); 
		Iterator<Propiedad> ite = null;
		try {
			ite = conexion.listaPropiedadesSinFotos().iterator();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ite != null) {
			while (ite.hasNext()) {
			this.añadirResidencia((Propiedad) ite.next());
		}
		}
		
	}
	
	//falta visualizar imagenes y añadir estilos.
	private void añadirResidencia(Propiedad unaResidencia) {
		
    	Button modificar = new Button("Modificar", e -> this.modificar(unaResidencia));
    	Button eliminar = new Button("Eliminar", e -> this.eliminar(unaResidencia));
    	HorizontalLayout botones = new HorizontalLayout(modificar,eliminar);
    	
		Label titulo = new Label(unaResidencia.getTitulo());
		Label ubicacion = new Label("ubicacion: "+ unaResidencia.getPais() + "," +
				unaResidencia.getProvincia() + "," + unaResidencia.getLocalidad() +
				"," + unaResidencia.getDomicilio());
		Label descripcion = new Label (unaResidencia.getDescripcion());
		Label montoBase = new Label ("monto base:" + String.valueOf(unaResidencia.getMontoBase()));
		
		VerticalLayout residenciaLayout = new VerticalLayout(titulo,ubicacion,descripcion,montoBase,botones);
		residenciaLayout.setSizeFull();
		residenciaLayout.setStyleName("lightBackground");
		
		Panel residenciaPanel = new Panel();
		residenciaPanel.setContent(residenciaLayout);
		
		mainLayout.addComponent(residenciaPanel);
		
	}
	
	private void modificar(Propiedad unaResidencia) {
		//TODO
	}
	
	private void eliminar(Propiedad unaResidencia) {
		//TODO
	}
}