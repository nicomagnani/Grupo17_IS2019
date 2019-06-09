package com.team17.homeSwitchHomeUI;



import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
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

import homeSwitchHome.HomeSwitchHome;


/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("hometheme")
public class MyUI extends UI {
	
	HomeSwitchHome sistema;
	Image img;
	Label title;
	
	Button residenciasButton;
	Button subastasButton;
	Button buscarFechaButton;
	Button buscarLugarButton;
	Button iniciarSesionButton;
	Button contactarButton;
	Button verFaqButton;
	Button registrarseButton;
	Button reservasButton;
	Button agregarResidenciaButton;
	Button cerrarSesionButton;
	
	CssLayout menu;
	CssLayout viewContainer;
	HorizontalLayout mainLayout;
	Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) { //UI principal.
    	this.vistaVisitante();
    }
    
    public void vistaVisitante() {
        
    	this.mostrarLogo();
        this.botonesVisitante();
        this.mostrarLayout();
        this.navigatorVisitante();
        //this.mostrar5Residencias TODO
    }
   
    public void vistaUsuario() {
        
    	this.mostrarLogo();
        this.botonesUsuario();
        this.mostrarLayout();
        this.navigatorUsuario();
    }
    
    public void vistaAdmin() {
        
    	this.mostrarLogo();
        this.botonesAdmin();
        this.mostrarLayout();
        this.navigatorAdmin();	//TODO
    }
    
    private void mostrarLogo() {
    	sistema = new HomeSwitchHome();
    	
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
        setContent(mainLayout);
    }
    
    private void botonesUsuario() {
    	residenciasButton = new Button("Residencias", e -> getNavigator().navigateTo("residencias"));
        residenciasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        subastasButton = new Button("Subastas", e -> getNavigator().navigateTo("subastas"));
        subastasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        buscarFechaButton = new Button("Búsqueda por fecha", e -> getNavigator().navigateTo("buscarFecha"));
        buscarFechaButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        buscarLugarButton = new Button("Búsqueda por lugar", e -> getNavigator().navigateTo("buscarLugar"));
        buscarLugarButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
                
        iniciarSesionButton = new Button("Iniciar Sesión", e -> getNavigator().navigateTo("iniciarSesion")); 
        iniciarSesionButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        contactarButton = new Button("Contactarse", e -> getNavigator().navigateTo("contactar")); 
        contactarButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        verFaqButton = new Button("Ayuda", e -> getNavigator().navigateTo("verFaq")); 
        verFaqButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        menu = new CssLayout(img, title, residenciasButton, subastasButton, buscarFechaButton, buscarLugarButton, iniciarSesionButton, contactarButton, verFaqButton);
        menu.addStyleName(ValoTheme.MENU_ROOT);
    }
    
    private void navigatorUsuario() {
    	
    	navigator = new Navigator(this, viewContainer);
        
        /*try {
			navigator.addView("residencias", new ResidenciasView());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
        //navigator.addView("subastas", new SubastasView());
        navigator.addView("buscarFecha", new BuscarFechaView());
        navigator.addView("buscarLugar", new BuscarLugarView());
        navigator.addView("iniciarSesion", new IniciarSesionView(sistema,navigator,this));		
        navigator.addView("contactar", new ContactarView());    	
        navigator.addView("verFaq", new VerFaqView());
    }
    
    private void botonesAdmin() {
    	
    	residenciasButton = new Button("Residencias", e -> getNavigator().navigateTo("residenciasAdmin")); //boton para navegar a una view
        residenciasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        subastasButton = new Button("Subastas", e -> getNavigator().navigateTo("subastasAdmin"));
        subastasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        reservasButton = new Button("Reservas", e -> getNavigator().navigateTo("reservasAdmin"));
        reservasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        agregarResidenciaButton = new Button("Agregar residencia", e -> getNavigator().navigateTo("agregarResidencia"));
        agregarResidenciaButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        cerrarSesionButton = new Button("Cerrar Sesión", e -> getNavigator().navigateTo("cerrarSesion"));
        cerrarSesionButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        menu = new CssLayout(img, title, residenciasButton, subastasButton, reservasButton, agregarResidenciaButton, cerrarSesionButton);
        menu.addStyleName(ValoTheme.MENU_ROOT);
    	
    }
    
    public void navigatorAdmin() {	//TODO
        
    	navigator = new Navigator(this, viewContainer);
    	
        navigator.addView("residenciasAdmin", new ResidenciasAdminView());
        navigator.addView("subastasAdmin", new SubastasAdminView());
        navigator.addView("reservasAdmin", new ReservasAdminView());
        navigator.addView("agregarResidencia", new AgregarResidenciaView());
        navigator.addView("cerrarSesion", new CerrarSesionView());
    }
    
    private void botonesVisitante() {
    	
    	iniciarSesionButton = new Button("Iniciar Sesión", e -> getNavigator().navigateTo("iniciarSesion")); 
        iniciarSesionButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        registrarseButton = new Button("Registrarse", e -> getNavigator().navigateTo("registrar")); 
        registrarseButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        contactarButton = new Button("Contactarse", e -> getNavigator().navigateTo("contactar"));
        contactarButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        verFaqButton = new Button("Ayuda", e -> getNavigator().navigateTo("verFaq"));
        verFaqButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        menu = new CssLayout(img, title, iniciarSesionButton, registrarseButton, contactarButton, verFaqButton);
        menu.addStyleName(ValoTheme.MENU_ROOT);
    }
    
    private void navigatorVisitante() {
    	navigator = new Navigator(this, viewContainer);
    	
    	navigator.addView("residenciasVisitante", new ResidenciasVisitanteView());
        navigator.addView("iniciarSesion", new IniciarSesionView(sistema,navigator,this));
        navigator.addView("registrar", new RegistrarView());	 //TODO
        navigator.addView("contactar", new ContactarView());    	
        navigator.addView("verFaq", new VerFaqView());
        navigator.navigateTo("residenciasVisitante"); //navega a la lista reducida de residencias - podria ser un botón
    	
    }  

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}