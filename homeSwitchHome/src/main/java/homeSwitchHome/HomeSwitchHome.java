package homeSwitchHome;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public final class HomeSwitchHome {

	private static Usuario usuarioActual;
	
	private static ArrayList<Usuario> usuarios;
	private static ArrayList<UsuarioAdministrador> administradores;
	private static ArrayList<Reserva> reservas;
	private static ArrayList<Propiedad> propiedades;

	
	public HomeSwitchHome() {

	}

	
	public static Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public static void setUsuarioActual(Usuario usuarioActual) {
		HomeSwitchHome.usuarioActual = usuarioActual;
	}
	
	public static boolean verificarMail(String email) {		//TODO
		   return true;
	}
	
	public void cerrarSesion(Usuario Usuario) 
	{
		
	}
	
	public List<Propiedad> mostrarResidenciasDisponibles(int unaSemana)
	{
		return propiedades;
		
	}
	
	public List<Propiedad> mostrarResidenciasAdmin()
	{
		return propiedades;
		
	}

	
	public List<Propiedad> mostrarResidencias() //Creo que esta repetido con mostrarResidenciasDisponibles
	{
		return propiedades;
		
	}
	
	public void agregarResidencia() //TODO
	{
		
	}
	
	public void modificarResidencia() //TODO
	{
		
	}
	
	public void contactarEmpresa()
	{
		
	}
	
	public List<Propiedad> buscarResidencias()
	{
		return propiedades;
		
	}
	
	public List<Propiedad> buscarResidenciasFechas(Date fechaInicio, Date fechaFin)
	{
		return propiedades;
		
	}
	
	public List<Propiedad> buscarResidenciasLugar(String localidad)
	{
		return propiedades;
		
	}
	
	public void abrirSubasta (Propiedad residencia, int semana) 
	{
		
	}
	
	public void CerrarSubasta (Propiedad residencia, int semana) 
	{
		
	}

	public void ofertar (String mail, float monto)
	{
		
	}
	
	public List<Reserva> mostrarReservasAdmin() //Tambien puede estar repetido
	{
		return reservas;
		
	}
}
