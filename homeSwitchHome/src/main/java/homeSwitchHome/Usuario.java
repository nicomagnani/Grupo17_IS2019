package homeSwitchHome;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Usuario {
	
	private String mail;
	private String contraseña;
	private String nombre;
	private String apellido;
	private Tarjeta tarjeta;
	private LocalDate fNac;
	private short creditos = 2;
	
	private List<Reserva> reservas = new ArrayList<Reserva>();
	
	
	public Usuario() {
		
	}
		
	public Usuario(String mail, String contraseña, String nombre, String apellido,
			Tarjeta tarjeta, LocalDate fNac) {
		this.mail = mail;
		this.contraseña = contraseña;
		this.nombre = nombre;
		this.apellido = apellido;
		this.tarjeta = tarjeta;
		this.fNac = fNac;
	}
	
	//comienzo de getters y setters
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	
	public String getContraseña() {
		return contraseña;
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Tarjeta getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(Tarjeta tarjeta) {
		this.tarjeta = tarjeta;
	}

	public LocalDate getfNac() {
		return fNac;
	}

	public void setfNac(LocalDate fNac) {
		this.fNac = fNac;
	}

	public short getCreditos() {
		return creditos;
	}

	public void setCreditos(short creditos) {
		this.creditos = creditos;
	}
	//fin de getters y setters
	
	
	public void realizarReserva(Propiedad unaPropiedad, int unaSemana) {
		
	}
	
	public List<Propiedad> verResidencias() {
		return null;
	}
	
	public void ofertarSubasta(ReservaSubasta unaSubasta, int unaCantidad) {
		
	}
	
}
