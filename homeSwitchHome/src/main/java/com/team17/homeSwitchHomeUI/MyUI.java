package com.team17.homeSwitchHomeUI;



import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;


/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */

@Theme("hometheme")
@Title("HomeSwitchHome")
public class MyUI extends UI {
	
	Image img;
	Label title;
	
	Button residenciasButton;
	Button misReservasButton;
	Button subastasButton;
	Button misSubastasButton;
	Button buscarFechaButton;
	Button buscarLugarButton;
	Button iniciarSesionButton;
	Button miPerfilButton;
	Button contactarButton;
	Button verFaqButton;
	Button registrarseButton;
	Button reservasButton;
	Button agregarResidenciaButton;
	Button solicitudesButton;
	Button cerrarSesionButton;
	
	CssLayout menu;
	CssLayout viewContainer;
	HorizontalLayout mainLayout;
	Navigator navigator;
	
	private static DetalleResidenciaView vistaDetalle;
	

    @Override
    protected void init(VaadinRequest vaadinRequest) { //UI principal.
    	this.vistaVisitante();
    }
    
    public void vistaVisitante() {
        
    	this.mostrarLogo();
        this.botonesVisitante();
        this.mostrarLayout();
        this.navigatorVisitante();
        navigator.navigateTo("");
    }
   
    public void vistaUsuario() {
        
    	this.mostrarLogo();
        this.botonesUsuario();
        this.mostrarLayout();
        this.navigatorUsuario();
        navigator.navigateTo("");
    }
    
    public void vistaUsuario(String unaVista) {
        
    	this.mostrarLogo();
        this.botonesUsuario();
        this.mostrarLayout();
        this.navigatorUsuario();
        navigator.navigateTo(unaVista);
    }
    
    //crea la sesión de usuario y agrega nuevas vistas una vez que se asignó una residencia actual
    public void vistaUsuarioConNuevaVista(String vista) {
        
    	this.mostrarLogo();
        this.botonesUsuario();
        this.mostrarLayout();
        this.navigatorUsuario();
        if (vista.equals("detalleResidenciaNormal")) {
        	navigator.addView("detalleResidenciaNormal", new DetalleResidenciaView("usuario",this));
        	navigator.navigateTo("detalleResidenciaNormal");
        } else
        	if (vista.equals("detalleResidenciaPorFechas")) {
        		navigator.addView("detalleResidenciaPorFechas", new DetalleResidenciaView("busqueda",this));
            	navigator.navigateTo("detalleResidenciaPorFechas");
            } else
            	if (vista.equals("reservarDirecta")) {
            		navigator.addView("reservarDirecta", new ReservarDirectaView(this));
                	navigator.navigateTo("reservarDirecta");
                }
    }
    
    public void vistaAdmin(String unaVista) {
        
    	this.mostrarLogo();
        this.botonesAdmin();
        this.mostrarLayout();
        this.navigatorAdmin();
        navigator.navigateTo(unaVista);
    }
    
    //crea la sesión de admin y agrega nuevas vistas una vez que se asignó una residencia actual
    public void vistaAdminConNuevaVista(String vista) {
        
    	this.mostrarLogo();
        this.botonesAdmin();
        this.mostrarLayout();
        this.navigatorAdmin();
        if (vista.equals("detalleResidencia")) {
        	navigator.addView("detalleResidencia", new DetalleResidenciaView("admin",this));
        	navigator.navigateTo("detalleResidencia");
        } else
        	if (vista.equals("modificarResidencia")) {
        		navigator.addView("modificarResidencia", new ModificarResidenciaView(this));
            	navigator.navigateTo("modificarResidencia");
            }
    }
    
    private void mostrarLogo() {    	
    	img = new Image();
    	img.setSource(new ThemeResource("logo.png"));
    	img.setWidth(250, Unit.PIXELS);
    	img.setHeight(159, Unit.PIXELS);
    	
    	title = new Label("Home Switch Home");
        title.addStyleName(ValoTheme.MENU_TITLE);
    	
        viewContainer = new CssLayout();
    }
    
    private void mostrarLayout() {
        mainLayout = new HorizontalLayout(menu, viewContainer);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(viewContainer, 3);
        mainLayout.setExpandRatio(menu, 1);
        setContent(mainLayout);

    }
    
    private void botonesUsuario() {
    	residenciasButton = new Button("Residencias", e -> getNavigator().navigateTo("residencias"));
        residenciasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);

