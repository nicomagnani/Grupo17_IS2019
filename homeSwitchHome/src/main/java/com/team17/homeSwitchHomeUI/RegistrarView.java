package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import org.vaadin.textfieldformatter.CreditCardFieldFormatter;
import org.vaadin.ui.NumberField;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.Tarjeta;
import homeSwitchHome.Usuario;
import homeSwitchHome.UsuarioAdministrador;
import homeSwitchHome.UsuarioComun;

public class RegistrarView extends Composite implements View {
	
	Label cabecera = new Label("Registrar usuario");
	TextField textoEmail = new TextField("Email:");
	PasswordField textoContraseña1 = new PasswordField("Contraseña:");
	PasswordField textoContraseña2 = new PasswordField("Repetir contraseña:");
	TextField textoNombre = new TextField("Nombre:");
	TextField textoApellido = new TextField("Apellido:");
	DateField fechaNac = new DateField("Fecha de nacimiento",LocalDate.now()); 
	
	Label labelTarjeta = new Label("Datos de tarjeta"); // TODO darle algún estilo para que sirva de cabecera
	TextField campoNroTarj = new TextField("Número de tarjeta:");
	CreditCardFieldFormatter formatter = new CreditCardFieldFormatter();
	TextField textoMarcaTarj = new TextField("Marca:");
	TextField textoTitTarj = new TextField("Titular de tarjeta:");
	DateField fechaVencTarj = new DateField("Fecha de vencimiento:"); //TODO: mostrar solo año+mes
	NumberField nroSegTarj = new NumberField("Código de seguridad:");
	
	Button botonAceptar = new Button("Registrarse");
	Label labelMsj = new Label();
	
	
	public RegistrarView(MyUI interfaz) {
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		formatter.addCreditCardChangedListener(e -> {
			// Any custom behavior based on the event
			e.getCreditCardType();
		});
		formatter.extend(campoNroTarj);
		
		botonAceptar.addClickListener(e -> aceptar(interfaz));
		
		FormLayout layout1 = new FormLayout(textoEmail, textoContraseña1, textoContraseña2, textoNombre, textoApellido, fechaNac);
		FormLayout layout2 = new FormLayout(campoNroTarj, textoMarcaTarj,textoTitTarj,fechaVencTarj, nroSegTarj);
		VerticalLayout mainLayout = new VerticalLayout(cabecera, layout1, labelTarjeta, layout2, botonAceptar, labelMsj);
		
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
					for (Usuario usuario : usuarios) {
						if ( usuario.getMail().equals(textoEmail.getValue()) )
							existe = true;
						break;
					}
					
					//si no encuentra en la tabla usuarios, busca en la tabla admins
					if (!existe) {
						
						ArrayList<UsuarioAdministrador> admins = new ArrayList<UsuarioAdministrador>();
						try {
							admins = conectar.listaAdmins();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						
						for (UsuarioAdministrador admin : admins) {
							if ( admin.getMail().equals(textoEmail.getValue()) )
								existe = true;
							break;
						}
						if (!existe) {				
							
							Tarjeta tarjeta = new Tarjeta( Long.parseLong(campoNroTarj.getValue()),
									textoMarcaTarj.getValue(), textoTitTarj.getValue(), fechaVencTarj.getValue(),
									Short.parseShort(nroSegTarj.getValue()) );
							UsuarioComun uc = new UsuarioComun(); //TODO cargar usuario con datos de los campos 
							labelMsj.setValue("Éxito. Registrando usuario...");
							ConnectionBD con = new ConnectionBD();
							con.agregarUsuario(uc);
							iniciarSesionUsuario(interfaz);
						}
					}
					
					// el ultimo mensaje solo se muestra cuando existe un usuario o admin registrado,
					// por eso no hace chequeo
					labelMsj.setValue("Error: El email ya se encuentra registrado.");				
					
				} else labelMsj.setValue("Error: Las contraseñas ingresadas no coinciden.");
			} else labelMsj.setValue("Error: Debe ser mayor de 18 años para registrar una cuenta.");
		} else labelMsj.setValue("Error: Al menos un campo se encuentra vacío.");
			
	}


	private boolean hayCamposVacios() {
		
    	return ( (textoEmail.isEmpty()) && (!textoContraseña1.isEmpty()) && (!textoContraseña2.isEmpty()) && (!textoNombre.isEmpty()) && 
    			(!textoApellido.isEmpty()) && (!fechaNac.isEmpty()) && (!campoNroTarj.isEmpty()) && (!textoMarcaTarj.isEmpty()) && 
    			(!textoTitTarj.isEmpty()) && (!fechaVencTarj.isEmpty()) && (!nroSegTarj.isEmpty()) );
	}
	
	
	private boolean esMayorDeEdad() {
		
		int edad = calcularEdad(fechaNac.getValue(), LocalDate.now());
		return (edad >= 18);
	}
	
	
    public static int calcularEdad(LocalDate birthDate, LocalDate currentDate) {
    	
    	if ((birthDate != null) && (currentDate != null)) {
    		return Period.between(birthDate, currentDate).getYears();
    	} else
    		return 0;
    }
    
    
	private boolean contraseñasCoinciden() {	
		
		return ( textoContraseña1.getValue() == textoContraseña2.getValue() );
	}


	private void iniciarSesionUsuario(MyUI interfaz) {
		
		interfaz.vistaUsuario();		
	}

}