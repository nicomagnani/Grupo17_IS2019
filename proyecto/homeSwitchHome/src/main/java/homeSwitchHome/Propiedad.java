package homeSwitchHome;

import java.awt.Image;

public class Propiedad {
<<<<<<< HEAD
	public Propiedad() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Propiedad(String titulo, String pais, String provincia, String localidad, String domicilio,
			String descripción, float montoBase, Image[] fotos) {
		super();
		this.titulo = titulo;
		this.pais = pais;
		this.provincia = provincia;
		this.localidad = localidad;
		this.domicilio = domicilio;
		this.descripción = descripción;
		this.montoBase = montoBase;
		this.fotos = fotos;
	}

	private String titulo,pais,provincia,localidad,domicilio,descripción;
=======
	private String titulo,pais,provincia,localidad,domicilio,descripcion;
>>>>>>> branch 'master' of https://github.com/nicomagnani/Grupo17_IS2019.git
	private float montoBase;
	private Image[] fotos = new Image[5];
	
<<<<<<< HEAD
	
=======
	public Propiedad(String titulo, String pais, String provincia, String localidad, String domicilio,
			String descripcion, int montoBase, Image[] fotos) {
	 		super();
	 		this.titulo = titulo;
	 		this.pais = pais;
	 		this.provincia = provincia;
	 		this.localidad = localidad;
	 		this.domicilio = domicilio;
	 		this.descripcion = descripcion;
	 		this.montoBase = montoBase;
	 		this.fotos = fotos;
	}
>>>>>>> branch 'master' of https://github.com/nicomagnani/Grupo17_IS2019.git
	
	public void reservarSemana(int unaSemana)
	{
		
	}
	
	public void anularReservaSemana(int unaSemana)
	{
		
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

<<<<<<< HEAD
	public String getDescripción() {
		return descripción;
	}

	public void setDescripción(String descripción) {
		this.descripción = descripción;
=======
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
>>>>>>> branch 'master' of https://github.com/nicomagnani/Grupo17_IS2019.git
	}

	public float getMontoBase() {
		return montoBase;
	}

	public void setMontoBase(float montoBase) {
		this.montoBase = montoBase;
	}

	public Image[] getFotos() {
		return fotos;
	}

	public void setFotos(Image[] fotos) {
		this.fotos = fotos;
	}
	
}
