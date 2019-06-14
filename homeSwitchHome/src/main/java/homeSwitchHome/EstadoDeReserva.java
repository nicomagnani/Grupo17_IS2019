package homeSwitchHome;

public enum EstadoDeReserva {
	NO_DISPONIBLE, //aguardando Hotsale
	DISPONIBLE_DIRECTA, // disponible para reserva directa
	DISPONIBLE_HOTSALE, // disponible para reserva hotsale
	DISPONIBLE_SUBASTA, // subasta en curso
	RESERVADA, // aguardando fecha de finalizacion
	FINALIZADA //tras un año, habria que definir si incluye 
}
