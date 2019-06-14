package homeSwitchHome;

import java.time.LocalDate;
import java.util.ArrayList;

public class Propiedad {

	private String titulo,pais,provincia,localidad,domicilio,descripcion;
	private float montoBase;
	private byte[][] fotos = new byte[5][];
	private byte[] foto1;
	private byte[] foto2;
	private byte[] foto3;
	private byte[] foto4;
	private byte[] foto5;
	
	
	
	private ArrayList<Reserva> reservas;
		
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

	public boolean añadirReserva(Reserva reserva) {		
		// >>true si tuvo exito
		return false;
	}
	
	public boolean eliminarReserva(Reserva reserva) {		
		// >>true si tuvo exito
		return false;
	} // >>true si tuvo exito
	
	public boolean tieneReservaEnFecha(LocalDate fechaDeInicio) {		
		// >>true si tuvo exito
		return false;
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
	
	public byte[] getFoto1() {
		return foto1;
	}

	public void setFoto1(byte[] foto1) {
		this.foto1 = foto1;
	}

	public byte[] getFoto2() {
		return foto2;
	}

	public void setFoto2(byte[] foto2) {
		this.foto2 = foto2;
	}

	public byte[] getFoto3() {
		return foto3;
	}

	public void setFoto3(byte[] foto3) {
		this.foto3 = foto3;
	}

	public byte[] getFoto4() {
		return foto4;
	}

	public void setFoto4(byte[] foto4) {
		this.foto4 = foto4;
	}

	public byte[] getFoto5() {
		return foto5;
	}

	public void setFoto5(byte[] foto5) {
		this.foto5 = foto5;
	}

	public ArrayList<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(ArrayList<Reserva> reservas) {
		this.reservas = reservas;
	}
	
}
