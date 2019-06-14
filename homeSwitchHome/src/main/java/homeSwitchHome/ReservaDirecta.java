package homeSwitchHome;

import java.time.LocalDate;

public class ReservaDirecta extends Reserva {

	public ReservaDirecta() {
		super();
	}
	
	public ReservaDirecta(LocalDate fechaInicio, String propiedad, EstadoDeReserva estado) {
		super(fechaInicio, propiedad, estado);
	}
	
	@Override
	public void borrarReserva() {
		// TODO Auto-generated method stub

	}

}
