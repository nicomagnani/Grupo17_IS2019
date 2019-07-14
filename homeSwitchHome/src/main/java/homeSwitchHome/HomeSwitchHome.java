package homeSwitchHome;

import java.time.LocalDate;


public final class HomeSwitchHome {

	private static Usuario usuarioActual;
	private static Propiedad propiedadActual;
	private static LocalDate fechaInicioBuscada;
	private static LocalDate fechaFinBuscada;
	private static Reserva reservaActual;
	
//	private static ArrayList<Usuario> usuarios;
//	private static ArrayList<UsuarioAdministrador> administradores;
//	private static ArrayList<Reserva> reservas;
//	private static ArrayList<Propiedad> propiedades;

	
	public HomeSwitchHome() {

	}

	
	public static Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public static void setUsuarioActual(Usuario usuarioActual) {
		HomeSwitchHome.usuarioActual = usuarioActual;
	}

	public static Propiedad getPropiedadActual() {
		return propiedadActual;
	}

	public static void setPropiedadActual(Propiedad propiedadActual) {
		HomeSwitchHome.propiedadActual = propiedadActual;
	}
	
	public static LocalDate getFechaInicioBuscada() {
		return fechaInicioBuscada;
	}

	public static void setFechaInicioBuscada(LocalDate fechaInicioBuscada) {
		HomeSwitchHome.fechaInicioBuscada = fechaInicioBuscada;
	}

	public static LocalDate getFechaFinBuscada() {
		return fechaFinBuscada;
	}

	public static void setFechaFinBuscada(LocalDate fechaFinBuscada) {
		HomeSwitchHome.fechaFinBuscada = fechaFinBuscada;
	}

	public static Reserva getReservaActual() {
		return reservaActual;
	}


	public static void setReservaActual(Reserva reservaActual) {
		HomeSwitchHome.reservaActual = reservaActual;
	}
	
}
