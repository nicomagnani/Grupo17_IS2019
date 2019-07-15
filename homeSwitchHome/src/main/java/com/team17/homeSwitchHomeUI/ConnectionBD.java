package com.team17.homeSwitchHomeUI;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import homeSwitchHome.EstadoDeReserva;
import homeSwitchHome.Propiedad;
import homeSwitchHome.Reserva;
import homeSwitchHome.ReservaDirecta;
import homeSwitchHome.ReservaHotsale;
import homeSwitchHome.ReservaSubasta;
import homeSwitchHome.Solicitud;
import homeSwitchHome.Tarjeta;
import homeSwitchHome.Usuario;
import homeSwitchHome.UsuarioAdministrador;
import homeSwitchHome.UsuarioComun;
import homeSwitchHome.UsuarioPremium;

public class ConnectionBD {
	Statement stmt;
	PreparedStatement ps;
	Connection con;

	public ConnectionBD() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/homeswitchhome","root","");
			stmt = (Statement) con.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	// Agrupaciones de los métodos (no es 100% preciso)
	//  por tablas	---> por acción
	// 1) propiedad	| 1) agregar
	// 2) reservas	| 2) modificar
	// 3) canceladas| 3) eliminar
	// 4) subastas	| 4) buscar
	// 5) admins	| 5) listar
	// 6) usuarios
	// 7) solicitudes


	public void agregarResidencia(Propiedad p) throws SQLException {
		byte[][] fotos = p.getFotos();
		ByteArrayInputStream bais;
		int col = 8;

		String query = "INSERT INTO propiedad (titulo,descripcion,pais,provincia,localidad,domicilio,monto,foto1,foto2,foto3,foto4,foto5)"
				+" VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,p.getTitulo());
		ps.setString(2,p.getDescripcion());
		ps.setString(3,p.getPais());
		ps.setString(4,p.getProvincia());
		ps.setString(5,p.getLocalidad());
		ps.setString(6,p.getDomicilio());
		ps.setFloat(7,p.getMontoBase());

		for (byte[] foto : fotos) {
			if (foto != null) {
				bais = new ByteArrayInputStream(foto);
				ps.setBinaryStream(col, bais);
			} else
				ps.setNull(col, Types.BLOB);
			col++; //incrementa fuera del if-else para asegurar que se guarde en la pos correcta
		}

		ps.executeUpdate();
		ps.close();
	}


	public void modificarResidencia(Propiedad p, String tituloOriginal, String localidadOriginal) throws SQLException{

		// parte 1: modifico residencia (tabla 'propiedad')
		byte[][] fotos = p.getFotos();
		ByteArrayInputStream bais;
		int col = 8;

		String query = "UPDATE propiedad"
				+ " SET titulo = ?,"
				+ " descripcion = ?,"
				+ " pais = ?,"
				+ " provincia = ?,"
				+ " localidad = ?,"
				+ " domicilio = ?,"
				+ " monto = ?,"
				+ " foto1 = ?,"
				+ " foto2 = ?,"
				+ " foto3 = ?,"
				+ " foto4 = ?,"
				+ " foto5 = ?"
				+ " WHERE titulo = ? AND localidad = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,p.getTitulo());
		ps.setString(2,p.getDescripcion());
		ps.setString(3,p.getPais());
		ps.setString(4,p.getProvincia());
		ps.setString(5,p.getLocalidad());
		ps.setString(6,p.getDomicilio());
		ps.setFloat(7,p.getMontoBase());

		ps.setString(13,tituloOriginal);
		ps.setString(14,localidadOriginal);

		for (byte[] foto : fotos) {
			if (foto != null) {
				bais = new ByteArrayInputStream(foto);
				ps.setBinaryStream(col, bais);
			} else
				ps.setNull(col, Types.BLOB);
			col++; //incrementa fuera del if-else para asegurar que se guarde en la pos correcta
		}

		ps.executeUpdate();
		ps.close();


		// parte 2: modifico referencias en otras tablas ('canceladas', 'reservas', 'subastas')
		// parte 2a: canceladas
		query = "UPDATE canceladas"
				+" SET propiedad = ?, localidad = ?"
				+" WHERE propiedad = ? AND localidad = ?";
		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,p.getTitulo());
		ps.setString(2,p.getLocalidad());
		ps.setString(3,tituloOriginal);
		ps.setString(4,localidadOriginal);

		ps.executeUpdate();
		ps.close();


