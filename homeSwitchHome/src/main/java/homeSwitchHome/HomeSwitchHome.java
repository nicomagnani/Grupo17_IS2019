package homeSwitchHome;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeSwitchHome {


	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private List<Reserva> reservas = new ArrayList<Reserva>();
	private List<Propiedad> propiedades = new ArrayList<Propiedad>();
	
	public void iniciarSesion(String usuario, String contraseña)
	{
		
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
	
	public ? expandirResidenciaAdmin() //TODO
	{
		
	}
	
	public List<Propiedad> mostrarResidencias() //Creo que esta repetido con mostrarResidenciasDisponibles
	{
		
	}
	
	public ? expandirResidencia() //TODO
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
		
	}
	
	public List<Propiedad> buscarResidenciasFechas(Date fechaInicio, Date fechaFin)
	{
		
	}
	
	public List<Propiedad> buscarResidenciasLugar(String localidad)
	{
		
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
		
	}
}
