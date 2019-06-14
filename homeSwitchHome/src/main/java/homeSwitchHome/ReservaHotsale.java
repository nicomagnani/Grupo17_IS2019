package homeSwitchHome;

import java.time.LocalDate;

public class ReservaHotsale extends Reserva {

	public ReservaHotsale() {
		super();
	}

	public ReservaHotsale(LocalDate fechaInicio, String propiedad, EstadoDeReserva estado) {
		super(fechaInicio, propiedad, estado);
	}

	@Override
	public void borrarReserva() {
		// TODO Auto-generated method stub

	}

}
