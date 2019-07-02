package homeSwitchHome;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

	abstract public class Reserva {

	private String propiedad;
	private String localidad;
	private String usuario;
	private LocalDate fechaInicio; //la fecha en que se comienza a publicar la reserva
	private EstadoDeReserva estado;
	private float monto;
	
	
	public Reserva() {
		
	}	
	
	public Reserva(String propiedad, LocalDate fechaInicio, EstadoDeReserva estado) {
		this.propiedad = propiedad;
		this.fechaInicio = fechaInicio;
		this.estado = estado; 
	}
	
	public Reserva(String propiedad, String usuario, LocalDate fechaInicio, EstadoDeReserva estado) {
		this.propiedad = propiedad;
		this.usuario = usuario;
		this.fechaInicio = fechaInicio;
		this.estado = estado; 
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
	
	public String getLocalidad() {		
		return localidad;
	}	

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
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
	
	//la fecha en que finaliza la publicacion
	public LocalDate getFechaFin() {
		return fechaInicio.plusYears(1);
	}
	
	//el rango de fechas en que se hace la reserva (de lunes a domingo)
	public LocalDate[] getFechasTiempoCompartido() {
		
		LocalDate[] fechas = new LocalDate[2];
		
		//fechas de inicio y fin del tiempo compartido
		//agrego 1 semana para que se cumpla al menos 1 año desde la creacion de la reserva
		fechas[0] = getFechaFin().plusWeeks(1).with(DayOfWeek.MONDAY);
		fechas[1] = getFechaFin().plusWeeks(1).with(DayOfWeek.SUNDAY);
		
		return fechas;
	}

	abstract public void borrarReserva();
	
	/*abstract public void setMontos(String[] montos);

	abstract public void setMontos(ArrayList<Float> montos);
	
	abstract public void setUsuarios(ArrayList<String> usuarios);

	abstract public void setUsuarios(String[] usuarios);
	
	abstract public void setFechaSubasta(LocalDate unaFecha);*/

	
}
