package com.team17.homeSwitchHomeUI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import org.vaadin.easyuploads.UploadField;
import org.vaadin.ui.NumberField;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import homeSwitchHome.Propiedad;

public class AgregarResidenciaView extends Composite implements View {  //.necesita composite y view para funcionar correctamente	

	private FormLayout formulario = new FormLayout();
	private TextField titulo = new TextField("Título");
	private TextArea descripcion = new TextArea("Descripción");
	private TextField pais = new TextField("País");
	private TextField provincia = new TextField("Provincia");
	private TextField localidad = new TextField("Localidad");
	private TextField domicilio = new TextField("Domicilio");
	private NumberField monto = new NumberField("Monto base");
	private Button aceptarButton = new Button("Aceptar");
	private Notification resultado1 = new Notification(" ");
	private Label resultado2 = new Label();
	private Binder<Propiedad> binder = new Binder<>(Propiedad.class);
	private Propiedad propiedad = new Propiedad();
	
	private Label labelTipoCarga = new Label("Seleccionar tipo de carga de imágenes");
	private RadioButtonGroup<String> single = new RadioButtonGroup<>();
	
	private UploadField uploadField = new UploadField();
	
	private TextField url = new TextField();
	private Button buttonAgregarFoto = new Button("Agregar foto");
	private Button buttonMostrarFotosFinales = new Button("Ver fotos agregadas");
	private Label labelMsg = new Label();
	private Image imageFinal1 = new Image("Foto 1");
	private Image imageFinal2 = new Image("Foto 2");
	private Image imageFinal3 = new Image("Foto 3");
	private Image imageFinal4 = new Image("Foto 4");
	private Image imageFinal5 = new Image("Foto 5");
	
