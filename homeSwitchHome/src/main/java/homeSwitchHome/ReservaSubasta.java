package homeSwitchHome;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaSubasta extends Reserva {
	
	// se deberia crear una tabla subastas o columnas con la informacion extra
	private ArrayList<Integer> montos = new ArrayList<Integer>();
	private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
	
	public ReservaSubasta() {
		super();
	}
	
	public ReservaSubasta(LocalDate fechaInicio, String propiedad, EstadoDeReserva estado) {
		super(fechaInicio, propiedad, estado);
	}

	@Override
	public void borrarReserva() {
		// TODO Auto-generated method stub

	}

}
