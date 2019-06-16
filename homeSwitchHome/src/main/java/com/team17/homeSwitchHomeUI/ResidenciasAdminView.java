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
	ConnectionBD conexion = new ConnectionBD(); 
	public ResidenciasAdminView() throws SQLException {
		
		mainLayout = new VerticalLayout();
		this.cargarResidencias();
        
	}

	private void cargarResidencias() throws SQLException {
		
		Iterator<Propiedad>ite=conexion.listaPropiedadesSinFotos().iterator();
		if (ite.hasNext() ==false) {
			Label titulo = new Label();
			titulo.setValue("no hay residencias disponibles");			
			setCompositionRoot(titulo);
		}else {
			
				while (ite.hasNext()) {
				this.añadirResidencia((Propiedad) ite.next());
			    }
				Button subasta = new Button("Abrir Subasta", e -> this.subastar(ite));	
				mainLayout.addComponent(subasta);
				setCompositionRoot((Component) mainLayout);
				
			
		}
		
		
	}
	
	private Object subastar(Iterator<Propiedad> propiedades) {
		// TODO Auto-generated method stub
		return null;
	}

	//falta visualizar imagenes y añadir estilos.
	private void añadirResidencia(Propiedad unaResidencia) {
		
    	Button modificar = new Button("Modificar", e -> this.modificar(unaResidencia));
    	Button eliminar = new Button("Eliminar", e -> {
			try {
				this.eliminar(unaResidencia);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
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
	
	private void eliminar(Propiedad unaResidencia) throws SQLException {
		conexion.eliminarResidencia(unaResidencia);
		conexion.listaPropiedadesSinFotos();
	}
}