package homeSwitchHome;

import java.time.LocalDate;

//clase usada para reunir datos de una oferta en una subasta
public class Oferta {

	private String propiedad;
	private String localidad;
	private LocalDate fechaReserva;
	private String fechaFinSubasta;
	private Float precio;
	private Float monto;
	
	public Oferta(String propiedad, String localidad, LocalDate fechaReserva, String fechaFinSubasta,
			Float precio, Float monto) {
		this.propiedad = propiedad;
		this.localidad = localidad;
		this.fechaReserva = fechaReserva;
		this.fechaFinSubasta = fechaFinSubasta;
		this.precio = precio;
		this.monto = monto;
	}
	
	
	public String getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public LocalDate getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(LocalDate fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	public String getFechaFinSubasta() {
		return fechaFinSubasta;
	}

	public void setFechaFinSubasta(String fechaFinSubasta) {
		this.fechaFinSubasta = fechaFinSubasta;
	}

	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}
	
	public String getPrecioString() {
		return Float.toString(precio);
	}
	
	public Float getMonto() {
		return monto;
	}	

	public void setMonto(Float monto) {
		this.monto = monto;
	}
	
	//incluye indicador de mejor oferta
	public String getMontoString() {
		if (esMayorOferta()) {
			return Float.toString(monto) + " (mejor oferta)";
		} else
			return Float.toString(monto);
	}
	
	public boolean esMayorOferta() {
		return (precio == monto);
	}
	
}
