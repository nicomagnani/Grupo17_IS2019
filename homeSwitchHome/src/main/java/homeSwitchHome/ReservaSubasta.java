package homeSwitchHome;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReservaSubasta extends Reserva {
	
	//NOTA: si la subasta no recibio ofertas, ambas listas estarán vacías
	private ArrayList<Float> montos = new ArrayList<>(); //contiene la historia del monto de la subasta (empezando por el monto actual)
	private ArrayList<String> usuarios = null; //contiene la historia de usuarios con una oferta válida (empezando por el ofertante actual)
	private LocalDate fechaInicioSubasta;
	
	public ReservaSubasta() {
		super();
	}
	
	public ReservaSubasta(LocalDate fechaInicio, String propiedad, EstadoDeReserva estado) {
		super(propiedad, fechaInicio, estado);
	}
	
	public ReservaSubasta(String propiedad, String usuario, LocalDate fechaInicio, EstadoDeReserva estado) {
		super(propiedad, usuario, fechaInicio, estado);
	}
	
	public ArrayList<Float> getMontos() {
		return montos;
	}

	public void setMontos(ArrayList<Float> montos) {
		this.montos = montos;
	}

	public ArrayList<String> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<String> usuarios) {
		this.usuarios = usuarios;
	}

	public LocalDate getFechaInicioSubasta() {
		return fechaInicioSubasta;
	}

	public void setFechaInicioSubasta(LocalDate fechaInicioSubasta) {
		this.fechaInicioSubasta = fechaInicioSubasta;
	}
	
	public LocalDate getFechaFinSubasta() {
		return this.getFechaInicioSubasta().plusDays(3);		
	}
	
	public String getFechaFinSubastaString() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		
		if ( LocalDate.now().isBefore(this.getFechaFinSubasta()) ) {
			return this.getFechaInicioSubasta().plusDays(3).atStartOfDay().format(formatter) + " hs.";
		} else
			return "Finalizada";
	}

	public String getTipo() {
		return "Subasta";
	}
	
	@Override
	public float getMonto() {
		if (getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA) {
			return montos.get(0);
		} else
			return super.getMonto();		
	}

}
