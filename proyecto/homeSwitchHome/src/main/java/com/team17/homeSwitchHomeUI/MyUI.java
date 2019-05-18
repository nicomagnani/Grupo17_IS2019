package com.team17.homeSwitchHomeUI;

import java.io.File;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("hometheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) { //UI principal.
    	
    	Image img = new Image();
    	img.setSource(new ThemeResource("logo.png"));
    	img.setWidth(250, Unit.PIXELS);
    	img.setHeight(159, Unit.PIXELS);
    	
    	Label title = new Label("Home Switch Home");
        title.addStyleName(ValoTheme.MENU_TITLE);

        Button residenciasButton = new Button("Residencias", e -> getNavigator().navigateTo("residencias")); //boton para navegar a una view
        residenciasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        Button subastasButton = new Button("Subastas", e -> getNavigator().navigateTo("subastas"));
        subastasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        Button buscarFechaButton = new Button("Búsqueda por fecha", e -> getNavigator().navigateTo("buscarFecha"));
        buscarFechaButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        Button buscarLugarButton = new Button("Búsqueda por lugar", e -> getNavigator().navigateTo("buscarLugar"));
        residenciasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
                
        Button iniciarSesionButton = new Button("Iniciar Sesión", e -> getNavigator().navigateTo("iniciarSesion")); 
        iniciarSesionButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        //TODO: link "contactenos"
        
        CssLayout menu = new CssLayout(img, title, residenciasButton, subastasButton, buscarFechaButton, buscarLugarButton, iniciarSesionButton);		//Lista de botones (y componentes) para views
        menu.addStyleName(ValoTheme.MENU_ROOT);
         
        CssLayout viewContainer = new CssLayout();

        HorizontalLayout mainLayout = new HorizontalLayout(menu, viewContainer);
        mainLayout.setSizeFull();
        setContent(mainLayout);

        Navigator navigator = new Navigator(this, viewContainer);		//Lista de views, agregar
        navigator.addView("", ResidenciasView.class);
        navigator.addView("residencias", ResidenciasView.class);
        navigator.addView("subastas", SubastasView.class);
        navigator.addView("buscarFecha", BuscarFechaView.class);
        navigator.addView("buscarLugar", BuscarLugarView.class);
        navigator.addView("iniciarSesion", IniciarSesionView.class);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
