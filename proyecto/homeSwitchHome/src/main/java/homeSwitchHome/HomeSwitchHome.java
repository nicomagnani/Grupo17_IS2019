package homeSwitchHome;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeSwitchHome {

public HomeSwitchHome() {


}
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private List<Reserva> reservas = new ArrayList<Reserva>();
	private List<Propiedad> propiedades = new ArrayList<Propiedad>();
	
	public int LOGIN_SUCCESS = 1;
	public int WRONG_USERNAME = 2;
	public int WRONG_PASSWORD = 3;
	
	
	public int iniciarSesion(String usuario, String contraseña)
	{
		return LOGIN_SUCCESS; //TODO
	}
	
	public void cerrarSesion(Usuario Usuario) 
	{
		
	}
	
	public List<Propiedad> mostrarResidenciasDisponibles(int unaSemana)
	{
		
	}
	
	public List<Propiedad> mostrarResidenciasAdmin()
	{
		
	}

	
	public List<Propiedad> mostrarResidencias() //Creo que esta repetido con mostrarResidenciasDisponibles
	{
		
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
