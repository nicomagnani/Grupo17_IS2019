package com.team17.homeSwitchHomeUI;

import java.sql.SQLException;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Solicitud;
import homeSwitchHome.Tarjeta;
import homeSwitchHome.Usuario;
import homeSwitchHome.UsuarioComun;
import homeSwitchHome.UsuarioPremium;

@Theme("hometheme")
@Title("Mi perfil - HomeSwitchHome")
public class MiPerfilView extends Composite implements View {  //.necesita composite y view para funcionar correctamente
	
	private Label cabecera = new Label("Perfil de usuario");
	
	private Button botonModificarDatos = new Button("Modificar mis datos");
	private Button botonModificarContraseña = new Button("Modificar Contraseña");
	private Button botonModificarTarjeta = new Button("Modificar Tarjeta");
	private Button botonPremium = new Button();
	
	private Window ventanaConfirmacion = new Window("Confirmar solicitud");
	private Label info = new Label("Indique un motivo (opcional):");
	private TextArea motivo = new TextArea();
	private Button botonConfirmar = new Button("Enviar");
	private Button botonCancelar = new Button("Cancelar");
	private Notification notifResultado;
	private VerticalLayout ventanaLayout = new VerticalLayout();

	private ConnectionBD conexion = new ConnectionBD();	
	private Usuario usuario;
	private Tarjeta tarjeta;
	private Solicitud solicitud = new Solicitud();
		
	
	public MiPerfilView(Navigator navigator) {		
		
		usuario = HomeSwitchHome.getUsuarioActual();
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		Label labelParte1 = new Label("<span style=\"text-align: left; font-weight: bold; text-decoration: underline; font-size: 120%;\">Datos generales</span>", ContentMode.HTML);
		Label labelMail = new Label("<span style=\"font-weight: bold;\">Email:</span> " + usuario.getMail(), ContentMode.HTML);	
		Label labelNombre = new Label("<span style=\"font-weight: bold;\">Nombre:</span> " + usuario.getNombre(), ContentMode.HTML);	
		Label labelApellido = new Label("<span style=\"font-weight: bold;\">Apellido:</span> " + usuario.getApellido(), ContentMode.HTML);
		Label labelFechaNac = new Label("<span style=\"font-weight: bold;\">Fecha de nac.:</span> " + usuario.getfNac().toString(), ContentMode.HTML);
		
		tarjeta = usuario.getTarjeta();
		String nuevoNro = ofuscarNro(tarjeta.getNumero());
		String tipoUsuario = obtenerTipoUsuario();
		
		Label labelParte2 = new Label("<span style=\"text-align: left; font-weight: bold; text-decoration: underline; font-size: 120%;\">Datos de tarjeta</span>", ContentMode.HTML);
		Label labelNroTarj = new Label("<span style=\"font-weight: bold;\">Número de tarjeta:</span> " + nuevoNro, ContentMode.HTML);
		Label labelMarcaTarj = new Label("<span style=\"font-weight: bold;\">Marca:</span> " + tarjeta.getMarca(), ContentMode.HTML);
		Label labelTitTarj = new Label("<span style=\"font-weight: bold;\">Titular:</span> " + tarjeta.getTitular(), ContentMode.HTML);
		Label labelFVencTarj = new Label("<span style=\"font-weight: bold;\">Fecha de venc.:</span> " + tarjeta.getfVenc().toString(), ContentMode.HTML);		
		
		Label labelParte3 = new Label("<span style=\"text-align: left; font-weight: bold; text-decoration: underline; font-size: 120%;\">Otros datos</span>", ContentMode.HTML);
		Label labelCreditos = new Label("<span style=\"font-weight: bold;\">Créditos disponibles:</span> " + usuario.getCreditos(), ContentMode.HTML);
		Label labelFVencCred = new Label("<span style=\"font-weight: bold;\">Vencimiento de los créditos:</span> " + usuario.getfVencCred(), ContentMode.HTML);
		Label labelTipoUsuario = new Label("<span style=\"font-weight: bold;\">Tipo de usuario:</span> " + tipoUsuario, ContentMode.HTML);		
		
		botonModificarDatos.addClickListener(e -> this.modificarUsuario(navigator));
		botonModificarContraseña.addClickListener(e -> this.modificarContraseña(navigator));
		botonModificarTarjeta.addClickListener(e -> this.mofificarTarjeta(navigator));
		
		botonPremium.addClickListener(e -> this.abrirVentanaConfirmacion());		
		botonPremium.setCaption((usuario instanceof UsuarioComun) ? "Solicitar estado Premium" : "Dejar de ser Premium");
		
		botonConfirmar.addClickListener(e -> {
			this.solicitarAltaBajaPremium(botonPremium);
		} );		
		botonCancelar.addClickListener(e -> ventanaConfirmacion.close() );

		motivo.setWidth("400");
		
		ventanaConfirmacion.center();
		ventanaConfirmacion.setWidth("500");
		ventanaConfirmacion.setResizable(true);
		ventanaConfirmacion.setModal(true);
		
		try {
			if ( conexion.existeSolicitud(usuario.getMail()) ) {
				botonPremium.setVisible(false);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		FormLayout formLayout = new FormLayout(labelParte1, labelMail, labelNombre, labelApellido, labelFechaNac,
				labelParte2, labelNroTarj, labelMarcaTarj, labelTitTarj , labelFVencTarj,
				labelParte3, labelCreditos, labelFVencCred, labelTipoUsuario);
		
		HorizontalLayout botones1Layout = new HorizontalLayout(botonModificarDatos,botonModificarContraseña,botonModificarTarjeta,botonPremium);
		
		VerticalLayout contentLayout = new VerticalLayout(formLayout, botones1Layout);
		contentLayout.setSizeUndefined();
		contentLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);
		contentLayout.setComponentAlignment(botones1Layout, Alignment.MIDDLE_CENTER);
		
		Panel panel = new Panel();
		panel.setContent(contentLayout);
		panel.setHeight("600");
		panel.setWidth("700");
		panel.addStyleName("scrollable");
		
		HorizontalLayout botones2Layout = new HorizontalLayout(botonConfirmar,botonCancelar);
		
		ventanaLayout = new VerticalLayout(info, motivo, botones2Layout);
		ventanaLayout.setComponentAlignment(info, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(motivo, Alignment.MIDDLE_CENTER);
		ventanaLayout.setComponentAlignment(botones2Layout, Alignment.MIDDLE_CENTER);
		
		ventanaConfirmacion.setContent(ventanaLayout);
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera,panel);
		
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		
        setCompositionRoot(mainLayout);
	}		
	

	private void solicitarAltaBajaPremium(Button boton) {		
		
		solicitud.setMail(usuario.getMail());
		solicitud.setTipo((usuario instanceof UsuarioComun) ? "alta" : "baja");
		solicitud.setMotivo(motivo.getValue());
		
		try {
			conexion.agregarSolicitud(solicitud);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.mostrarNotificacion("<p style=\"text-align: center;\"><strong>Solicitud enviada.</strong></p>"
				+"<p style=\"text-align: center;\">Para completar el pedido, comuníquese por teléfono con la empresa</p>"
				+"<p style=\"text-align: center;\">o acérquese a una de nuestras oficinas.</p>", Notification.Type.HUMANIZED_MESSAGE);		
		ventanaConfirmacion.close();
		boton.setVisible(false);		
	}
	
	
	private void abrirVentanaConfirmacion() {
		UI.getCurrent().addWindow(ventanaConfirmacion);		
	}


	private void mofificarTarjeta(Navigator navigator) {
		navigator.navigateTo("modificarTarjeta");	
	}


	private void modificarContraseña(Navigator navigator) {
		navigator.navigateTo("modificarContraseña");
	}

	
	private String ofuscarNro(long n) {
		StringBuilder stb = new StringBuilder(String.valueOf(n));
		if (stb.length() > 3) {
			for (int i = 0; i < stb.length()-3; i++) {
				stb.setCharAt(i, '*');
			}
		} else
			stb = new StringBuilder("***");
		return stb.toString();
	}

	
	private String obtenerTipoUsuario() {
		return (usuario instanceof UsuarioPremium) ? "Usuario Premium" : "Usuario Común";
	}
	

	private void modificarUsuario(Navigator navigator) {
		navigator.navigateTo("modificarPerfil");
	}
	

	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	
		notifResultado = new Notification(st, tipo);
		notifResultado.setHtmlContentAllowed(true);
    	notifResultado.setDelayMsec(-1);;
    	notifResultado.show(Page.getCurrent());
    }
	
}
