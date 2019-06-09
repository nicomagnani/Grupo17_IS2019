package homeSwitchHome;

public class Propiedad {

	private String titulo,pais,provincia,localidad,domicilio,descripcion;

	private float montoBase;
	private byte[][] fotos = new byte[5][];
		
	public Propiedad() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Propiedad(String titulo, String pais, String provincia, String localidad, String domicilio,
			String descripción, float montoBase, byte[][] fotos) {
		super();
		this.titulo = titulo;
		this.pais = pais;
		this.provincia = provincia;
		this.localidad = localidad;
		this.domicilio = domicilio;
		this.descripcion = descripción;
		this.montoBase = montoBase;
		this.fotos = fotos;
	}	

	public Propiedad(String titulo, String pais, String provincia, String localidad, String domicilio,
			String descripcion, int montoBase, byte[][] fotos) {
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


	public String getDescripción() {
		return descripcion;
	}

	public void setDescripción(String descripción) {
		this.descripcion = descripción;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;

	}

	public float getMontoBase() {
		return montoBase;
	}

	public void setMontoBase(float montoBase) {
		this.montoBase = montoBase;
	}

	public byte[][] getFotos() {
		return fotos;
	}

	public void setFotos(byte[][] fotos) {
		this.fotos = fotos;
	}
	
}
