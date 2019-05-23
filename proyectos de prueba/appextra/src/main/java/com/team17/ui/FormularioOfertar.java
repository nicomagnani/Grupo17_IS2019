package com.team17.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;

public class FormularioOfertar extends FormLayout {
	private EmailField campoEmail = new EmailField("Dirección de correo electrónico");
	private NumberField campoMonto = new NumberField("Monto a ofertar");	
	private Button botonAceptar = new Button("Aceptar");
	
	/* en esta parte interactua con la base de datos
	private Binder<Customer> binder = new Binder<>(Customer.class);
	private CustomerService service = CustomerService.getInstance();
	*/
	
	private MainLayout mainLayout;
	
	public FormularioOfertar(MainLayout mainLayout) {		

		campoMonto.setPrefixComponent(new Span("$"));
		this.mainLayout = mainLayout;

	 // status.setItems(CustomerStatus.values());
	    
	    add(campoEmail, campoMonto, botonAceptar);
	    
	  // binder.bindInstanceFields(this);
	    
	    botonAceptar.addClickListener(event -> aceptar());
	}
	
	private void aceptar() {
	   /*lo que sucede al enviar la oferta
		Customer customer = binder.getBean();
	    service.save(customer);
	    mainView.updateList();
	    setCustomer(null);
		*/
	}
	
}
