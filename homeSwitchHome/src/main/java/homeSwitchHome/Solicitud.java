package homeSwitchHome;

public class Solicitud {

	private String mail;
	private String tipo; //"alta" o "baja" - podria implementrse como enum pero no es necesario
	private String motivo; //motivo indicado por el usuario al realizar la solicitud
	
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
	
	public String getMotivo() {
		if (motivo == null) {
			return "-";
		} else
			return motivo;
	}
	
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
}
