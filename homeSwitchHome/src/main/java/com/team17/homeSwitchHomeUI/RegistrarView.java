package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.beanutils.converters.ShortConverter;
import org.vaadin.ui.NumberField;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.data.util.*;
import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Tarjeta;
import homeSwitchHome.Usuario;
import homeSwitchHome.UsuarioAdministrador;
import homeSwitchHome.UsuarioComun;
import homeSwitchHome.StringToShortConverter;


public class RegistrarView extends Composite implements View {
	
	Label cabecera = new Label("Registrar usuario");
	Label labelParte1 = new Label("<span style=\"text-align: left; font-weight: bold; text-decoration: underline; font-size: 120%;\">Datos generales</span>", ContentMode.HTML);
	TextField textoEmail = new TextField("Email:");
	
	PasswordField textoContraseña1 = new PasswordField("Contraseña:");
	PasswordField textoContraseña2 = new PasswordField("Repetir contraseña:");
	
	TextField textoNombre = new TextField("Nombre:");
	TextField textoApellido = new TextField("Apellido:");
	DateField fechaNac = new DateField("Fecha de nacimiento",LocalDate.now()); 
	
	Label labelParte2 = new Label("<span style=\"text-align: left; font-weight: bold; text-decoration: underline; font-size: 120%;\">Datos de tarjeta</span>", ContentMode.HTML);
	NumberField campoNroTarj = new NumberField("Número de tarjeta:");
	RadioButtonGroup<String> campoMarcaTarj = new RadioButtonGroup<>("Marca:");
	TextField textoTitTarj = new TextField("Titular de tarjeta:");
	DateField fechaVencTarj = new DateField("Fecha de vencimiento:");
	PasswordField nroSegTarj = new PasswordField("Código de seguridad:");
	
	Button botonAceptar = new Button("Registrarse");
	Notification notification = new Notification("asd");	
	
	
	public RegistrarView(MyUI interfaz) {
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
				
		fechaNac.setValue(LocalDate.parse("2000-01-01"));
	
		campoNroTarj.setMaxLength(16);
		campoNroTarj.setDecimalAllowed(false);
		campoNroTarj.setGroupingUsed(false);
		campoNroTarj.setNegativeAllowed(false);

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
		
		botonAceptar.addClickListener( e -> aceptar(interfaz) );
		
		
		FormLayout layout1 = new FormLayout(textoEmail, textoContraseña1, textoContraseña2, textoNombre, textoApellido, fechaNac);
		FormLayout layout2 = new FormLayout(campoNroTarj, campoMarcaTarj,textoTitTarj,fechaVencTarj, nroSegTarj);
		VerticalLayout mainLayout = new VerticalLayout(cabecera, labelParte1, layout1, labelParte2, layout2, botonAceptar);
		mainLayout.setComponentAlignment(labelParte2, Alignment.BOTTOM_LEFT);
		
		setCompositionRoot(mainLayout);
	}
		
    	
    private void aceptar(MyUI interfaz) {
    			
		if (!hayCamposVacios()) {
			
			if (esMayorDeEdad()) {
			
				if (contraseñasCoinciden()) {
										
					ConnectionBD conectar = new ConnectionBD();
					ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
					
					try {
						usuarios = conectar.listaUsuarios();
					} catch (SQLException e) {
						e.printStackTrace();						
					}
					
					// es true si existe un usuario con el mismo email
					boolean existe = false;
					
					//busco en la tabla usuarios
					for (int i = 0; ( (i < usuarios.size()) && !existe ); i++) {
						if ( usuarios.get(i).getMail().equals(textoEmail.getValue()) )
							existe = true;							
					}
					
					//si no encuentra en la tabla usuarios, busca en la tabla admins
					if (!existe) {
						
						ArrayList<UsuarioAdministrador> admins = new ArrayList<UsuarioAdministrador>();
						try {
							admins = conectar.listaAdmins();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						
						for (int i = 0; ( (i < admins.size()) && !existe ); i++) {
							if ( admins.get(i).getMail().equals(textoEmail.getValue()) )
								existe = true;								
						}
						
						if (!existe) {				
							
							Tarjeta tarjeta = new Tarjeta( Long.parseLong(campoNroTarj.getValue()),
									campoMarcaTarj.getValue(), textoTitTarj.getValue(), fechaVencTarj.getValue(),
									Short.parseShort(nroSegTarj.getValue()) );
							
							UsuarioComun usuario = new UsuarioComun(textoEmail.getValue(), textoContraseña1.getValue(),
									textoNombre.getValue(), textoApellido.getValue(), tarjeta, fechaNac.getValue());
							
							mostrarNotificacion("Éxito. Registrando usuario...", Notification.Type.HUMANIZED_MESSAGE);
							
							ConnectionBD con = new ConnectionBD();							
							try {
								con.agregarUsuario(usuario);
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
							iniciarSesionUsuario(usuario.getMail(), interfaz);
						}
					}
					
					if (existe) {
						mostrarNotificacion("Error: El email ya se encuentra registrado.", Notification.Type.ERROR_MESSAGE);
					}					
				} else {
					mostrarNotificacion("Error: Las contraseñas ingresadas no coinciden.", Notification.Type.ERROR_MESSAGE);
				}
			} else {
				mostrarNotificacion("Error: Debe ser mayor de 18 años para registrar una cuenta.", Notification.Type.ERROR_MESSAGE);
			}
		} else {
			mostrarNotificacion("Error: Al menos un campo se encuentra vacío.", Notification.Type.ERROR_MESSAGE);
		}
			
	}

    
    private void mostrarNotificacion(String st, Notification.Type tipo) {
    	notification = new Notification(st, tipo);
    	notification.setDelayMsec(5000);
		notification.show(Page.getCurrent());
    }
    
    
	private boolean hayCamposVacios() {
		
    	return ( (textoEmail.isEmpty()) || (textoContraseña1.isEmpty()) || (textoContraseña2.isEmpty()) || (textoNombre.isEmpty()) || 
    			(textoApellido.isEmpty()) || (fechaNac.isEmpty()) || (campoNroTarj.isEmpty()) || (campoMarcaTarj.isEmpty()) || 
    			(textoTitTarj.isEmpty()) || (fechaVencTarj.isEmpty()) || (nroSegTarj.isEmpty()) );
	}
	
	
	private boolean esMayorDeEdad() {
		
		int edad = calcularEdad(fechaNac.getValue(), LocalDate.now());
		return (edad >= 18);
	}
	
	
    private static int calcularEdad(LocalDate birthDate, LocalDate currentDate) {
    	
    	if ((birthDate != null) && (currentDate != null)) {
    		return Period.between(birthDate, currentDate).getYears();
    	} else
    		return 0;
    }
    
    
	private boolean contraseñasCoinciden() {
		return Objects.equals(textoContraseña1.getValue(),
				textoContraseña2.getValue());
	}


	private void iniciarSesionUsuario(String usuarioActual, MyUI interfaz) {
		HomeSwitchHome.setUsuarioActual(usuarioActual);
		interfaz.vistaUsuario();
	}

}