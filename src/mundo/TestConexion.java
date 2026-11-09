package mundo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestConexion {

    public static void main(String[] args) {
        // Configura los parámetros de conexión
        String iP = "localhost";          // IP local de XAMPP
        String puerto = "3306";           // Puerto por defecto de MySQL en XAMPP
        String nombreBD = "hotel";  // Nombre de tu base de datos
        String usuario = "root";          // Usuario por defecto de MySQL en XAMPP
        String password = "";             // Contraseña, si no has configurado una, déjala vacía

        // Crea una instancia de la clase ConexionBd
        ConexionBD conexion = new ConexionBD(iP, puerto, nombreBD, usuario, password);

        try {
            // Conéctate a la base de datos MySQL
            conexion.ConexionBDMySQL();
            System.out.println("¡Conexión exitosa!");

            // Realiza una consulta simple
            ResultSet rs = conexion.consultar("SELECT * FROM cliente LIMIT 10"); // Ajusta la tabla según tu esquema
            while (rs.next()) {
                System.out.println("Registro encontrado: " + rs.getString(1));  // Imprime el primer campo del primer registro
            }

            // Cierra la conexión
            conexion.cerrar();

        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        }
    }
}