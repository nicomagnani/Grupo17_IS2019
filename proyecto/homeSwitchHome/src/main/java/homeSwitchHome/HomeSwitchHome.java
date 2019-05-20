package homeSwitchHome;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.team17.homeSwitchHomeUI.ConnectionBD;

public class HomeSwitchHome {

public HomeSwitchHome() {


}
	private ConnectionBD conexion = new ConnectionBD();
	private ArrayList<Usuario> usuarios;
	private ArrayList<UsuarioAdministrador> administradores;
	private ArrayList<Reserva> reservas;
	private ArrayList<Propiedad> propiedades;
	
	public static final int LOGIN_SUCCESS = 1;
	public static final int WRONG_PASSWORD = 2;
	public static final int WRONG_USERNAME = 3;
	public static final int INVALID_EMAIL = 4;
	
	
	
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
