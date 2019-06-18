package homeSwitchHome;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReservaSubasta extends Reserva {
	
	//NOTA: si la subasta no recibio ofertas, ambas listas estarán vacías
	private ArrayList<Float> montos = new ArrayList<>(); //contiene la historia del monto de la subasta (empezando por el monto actual)
	private ArrayList<String> usuarios = new ArrayList<>(); //contiene la historia de usuarios con una oferta válida (empezando por el ofertante actual)
	private LocalDate fechaSubasta = LocalDate.now();
	
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


	@Override
	public void borrarReserva() {
		// TODO Auto-generated method stub

	}

}
