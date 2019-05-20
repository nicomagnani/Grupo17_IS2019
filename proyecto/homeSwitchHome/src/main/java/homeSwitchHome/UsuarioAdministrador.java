package homeSwitchHome;

import java.awt.List;

public class UsuarioAdministrador extends Usuario {

	public UsuarioAdministrador() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private String contraseña;
	private String mail;
	public String getContraseña() {
		return contraseña;
	}
	
	private Propiedad agregarResidencia() {
		return null;
	} //TODO
	
	private void modificarResidencia() {
	} //TODO
	
	private List verReservas()
	{
		return null;
		
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
}
