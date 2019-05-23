package entities;



public class Propiedad  {
 /**
	 * 
	 */
	private long id;
	private static final long serialVersionUID = 1L;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private  String titulo,pais,provincia,localidad,domicilio,descripcion;
	//falta foto de tipo image
	private int montoBase;

 	public Propiedad(String titulo, String pais, String provincia, String localidad, String domicilio, String descripcion,
		int montoBase) {
 		super();
 		this.titulo = titulo;
 		this.pais = pais;
 		this.provincia = provincia;
 		this.localidad = localidad;
 		this.domicilio = domicilio;
 		this.descripcion = descripcion;
 		this.montoBase = montoBase;
 	}

	public Propiedad() {
	
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
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getMontoBase() {
		return montoBase;
	}
	public void setMontoBase(int montoBase) {
		this.montoBase = montoBase;
	}

}
