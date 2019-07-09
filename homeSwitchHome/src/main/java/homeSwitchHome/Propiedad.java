package homeSwitchHome;

import java.time.LocalDate;
import java.util.ArrayList;

public class Propiedad {

	private String titulo,pais,provincia,localidad,domicilio,descripcion;
	private float montoBase;
	private byte[][] fotos = new byte[5][];
	
	//campos para almacenar y acceder a las fotos individualmente
	private byte[] foto1;
	private byte[] foto2;
	private byte[] foto3;
	private byte[] foto4;
	private byte[] foto5;
	
	private ArrayList<Reserva> reservas;
	
	//es importante actualizarlos mediante hayReservaEntreFechas o actualizarTiposDeReservasDisponibles
	private boolean dispDirecta;
	private boolean dispSubasta;
	private boolean dispHotsale;
	
	
	public Propiedad() {
		
	}

	public Propiedad(String titulo, String pais, String provincia, String localidad, String domicilio,
			String descripcion, float montoBase, byte[][] fotos) {

		this.titulo = titulo;
		this.pais = pais;
		this.provincia = provincia;
		this.localidad = localidad;
		this.domicilio = domicilio;
		this.descripcion = descripcion;
		this.montoBase = montoBase;
		this.fotos = fotos;
	}

	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public float getMontoBase() {
		return montoBase;
	}

	public void setMontoBase(float montoBase) {
		this.montoBase = montoBase;
	}

	public byte[][] getFotos() {
		return fotos;
	}

	public void setFotos(byte[][] fotos) {
		this.fotos = fotos;
	}
	
	public byte[] getFoto1() {
		return foto1;
	}

	public void setFoto1(byte[] foto1) {
		this.foto1 = foto1;
	}

	public byte[] getFoto2() {
		return foto2;
	}

	public void setFoto2(byte[] foto2) {
		this.foto2 = foto2;
	}

	public byte[] getFoto3() {
		return foto3;
	}

	public void setFoto3(byte[] foto3) {
		this.foto3 = foto3;
	}

	public byte[] getFoto4() {
		return foto4;
	}

	public void setFoto4(byte[] foto4) {
		this.foto4 = foto4;
	}

	public byte[] getFoto5() {
		return foto5;
	}

	public void setFoto5(byte[] foto5) {
		this.foto5 = foto5;
	}

	public ArrayList<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(ArrayList<Reserva> reservas) {
		this.reservas = reservas;
	}

	public boolean isDispDirecta() {
		return dispDirecta;
	}

	public void setDispDirecta(boolean disponibleDirecta) {
		this.dispDirecta = disponibleDirecta;
	}

	public boolean isDispSubasta() {
		return dispSubasta;
	}

	public void setDispSubasta(boolean disponibleSubasta) {
		this.dispSubasta = disponibleSubasta;
	}

	public boolean isDispHotsale() {
		return dispHotsale;
	}

	public void setDispHotsale(boolean disponibleHotsale) {
		this.dispHotsale = disponibleHotsale;
	}
	
	
	public boolean hayReservaEnFecha(LocalDate fecha) {
		
		LocalDate[] r;
		boolean ok = false;
		
		if (reservas != null)
			for (int i=0; ((i < reservas.size()) && (!ok)); i++) {
				r = reservas.get(0).getFechasTiempoCompartido();
				if ( (!fecha.isBefore(r[0])) && (!fecha.isAfter(r[1])) )
					ok = true;
			}
		
		return ok;
	}
	
			
	public boolean hayReservaEntreFechas(LocalDate fecha1, LocalDate fecha2) {
		
		dispDirecta = false;
		dispSubasta = false;
		dispHotsale = false;		
		
		LocalDate[] f;
		
		if (reservas != null)
			for (Reserva res : reservas) {
				f = res.getFechasTiempoCompartido();
				
				//si existen reservas de todos los tipos, ya no necesita recorrer la lista de reservas
				if (dispDirecta && dispSubasta && dispHotsale) {
					return true;			
				}
				
				//si hay intersección entre ambos rangos de fechas
				if ( (!fecha1.isAfter(f[1])) && (!fecha2.isBefore(f[0])) ) 
					if (res.getEstado() == EstadoDeReserva.DISPONIBLE_DIRECTA) {
						dispDirecta = true;
					} else 
						if (res.getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA) {
							dispSubasta = true;
						} else
							if (res.getEstado() == EstadoDeReserva.DISPONIBLE_HOTSALE) {
								dispHotsale = true;
							}
			}
		
		return (dispDirecta || dispSubasta || dispHotsale);
	}
	
	
	public void actualizarTiposDeReservasDisponibles() {
		
		dispDirecta = false;
		dispSubasta = false;
		dispHotsale = false;
		
		if (reservas != null)
			for (Reserva res : reservas) {
				
				//si existen reservas de todos los tipos, ya no necesita recorrer la lista de reservas
				if (dispDirecta && dispSubasta && dispHotsale) {
					break;			
				}
				
				//si hay intersección entre ambos rangos de fechas
				if (res.getEstado() == EstadoDeReserva.DISPONIBLE_DIRECTA) {
						dispDirecta = true;
					} else 
						if (res.getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA) {
							dispSubasta = true;
						} else
							if (res.getEstado() == EstadoDeReserva.DISPONIBLE_HOTSALE) {
								dispHotsale = true;
							}
			}
	}
	
	
	public boolean hayReservasRealizadas() {
		
		for (Reserva reserva : reservas) {
			if (reserva.getEstado() == EstadoDeReserva.RESERVADA) {
				return true;
			}
		}		
		return false;		
	}
	
	
	public boolean haySubastasEncurso() {
		
		for (Reserva reserva : reservas) {
			if (reserva.getEstado() == EstadoDeReserva.DISPONIBLE_SUBASTA) {
				return true;
			}
		}
		return false;
	}
	
}
