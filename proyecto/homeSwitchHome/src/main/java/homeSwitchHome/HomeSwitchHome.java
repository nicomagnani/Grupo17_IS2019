package homeSwitchHome;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeSwitchHome {
<<<<<<< HEAD
public HomeSwitchHome() {
=======

	public HomeSwitchHome() {
>>>>>>> branch 'master' of https://github.com/nicomagnani/Grupo17_IS2019.git
		
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
	
<<<<<<< HEAD
	//public ? expandirResidenciaAdmin() //TODO
	//{
=======
	/*public ? expandirResidenciaAdmin() //TODO
	{
>>>>>>> branch 'master' of https://github.com/nicomagnani/Grupo17_IS2019.git
		
<<<<<<< HEAD
//	}
=======
	}*/
>>>>>>> branch 'master' of https://github.com/nicomagnani/Grupo17_IS2019.git
	
	public List<Propiedad> mostrarResidencias() //Creo que esta repetido con mostrarResidenciasDisponibles
	{
		
	}
	
<<<<<<< HEAD
	//public ? expandirResidencia() //TODO
	//{
=======
	/*public ? expandirResidencia() //TODO
	{
>>>>>>> branch 'master' of https://github.com/nicomagnani/Grupo17_IS2019.git
				
<<<<<<< HEAD
	//}
=======
	}*/
>>>>>>> branch 'master' of https://github.com/nicomagnani/Grupo17_IS2019.git
	
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
