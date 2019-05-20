package com.team17.homeSwitchHomeUI;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import  homeSwitchHome.Propiedad;
import homeSwitchHome.UsuarioAdministrador;
public class ConnectionBD {
	Statement stmt;
	Connection con;
		public ConnectionBD() {
			
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("error1");
		}
		try {
			
			con = DriverManager.getConnection("jdbc:mysql://localhost/homeswitchhome","root","");
			 stmt= (Statement) con.createStatement();
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		
		}
		}
		
		
		public ArrayList<Propiedad> listaPropiedades() throws SQLException{
			ArrayList<Propiedad> propiedades= new ArrayList<Propiedad>();
			
			String query = "SELECT * FROM propiedad";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
		      {
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
		public ArrayList<UsuarioAdministrador> listaUsuarios() throws SQLException{
               ArrayList<UsuarioAdministrador> usuarios= new ArrayList<UsuarioAdministrador>();
			
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
	
		
		
}

