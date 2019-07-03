package homeSwitchHome;

import java.time.LocalDate;

public class ReservaDirecta extends Reserva {

	public ReservaDirecta() {
		super();
	}
	
	public ReservaDirecta(LocalDate fechaInicio, String propiedad, EstadoDeReserva estado) {
		super(propiedad, fechaInicio, estado);
	}
	
	public ReservaDirecta(String propiedad, String usuario, LocalDate fechaInicio, EstadoDeReserva estado) {
		super(propiedad, usuario, fechaInicio, estado);
	}
	
	public String getTipo() {
		return "Directa";
	}

}
