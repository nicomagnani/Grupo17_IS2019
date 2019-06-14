package homeSwitchHome;

import java.time.LocalDate;

abstract public class Reserva {

	private LocalDate fechaInicio;
	private String propiedad;
	private String usuario;
	private float monto;
	private EstadoDeReserva estado;
	
	
	public Reserva(LocalDate fechaInicio, String propiedad) { //crearReserva(), tal vez lo borremos
		fechaInicio = this.fechaInicio;
		propiedad = this.propiedad;
		//tambien se deberia indicar el estado de la reserva 
	}
	
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public EstadoDeReserva getEstado() {
		return estado;
	}

	public void setEstado(EstadoDeReserva estado) {
		this.estado = estado;
	}


	private int semana;
	
	abstract public void borrarReserva();
	
}