        misReservasButton = new Button("Mis reservas", e -> getNavigator().navigateTo("misReservas")); 
        misReservasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        subastasButton = new Button("Subastas", e -> getNavigator().navigateTo("subastas"));
        subastasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);        

        misSubastasButton = new Button("Ofertas en subastas", e -> getNavigator().navigateTo("misSubastas"));
        misSubastasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        buscarFechaButton = new Button("Búsqueda por fecha", e -> getNavigator().navigateTo("buscarFecha"));
        buscarFechaButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        buscarLugarButton = new Button("Búsqueda por lugar", e -> getNavigator().navigateTo("buscarLugar"));
        buscarLugarButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
                        
        miPerfilButton = new Button("Mi perfil", e -> getNavigator().navigateTo("miPerfil")); 
        miPerfilButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        contactarButton = new Button("Contactarse", e -> getNavigator().navigateTo("contactar")); 
        contactarButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);        

        verFaqButton = new Button("Ayuda", e -> getNavigator().navigateTo("verFaq")); 
        verFaqButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        cerrarSesionButton = new Button("Cerrar Sesión", e -> vistaVisitante());
        cerrarSesionButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        menu = new CssLayout(img, title, residenciasButton, misReservasButton, subastasButton, misSubastasButton, buscarFechaButton,
        		buscarLugarButton, miPerfilButton, contactarButton, verFaqButton, cerrarSesionButton);
        
        menu.addStyleName(ValoTheme.MENU_ROOT);
    }
    
 private void navigatorUsuario() {
    	
    	navigator = new Navigator(this, viewContainer);
        
    	try {
			navigator.addView("", new ResidenciasUsuarioView(true,this));
			navigator.addView("residencias", new ResidenciasUsuarioView(false,this));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}	
    	navigator.addView("misReservas", new MisReservasView());
    	navigator.addView("subastas", new SubastasUsuarioView());
    	navigator.addView("misSubastas", new MisSubastasView());
        navigator.addView("buscarFecha", new BuscarFechaView(this));
        navigator.addView("buscarLugar", new BuscarLugarView(this));
        navigator.addView("miPerfil", new MiPerfilView(navigator));
        navigator.addView("modificarPerfil", new ModificarPerfilView(navigator,this));
        navigator.addView("modificarTarjeta", new ModificarTarjetaView(navigator,this));
        navigator.addView("modificarContraseña", new ModificarContraseñaView(navigator,this));
        navigator.addView("contactar", new ContactarView());    	
        navigator.addView("verFaq", new VerFaqView());
        
    }
    
    
    private void botonesAdmin() {
    	
    	residenciasButton = new Button("Residencias", e -> getNavigator().navigateTo("residenciasAdmin"));
        residenciasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        reservasButton = new Button("Reservas", e -> getNavigator().navigateTo("reservasAdmin"));
        reservasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        agregarResidenciaButton = new Button("Agregar residencia", e -> getNavigator().navigateTo("agregarResidencia"));
        agregarResidenciaButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        solicitudesButton = new Button("Solcitudes Premium", e -> getNavigator().navigateTo("solicitudesPremium"));
        solicitudesButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        cerrarSesionButton = new Button("Cerrar Sesión", e -> vistaVisitante());
        cerrarSesionButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        menu = new CssLayout(img, title, residenciasButton, reservasButton, agregarResidenciaButton,
        		solicitudesButton, cerrarSesionButton);
        menu.addStyleName(ValoTheme.MENU_ROOT);
    	
    }
    
    public void navigatorAdmin() {
        
    	navigator = new Navigator(this, viewContainer);
    	
        try {
			navigator.addView("residenciasAdmin", new ResidenciasAdminView(this,navigator));
		} catch (SQLException e) {
			e.printStackTrace();
		}
        navigator.addView("reservasAdmin", new ReservasAdminView());
        navigator.addView("agregarResidencia", new AgregarResidenciaView(this));
        navigator.addView("solicitudesPremium", new SolicitudesPremiumView());

    }
    
    
    private void botonesVisitante() {
    	
    	residenciasButton = new Button("Bienvenida", e -> getNavigator().navigateTo("residenciasVisitante")); 
    	residenciasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
    	
    	iniciarSesionButton = new Button("Iniciar Sesión", e -> getNavigator().navigateTo("iniciarSesion")); 
        iniciarSesionButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        registrarseButton = new Button("Registrarse", e -> getNavigator().navigateTo("registrar")); 
        registrarseButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        contactarButton = new Button("Contactarse", e -> getNavigator().navigateTo("contactar"));
        contactarButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        verFaqButton = new Button("Ayuda", e -> getNavigator().navigateTo("verFaq"));
        verFaqButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        menu = new CssLayout(img, title, residenciasButton, iniciarSesionButton, registrarseButton, contactarButton, verFaqButton);
        menu.addStyleName(ValoTheme.MENU_ROOT);
        menu.setWidth("250");
    }
    
    private void navigatorVisitante() {
    	navigator = new Navigator(this, viewContainer);
    	
    	try {
			navigator.addView("", new ResidenciasVisitanteView());
			navigator.addView("residenciasVisitante", new ResidenciasVisitanteView());
		} catch (SQLException e) {
			e.printStackTrace();
		}
        navigator.addView("iniciarSesion", new IniciarSesionView(this));
        navigator.addView("registrar", new RegistrarUsuarioView(this));
        navigator.addView("contactar", new ContactarView());    	
        navigator.addView("verFaq", new VerFaqView());    	
    }  
    

    //reemplazado por estilo de bordes
//    public static Label separador() {
//    	return new Label("____________________________________________________________________");
//    }

    public static DetalleResidenciaView getVistaDetalle() {
		return vistaDetalle;
	}

	public static void setVistaDetalle(DetalleResidenciaView vistaDetalle) {
		MyUI.vistaDetalle = vistaDetalle;
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}