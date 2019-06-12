package homeSwitchHome;

import java.time.LocalDate;

public class UsuarioComun extends Usuario {

	public UsuarioComun() {
		
		super();
	}
	
	public UsuarioComun(String mail, String contraseña, String nombre, String apellido,
			Tarjeta tarjeta, LocalDate fNac) {
		
		super(mail,contraseña,nombre,apellido,tarjeta,fNac);		
	}

	
}
