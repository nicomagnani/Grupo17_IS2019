package com.team17.homeSwitchHomeUI;

import com.vaadin.navigator.View;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class VerFaqView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	public VerFaqView() {
		Label titulo = new Label("Sección de Ayuda");
		titulo.addStyleName(ValoTheme.MENU_TITLE);        
		
		Label texto1 = new Label("<h3><span style=\"text-decoration: underline;\"><strong>Preguntas y Respuestas Frecuentes (FAQ)</strong></span></h3>" );
		texto1.setContentMode(com.vaadin.shared.ui.ContentMode.HTML);
		
		//las preguntas y respuestas se declararan variables para facilitar la legibilidad y edicion del faq
		String preg1, preg2, preg3, preg4, rta1, rta2, rta3, rta4; //texto de las preguntas y respuestas
		String codInicio, codMitad, codFin; //contienen las etiquetas html para las preg+rtas

		preg1 = "¿Cómo participo en una subasta?";
		preg2 = "¿Cómo me informo si he ganado la subasta?";
		preg3 = "¿Qué tipo de pagos se aceptan?";
		preg4 = rta1 = rta2 = rta3 = rta4 = "(completar por el dueño)";

		codInicio = "<p><strong><span style=\"text-decoration: underline;\">Preg:</span> ";
		codMitad = "</strong><br/>" + "<span style=\"text-decoration: underline;\"><strong>Rta:</strong></span> ";
		codFin = "</p>";
		
		Label texto2 = new Label(codInicio + preg1 + codMitad + rta1 + codFin + "\n" +
				codInicio + preg2 + codMitad + rta2 + codFin + "\n" +
				codInicio + preg3 + codMitad + rta3 + codFin + "\n" +
				codInicio + preg4 + codMitad + rta4 + codFin);
		texto2.setContentMode(com.vaadin.shared.ui.ContentMode.HTML);
		
		VerticalLayout mainLayout = new VerticalLayout(titulo,texto1,texto2);
		
		setCompositionRoot(mainLayout);
    }
}