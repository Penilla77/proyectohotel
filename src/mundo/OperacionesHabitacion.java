package mundo;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesHabitacion {

private static Habitacion crearHabitacion(ResultSet rs) throws Exception {
    return new Habitacion(rs.getInt("id"), rs.getString("numero"), rs.getString("estado"), rs.getString("tipoHabitacion"),
            rs.getBigDecimal("precioNoche"), rs.getInt("capacidad"), rs.getInt("idCliente"));
}

public static void Guardar(Habitacion h) throws Exception{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("INSERT INTO habitacion (id, numero, estado, tipoHabitacion, precioNoche, capacidad, idCliente) VALUES (?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, h.getId());
        ps.setString(2, h.getNumero());
        ps.setString(3, h.getEstado());
        ps.setString(4, h.getTipoHabitacion());
        ps.setBigDecimal(5, h.getPrecioNoche());
        ps.setInt(6, h.getCapacidad());
        if (h.getIdCliente() == 0) {
            ps.setNull(7, Types.INTEGER);
        } else {
            ps.setInt(7, h.getIdCliente());
        }
        ps.executeUpdate();
        ps.close();
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static int nuevoId() throws Exception{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COALESCE(MAX(id), 0) + 1 FROM habitacion");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int id = rs.getInt(1);
        rs.close();
        ps.close();
        return id;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static LinkedList<Habitacion> listar() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM habitacion ORDER BY id");
        ResultSet rs = ps.executeQuery();
        LinkedList<Habitacion> lista = new LinkedList();
        while (rs.next()) {
            lista.add(crearHabitacion(rs));
        }
        rs.close();
        ps.close();
        return lista;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Habitacion buscar(int id) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM habitacion WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Habitacion h1 = null;
        if (rs.next()) {
            h1 = crearHabitacion(rs);
        }
        rs.close();
        ps.close();
        return h1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Habitacion buscarPorNumero(String numero) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM habitacion WHERE numero = ?");
        ps.setString(1, numero);
        ResultSet rs = ps.executeQuery();
        Habitacion h1 = null;
        if (rs.next()) {
            h1 = crearHabitacion(rs);
        }
        rs.close();
        ps.close();
        return h1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static String numero(int id) throws Exception {
       Habitacion h1 = buscar(id);
       if(h1 == null) {
           return "(eliminada)";
       }
       return h1.getNumero();
}

public static boolean modificar(int id, String numero, String estado, String tipoHabitacion, BigDecimal precioNoche, int capacidad) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("UPDATE habitacion SET numero = ?, estado = ?, tipoHabitacion = ?, precioNoche = ?, capacidad = ? WHERE id = ?");
        ps.setString(1, numero);
        ps.setString(2, estado);
        ps.setString(3, tipoHabitacion);
        ps.setBigDecimal(4, precioNoche);
        ps.setInt(5, capacidad);
        ps.setInt(6, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static void actualizarEstado(int id, String estado, int idCliente) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("UPDATE habitacion SET estado = ?, idCliente = ? WHERE id = ?");
        ps.setString(1, estado);
        if (idCliente == 0) {
            ps.setNull(2, Types.INTEGER);
        } else {
            ps.setInt(2, idCliente);
        }
        ps.setInt(3, id);
        ps.executeUpdate();
        ps.close();
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean eliminar(int id) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("DELETE FROM habitacion WHERE id = ?");
        ps.setInt(1, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } catch (SQLIntegrityConstraintViolationException e) {
        throw new Exception("No se puede eliminar: la habitación tiene reservas, check-in o cuentas registradas");
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static DefaultTableModel modeloHabitacion() throws Exception {
    LinkedList<Object[]> lh = ConexionBD.filas("SELECT h.id, h.numero, h.estado, h.tipoHabitacion, h.precioNoche, h.capacidad, "
            + "CASE WHEN h.idCliente IS NULL THEN '-' ELSE COALESCE(CONCAT(c.nombre, ' ', c.apellido), '(eliminado)') END "
            + "FROM habitacion h LEFT JOIN cliente c ON c.id = h.idCliente ORDER BY h.id");
    String[] columnas = {"Id", "Número", "Estado", "Tipo", "Precio por noche", "Capacidad", "Cliente"};
    Object[][] datos = new Object[lh.size()][7];
    for (int i = 0; i < lh.size(); i++) {
        Object[] h = lh.get(i);
        datos[i][0] = h[0];
        datos[i][1] = h[1];
        datos[i][2] = h[2];
        datos[i][3] = h[3];
        datos[i][4] = Tablas.dinero((BigDecimal) h[4]);
        datos[i][5] = h[5];
        datos[i][6] = h[6];
    }
    return Tablas.modelo(columnas, datos);
}

}