		// parte 2b1: 'reservas' - semanas sin reservar
		query = "UPDATE reservas"
				+" SET propiedad = ?, localidad = ?, monto = ?"
				+" WHERE propiedad = ? AND localidad = ? AND estado <> 'RESERVADA'";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,p.getTitulo());
		ps.setString(2,p.getLocalidad());
		ps.setFloat(3,p.getMontoBase());
		ps.setString(4,tituloOriginal);
		ps.setString(5,localidadOriginal);

		ps.executeUpdate();
		ps.close();


		// parte 2b2: 'reservas' - semanas reservadas
		query = "UPDATE reservas"
				+" SET propiedad = ?, localidad = ?"
				+" WHERE propiedad = ? AND localidad = ? AND estado = 'RESERVADA'";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,p.getTitulo());
		ps.setString(2,p.getLocalidad());
		ps.setString(3,tituloOriginal);
		ps.setString(4,localidadOriginal);

		ps.executeUpdate();
		ps.close();

		// parte 2c: 'subastas'
		query = "UPDATE subastas"
				+" SET propiedad = ?, localidad = ?"
				+" WHERE propiedad = ? AND localidad = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,p.getTitulo());
		ps.setString(2,p.getLocalidad());
		ps.setString(3,tituloOriginal);
		ps.setString(4,localidadOriginal);

		ps.executeUpdate();
		ps.close();
	}


	public void modificarResidenciaEnSubasta(Propiedad p, String tituloOriginal, String localidad) throws SQLException{

		// parte 1: modifico residencia (tabla 'propiedad')
		byte[][] fotos = p.getFotos();
		ByteArrayInputStream bais;
		int col = 3;

		String query = "UPDATE propiedad"
				+ " SET descripcion = ?,"
				+ " monto = ?,"
				+ " foto1 = ?,"
				+ " foto2 = ?,"
				+ " foto3 = ?,"
				+ " foto4 = ?,"
				+ " foto5 = ?"
				+ " WHERE titulo = ? AND localidad = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,p.getDescripcion());
		ps.setFloat(2,p.getMontoBase());

		for (byte[] foto : fotos) {
			if (foto != null) {
				bais = new ByteArrayInputStream(foto);
				ps.setBinaryStream(col, bais);
			} else
				ps.setNull(col, Types.BLOB);
			col++; //incrementa fuera del if-else para asegurar que se guarde en la pos correcta
		}

		ps.setString(8,tituloOriginal);
		ps.setString(9,localidad);

		ps.executeUpdate();
		ps.close();


		// parte 2: modifico referencias en otras tablas ('reservas' - solo semanas sin reservar)
		query = "UPDATE reservas"
				+" SET monto = ?"
				+" WHERE propiedad = ? AND localidad = ? AND estado <> 'RESERVADA'";

		ps = (PreparedStatement) con.prepareStatement(query);
								   
		ps.setFloat(1,p.getMontoBase());
		ps.setString(2,tituloOriginal);
		ps.setString(3,localidad);

		ps.executeUpdate();
		ps.close();

	}


    public void eliminarResidencia(Propiedad unaResidencia) throws SQLException {

    	String query ="DELETE FROM propiedad WHERE titulo = ? AND localidad = ?";
    	ps = (PreparedStatement) con.prepareStatement(query);

    	ps.setString(1, unaResidencia.getTitulo());
    	ps.setString(2, unaResidencia.getLocalidad());

    	ps.executeUpdate();
    	ps.close();

    	//en caso de haber reservas y/o subastas asociadas a la residencias, las elimino
    	if ( !unaResidencia.getReservas().isEmpty() ) {

    		//elimino reservas
    		query ="DELETE FROM reservas WHERE propiedad = ? AND localidad = ?";
    		ps = (PreparedStatement) con.prepareStatement(query);

        	ps.setString(1, unaResidencia.getTitulo());
        	ps.setString(2, unaResidencia.getLocalidad());

        	ps.executeUpdate();
        	ps.close();

        	//elimino subastas
        	query ="DELETE FROM subastas WHERE propiedad = ? AND localidad = ?";
    		ps = (PreparedStatement) con.prepareStatement(query);

        	ps.setString(1, unaResidencia.getTitulo());
        	ps.setString(2, unaResidencia.getLocalidad());

        	ps.executeUpdate();
        	ps.close();
    	}
    }


	public Propiedad buscarResidencia(String titulo, String localidad) throws SQLException {

		Propiedad propiedad = new Propiedad();

		String query = "SELECT * FROM propiedad WHERE titulo = '"+titulo+"' AND localidad = '"+localidad+"'";
		ResultSet rs = stmt.executeQuery(query);

		if (rs.next()) {
			propiedad.setTitulo(rs.getString("titulo"));
			propiedad.setLocalidad(rs.getString("localidad"));
			propiedad.setDomicilio(rs.getString("domicilio"));
			propiedad.setDescripcion(rs.getString("descripcion"));
			propiedad.setMontoBase(rs.getFloat("monto"));

			propiedad.setFoto1(rs.getBytes("foto1"));
			propiedad.setFoto2(rs.getBytes("foto2"));
			propiedad.setFoto3(rs.getBytes("foto3"));
			propiedad.setFoto4(rs.getBytes("foto4"));
			propiedad.setFoto5(rs.getBytes("foto5"));
		}

		return propiedad;
	}


	public ArrayList<Propiedad> listaResidenciasSinFotos() throws SQLException {

		ArrayList<Propiedad> propiedades = new ArrayList<Propiedad>();

		String query = "SELECT * FROM propiedad";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			Propiedad propiedad = new Propiedad();
			propiedad.setTitulo(rs.getString("titulo"));
			propiedad.setLocalidad(rs.getString("localidad"));
			propiedad.setDomicilio(rs.getString("domicilio"));
			propiedad.setDescripcion(rs.getString("descripcion"));
			propiedad.setMontoBase(rs.getFloat("monto"));
			propiedades.add(propiedad);
	      }
		return propiedades;
	}


	public ArrayList<Propiedad> listaResidenciasConFotos() throws SQLException {

		Propiedad propiedad;

		ArrayList<Propiedad> propiedades = new ArrayList<Propiedad>();

		String query = "SELECT * FROM propiedad";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			propiedad = new Propiedad();
			propiedad.setTitulo(rs.getString("titulo"));
			propiedad.setLocalidad(rs.getString("localidad"));
			propiedad.setDomicilio(rs.getString("domicilio"));
			propiedad.setDescripcion(rs.getString("descripcion"));
			propiedad.setMontoBase(rs.getFloat("monto"));

			propiedad.setFoto1(rs.getBytes("foto1"));
			propiedad.setFoto2(rs.getBytes("foto2"));
			propiedad.setFoto3(rs.getBytes("foto3"));
			propiedad.setFoto4(rs.getBytes("foto4"));
			propiedad.setFoto5(rs.getBytes("foto5"));

			propiedades.add(propiedad);
	      }

		return propiedades;
	}


	//devuelve 5 propiedades al azar
	public ArrayList<Propiedad> listaResidenciasVisitante() throws SQLException {

		ArrayList<Propiedad> propiedades = new ArrayList<Propiedad>();
		Propiedad propiedad;

		String query = "SELECT * FROM propiedad ORDER BY RAND() LIMIT 5;";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			propiedad = new Propiedad();

			propiedad.setTitulo(rs.getString("titulo"));
			propiedad.setLocalidad(rs.getString("localidad"));
			propiedad.setFoto1(rs.getBytes("foto1"));

			propiedades.add(propiedad);
		}
		return propiedades;
	}


	public ArrayList<Propiedad> listaResidenciasPorLugar(String st) throws SQLException {

		ArrayList<Propiedad> propiedades = new ArrayList<Propiedad>();
		ArrayList<Propiedad> propiedades2 = new ArrayList<Propiedad>();
		Propiedad propiedad;

		String query = "SELECT * FROM propiedad WHERE localidad = '"+st+"'";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			propiedad = new Propiedad();
			propiedad.setTitulo(rs.getString("titulo"));
			propiedad.setLocalidad(rs.getString("localidad"));
			propiedad.setDomicilio(rs.getString("domicilio"));
			propiedad.setDescripcion(rs.getString("descripcion"));
			propiedad.setMontoBase(rs.getFloat("monto"));

			propiedad.setFoto1(rs.getBytes("foto1"));

			propiedades.add(propiedad);
		}

		//filtro propiedades sin reservas disponibles
		for (Propiedad p : propiedades) {
			p.setReservas( this.listaReservasPorPropiedad(p.getTitulo(),p.getLocalidad()) );

			if (!p.getReservas().isEmpty()) {
				p.actualizarTiposDeReservasDisponibles();

				if ( (p.isDispDirecta()) || (p.isDispSubasta()) || (p.isDispHotsale()) )
					propiedades2.add(p);
			}
		}

		return propiedades2;
	}


	public ArrayList<Propiedad> listaResidenciasPorFecha(LocalDate fecha1, LocalDate fecha2) throws SQLException {

		ArrayList<Propiedad> propiedades = new ArrayList<Propiedad>();
		ArrayList<Propiedad> propiedades2 = new ArrayList<Propiedad>();
		Propiedad propiedad;

		String query = "SELECT * FROM propiedad";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			propiedad = new Propiedad();
			propiedad.setTitulo(rs.getString("titulo"));
			propiedad.setLocalidad(rs.getString("localidad"));
			propiedad.setDomicilio(rs.getString("domicilio"));
			propiedad.setDescripcion(rs.getString("descripcion"));
			propiedad.setMontoBase(rs.getFloat("monto"));

			propiedad.setFoto1(rs.getBytes("foto1"));

			propiedades.add(propiedad);
		}

		//filtro propiedades sin reservas disponibles
		for (Propiedad p : propiedades) {
			p.setReservas(listaReservasPorPropiedad(p.getTitulo(),p.getLocalidad()));

			if (p.hayReservaEntreFechas(fecha1,fecha2)) {
				propiedades2.add(p);
			}
		}

		return propiedades2;
	}


	public void realizarReservaDirecta(Reserva r) throws SQLException {

		//parte 1, se actualiza tipo de reserva
		String query = "UPDATE reservas"
				+" SET usuario = ?, tipo = ?, estado = ?"
				+" WHERE propiedad = ? AND localidad = ? AND fecha_inicio = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,r.getUsuario());
		ps.setString(2,"directa");
		ps.setString(3,"RESERVADA");
		ps.setString(4,r.getPropiedad());
		ps.setString(5,r.getLocalidad());
		ps.setDate(6, Date.valueOf(r.getFechaInicio()));

		ps.executeUpdate();
		ps.close();
	}
	

	public void realizarReservaHotsale(ReservaHotsale rh) throws SQLException {
		
		//parte 1, se actualiza tipo de reserva
		String query = "UPDATE reservas"
				+" SET usuario = ?, tipo = ?, estado = ?"
				+" WHERE propiedad = ? AND localidad = ? AND fecha_inicio = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,rh.getUsuario());
		ps.setString(2,"hotsale");
		ps.setString(3,"RESERVADA");
		ps.setString(4,rh.getPropiedad());
		ps.setString(5,rh.getLocalidad());
		ps.setDate(6, Date.valueOf(rh.getFechaInicio()));

		ps.executeUpdate();
		ps.close();		
	}
	
	
	public ReservaSubasta buscarReservaDeSubasta(ReservaSubasta r) throws SQLException {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String fechaComoString = r.getFechaInicio().format(formatter);

		String query = "SELECT * FROM reservas WHERE propiedad = '"+r.getPropiedad()+"' AND localidad = '"
				+r.getLocalidad()+"' AND fecha_inicio = '"+fechaComoString+"'";
		ResultSet rs = stmt.executeQuery(query);

		if (rs.next()) {
			r.setMonto(rs.getFloat("monto"));
			r.setEstado(EstadoDeReserva.valueOf(rs.getString("estado")));
		}
			
		return r;
	}


	public ArrayList<Reserva> listaReservas() throws SQLException {

		ArrayList<Reserva> reservas = new ArrayList<Reserva>();
		Reserva reserva = null;
		ReservaDirecta reservaDirecta;
		ReservaSubasta reservaSubasta;
		ReservaHotsale reservaHotsale;

		String query = "SELECT * FROM reservas";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			String tipo = rs.getString("tipo");
			if (tipo.equals("directa")) {
				reservaDirecta = new ReservaDirecta();
				//asigno campos exclusivos de ReservaDirecta
				reserva = reservaDirecta;
			} else
				if (tipo.equals("subasta")) {
					reservaSubasta = new ReservaSubasta();
					//asigno campos exclusivos de ReservaSubasta
					reserva = reservaSubasta;
				} else
					if (tipo.equals("hotsale")) {
						reservaHotsale = new ReservaHotsale();
						//asigno campos exclusivos de ReservaHotsale
						reserva = reservaHotsale;
					}

			reserva.setPropiedad(rs.getString("propiedad"));
			reserva.setLocalidad(rs.getString("localidad"));
			reserva.setUsuario(rs.getString("usuario"));
			reserva.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
			reserva.setEstado(EstadoDeReserva.valueOf(rs.getString("estado")));
			reserva.setMonto(rs.getFloat("monto"));

			reservas.add(reserva);
		}

		return reservas;
	}


	public ArrayList<Reserva> listaReservasPorUsuario(String mail) throws SQLException {

		ArrayList<Reserva> reservas = new ArrayList<Reserva>();
		Reserva reserva = null;
		ReservaDirecta reservaDirecta;
		ReservaSubasta reservaSubasta;
		ReservaHotsale reservaHotsale;

		String query = "SELECT * FROM reservas WHERE usuario = '"+mail+"'";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			String tipo = rs.getString("tipo");
			if (tipo.equals("directa")) {
				reservaDirecta = new ReservaDirecta();
				//asigno campos exclusivos de ReservaDirecta
				reserva = reservaDirecta;
			} else
				if (tipo.equals("subasta")) {
					reservaSubasta = new ReservaSubasta();
					//asigno campos exclusivos de ReservaSubasta
					reserva = reservaSubasta;
				} else
					if (tipo.equals("hotsale")) {
						reservaHotsale = new ReservaHotsale();
						//asigno campos exclusivos de ReservaHotsale
						reserva = reservaHotsale;
					}

			reserva.setPropiedad(rs.getString("propiedad"));
			reserva.setLocalidad(rs.getString("localidad"));
			reserva.setUsuario(rs.getString("usuario"));
			reserva.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
			reserva.setEstado(EstadoDeReserva.valueOf(rs.getString("estado")));
			reserva.setMonto(rs.getFloat("monto"));

			reservas.add(reserva);
		}

		return reservas;
	}


	public ArrayList<Reserva> listaReservasPorPropiedad(String titulo, String localidad) throws SQLException {

		ArrayList<Reserva> reservas = new ArrayList<Reserva>();
		Reserva reserva = null;
		ReservaDirecta reservaDirecta;
		ReservaSubasta reservaSubasta;
		ReservaHotsale reservaHotsale;

		String query = "SELECT * FROM reservas WHERE propiedad = '"+titulo+"' AND localidad = '"+localidad+"'";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			String tipo = rs.getString("tipo");
			if (tipo.equals("directa")) {
				reservaDirecta = new ReservaDirecta();
				//asigno campos exclusivos de ReservaDirecta
				reserva = reservaDirecta;
			} else
				if (tipo.equals("subasta")) {
					reservaSubasta = new ReservaSubasta();
					//asigno campos exclusivos de ReservaSubasta
					reserva = reservaSubasta;
				} else
					if (tipo.equals("hotsale")) {
						reservaHotsale = new ReservaHotsale();
						//asigno campos exclusivos de ReservaHotsale
						reserva = reservaHotsale;
					}

			reserva.setPropiedad(rs.getString("propiedad"));
			reserva.setLocalidad(rs.getString("localidad"));
			reserva.setUsuario(rs.getString("usuario"));
			reserva.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
			reserva.setEstado(EstadoDeReserva.valueOf(rs.getString("estado")));
			reserva.setMonto(rs.getFloat("monto"));

			reservas.add(reserva);
		}

		return reservas;
	}


	public ArrayList<Reserva> listaCanceladasPorUsuario(String mail) throws SQLException {

		ArrayList<Reserva> reservas = new ArrayList<Reserva>();
		Reserva reserva = null;
		ReservaDirecta reservaDirecta;
		ReservaSubasta reservaSubasta;
		ReservaHotsale reservaHotsale;

		String query = "SELECT * FROM canceladas WHERE usuario = '"+mail+"'";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			String tipo = rs.getString("tipo");
			if (tipo.equals("directa")) {
				reservaDirecta = new ReservaDirecta();
				//asigno campos exclusivos de ReservaDirecta
				reserva = reservaDirecta;
			} else
				if (tipo.equals("subasta")) {
					reservaSubasta = new ReservaSubasta();
					//asigno campos exclusivos de ReservaSubasta
					reserva = reservaSubasta;
				} else
					if (tipo.equals("hotsale")) {
						reservaHotsale = new ReservaHotsale();
						//asigno campos exclusivos de ReservaHotsale
						reserva = reservaHotsale;
					}

			reserva.setPropiedad(rs.getString("propiedad"));
			reserva.setLocalidad(rs.getString("localidad"));
			reserva.setUsuario(rs.getString("usuario"));
			reserva.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
			reserva.setEstado(EstadoDeReserva.valueOf(rs.getString("estado")));
			reserva.setMonto(rs.getFloat("monto"));

			reservas.add(reserva);
		}

		return reservas;
	}


	public void abrirHotsale(Reserva r, Float montoFinal) throws SQLException {
		
		String query = "UPDATE reservas"
				+" SET tipo = 'hotsale', estado = 'DISPONIBLE', monto = ?"
				+" WHERE propiedad = ? AND localidad = ? AND fecha_inicio = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setFloat(1,montoFinal);
		ps.setString(2,r.getPropiedad());
		ps.setString(3,r.getLocalidad());
		ps.setDate(4, Date.valueOf(r.getFechaInicio()));

		ps.executeUpdate();
		ps.close();		
	}
	

	public void cerrarHotsale(ReservaHotsale rh) throws SQLException {
		
		String query = "UPDATE reservas"
				+" SET estado = 'EN_ESPERA'"
				+" WHERE propiedad = ? AND localidad = ? AND fecha_inicio = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,rh.getPropiedad());
		ps.setString(2,rh.getLocalidad());
		ps.setDate(3, Date.valueOf(rh.getFechaInicio()));
		
		ps.executeUpdate();
		ps.close();		
	}
	

	public void abrirSubasta(Reserva r) throws SQLException {

		//parte 1, se actualiza tabla reserva
		String query = "UPDATE reservas"
				+ " SET tipo = 'subasta', estado = 'DISPONIBLE'"
				+" WHERE propiedad = ? AND localidad = ? AND fecha_inicio = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,r.getPropiedad());
		ps.setString(2,r.getLocalidad());
		ps.setDate(3, Date.valueOf(r.getFechaInicio()));

		ps.executeUpdate();
		ps.close();

		//parte 2, se agrega subasta a la tabla subastas
		query = "INSERT INTO subastas (propiedad, localidad, fecha_inicio, fecha_subasta)"
				+" VALUES (?,?,?,?)";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,r.getPropiedad());
		ps.setString(2,r.getLocalidad());
		ps.setDate(3, Date.valueOf(r.getFechaInicio()));
		ps.setDate(4, Date.valueOf(LocalDate.now()));

		ps.executeUpdate();
		ps.close();
	}


	public void cerrarSubastaConGanador(ReservaSubasta res) throws SQLException {

		//parte 1, se actualiza tabla reserva
		String query = "UPDATE reservas"
				+" SET usuario = ?, estado = 'RESERVADA', monto = ?"
				+" WHERE propiedad = ? AND localidad = ? AND fecha_inicio = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,res.getUsuarios().get(0));
		ps.setFloat(2,res.getMontos().get(0));
		ps.setString(3,res.getPropiedad());
		ps.setString(4,res.getLocalidad());
		ps.setDate(5,Date.valueOf(res.getFechaInicio()));

		ps.executeUpdate();
		ps.close();


		//parte 2, se actualiza subasta (en caso de que se hayan eliminado ofertas inválidas)
		query = "UPDATE subastas"
				+" SET montos = ?, usuarios = ?"
				+" WHERE propiedad = ? AND localidad = ? AND fecha_inicio = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,res.getMontosString());
		ps.setString(2,res.getUsuariosString());
		ps.setString(3,res.getPropiedad());
		ps.setString(4,res.getLocalidad());
		ps.setDate(5, Date.valueOf(res.getFechaInicio()));

		ps.executeUpdate();
		ps.close();
	}


	public void cerrarSubastaSinGanador(ReservaSubasta res) throws SQLException {

		//parte 1, se actualiza tabla reserva
		String query = "UPDATE reservas"
				+" SET tipo = 'hotsale', estado = 'EN_ESPERA'"
				+" WHERE propiedad = ? AND localidad = ? AND fecha_inicio = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,res.getPropiedad());
		ps.setString(2,res.getLocalidad());
		ps.setDate(3, Date.valueOf(res.getFechaInicio()));

		ps.executeUpdate();
		ps.close();


		//parte 2, se actualiza subasta (en caso de que se hayan eliminado ofertas inválidas)
		query = "UPDATE subastas"
				+" SET montos = ?, usuarios = ?"
				+" WHERE propiedad = ? AND localidad = ? AND fecha_inicio = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		if (res.getMontosString().equals("")) {
			ps.setString(1, null);
		} else ps.setString(1,res.getMontosString());
		
		if (res.getUsuariosString().equals("")) {
			ps.setString(1, null);
		} else ps.setString(2,res.getUsuariosString());			
			
		ps.setString(3,res.getPropiedad());
		ps.setString(4,res.getLocalidad());
		ps.setDate(5, Date.valueOf(res.getFechaInicio()));

		ps.executeUpdate();
		ps.close();
	}


	public void modificarSubasta(ReservaSubasta rs) throws SQLException {

		String montosString = "", usuariosString = "";

		for ( Float m : rs.getMontos() )
			montosString += String.valueOf(m) + " ";


		for ( String u : rs.getUsuarios() )
			usuariosString += u + " ";
		
		String query = "UPDATE subastas"
				+ " SET montos = ?, usuarios = ?"
				+" WHERE propiedad = ? AND localidad = ? AND fecha_inicio = ?";

		ps = (PreparedStatement) con.prepareStatement(query);

		if (montosString.equals("")) {
			ps.setString(1, null);
		} else ps.setString(1,montosString);
		
		if (usuariosString.equals("")) {
			ps.setString(1, null);
		} else ps.setString(2,usuariosString);
		
		ps.setString(2,usuariosString);
		ps.setString(3,rs.getPropiedad());
		ps.setString(4,rs.getLocalidad());
		ps.setDate(5, Date.valueOf(rs.getFechaInicio()));

		ps.executeUpdate();
		ps.close();
	}


	public ReservaSubasta buscarSubasta(String propiedad, String localidad, LocalDate fechaInicio, EstadoDeReserva estado, Float montoOriginal) throws SQLException {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String fechaComoString = fechaInicio.format(formatter);

		String query = "SELECT * FROM subastas WHERE propiedad = '"+propiedad+"' AND localidad = '"
		+localidad+"' AND fecha_inicio = '"+fechaComoString+"'";
		ResultSet rs = stmt.executeQuery(query);

		ReservaSubasta reserva = new ReservaSubasta();
		String preMontos;
		String[] preMontosArray;
		String preUsuarios;
		ArrayList<Float> montos;

		while (rs.next()) {
			reserva.setPropiedad(rs.getString("propiedad"));
			reserva.setLocalidad(rs.getString("localidad"));
			reserva.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
			reserva.setFechaInicioSubasta(rs.getDate("fecha_subasta").toLocalDate());
			reserva.setEstado(estado);
			reserva.setMonto(montoOriginal);

			preMontos = rs.getString("montos");
			if (preMontos != null) {
				preMontosArray = preMontos.split("\\s+");
				montos = new ArrayList<>();
				for (String st : preMontosArray)
					montos.add(Float.parseFloat(st));
				reserva.setMontos(montos);
			}

			preUsuarios = rs.getString("usuarios");
			//usuarios (los ofertantes) se inicializa como null, en caso de no haber ofertas no hace falta asignarlo
			if (preUsuarios != null) {
				reserva.setUsuarios( new ArrayList<>(Arrays.asList(preUsuarios.split("\\s+"))) );
			}
		}

		return reserva;
	}


	public ArrayList<ReservaSubasta> listaSubastas() throws SQLException {

		ArrayList<ReservaSubasta> reservas = new ArrayList<>();
		ReservaSubasta reserva;
		String preMontos;
		String[] preMontosArray;
		String preUsuarios;
		ArrayList<Float> montos;

		String query = "SELECT * FROM subastas";
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			reserva = new ReservaSubasta();
			montos = new ArrayList<>();

			reserva.setPropiedad(rs.getString("propiedad"));
			reserva.setLocalidad(rs.getString("localidad"));
			reserva.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
			reserva.setFechaInicioSubasta(rs.getDate("fecha_subasta").toLocalDate());

			preMontos = rs.getString("montos");
			if (preMontos != null) {
				preMontosArray = preMontos.split("\\s+");
				for (String st : preMontosArray)
					montos.add(Float.parseFloat(st));
				reserva.setMontos(montos);
			}

			preUsuarios = rs.getString("usuarios");
			//usuarios (los ofertantes) se inicializa como null, en caso de no haber ofertas no hace falta asignarlo
			if (preUsuarios != null) {
				reserva.setUsuarios( new ArrayList<>(Arrays.asList(preUsuarios.split("\\s+"))) );
			}

			reservas.add(reserva);
		}

		return reservas;
	}


	//metodo que devuelve los admins de la bd
	public ArrayList<UsuarioAdministrador> listaAdmins() throws SQLException {

		String query = "SELECT * FROM administradores";
		ResultSet rs = stmt.executeQuery(query);

		ArrayList<UsuarioAdministrador> admins = new ArrayList<UsuarioAdministrador>();
		UsuarioAdministrador admin = new UsuarioAdministrador();

		while (rs.next()) {
			admin.setMail(rs.getString("mail"));
			admin.setContraseña(rs.getString("contraseña"));
			admins.add(admin);
	     }
		return admins;
	}


	public void agregarUsuario(UsuarioComun uc) throws SQLException {
		Tarjeta tarjeta = uc.getTarjeta();

		String query = "INSERT INTO usuarios (mail, contraseña, nombre, apellido, f_nac, creditos,"
				+ " nro_tarj, marca_tarj, titu_tarj, venc_tarj, cod_tarj)"
				+" VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1, uc.getMail());
		ps.setString(2, uc.getContraseña());
		ps.setString(3, uc.getNombre());
		ps.setString(4, uc.getApellido());
		ps.setDate(5, Date.valueOf(uc.getfNac()));
		ps.setShort(6, uc.getCreditos());
		ps.setLong(7, tarjeta.getNumero());
		ps.setString(8, tarjeta.getMarca());
		ps.setString(9, tarjeta.getTitular());
		ps.setDate(10, Date.valueOf(tarjeta.getfVenc()));
		ps.setShort(11, tarjeta.getCodigo());

	    ps.executeUpdate();
		ps.close();
	}


	public void modificarUsuarioDatos(String mailOriginal, String mailNuevo, String nombre, String apellido, LocalDate fechaNacimiento) throws SQLException {

		// parte 1: modifica usuario (tabla 'usuarios')
		String query = "UPDATE usuarios"
				+ " SET mail = ?, nombre = ?, apellido = ?, f_nac = ?"
				+ " WHERE mail = ?";
		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,mailNuevo);
		ps.setString(2,nombre);
		ps.setString(3,apellido);
		ps.setDate(4,Date.valueOf(fechaNacimiento));
		ps.setString(5,mailOriginal);

		ps.executeUpdate();
		ps.close();


		// parte 2: modifica referencias en otras tablas ('canceladas', 'reservas', 'solicitudes', 'subastas')
		// parte 2a: canceladas
		query = "UPDATE canceladas"
				+ " SET usuario = ?"
				+ " WHERE usuario = ?";
		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,mailNuevo);
		ps.setString(2,mailOriginal);

		ps.executeUpdate();
		ps.close();


		// parte 2b: reservas
		query = "UPDATE reservas"
				+ " SET usuario = ?"
				+ " WHERE usuario = ?";
		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,mailNuevo);
		ps.setString(2,mailOriginal);

		ps.executeUpdate();
		ps.close();


		// parte 2c: solicitudes
		query = "UPDATE solicitudes"
				+ " SET usuario = ?"
				+ " WHERE usuario = ?";
		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1,mailNuevo);
		ps.setString(2,mailOriginal);

		ps.executeUpdate();
		ps.close();


		// parte 2d: subastas. para cada subasta, obtengo la lista de usuarios, la modifico y luego reenvio la lista
		ArrayList<ReservaSubasta> subastas = this.listaSubastas();
		ArrayList<String> usuarios;
		boolean ok;

		for (ReservaSubasta res : subastas) {
			usuarios = res.getUsuarios();
			ok = false;
			if (usuarios != null)
				for (int i=0; i<usuarios.size(); i++) {
					if (usuarios.get(i).equals(mailOriginal)) {
						usuarios.remove(i);
						usuarios.add(i, mailNuevo);
						ok = true;
					}
				}
			if (ok)
				this.modificarSubasta(res);
		}
	}


	public void modificarUsuarioTarjeta(long numTarj, String marca, String titular, LocalDate fechaVencimiento, short numSeg, String mail) throws SQLException {

		String query = "UPDATE usuarios "
				+ "SET nro_tarj = ?, marca_tarj = ?, titu_tarj = ?, venc_tarj = ?, cod_tarj = ? "
				+ "WHERE mail = '"+mail+"'";
		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setLong(1, numTarj);
		ps.setString(2, marca);
		ps.setString(3, titular);
		ps.setDate(4, Date.valueOf(fechaVencimiento));
		ps.setLong(5, numSeg);

		ps.executeUpdate();
		ps.close();
	}


	public void modificarUsuarioContraseña(String mail, String contraseña) throws SQLException {

		String query ="UPDATE usuarios"
				+ " SET contraseña = ?"
				+ " WHERE mail = ?";
		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1, contraseña);
		ps.setString(2, mail);

		ps.executeUpdate();
		ps.close();
	}


	public void modificarUsuarioCreditos(String mail, String operacion, int cantidad) throws SQLException {

		String modificador = operacion+" "+cantidad;


		String query ="UPDATE usuarios"
				+ " SET creditos = creditos " + modificador
				+ " WHERE mail = '"+mail+"'";
		ps = (PreparedStatement) con.prepareStatement(query);

		ps.executeUpdate();
		ps.close();
	}


	public void modificarDeUsuarioTipo(String mail, String tipo) throws SQLException {

		boolean t2 = tipo.equals("alta");

		String query = "UPDATE usuarios"
				+ " SET premium = "+t2
				+ " WHERE mail = '"+mail+"'";
		ps = (PreparedStatement) con.prepareStatement(query);

		ps.executeUpdate();
		ps.close();
	}


	public Usuario buscarUsuario(String mail) throws SQLException {

		String query = "SELECT * FROM usuarios WHERE mail = '"+mail+"'";
		ResultSet rs = stmt.executeQuery(query);

		Usuario usuario = null;
		Tarjeta tarjeta = new Tarjeta();

		while (rs.next()) {
			if (rs.getBoolean("premium")) {
				usuario = new UsuarioPremium();
			} else {
				usuario = new UsuarioComun();
			}
			usuario.setMail(rs.getString("mail"));
			usuario.setContraseña(rs.getString("contraseña"));
			usuario.setNombre(rs.getString("nombre"));
			usuario.setApellido(rs.getString("apellido"));
			usuario.setfNac((rs.getDate("f_nac").toLocalDate()));
			usuario.setCreditos(rs.getShort("creditos"));

			tarjeta.setNumero(rs.getLong("nro_tarj"));
			tarjeta.setMarca(rs.getString("marca_tarj"));
			tarjeta.setTitular(rs.getString("titu_tarj"));
			tarjeta.setfVenc(rs.getDate("venc_tarj").toLocalDate());
			tarjeta.setCodigo(rs.getShort("cod_tarj"));
			usuario.setTarjeta(tarjeta);
		}

		return usuario;
	}


	public ArrayList<Usuario> listaUsuarios() throws SQLException {

		String query = "SELECT * FROM usuarios";
		ResultSet rs = stmt.executeQuery(query);

		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		Usuario usuario;
		Tarjeta tarjeta = new Tarjeta();

		while (rs.next()) {
			tarjeta = new Tarjeta();
			
			if (!rs.getBoolean("premium")) {
				usuario = new UsuarioComun();
			} else {
				usuario = new UsuarioPremium();
			}
			usuario.setMail(rs.getString("mail"));
			usuario.setContraseña(rs.getString("contraseña"));
			usuario.setNombre(rs.getString("nombre"));
			usuario.setApellido(rs.getString("apellido"));
			usuario.setfNac((rs.getDate("f_nac").toLocalDate()));
			usuario.setCreditos(rs.getShort("creditos"));

			tarjeta.setNumero(rs.getLong("nro_tarj"));
			tarjeta.setMarca(rs.getString("marca_tarj"));
			tarjeta.setTitular(rs.getString("titu_tarj"));
			tarjeta.setfVenc(rs.getDate("venc_tarj").toLocalDate());
			tarjeta.setCodigo(rs.getShort("cod_tarj"));
			usuario.setTarjeta(tarjeta);

			usuarios.add(usuario);
		}

		return usuarios;
	}


	public void agregarSolicitud(Solicitud s) throws SQLException {

		String query = "INSERT INTO solicitudes (usuario, tipo, motivo)"
				+" VALUES (?,?,?)";

		ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1, s.getMail());
		ps.setString(2, s.getTipo());
		ps.setString(3, s.getMotivo());

	    ps.executeUpdate();
		ps.close();
	}


	public void eliminarSolicitud(String mail) throws SQLException {

		String query ="DELETE FROM solicitudes"
				+ " WHERE usuario = ?";

    	ps = (PreparedStatement) con.prepareStatement(query);

    	ps.setString(1, mail);

    	ps.executeUpdate();
    	ps.close();
	}


	public boolean existeSolicitud(String mail) throws SQLException {

		String query = "SELECT * FROM solicitudes WHERE usuario = '"+mail+"' LIMIT 1";
		ResultSet rs = stmt.executeQuery(query);

		return rs.next();
	}


	public ArrayList<Solicitud> listaSolicitudes() throws SQLException {

		String query = "SELECT * FROM solicitudes";
		ResultSet rs = stmt.executeQuery(query);

		ArrayList<Solicitud> solicitudes = new ArrayList<>();
		Solicitud solicitud;

		while (rs.next()) {

			solicitud = new Solicitud();
			solicitud.setMail(rs.getString("usuario"));
			solicitud.setTipo(rs.getString("tipo"));
			solicitud.setMotivo(rs.getString("motivo"));

			solicitudes.add(solicitud);
		}

		return solicitudes;
	}



}

