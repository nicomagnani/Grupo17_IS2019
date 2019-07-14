package homeSwitchHome;

import java.time.LocalDate;

public class ReservaHotsale extends Reserva {

	public ReservaHotsale() {
		super();
	}

	public ReservaHotsale(LocalDate fechaInicio, String propiedad, EstadoDeReserva estado) {
		super(propiedad, fechaInicio, estado);
	}
	
	public ReservaHotsale(String propiedad, String usuario, LocalDate fechaInicio, EstadoDeReserva estado) {
		super(propiedad, usuario, fechaInicio, estado);
	}
	
	public String getTipo() {
		return "Hotsale";
	}	

}
