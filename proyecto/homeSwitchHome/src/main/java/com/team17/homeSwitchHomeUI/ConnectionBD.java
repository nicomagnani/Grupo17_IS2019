package com.team17.homeSwitchHomeUI;


	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.SQLException;
	import java.sql.Statement;


public class ConnectionBD {

		public void Conectar() {
			
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Connection con;

			con = DriverManager.getConnection("jdbc:mysql://localhost/homeswitchhome","root","");
			Statement stmt=con.createStatement();
			stmt.executeUpdate("INSERT INTO propiedad VALUES('123')" );
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
	}

