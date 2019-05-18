package com.team17.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route("")
@PWA(name = "Project Base for Vaadin Flow", shortName = "Project Base")
public class MainLayout extends VerticalLayout {

    private FormularioOfertar oferta = new FormularioOfertar(this);
	//aca se inicializan los elementos de la pagina donde se realizan ofertas
	public MainLayout() {	    
	    //aca se agregan  e inicializan los elementos restantes
		
		add(oferta);
	}
        
	
}
