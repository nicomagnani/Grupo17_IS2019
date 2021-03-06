package homeSwitchHome;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReservaSubasta extends Reserva {
	
	//NOTA: si la subasta no recibio ofertas, ambas listas estarán vacías
	private ArrayList<Float> montos = null; //contiene la historia del monto de la subasta (empezando por el monto actual)
	private ArrayList<String> usuarios = null; //contiene la historia de usuarios con una oferta válida (empezando por el ofertante actual)
	private LocalDate fechaInicioSubasta;
	
	public ReservaSubasta() {
		super();
	}
	
	public ReservaSubasta(LocalDate fechaInicio, String propiedad, EstadoDeReserva estado) {
		super(propiedad, fechaInicio, estado);
	}
	
	public ReservaSubasta(String propiedad, String usuario, LocalDate fechaInicio, EstadoDeReserva estado) {
		super(propiedad, usuario, fechaInicio, estado);
	}
	
	@Override
	public float getMonto() {
		if ( (getEstado() == EstadoDeReserva.DISPONIBLE)
				&& (montos != null) && (!montos.isEmpty())
				&& (montos.get(0) > super.getMonto()) ) {
			return montos.get(0);
		} else
			return super.getMonto();		
	}
	
	public ArrayList<Float> getMontos() {
		return montos;
	}

	public void setMontos(ArrayList<Float> montos) {
		this.montos = montos;
	}

	public ArrayList<String> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<String> usuarios) {
		this.usuarios = usuarios;
	}	
	
	//para enviar a la BD
	public String getMontosString() {
	
		String st = "";
		if ( (usuarios != null) && (!usuarios.isEmpty()) )
			for (Float m : montos)
				st += String.valueOf(m)+" ";
		
		//elimino espacio al final
		if (!st.equals(""))
			st = st.substring(0, st.length()-1);
		
		return st;
	}
	
	//para enviar a la BD
	public String getUsuariosString() {
		
		String st = "";
		if ( (usuarios != null) && (!usuarios.isEmpty()) )
			for (String u : usuarios)
				st += u+" ";
		
		//elimino espacio al final
		if (!st.equals(""))
			st = st.substring(0, st.length()-1);			
		
		return st;		
	}

	public LocalDate getFechaInicioSubasta() {
		return fechaInicioSubasta;
	}

	public void setFechaInicioSubasta(LocalDate fechaInicioSubasta) {
		this.fechaInicioSubasta = fechaInicioSubasta;
	}
	
	public LocalDate getFechaFinSubasta() {
		return this.getFechaInicioSubasta().plusDays(3);
	}
	
	public String getFechaFinSubastaString() {
		
		if (this.getEstado() == EstadoDeReserva.DISPONIBLE) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		
			if ( LocalDate.now().isBefore(this.getFechaFinSubasta()) ) {
				return this.getFechaFinSubasta().atStartOfDay().format(formatter) + " hs.";
			} else
				return this.getFechaFinSubasta().atStartOfDay().format(formatter) + " hs." + " (Finalizando)";
		} else
			return "Finalizada";
	}

	public String getTipo() {
		return "Subasta";
	}
	
	//debe verificarse previamente que cumpla los requisitos
	public String getOfertaGanadora() {
		if ( (montos != null) && (montos.size() > 1) ) {
			return usuarios.get(0)+" ($"+montos.get(0)+")";
		} else
			return "Sin ganador";
	}
	
	//elimina una oferta de la subasta (por ejemplo, si fue cancelada por el usuario o porque no es válida)
	public void eliminarOferta(int pos) {
		montos.remove(pos);
		usuarios.remove(pos);
	}	

}
