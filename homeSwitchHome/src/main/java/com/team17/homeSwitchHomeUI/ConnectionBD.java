package com.team17.homeSwitchHomeUI;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import homeSwitchHome.Propiedad;
import homeSwitchHome.Usuario;
import homeSwitchHome.UsuarioComun;
import homeSwitchHome.UsuarioPremium;
import homeSwitchHome.UsuarioAdministrador;

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
		
		
		public ArrayList<Propiedad> listaPropiedades() throws SQLException {
			
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
		
		
		//metodo que devuelve los usuarios comunes+premium de la bd
		public ArrayList<Usuario> listaUsuarios() throws SQLException {
               
			ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
			
			String query = "SELECT * FROM usuarios";
			ResultSet rs = stmt.executeQuery(query);
			
//			while (rs.next()) {
//				if (!rs.getBoolean("premium")) {						
//					UsuarioComun usuario = new UsuarioComun();
//				} else {
//					UsuarioPremium usuario = new UsuarioPremium(); }
//  				usuario.setMail(rs.getString("mail"));
//				usuario.setContraseña(rs.getString("contraseña"));
//				usuarios.add(usuario);
//			}
			
			return usuarios;
		}		
		
		
		//metodo que devuelve los admins de la bd
		public ArrayList<UsuarioAdministrador> listaAdmins() throws SQLException {
               
			ArrayList<UsuarioAdministrador> usuarios = new ArrayList<UsuarioAdministrador>();
			
			String query = "SELECT * FROM administradores";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
		      {
				UsuarioAdministrador usuario = new UsuarioAdministrador();
  				usuario.setMail(rs.getString("mail"));
				usuario.setContraseña(rs.getString("contraseña"));
				usuarios.add(usuario);
		     }
		return usuarios;
		}
		
		
		public void AgregarDatos(String titulo, String pais) throws SQLException {

			
			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement("INSERT INTO propiedad (titulo,pais,provincia,localidad,domicilio,descripcion,monto,imagen)"
			        + "VALUES (?,?,?,?,?,?,?,?)" );
			
			FileInputStream fis = null;
			 PreparedStatement ps = null;
			 con.setAutoCommit(false);
				//File file = new File(ruta);
			//	fis = new FileInputStream(file);
			pstmt.setString(1, titulo );
		    pstmt.setString(2,pais);
		    pstmt.setString(3,"provincia");
		    pstmt.setString(4,"loc");
		    pstmt.setString(5,"dom");
		    pstmt.setString(6,"desc");
		    pstmt.setInt(7, 100);
		  
			pstmt.executeUpdate();
			
		}


		public void agregarResidencia(Propiedad p) throws SQLException {
			byte[][] fotos = p.getFotos();			
			ByteArrayInputStream bais;
			int col = 8, x = 0, y = 1;
			
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
		
		
		public void AgregarUsuario(String mail, String contraseña, int tarjeta) throws SQLException {
			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement("INSERT INTO administradores (mail,contraseña,numTarjeta)"
			        + "VALUES (?,?,?)" );
			pstmt.setString(1, mail );
		    pstmt.setString(2,contraseña);
		    pstmt.setInt(3,tarjeta);
		    pstmt.executeUpdate();
			
		}
		
}

