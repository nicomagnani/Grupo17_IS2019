package homeSwitchHome;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaSubasta extends Reserva {

	private int montos;
	private List<Usuario> usuario = new ArrayList<Usuario>();
	
	public ReservaSubasta(LocalDate fechaInicio, String propiedad) {	//crearReserva()
		super(fechaInicio, propiedad);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void borrarReserva() {
		// TODO Auto-generated method stub

	}

}
