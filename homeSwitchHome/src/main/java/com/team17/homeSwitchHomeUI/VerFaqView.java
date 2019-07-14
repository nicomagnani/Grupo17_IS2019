package com.team17.homeSwitchHomeUI;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Title("Ayuda - HomeSwitchHome")
public class VerFaqView extends Composite implements View {  //.necesita composite y view para funcionar correctamente

	Label cabecera = new Label("Sección de Ayuda");
	Label texto1 = new Label("<h3><span style=\"text-decoration: underline;\"><strong>Preguntas y"
			+ " Respuestas Frecuentes (FAQ)</strong></span></h3>", ContentMode.HTML);
	
	public VerFaqView() {
		
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		//las preguntas y respuestas se declararan variables para facilitar la legibilidad y edicion del faq
		String preg1, rta1, preg2, rta2, preg3, rta3, preg4, rta4, preg5, rta5, preg6, rta6, preg7, rta7; //texto de las preguntas y respuestas
		String codInicio, codMitad, codFin; //contienen las etiquetas html para las preg+rtas

		preg1 = "¿Qué requisitos debo cumplir para hacer una reserva directa?";
		preg2 = "¿Cómo participo en una subasta?";
		preg3 = "¿Qué son los Hotsales?";
		preg4 = "¿Cuáles son los beneficios de ser usuario premium?";
		preg5 = "¿Qué formas de pago son aceptadas?";
		preg6 = "¿Para qué sirven los créditos?";
		preg7 = "¿Se puede cancelar una reserva?";
		rta1 = rta2 = rta3 = rta4 = rta5 = rta6 = rta7 = "(completar por el dueño)";

		codInicio = "<p><strong><span style=\"text-decoration: underline;\">Preg:</span> ";
		codMitad = "</strong><br/>" + "<span style=\"text-decoration: underline;\"><strong>Rta:</strong></span> ";
		codFin = "</p>";
		
		Label texto2 = new Label(codInicio + preg1 + codMitad + rta1 + codFin + "\n" +
				codInicio + preg2 + codMitad + rta2 + codFin + "\n" +
				codInicio + preg3 + codMitad + rta3 + codFin + "\n" +
				codInicio + preg4 + codMitad + rta4 + codFin + "\n" +
				codInicio + preg5 + codMitad + rta5 + codFin + "\n" +
				codInicio + preg6 + codMitad + rta6 + codFin + "\n" +
				codInicio + preg7 + codMitad + rta7 + codFin);
		texto2.setContentMode(ContentMode.HTML);
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera,texto1,texto2);
		
		setCompositionRoot(mainLayout);
    }
}