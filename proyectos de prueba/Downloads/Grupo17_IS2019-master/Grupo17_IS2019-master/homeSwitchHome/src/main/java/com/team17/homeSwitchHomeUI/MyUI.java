package com.team17.homeSwitchHomeUI;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
    protected void init(VaadinRequest vaadinRequest) { //UI principal
    	final HorizontalLayout layout = new HorizontalLayout();
        final VerticalLayout bar = new VerticalLayout();
        final VerticalLayout screen = new VerticalLayout();
        final VerticalLayout busqueda = new VerticalLayout();
    	ConectionBD conectar = new ConectionBD();
    	conectar.Conectar();
        
        final Label residencias = new Label("Residencias");
        
        final Label subastas = new Label("Subastas");
        
        final Label buscar = new Label("Buscar");
        
        final TextField fechas = new TextField();
        fechas.setCaption("Fechas:");
        
        final TextField lugar = new TextField();
        lugar.setCaption("Lugar:");

        final Label contactenos = new Label("Contactenos");

        Button login = new Button("Click Me");
        
        
        busqueda.addComponents(fechas, lugar);
        
        bar.addComponents(residencias, subastas, buscar, busqueda, contactenos, login);
        bar.setWidth("20%");
        
        screen.setStyleName("backColorGrey");
        screen.setHeight("100%");
        screen.setWidth("100%");
        
        layout.addComponents(bar,screen);
        layout.setHeight("100%");
        layout.setWidth("100%");
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
