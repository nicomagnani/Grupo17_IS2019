package com.team17.homeSwitchHomeUI;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import homeSwitchHome.Propiedad;
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

			while (rs.next())
		      {

  				admin.setMail(rs.getString("mail"));
				admin.setContraseña(rs.getString("contraseña"));
				admins.add(admin);
		     }
		return admins;
		}


		//metodo obsoleto, usar agregarResidencia
//		public void AgregarDatos(String titulo, String pais) throws SQLException {
//
//
//			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement("INSERT INTO propiedad (titulo,pais,provincia,localidad,domicilio,descripcion,monto,imagen)"
//			        + "VALUES (?,?,?,?,?,?,?,?)" );
//
//			FileInputStream fis = null;
//			 PreparedStatement ps = null;
//			 con.setAutoCommit(false);
//				//File file = new File(ruta);
//			//	fis = new FileInputStream(file);
//			pstmt.setString(1, titulo );
//		    pstmt.setString(2,pais);
//		    pstmt.setString(3,"provincia");
//		    pstmt.setString(4,"loc");
//		    pstmt.setString(5,"dom");
//		    pstmt.setString(6,"desc");
//		    pstmt.setInt(7, 100);
//
//			pstmt.executeUpdate();
//
//		}


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
					ps.setNull(col, java.sql.Types.BLOB);
				col++; //incrementa fuera del if-else para asegurar que se guarde en la pos correcta
			}

			ps.executeUpdate();
			ps.close();
			con.close();
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
			ps.setDate(5, java.sql.Date.valueOf(uc.getfNac()));
			ps.setShort(6, uc.getCreditos());
			ps.setLong(7, tarjeta.getNumero());
			ps.setString(8, tarjeta.getMarca());
			ps.setString(9, tarjeta.getTitular());
			ps.setDate(10, java.sql.Date.valueOf(tarjeta.getfVenc()));
			ps.setShort(11, tarjeta.getCodigo());

		    ps.executeUpdate();
			ps.close();
			con.close();

		}

}

