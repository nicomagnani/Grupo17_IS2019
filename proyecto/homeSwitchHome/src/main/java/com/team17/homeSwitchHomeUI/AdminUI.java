package com.team17.homeSwitchHomeUI;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.HomeSwitchHome;

public class AdminUI extends UI{
	
	/*HomeSwitchHome sistema;
	
	public AdminUI(HomeSwitchHome unSistema) { 
    	
    	 sistema = unSistema;
    } */

	@Override
	protected void init(VaadinRequest request) { //UI principal admin.

    	Image img = new Image("logo.png");
    	Label title = new Label("Home Switch Home");
        title.addStyleName(ValoTheme.MENU_TITLE);

        Button residenciasButton = new Button("Residencias", e -> getNavigator().navigateTo("residenciasAdmin")); //boton para navegar a una view
        residenciasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        Button subastasButton = new Button("Subastas", e -> getNavigator().navigateTo("subastasAdmin"));
        subastasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        Button reservasButton = new Button("Resrvas", e -> getNavigator().navigateTo("buscarFechaAdmin"));
        reservasButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        Button cerrarSesionButton = new Button("Cerrar Sesión", e -> getNavigator().navigateTo("cerrarSesion"));
        cerrarSesionButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        
        CssLayout menu = new CssLayout(img, title, residenciasButton, subastasButton, reservasButton, cerrarSesionButton);		//Lista de botones (y componentes) para views
        menu.addStyleName(ValoTheme.MENU_ROOT);
         
        CssLayout viewContainer = new CssLayout();

        HorizontalLayout mainLayout = new HorizontalLayout(menu, viewContainer);
        mainLayout.setSizeFull();
        setContent(mainLayout);

        Navigator navigatorAdmin = new Navigator(this, viewContainer);		//Lista de views, agregar
        navigatorAdmin.addView("", new ResidenciasAdminView());
        navigatorAdmin.addView("residenciasAdmin", new ResidenciasAdminView());
        navigatorAdmin.addView("subastasAdmin", new SubastasAdminView());
        navigatorAdmin.addView("reservasAdmin", new ReservasAdminView());
        navigatorAdmin.addView("cerrarSesion", new CerrarSesionView());
		
	}

}
