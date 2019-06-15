package homeSwitchHome;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReservaSubasta extends Reserva {
	
	// se deberia crear una tabla subastas o columnas con la informacion extra
	private ArrayList<Integer> montos = new ArrayList<Integer>();
	private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
	
	public ReservaSubasta() {
		super();
	}
	
	public ReservaSubasta(LocalDate fechaInicio, String propiedad, EstadoDeReserva estado) {
		super(propiedad, fechaInicio, estado);
	}
	
	public ReservaSubasta(String propiedad, String usuario, LocalDate fechaInicio, EstadoDeReserva estado) {
		super(propiedad, usuario, fechaInicio, estado);
	}

	@Override
	public void borrarReserva() {
		// TODO Auto-generated method stub

	}

}
