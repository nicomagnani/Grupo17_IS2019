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
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
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

import homeSwitchHome.EstadoDeReserva;
import homeSwitchHome.HomeSwitchHome;
import homeSwitchHome.Propiedad;
import homeSwitchHome.Reserva;

public class ModificarResidenciaView extends Composite implements View {

	private Label cabecera = new Label();

	private TextField titulo = new TextField("Título");
	private TextArea descripcion = new TextArea("Descripción");
	private TextField pais = new TextField("País");
	private TextField provincia = new TextField("Provincia");
	private TextField localidad = new TextField("Localidad");
	private TextField domicilio = new TextField("Domicilio");
	private NumberField monto = new NumberField("Monto base");
	private Button aceptarButton = new Button("Aceptar");
	private Notification msjResultado = new Notification(" ");

	//el binder asocia los campos del formulario con los de un objeto Propiedad
//	private Binder<Propiedad> binder = new Binder<>(Propiedad.class);

	private Label labelTipoCarga = new Label("Seleccionar tipo de carga de imágenes");
	private RadioButtonGroup<String> tipoCargaRadioButton = new RadioButtonGroup<>();
	private UploadField uploadField = new UploadField();
	private TextField url = new TextField();
	private Button agregarFotoButton = new Button("Agregar foto");
	private Button mostrarFotosFinalesButton = new Button("Ver fotos agregadas");
	private Label msjFoto = new Label();
	private Image image1 = new Image("Foto 1");
	private Image image2 = new Image("Foto 2");
	private Image image3 = new Image("Foto 3");
	private Image image4 = new Image("Foto 4");
	private Image image5 = new Image("Foto 5");

	private FormLayout formulario;
	private HorizontalLayout verFotosLayout;
	private Panel panel;
	private VerticalLayout mainLayout;

	private Propiedad propiedad;
	private Propiedad propiedad2;
	private ArrayList<Reserva> reservas = new ArrayList<Reserva>();
	private String op;
	private byte[][] fotos = new byte[5][]; // las 5 fotos
	private byte[] preFoto;
	private boolean enSubasta;

	private ConnectionBD conexion;
	private MyUI interfaz;


	public ModificarResidenciaView(MyUI interfaz) {

		this.interfaz = interfaz;
		propiedad = HomeSwitchHome.getPropiedadActual();
		enSubasta = this.seEncuentraEnSubasta();

//		this.inicializarBinder();
		this.inicializarComponentes();
		this.inicializarLayouts();

		setCompositionRoot(mainLayout);
    }


