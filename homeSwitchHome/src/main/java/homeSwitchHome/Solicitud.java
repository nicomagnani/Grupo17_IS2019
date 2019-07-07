package homeSwitchHome;

public class Solicitud {

	private String mail;
	private String tipo; //"alta" o "baja" - podria implementrse como enum pero no es necesario
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
