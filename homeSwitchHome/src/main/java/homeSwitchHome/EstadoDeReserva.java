package homeSwitchHome;

public enum EstadoDeReserva {
	EN_ESPERA("En espera"), //aguardando Hotsale
	DISPONIBLE("Disponible"), //disponible para reservar
	RESERVADA("Reservada"), // aguardando fecha de finalizacion
	CANCELADA("Cancelada"), // reserva cancelada, las de este tipo se guardan en otra tabla
	FINALIZADA("Finalizada"); //tras pasar un año
	
	final private String text;
	
	EstadoDeReserva (String text) {
		this.text = text;
	}

	public String toStringAux() {
		return text;
	}
	
}