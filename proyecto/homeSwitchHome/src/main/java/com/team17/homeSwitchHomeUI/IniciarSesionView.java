package com.team17.homeSwitchHomeUI;
import java.sql.SQLException;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import homeSwitchHome.HomeSwitchHome;

@Theme("hometheme")
public class IniciarSesionView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	public IniciarSesionView(HomeSwitchHome sistema) {
		
		TextField textoTitulo = new TextField("Ttiulo:");
		TextField textoPais = new TextField("Pais:");
		
		Button login = new Button("Iniciar Sesión");
		login.addClickListener(e -> {
		    ConnectionBD conectar= new ConnectionBD();
		    try {
				conectar.AgregarDatos("untitulo","arg");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
		
		VerticalLayout mainLayout = new VerticalLayout(textoTitulo, textoPais, login);
        setCompositionRoot(mainLayout);
    }

	
}
