/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mundo;

/**
 *
 * @author COORD RYC
 */
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

public class ConexionBD {
    Connection ConexionBD;
	Statement sentencia;
	String usuario, password, iP, puerto, nombreBD;

	public ConexionBD(String iP, String puerto, String nombreBD, String usuario, String password){
		this.usuario = usuario;
		this.password = password;
		this.iP = iP;
		this.puerto = puerto;
		this.nombreBD = nombreBD;
	}

	// sw es true si va a conectar con MySQL (3306) y postgresql (5432)
	// sw es false si va a conectar con access porque es por medio del odbc
	private void conectar	(String driver, String puente, boolean sw) 
					throws ClassNotFoundException,
						SQLException,
						InstantiationException, 
						IllegalAccessException{

		Class.forName(driver).newInstance();
		if (sw)
 			ConexionBD = DriverManager.getConnection ("jdbc:"+puente+
								"://"+iP+
								":"+puerto+
								"/"+nombreBD,usuario,password);
		else
			ConexionBD = DriverManager.getConnection ("jdbc:"+puente+
								":"+nombreBD,usuario,password);
		sentencia = ConexionBD.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
								    ResultSet.CONCUR_UPDATABLE);
	}

	// Con JDBC
	public void ConexionBDJDBC() throws 	ClassNotFoundException, 
							SQLException,
							InstantiationException,
							IllegalAccessException{
		conectar("sun.jdbc.odbc.JdbcOdbcDriver","odbc",false);
	}

	// Con MySQL
	public void ConexionBDMySQL() throws   ClassNotFoundException,
							SQLException,
							InstantiationException,
							IllegalAccessException{
	
                conectar("com.mysql.cj.jdbc.Driver", "mysql", true);
	}

	// Con PostgreSql
	public void ConexionBDPostgres() throws ClassNotFoundException,
							SQLException,
							InstantiationException,
							IllegalAccessException{
		conectar("org.postgresql.Driver","postgresql",true);
	}

	// Devuelve la conexión abierta para poder usar PreparedStatement
	public Connection getConexion(){
		return ConexionBD;
	}

	public void actualizar(String actualiza) throws SQLException{
		sentencia.executeUpdate(actualiza);
	}

	public ResultSet consultar(String consulta) throws SQLException{
		return (sentencia.executeQuery(consulta));
	}

	// Devuelve el numero de filas de la tabla virtual
	public int contar(ResultSet rS) throws SQLException{
		int cont = 0;
		rS.beforeFirst();
		while (rS.next()) cont++;
		return (cont);
	}

	public void cerrar() throws SQLException{
		ConexionBD.close();
		sentencia.close();
	}
}
