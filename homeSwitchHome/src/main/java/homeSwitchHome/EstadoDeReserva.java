package homeSwitchHome;

public enum EstadoDeReserva {
	NO_DISPONIBLE("No disponible"), //aguardando Hotsale
	DISPONIBLE_DIRECTA("Disponible (Reserva directa)"), // disponible para reserva directa
	DISPONIBLE_HOTSALE("Disponible (Hotsale)"), // disponible para reserva hotsale
	DISPONIBLE_SUBASTA("Disponible (En subasta)"), // subasta en curso
	RESERVADA("Ya reservada"), // aguardando fecha de finalizacion
	FINALIZADA("Publicación finalizada"); //tras pasar un año
	
	final private String text;
	
	EstadoDeReserva (String text) {
		this.text = text;
	}

	public String toStringAux() {
		return text;
	}
	
}