package homeSwitchHome;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

	public Usuario() {	//crearUsuario(), tal vez lo borremos
		super();
		// TODO Auto-generated method stub
	}

	private String mail;
	private List<Reserva> reservas = new ArrayList<Reserva>();
	
	public String getMail() {
		return mail;
	}
	
	public void realizarReserva(Propiedad unaPropiedad, int unaSemana)
	{
		
	}
	
	public List<Propiedad> verResidencias()
	{
		return null;
		//TODO
	}
	
	public void ofertarSubasta(ReservaSubasta unaSubasta, int unaCantidad) 
	{
		
	}
}
