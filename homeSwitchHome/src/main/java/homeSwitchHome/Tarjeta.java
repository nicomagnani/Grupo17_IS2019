package homeSwitchHome;

import java.time.LocalDate;

public class Tarjeta {	
	private long numero;
	private String marca;
	private String titular;
	private LocalDate fVenc;
	private short codigo;
	
	public Tarjeta() {
		
	}
	
	public Tarjeta(long numero, String marca, String titular, LocalDate fVenc, short codigo) {
		this.numero = numero;
		this.marca = marca;
		this.titular = titular;
		this.fVenc = fVenc;
		this.codigo = codigo;		
	}
	
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
	public LocalDate getfVenc() {
		return fVenc;
	}
	public void setfVenc(LocalDate fVenc) {
		this.fVenc = fVenc;
	}
	public short getCodigo() {
		return codigo;
	}
	public void setCodigo(short codigo) {
		this.codigo = codigo;
	}	
}
