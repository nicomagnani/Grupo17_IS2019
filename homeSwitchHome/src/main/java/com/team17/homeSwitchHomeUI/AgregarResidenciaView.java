package com.team17.homeSwitchHomeUI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;

import org.vaadin.easyuploads.UploadField;
import org.vaadin.ui.NumberField;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.navigator.View;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
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
	private Label resultado1 = new Label();
	private Label resultado2 = new Label();
	private Binder<Propiedad> binder = new Binder<>(Propiedad.class);
	private Propiedad propiedad = new Propiedad();
	
	private RadioButtonGroup<String> single = new RadioButtonGroup<>("Seleccionar tipo de carga de imágenes");
	
	private UploadField uploadField = new UploadField();
	
	private TextField url = new TextField();
	private Button buttonAgregar = new Button("Agregar foto");
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
	
	public AgregarResidenciaView() {
		
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
		uploadField.setCaption("Añadir imagen desde dispositivo local:");
		uploadField.setAcceptFilter("image/*");
		uploadField.setClearButtonVisible(false);
		uploadField.setDisplayUpload(true);
		
		//configuro el boton que carga una imagen
		buttonAgregar.addClickListener(e -> {
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
				aceptar();
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
		
		//form2 contiene los componentes para cargar imagenes
		FormLayout form2 = new FormLayout();
		form2.addComponent(single);
		
		VerticalLayout cargarFotoLayout = new VerticalLayout(uploadField,url);
		VerticalLayout agregarFotoLayout = new VerticalLayout(labelMsg,buttonAgregar);
		HorizontalLayout cargaryAgregarFotoLayout = new HorizontalLayout(cargarFotoLayout,agregarFotoLayout);
		form2.addComponent(cargaryAgregarFotoLayout);
		
		HorizontalLayout verFotosLayout = new HorizontalLayout(imageFinal1, imageFinal2, imageFinal3, imageFinal4, imageFinal5);
		form2.addComponent(buttonMostrarFotosFinales);
		form2.addComponent(verFotosLayout);
		
		formulario.addComponent(form2);
		
		formulario.addComponent(aceptarButton);
		formulario.addComponent(resultado1);
		formulario.addComponent(resultado2);
		
		VerticalLayout mainLayout = new VerticalLayout(cabecera, formulario);
		
		setCompositionRoot(mainLayout);
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
		
		  url.setCaption("Lectura completa");
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
	private void aceptar() throws SQLException {		
		boolean cumple = true;
		
		if ((titulo.isEmpty()) || (descripcion.isEmpty()) || (pais.isEmpty()) || (provincia.isEmpty()) || (localidad.isEmpty()) || 
				(domicilio.isEmpty()) || (monto.isEmpty())) {
			cumple = false;			
		} else {
			try {
	        binder.writeBean(propiedad);	        
			} catch (ValidationException e) {
	    	  e.printStackTrace();
	    	  resultado1.setValue("ERROR DESCONOCIDO");
			}
		}
		propiedad.setFotos(fotos);
		
//		resultado1.setValue(titulo.getValue()+" "+descripcion.getValue()+" "+pais.getValue()+" "+provincia.getValue()+" "+localidad.getValue()+" "+domicilio.getValue()+" "+monto.getValue()+cumple);
		resultado1.setValue("Agregando residencia...");
		
		//si cumple todos los requisitos, cargo la residencia y borro el formulario
		if (cumple) {
			ConnectionBD con = new ConnectionBD();
			con.agregarResidencia(propiedad);
			resultado1.setValue("Éxito.");
			
			//reinicio todos los campos
			for ( Component comp : formulario ) {
			    if (comp instanceof AbstractTextField) {
			    	((AbstractTextField) comp).setValue(((AbstractTextField) comp).getEmptyValue());
			    }
			}
			url.setValue(url.getEmptyValue());
			uploadField.setValue(uploadField.getEmptyValue());
			imageFinal1.setSource(null); imageFinal2.setSource(null); imageFinal3.setSource(null); imageFinal4.setSource(null); imageFinal5.setSource(null);
	    	fotos = new byte[5][];
	    	tot = 0;
		}
		
	}
}