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

	public ArrayList<Propiedad> listaPropiedadesSinFotos() throws SQLException {

		ArrayList<Propiedad> propiedades = new ArrayList<Propiedad>();

		String query = "SELECT * FROM propiedad";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			Propiedad propiedad = new Propiedad();
			propiedad.setTitulo(rs.getString("titulo"));
			propiedad.setPais(rs.getString("pais"));
			propiedad.setProvincia(rs.getString("provincia"));
			propiedad.setLocalidad(rs.getString("localidad"));
			propiedad.setDomicilio(rs.getString("domicilio"));
			propiedad.setDescripción(rs.getString("descripcion"));
			propiedad.setMontoBase(rs.getInt("monto"));
			propiedades.add(propiedad);
	      }
		return propiedades;
	}

	
	public ArrayList<Propiedad> listaPropiedadesConFotos() throws SQLException {

		Propiedad propiedad;

		ArrayList<Propiedad> propiedades = new ArrayList<Propiedad>();

		String query = "SELECT * FROM propiedad";
		ResultSet rs = stmt.executeQuery(query);
		
		while (rs.next()) {
			propiedad = new Propiedad();
			propiedad.setTitulo(rs.getString("titulo"));
			propiedad.setPais(rs.getString("pais"));
			propiedad.setProvincia(rs.getString("provincia"));
			propiedad.setLocalidad(rs.getString("localidad"));
			propiedad.setDomicilio(rs.getString("domicilio"));
			propiedad.setDescripción(rs.getString("descripcion"));
			propiedad.setMontoBase(rs.getInt("monto"));
			
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
	public ArrayList<Propiedad> listaPropiedadesVisitante() throws SQLException {
		
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
	
	
	public ArrayList<Propiedad> listaPropiedadesPorLugar(String st) throws SQLException {
		
		ArrayList<Propiedad> propiedades = new ArrayList<Propiedad>();
		ArrayList<Propiedad> propiedades2 = new ArrayList<Propiedad>();
		Propiedad propiedad;
		
		String query = "SELECT * FROM propiedad WHERE localidad = '"+st+"'";
		ResultSet rs = stmt.executeQuery(query);
		 
		while (rs.next()) {
			propiedad = new Propiedad();
			propiedad.setTitulo(rs.getString("titulo"));
			propiedad.setPais(rs.getString("pais"));
			propiedad.setProvincia(rs.getString("provincia"));
			propiedad.setLocalidad(rs.getString("localidad"));
			propiedad.setDomicilio(rs.getString("domicilio"));
			propiedad.setDescripción(rs.getString("descripcion"));
			propiedad.setMontoBase(rs.getInt("monto"));
			
			propiedad.setFoto1(rs.getBytes("foto1"));				

			propiedades.add(propiedad);
		}			
		
		//filtro propiedades sin reservas disponibles
		for (Propiedad p : propiedades) {
			p.setReservas(listaReservasPorPropiedad(p.getTitulo(),p.getLocalidad()));
			if (p.getReservas().size() > 0) {
				p.actualizarTiposDeReservasDisponibles();
				propiedades2.add(p);
			}				
		}	
		
		return propiedades2;
	}
	
	
	public ArrayList<Propiedad> listaPropiedadesPorFecha(LocalDate fecha1, LocalDate fecha2) throws SQLException {
		
		ArrayList<Propiedad> propiedades = new ArrayList<Propiedad>();
		ArrayList<Propiedad> propiedades2 = new ArrayList<Propiedad>();
		Propiedad propiedad;
		
		String query = "SELECT * FROM propiedad";
		ResultSet rs = stmt.executeQuery(query);
		
		while (rs.next()) {
			propiedad = new Propiedad();
			propiedad.setTitulo(rs.getString("titulo"));
			propiedad.setPais(rs.getString("pais"));
			propiedad.setProvincia(rs.getString("provincia"));
			propiedad.setLocalidad(rs.getString("localidad"));
			propiedad.setDomicilio(rs.getString("domicilio"));
			propiedad.setDescripción(rs.getString("descripcion"));
			propiedad.setMontoBase(rs.getInt("monto"));
			
			propiedad.setFoto1(rs.getBytes("foto1"));
			
			propiedades.add(propiedad);					
		}			

		//filtro propiedades sin reservas disponibles			
		for (Propiedad p : propiedades) {
			p.setReservas(listaReservasPorPropiedad(p.getTitulo(),p.getLocalidad()));				
			if (p.hayReservaEntreFechas(fecha1, fecha2)) {
				propiedades2.add(p);
			}
		}
		
		return propiedades2;
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
	
	
	//No me funciona, solo esta implementado la parte de subasta.
	public ArrayList<Reserva> listaReservasPorEstado(EstadoDeReserva estado) throws SQLException {
		
		ArrayList<Reserva> reservas = new ArrayList<Reserva>();
		Reserva reserva = null;
		ReservaDirecta reservaDirecta;
		ReservaSubasta reservaSubasta;
		ReservaHotsale reservaHotsale;
		
		String query = "SELECT * FROM reservas WHERE estado = '"+estado.toString()+"'";
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
	
	
	public void comenzarReservaSubasta(Reserva r) throws SQLException {		
		
		//parte 1, se actualiza tipo de reserva
		String query = "UPDATE reservas"
				+ " SET tipo = ?, estado = ?"
				+" WHERE propiedad = ? AND localidad = ? AND fecha_inicio = ?";
			
		ps = (PreparedStatement) con.prepareStatement(query);
		
		ps.setString(1,"subasta");
		ps.setString(2,"DISPONIBLE_SUBASTA");
		ps.setString(3,r.getPropiedad());
		ps.setString(4,r.getLocalidad());
		ps.setDate(5, Date.valueOf(r.getFechaInicio()));		
		
		ps.executeUpdate();
		ps.close();
		
		//parte 2, se agrega subasta a la tabla subastas		
		query = "INSERT INTO subastas (propiedad, localidad, fecha_inicio, fecha_subasta, montos)"
				+" VALUES (?,?,?,?,?)";
			
		ps = (PreparedStatement) con.prepareStatement(query);
		
		ps.setString(1,r.getPropiedad());
		ps.setString(2,r.getLocalidad());
		ps.setDate(3, Date.valueOf(r.getFechaInicio()));
		ps.setDate(4, Date.valueOf(LocalDate.now()));
		ps.setString(5,String.valueOf(r.getMonto()));
		
		ps.executeUpdate();
		ps.close();
		con.close();		
	}
	
	
	public ArrayList<ReservaSubasta> listaSubastas() throws SQLException {

		ArrayList<ReservaSubasta> reservas = new ArrayList<>();
		ReservaSubasta reserva;
		String[] preMontos;
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
						
			preMontos = (rs.getString("montos").split("\\s+"));
			for (String st : preMontos)
				montos.add(Float.parseFloat(st));
			reserva.setMontos(montos);			
			
			preUsuarios = rs.getString("usuarios");
			if (preUsuarios != null) {
				reserva.setUsuarios( new ArrayList<>(Arrays.asList(rs.getString("usuarios").split("\\s+"))) );
			} //usuarios se inicializa como null, en caso de no haber ofertas no hace falta asignarlo				
			
			reservas.add(reserva);
		}
		
		return reservas;
	}	


	public ReservaSubasta buscarSubasta(String propiedad,  String localidad, LocalDate fechaInicio) throws SQLException {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-LL-dd");
		String fechaComoString = fechaInicio.format(formatter);
		
		String query = "SELECT * FROM subastas WHERE propiedad = '"+propiedad+"' AND localidad = '"
		+localidad+"' AND fecha_inicio = '"+fechaComoString+"'";
		ResultSet rs = stmt.executeQuery(query);
		
		ReservaSubasta reserva = new ReservaSubasta();
		String[] lista1;	
		String preUsuarios;
		ArrayList<Float> montos = new ArrayList<>();

		while (rs.next()) {
			reserva.setPropiedad(rs.getString("propiedad"));
			reserva.setLocalidad(rs.getString("localidad"));
			reserva.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
			reserva.setFechaInicioSubasta(rs.getDate("fecha_subasta").toLocalDate());
						
			lista1 = (rs.getString("montos").split("\\s+"));
			for (String st : lista1)
				montos.add(Float.parseFloat(st));			
			reserva.setMontos(montos);
			
			preUsuarios = rs.getString("usuarios");
			if (preUsuarios != null) {
				reserva.setUsuarios( new ArrayList<>(Arrays.asList(preUsuarios.split("\\s+"))) );
			} //usuarios se inicializa como null, en caso de no haber ofertas no hace falta asignarlo		
		}

		return reserva;
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
	
	
	//metodo que devuelve los usuarios comunes+premium de la bd
	public ArrayList<Usuario> listaUsuarios() throws SQLException {

		String query = "SELECT * FROM usuarios";
		ResultSet rs = stmt.executeQuery(query);

		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		Usuario usuario;
		Tarjeta tarjeta = new Tarjeta();

		while (rs.next()) {
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
		con.close();
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
		con.close();
	}


	public void ModificarPerfil(String mailOriginal, String mailNuevo, String nombre, String apellido, LocalDate fechaNacimiento) throws SQLException {
		
		String query = "UPDATE usuarios "
				+ "SET mail = ?, nombre = ?, apellido = ?, f_nac = ? "
				+ "WHERE mail = '"+mailOriginal+"'";		
		ps = (PreparedStatement) con.prepareStatement(query);
		
		ps.setString(1, mailNuevo);
		ps.setString(2, nombre);
		ps.setString(3, apellido);
		ps.setDate(4, Date.valueOf(fechaNacimiento));
		
		ps.executeUpdate();
	}


	public void modificarTarjeta(long numTarj, String marca, String titular, LocalDate fechaVencimiento, short numSeg, String mail) throws SQLException {		
		
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
	}


	public void cambiarContraseña(String mail, String value) throws SQLException {		
		
		String query ="UPDATE usuarios "
				+ "SET contraseña = ? "
				+ "WHERE mail = '"+mail+"'";		
		ps = (PreparedStatement) con.prepareStatement(query);
		
		ps.setString(1, value);
		
		ps.executeUpdate();
	}
	
	
	public void agregarCredito(String mail) throws SQLException {		
		
		String query ="UPDATE usuarios "
				+ "SET creditos = creditos + 1 "
				+ "WHERE mail = '"+mail+"'";
		ps = (PreparedStatement) con.prepareStatement(query);
		
		ps.executeUpdate();
	}


}

