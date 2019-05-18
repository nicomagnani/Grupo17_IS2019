package com.team17.homeSwitchHomeUI;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@Theme("hometheme")
public class IniciarSesionView extends Composite implements View {  //necesita composite y view para funcionar correctamente

	public IniciarSesionView() {
		Label texto1 = new Label("primer texto");
		Label texto2 = new Label("segundo texto");
		Button boton = new Button("boton");
		HorizontalLayout mainLayout = new HorizontalLayout(texto1,texto2,boton);
        setCompositionRoot(mainLayout);
    }
}