	private boolean seEncuentraEnSubasta() {

		conexion = new ConnectionBD();
		try {
			reservas = conexion.listaReservasPorPropiedad(propiedad.getTitulo(), propiedad.getLocalidad());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (Reserva r : reservas) {
			//chequea si la residencia tiene una semana activa
			if ( (r.getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA) ) {
				return true;
			}
		}
		return false;
	}

//	private void inicializarBinder() {
//
//		binder.readBean(propiedad);
//		binder.bind(descripcion, Propiedad::getDescripcion, Propiedad::setDescripcion);
//		binder.forField(monto)
//				.withConverter(new StringToFloatConverter(""))
//				.bind(Propiedad::getMontoBase, Propiedad::setMontoBase);
//
//		if (!enSubasta) {
//			binder.bind(titulo, Propiedad::getTitulo, Propiedad::setTitulo);
//			binder.bind(pais, Propiedad::getPais, Propiedad::setPais);
//			binder.bind(provincia, Propiedad::getProvincia, Propiedad::setProvincia);
//			binder.bind(localidad, Propiedad::getLocalidad, Propiedad::setLocalidad);
//			binder.bind(domicilio, Propiedad::getDomicilio, Propiedad::setDomicilio);
//		}
//	}


	private void inicializarComponentes() {

		cabecera.addStyleName(ValoTheme.MENU_TITLE);

		descripcion.setValue(propiedad.getDescripcion());
		monto.setValue(String.valueOf(propiedad.getMontoBase()));
		monto.setDecimalPrecision(2);
		monto.setDecimalSeparator('.');
		monto.setGroupingUsed(false);
		
		new Binder<Propiedad>().forField(monto)
			    .withValidator(new RegexpValidator("", "[-+]?[0-9]*\\.?[0-9]+"))
			    .withConverter(new StringToFloatConverter(""))
			    .bind(Propiedad::getMontoBase, Propiedad::setMontoBase);

		
		
		if (!enSubasta) {
			cabecera.setValue("Modificar residencia (sin subasta activa)");
			titulo.setValue(propiedad.getTitulo());
			pais.setValue(propiedad.getPais());
			provincia.setValue(propiedad.getProvincia());
			localidad.setValue(propiedad.getLocalidad());
			domicilio.setValue(propiedad.getDomicilio());
		} else {
			cabecera.setValue("Modificar residencia (en subasta)");
			titulo.setVisible(false);
			pais.setVisible(false);
			provincia.setVisible(false);
			localidad.setVisible(false);
			domicilio.setVisible(false);
		}

		//configuro el radiobox para elegir el tipo de foto
		tipoCargaRadioButton.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		tipoCargaRadioButton.setItems("Local", "URL");
		tipoCargaRadioButton.addValueChangeListener(e -> {
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

		//oculto el campo de url
		url.setVisible(false);

		//configuro el boton que carga una imagen
		agregarFotoButton.addClickListener( e -> agregarFoto() );

		//configuro el boton que muestra las fotos cargadas
		mostrarFotosFinalesButton.addClickListener( e -> mostrarFotosFinales() );

		image1.setWidth(100, Unit.PIXELS);
		image2.setWidth(100, Unit.PIXELS);
		image3.setWidth(100, Unit.PIXELS);
		image4.setWidth(100, Unit.PIXELS);
		image5.setWidth(100, Unit.PIXELS);
		image1.setVisible(false);
		image2.setVisible(false);
		image3.setVisible(false);
		image4.setVisible(false);
		image5.setVisible(false);
		fotos[0] = propiedad.getFoto1();
		fotos[1] = propiedad.getFoto2();
		fotos[2] = propiedad.getFoto3();
		fotos[3] = propiedad.getFoto4();
		fotos[4] = propiedad.getFoto5();

		//configuro boton para modificar una residencia
		aceptarButton.addClickListener(e -> {
			try {
				aceptar();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
	}


	private void inicializarLayouts() {

		//agregando los layouts a la vista
		formulario = new FormLayout(titulo,  descripcion, pais, provincia, localidad, domicilio,
				monto, labelTipoCarga, tipoCargaRadioButton, uploadField, url, msjFoto,
				agregarFotoButton, mostrarFotosFinalesButton);

		verFotosLayout = new HorizontalLayout(image1, image2, image3, image4, image5);
		verFotosLayout.setWidth("650");
		verFotosLayout.addStyleName("scrollable");
		formulario.addComponent(verFotosLayout);

		panel = new Panel(formulario);
		panel.setHeight("600");
		panel.setWidth("750");
		panel.addStyleName("scrollable");

		mainLayout = new VerticalLayout(cabecera, panel, aceptarButton);
		mainLayout.setComponentAlignment(cabecera, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(aceptarButton, Alignment.MIDDLE_CENTER);
	}


	private void mostrarFotosFinales() {

		image1.setSource(null);
		image2.setSource(null);
		image3.setSource(null);
		image4.setSource(null);
		image5.setSource(null);
		
		if (fotos[0] != null) {
			mostrarFotoCargada(fotos[0],image1);
			image1.setVisible(true);
		}
		if (fotos[1] != null) {
			mostrarFotoCargada(fotos[1],image2);
			image2.setVisible(true);
		}
		if (fotos[2] != null) {
			mostrarFotoCargada(fotos[2],image3);
			image3.setVisible(true);
		}
		if (fotos[3] != null) {
			mostrarFotoCargada(fotos[3],image4);
			image4.setVisible(true);			
		}
		if (fotos[4] != null) {
			mostrarFotoCargada(fotos[4],image5);
			image5.setVisible(true);			
		}
	}
	
	
	private int cantidadFotosCargadas() {
		
		int cant = 0;
		
		for (byte[] foto : fotos) {
			if (foto != null)
				cant++;
		}
		
		return cant;
	}


	private void agregarFoto() {

		int cant = this.cantidadFotosCargadas();
		
		if (cant < 5) {
			if (op == "Local") {
				if (!uploadField.isEmpty()) {
					fotos[cant] = uploadField.getValue();
					uploadField.setValue(uploadField.getEmptyValue());
					msjFoto.setValue("Éxito");
				} else
					msjFoto.setValue("Error: Seleccione una foto");
			} else
				if (op == "URL") {
					if (!url.isEmpty()) {
						cargarDesdeURL(url.getValue());
						fotos[cant] = preFoto;
						preFoto = null;
						url.setValue(url.getEmptyValue());
						msjFoto.setValue("Éxito");
					} else
						msjFoto.setValue("Error: Indique una URL");
				}
		} else
			msjFoto.setValue("Error: La cantidad máxima de fotos es 5");
	}


	private void cargarDesdeURL(String st) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;

		try {
			is = new URL(st).openStream();
			byte[] byteChunk = new byte[4096];
			int n;

			while ( (n = is.read(byteChunk)) > 0 )
				baos.write(byteChunk, 0, n);

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


	private void mostrarFotoCargada(byte[] foto, Image image) {
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

		if ( !hayCamposVacios() ) {
			
			float montoFinal = Float.parseFloat(monto.getValue());
			
			if ( esNroPositivo(montoFinal) ) {	

				propiedad = new Propiedad( titulo.getValue(), pais.getValue(), provincia.getValue(), localidad.getValue(),
						domicilio.getValue(), descripcion.getValue(), montoFinal, fotos );
				
				propiedad.setFotos(fotos);
				propiedad2 = HomeSwitchHome.getPropiedadActual();
		
				//si cumple todos los requisitos, actualizo la residencia y cargo una nueva sesión de admin
				if ( !enSubasta && seHaModificadoTitulo(propiedad2) && existePropiedad() ) {
					mostrarNotificacion("Error: Ya existe una propiedad con el mismo título en esa localidad.", Notification.Type.ERROR_MESSAGE);
				} else {
					conexion = new ConnectionBD();
					if (enSubasta) {
						conexion.modificarResidenciaEnSubasta(propiedad, propiedad2.getTitulo(), propiedad2.getLocalidad());
					} else {
						conexion.modificarResidencia(propiedad, propiedad2.getTitulo(), propiedad2.getLocalidad());
					}
					
					//actualizo la propiedad a mostrar en el detalle
					conexion = new ConnectionBD();
					propiedad2 = conexion.buscarResidencia( propiedad2.getTitulo(), propiedad2.getLocalidad() );
					HomeSwitchHome.setPropiedadActual(propiedad2);
					
					//muestro msj de éxito y muestro el detalle de la residencia actualizada
					mostrarNotificacion("Éxito.", Notification.Type.HUMANIZED_MESSAGE);
					interfaz.vistaAdminConNuevaVista("detalleResidencia");
				}
			} else mostrarNotificacion("Error: El monto debe ser mayor a 0.", Notification.Type.ERROR_MESSAGE);

		} else mostrarNotificacion("Error: Al menos un campo se encuentra vacío.", Notification.Type.ERROR_MESSAGE);

	}


    private boolean esNroPositivo(float n) {
    	return (n > 0);
	}


	private boolean seHaModificadoTitulo(Propiedad p2) {
    	return ( !propiedad.getTitulo().equals(propiedad2.getTitulo()) );
	}


	private boolean existePropiedad() throws SQLException {

		conexion = new ConnectionBD();
    	ArrayList<Propiedad> propiedades = conexion.listaResidenciasSinFotos();

    	for (Propiedad p : propiedades) {
    		if ( (p.getTitulo().equals(propiedad.getTitulo())) && (p.getLocalidad().equals(propiedad.getLocalidad())) )
    			return true;
		}
    	return false;
	}


	private boolean hayCamposVacios() {
    	if (enSubasta) {
    		return ( descripcion.isEmpty() || monto.isEmpty() );
    	} else
    		return ( (titulo.isEmpty()) || (descripcion.isEmpty()) || (pais.isEmpty()) || (provincia.isEmpty()) ||
    				(localidad.isEmpty()) || (domicilio.isEmpty()) || (monto.isEmpty()) );
	}


	private void mostrarNotificacion(String st, Notification.Type tipo) {
		msjResultado = new Notification(st, tipo);
    	msjResultado.setDelayMsec(5000);
    	msjResultado.show(Page.getCurrent());
    }
}
