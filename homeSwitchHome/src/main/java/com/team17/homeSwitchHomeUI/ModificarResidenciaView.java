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

import com.vaadin.annotations.Title;
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
import homeSwitchHome.ReservaSubasta;

@Title("Modificar residencia - HomeSwitchHome")
public class ModificarResidenciaView extends Composite implements View {

	private Label cabecera = new Label("Modificar residencia");

	private TextField titulo = new TextField("Título");
	private TextArea descripcion = new TextArea("Descripción");
	private TextField localidad = new TextField("Localidad");
	private TextField domicilio = new TextField("Domicilio");
	private NumberField monto = new NumberField("Monto base");
	private Button aceptarButton = new Button("Aceptar");
	private Notification msjResultado = new Notification(" ");

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
	private Propiedad propiedadActual;
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
			if ( (r.getEstado() == EstadoDeReserva.DISPONIBLE) && (r instanceof ReservaSubasta) ) {
				return true;
			}
		}
		return false;
	}

	private void inicializarComponentes() {		

		cabecera.addStyleName(ValoTheme.MENU_TITLE);

		descripcion.setValue(propiedad.getDescripcion());
		monto.setValue(String.valueOf(propiedad.getMontoBase()));
		monto.setDecimalPrecision(2);
		monto.setDecimalSeparator(',');
		monto.setGroupingUsed(true);
		
		new Binder<Propiedad>().forField(monto)
			    .withValidator(new RegexpValidator("", "[-+]?[0-9]*\\.?[0-9]+"))
			    .withConverter(new StringToFloatConverter(""))
			    .bind(Propiedad::getMontoBase, Propiedad::setMontoBase);
		
		if (!enSubasta) {
			cabecera.setValue("Modificar residencia (sin subasta activa)");
			titulo.setValue(propiedad.getTitulo());
			localidad.setValue(propiedad.getLocalidad());
			domicilio.setValue(propiedad.getDomicilio());
		} else {
			cabecera.setValue("Modificar residencia (en subasta)");
			titulo.setVisible(false);
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
		
		image1.addClickListener( e -> this.eliminarFoto(0) );
		image2.addClickListener( e -> this.eliminarFoto(1) );
		image3.addClickListener( e -> this.eliminarFoto(2) );
		image4.addClickListener( e -> this.eliminarFoto(3) );
		image5.addClickListener( e -> this.eliminarFoto(4) );
		fotos[0] = propiedad.getFoto1();
		fotos[1] = propiedad.getFoto2();
		fotos[2] = propiedad.getFoto3();
		fotos[3] = propiedad.getFoto4();
		fotos[4] = propiedad.getFoto5();
		image1.setVisible(false);
		image2.setVisible(false);
		image3.setVisible(false);
		image4.setVisible(false);
		image5.setVisible(false);

		//configuro boton para modificar una residencia
		aceptarButton.addClickListener(e -> {
			try {
				aceptar();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
	}


	private void eliminarFoto(int i) {
		
		fotos[i] = null;
		
		this.mostrarFotosFinales();
	}


	private void inicializarLayouts() {

		//agregando los layouts a la vista
		formulario = new FormLayout(titulo,  descripcion, localidad, domicilio,
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

		image1.setVisible(false);
		image2.setVisible(false);
		image3.setVisible(false);
		image4.setVisible(false);
		image5.setVisible(false);
		image1.setSource(null);
		image2.setSource(null);
		image3.setSource(null);
		image4.setSource(null);
		image5.setSource(null);
		image1.setWidth("1");
		image2.setWidth("1");
		image3.setWidth("1");
		image4.setWidth("1");
		image5.setWidth("1");
		
		if (fotos[0] != null) {
			mostrarFotoCargada(fotos[0],image1);
			image1.setVisible(true);
			image1.setWidth(100, Unit.PIXELS);
		}
		if (fotos[1] != null) {
			mostrarFotoCargada(fotos[1],image2);
			image2.setVisible(true);
			image2.setWidth(100, Unit.PIXELS);			
		}
		if (fotos[2] != null) {
			mostrarFotoCargada(fotos[2],image3);
			image3.setVisible(true);
			image3.setWidth(100, Unit.PIXELS);
		}
		if (fotos[3] != null) {
			mostrarFotoCargada(fotos[3],image4);
			image4.setVisible(true);
			image4.setWidth(100, Unit.PIXELS);			
		}
		if (fotos[4] != null) {
			mostrarFotoCargada(fotos[4],image5);
			image5.setVisible(true);
			image5.setWidth(100, Unit.PIXELS);			
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
			
			int pos = this.primerPosicionVacia();
			
			if (op == "Local") {
				if (!uploadField.isEmpty()) {
					fotos[pos] = uploadField.getValue();
					uploadField.setValue(uploadField.getEmptyValue());
					msjFoto.setValue("Éxito");
				} else
					msjFoto.setValue("Error: Seleccione una foto");
			} else
				if (op == "URL") {
					if (!url.isEmpty()) {
						cargarDesdeURL(url.getValue());
						fotos[pos] = preFoto;
						preFoto = null;
						url.setValue(url.getEmptyValue());
						msjFoto.setValue("Éxito");
					} else
						msjFoto.setValue("Error: Indique una URL");
				}
		} else
			msjFoto.setValue("Error: La cantidad máxima de fotos es 5");
	}


	private int primerPosicionVacia() {
		
		for (int i=0; i < fotos.length; i++) {
			if (fotos[i] == null)
				return i;
		}
		return -1;
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
			
			if (montoFinal > 0) {	

				propiedad = new Propiedad( titulo.getValue(), localidad.getValue(),
						domicilio.getValue(), descripcion.getValue(), montoFinal, fotos );
				
				propiedad.setFotos(fotos);
				propiedadActual = HomeSwitchHome.getPropiedadActual();
		
				//si cumple todos los requisitos, actualizo la residencia y cargo una nueva sesión de admin
				if ( (!enSubasta) && (this.seHaModificadoTitulo()) && (this.existePropiedad()) ) {
					mostrarNotificacion("Error: Ya existe una propiedad con el mismo título en esa localidad.", Notification.Type.ERROR_MESSAGE);
				} else {
					conexion = new ConnectionBD();
					if (enSubasta) {
						conexion.modificarResidenciaEnSubasta(propiedad, propiedadActual.getTitulo(), propiedadActual.getLocalidad());
					} else {
						conexion.modificarResidencia(propiedad, propiedadActual.getTitulo(), propiedadActual.getLocalidad());
					}
					
					//actualizo la propiedad a mostrar en el detalle
					conexion = new ConnectionBD();
					if (enSubasta) {
						propiedadActual = conexion.buscarResidencia( propiedadActual.getTitulo(), propiedadActual.getLocalidad() );
					} else
						propiedadActual = conexion.buscarResidencia( propiedad.getTitulo(), propiedad.getLocalidad() );
					
					HomeSwitchHome.setPropiedadActual(propiedadActual);
					
					//muestro msj de éxito y muestro el detalle de la residencia actualizada
					mostrarNotificacion("Éxito.", Notification.Type.HUMANIZED_MESSAGE);
					interfaz.vistaAdminConNuevaVista("detalleResidencia");
				}
			} else mostrarNotificacion("Error: El monto debe ser mayor a 0.", Notification.Type.ERROR_MESSAGE);

		} else mostrarNotificacion("Error: Al menos un campo se encuentra vacío.", Notification.Type.ERROR_MESSAGE);

	}


	private boolean seHaModificadoTitulo() {
    	return ( !propiedad.getTitulo().equals(propiedadActual.getTitulo()) );
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
    		return ( (titulo.isEmpty()) || (descripcion.isEmpty()) ||(localidad.isEmpty())
    				|| (domicilio.isEmpty()) || (monto.isEmpty()) );
	}


	private void mostrarNotificacion(String st, Notification.Type tipo) {
		msjResultado = new Notification(st, tipo);
    	msjResultado.setDelayMsec(5000);
    	msjResultado.show(Page.getCurrent());
    }
}
