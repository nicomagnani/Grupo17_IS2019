package homeSwitchHome;

import java.util.Date;

public class Tarjeta {	
	private long numero;
	private String marca;
	private String titular;
	private Date fVenc;
	private short codigo;
	
	public long getNumero() {
		return numero;
	}
	public void setNumero(long numero) {
		this.numero = numero;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public Date getfVenc() {
		return fVenc;
	}
	public void setfVenc(Date fVenc) {
		this.fVenc = fVenc;
	}
	public short getCodigo() {
		return codigo;
	}
	public void setCodigo(short codigo) {
		this.codigo = codigo;
	}	
}
