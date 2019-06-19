package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.time.LocalDate;

import org.vaadin.ui.NumberField;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.StringToShortConverter;
import homeSwitchHome.Tarjeta;
import homeSwitchHome.Usuario;

public class ModificarTarjetaView extends Composite implements View {
	
	Label labelParte2 = new Label("<span style=\"text-align: left; font-weight: bold; text-decoration: underline; font-size: 120%;\">Datos de tarjeta</span>", ContentMode.HTML);
	NumberField campoNroTarj = new NumberField("Número de tarjeta:");
	RadioButtonGroup<String> campoMarcaTarj = new RadioButtonGroup<>("Marca:");
	TextField textoTitTarj = new TextField("Titular de tarjeta:");
	DateField fechaVencTarj = new DateField("Fecha de vencimiento:");
	PasswordField nroSegTarj = new PasswordField("Código de seguridad:");
	Button botonAceptar= new Button("Modificar");
	Button botonCancelar= new Button("Cancelar");
	Usuario usuario;
	ConnectionBD conectar;
	Notification notification = new Notification("sd");
	
	public ModificarTarjetaView(Navigator navigator, MyUI interfaz) {
		ConnectionBD conectar = new ConnectionBD();		
		try {
			usuario = conectar.buscarUsuario(HomeSwitchHome.getUsuarioActual());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}			
		
		campoNroTarj.setMaxLength(16);
		campoNroTarj.setDecimalAllowed(false);
		campoNroTarj.setGroupingUsed(false);
		campoNroTarj.setNegativeAllowed(false);		
		
		campoNroTarj.setValue((double) usuario.getTarjeta().getNumero());
		textoTitTarj.setValue(usuario.getTarjeta().getTitular());       
       
		campoMarcaTarj.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		campoMarcaTarj.setItems("VISA", "MasterCard");			
		fechaVencTarj.setResolution(DateResolution.MONTH);
		fechaVencTarj.setTextFieldEnabled(false);		
		fechaVencTarj.setValue(LocalDate.parse("2020-01-01"));
		fechaVencTarj.setRangeStart(LocalDate.now());
		nroSegTarj.setMaxLength(4);
		
		new Binder<Tarjeta>().forField(nroSegTarj)
	    .withValidator(new RegexpValidator("Ingrese su código de seguridad", "[-]?[0-9]*\\.?,?[0-9]+"))
	    .withConverter(new StringToShortConverter())
	    .bind(Tarjeta::getCodigo, Tarjeta::setCodigo);

		botonAceptar.addClickListener( e -> modificar(interfaz) );
		botonCancelar.addClickListener(e-> cancelar(navigator));
		
		FormLayout layout2 = new FormLayout(campoNroTarj, campoMarcaTarj, textoTitTarj, fechaVencTarj, nroSegTarj);
		HorizontalLayout botonesLayout = new HorizontalLayout(botonAceptar, botonCancelar);
		VerticalLayout mainLayout = new VerticalLayout(labelParte2, layout2, botonesLayout);
		
		mainLayout.setComponentAlignment(botonesLayout, Alignment.MIDDLE_CENTER);
		
		setCompositionRoot(mainLayout);
		
	}
	private void cancelar(Navigator navigator) {
		navigator.navigateTo("miPerfil");
	}
	
	private void modificar(MyUI interfaz) {
		ConnectionBD conectar = new ConnectionBD();		
		try {
			usuario = conectar.buscarUsuario(HomeSwitchHome.getUsuarioActual());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}	
	       
	       if((campoNroTarj.isEmpty()) || (campoMarcaTarj.isEmpty()) || 
    			(textoTitTarj.isEmpty()) || (fechaVencTarj.isEmpty()) || (nroSegTarj.isEmpty())) {
	    	   this.mostrarNotificacion("Error: Hay campos vacíos.", Notification.Type.ERROR_MESSAGE);
	       }else {
	    	   conectar.modificarTarjeta(Long.parseLong(campoNroTarj.getValue()),
						campoMarcaTarj.getValue(), textoTitTarj.getValue(), fechaVencTarj.getValue(),
						Short.parseShort(nroSegTarj.getValue()), usuario.getMail() );
	    		   
	    	   this.mostrarNotificacion("Modificación exitosa", Notification.Type.HUMANIZED_MESSAGE);
	    	   interfaz.vistaUsuario("miPerfil");
	    	   
	       }
	       
	}
	
    private void mostrarNotificacion(String st, Notification.Type tipo) {
    	notification = new Notification(st, tipo);
    	notification.setDelayMsec(5000);
		notification.show(Page.getCurrent());
    }
    
}

	