	private String op;
	private byte[][] fotos = new byte[5][]; // las 5 fotos
	private byte[] preFoto;
	private int tot = 0;
	
	
	public AgregarResidenciaView(MyUI interfaz) {
		
		Label cabecera = new Label("Agregar una residencia");
		cabecera.addStyleName(ValoTheme.MENU_TITLE);
		
		//el binder asocia escrito en el formulario a los campos de un objeto Propiedad 
		binder.readBean(propiedad);
		binder.bind(titulo, Propiedad::getTitulo, Propiedad::setTitulo);
		binder.bind(descripcion, Propiedad::getDescripcion, Propiedad::setDescripcion);
		binder.bind(pais, Propiedad::getPais, Propiedad::setPais);
		binder.bind(provincia, Propiedad::getProvincia, Propiedad::setProvincia);
		binder.bind(localidad, Propiedad::getLocalidad, Propiedad::setLocalidad);
		binder.bind(domicilio, Propiedad::getDomicilio, Propiedad::setDomicilio);
		binder.forField(monto).withConverter(new StringToFloatConverter("")).
		bind(Propiedad::getMontoBase, Propiedad::setMontoBase);
		
		//configuro el radiobox para elegir el tipo de foto
		single.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		single.setItems("Local", "URL");
		single.addValueChangeListener(e -> {
			op = e.getValue();			
			if (op == "Local") {
				uploadField.setVisible(true);
				url.setVisible(false);				
			} else
				if (op == "URL") {
					url.setVisible(true);
					uploadField.setVisible(false);
				}
		});
		
		//configuro el componente para subir imagenes locales
		uploadField.setAcceptFilter("image/*");
		uploadField.setClearButtonVisible(false);
		uploadField.setDisplayUpload(true);
		uploadField.setVisible(false);

		url.setVisible(false);
		
		//configuro el boton que carga una imagen
		buttonAgregarFoto.addClickListener(e -> {
			if (tot < 5) {
				if (op == "Local") {				
					if (!uploadField.isEmpty()) {
						fotos[tot++] = uploadField.getValue();
						uploadField.setValue(uploadField.getEmptyValue());
						labelMsg.setValue("Éxito");
					} else
						labelMsg.setValue("Error: Seleccione una foto");
				} else
					if (op == "URL") {
						if (!url.isEmpty()) {
							showFromURL(url.getValue());
							fotos[tot++] = preFoto;
							preFoto = null;
							url.setValue(url.getEmptyValue());
							labelMsg.setValue("Éxito");							
						} else
							labelMsg.setValue("Error: Indique una URL");
					}				
			} else
				labelMsg.setValue("Error: La cantidad máxima de fotos es 5");
		});

		//configuro el boton que muestra las fotos cargadas
		buttonMostrarFotosFinales.addClickListener(e -> {
			imageFinal1.setSource(null);
			imageFinal2.setSource(null);
			imageFinal3.setSource(null);
			imageFinal4.setSource(null);
			imageFinal5.setSource(null);
			if (fotos[0] != null)
				showFinal(fotos[0],imageFinal1);
			if (fotos[1] != null)
				showFinal(fotos[1],imageFinal2);
			if (fotos[2] != null)
				showFinal(fotos[2],imageFinal3);
			if (fotos[3] != null)
				showFinal(fotos[3],imageFinal4);
			if (fotos[4] != null)
				showFinal(fotos[4],imageFinal5);
		});		
		
		//configuro boton para agregar una residencia
		aceptarButton.addClickListener(e -> {
			try {
				aceptar(interfaz);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		
		//agregando los layouts a la vista
		formulario.addComponent(titulo);
		formulario.addComponent(descripcion);
		formulario.addComponent(pais);
		formulario.addComponent(provincia);
		formulario.addComponent(localidad);
		formulario.addComponent(domicilio);
		formulario.addComponent(monto);
		formulario.addComponent(labelTipoCarga);
		formulario.addComponent(single);
		formulario.addComponent(uploadField);
		formulario.addComponent(url);
		formulario.addComponent(labelMsg);
		
		imageFinal1.setWidth(100, Unit.PIXELS);
		imageFinal2.setWidth(100, Unit.PIXELS);
		imageFinal3.setWidth(100, Unit.PIXELS);
		imageFinal4.setWidth(100, Unit.PIXELS);
		imageFinal5.setWidth(100, Unit.PIXELS);
		
		HorizontalLayout verFotosLayout = new HorizontalLayout(imageFinal1, imageFinal2, imageFinal3, imageFinal4, imageFinal5);
		verFotosLayout.setWidth("650");
		verFotosLayout.addStyleName("scrollable");
		
		formulario.addComponent(buttonAgregarFoto);
		formulario.addComponent(buttonMostrarFotosFinales);
		formulario.addComponent(verFotosLayout);
				
		Panel panel = new Panel(formulario);
		panel.setHeight("600");
		panel.setWidth("750");
		panel.addStyleName("scrollable");
		
		VerticalLayout main = new VerticalLayout(cabecera, panel, aceptarButton, resultado2);
		main.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		main.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		main.setComponentAlignment(aceptarButton, Alignment.MIDDLE_CENTER);
		main.setComponentAlignment(resultado2, Alignment.MIDDLE_CENTER);		
		
		setCompositionRoot(main);
    }
	
	
	//carga imagen desde url
	private void showFromURL(String st) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		
		try {
		  is = new URL(st).openStream();
		  byte[] byteChunk = new byte[4096];
		  int n;
		  
		  while ( (n = is.read(byteChunk)) > 0 ) {
		    baos.write(byteChunk, 0, n);
		  }
		  
		  if (is != null)
			  is.close();
		
		  preFoto = baos.toByteArray();		  
		}
		
		catch (IOException e) {
			e.printStackTrace ();
		}
		
		StreamResource resource = new StreamResource(
	            new StreamResource.StreamSource() {
	                @Override
	                public InputStream getStream() {
	                    return new ByteArrayInputStream(preFoto);
	                }
	            }, "filename.png");
	    resource.setCacheTime(0);
	}
	
	
	//muestra las fotos cargadas
	private void showFinal(byte[] foto, Image image) {
	    StreamResource resource = new StreamResource(
	            new StreamResource.StreamSource() {
	                @Override
	                public InputStream getStream() {
	                    return new ByteArrayInputStream(foto);
	                }
	            }, "filename.png");
	    image.setSource(resource);
	}
	
	
	//chequea requisitos finales y sube la residencia a la base de datos
	private void aceptar(MyUI interfaz) throws SQLException {		
		
		if ( !hayCamposVacios() ) {			
			try {
		        binder.writeBean(propiedad);        
				} catch (ValidationException e) {
		    	  e.printStackTrace();
		    	  mostrarNotificacion("Error de la base de datos.", Notification.Type.ERROR_MESSAGE);
				}
			propiedad.setFotos(fotos);			
			
			if ( !existePropiedad() ) {
				//si cumple todos los requisitos, cargo la residencia y borro el formulario
				ConnectionBD con = new ConnectionBD();
				con.agregarResidencia(propiedad);
				mostrarNotificacion("Éxito.", Notification.Type.HUMANIZED_MESSAGE);
				
				//reinicio todos los campos
				for ( Component comp : formulario ) {
				    if (comp instanceof AbstractTextField) {
				    	((AbstractTextField) comp).setValue(((AbstractTextField) comp).getEmptyValue());
				    }
				}
				
				//recarga la sesion de admin con la nueva residencia
				interfaz.vistaAdmin("agregarResidencia");
				
			} else mostrarNotificacion("Error: Ya existe una propiedad con el mismo título en esa localidad.", Notification.Type.ERROR_MESSAGE);
		} else mostrarNotificacion("Error: Al menos un campo se encuentra vacío.", Notification.Type.ERROR_MESSAGE);
	}
			
	
    private boolean existePropiedad() throws SQLException {
    	ConnectionBD con = new ConnectionBD();
    	 ArrayList<Propiedad> propiedades = con.listaPropiedadesSinFotos();
    	 
    	 boolean existe = false;
    	 for (int i = 0; ( (i < propiedades.size()) && !existe ); i++) {
				if ( (propiedades.get(i).getTitulo().equals(propiedad.getTitulo())) &&
						(propiedades.get(i).getLocalidad().equals(propiedad.getLocalidad())) )
					existe = true;								
			}
    	
    	return existe;
	}
    

	private boolean hayCamposVacios() {
    	return ((titulo.isEmpty()) || (descripcion.isEmpty()) || (pais.isEmpty()) || (provincia.isEmpty()) || (localidad.isEmpty()) || 
				(domicilio.isEmpty()) || (monto.isEmpty()));
	}
	

	private void mostrarNotificacion(String st, Notification.Type tipo) {
    	resultado1 = new Notification(st, tipo);
    	resultado1.setDelayMsec(5000);
    	resultado1.show(Page.getCurrent());
    }
}