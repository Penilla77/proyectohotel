package mundo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesReserva {

private static Reserva crearReserva(ResultSet rs) throws Exception {
    return new Reserva(rs.getInt("idReserva"), rs.getInt("idCliente"), rs.getInt("idHabitacion"),
            rs.getTimestamp("fechaEntrada").toLocalDateTime(), rs.getTimestamp("fechaSalida").toLocalDateTime(),
            rs.getTimestamp("createdAt").toLocalDateTime(), rs.getString("tipoReserva"), rs.getString("estado"));
}

public static void Guardar(Reserva r) throws Exception{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("INSERT INTO reserva (idReserva, idCliente, idHabitacion, fechaEntrada, fechaSalida, createdAt, tipoReserva, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, r.getIdReserva());
        ps.setInt(2, r.getIdCliente());
        ps.setInt(3, r.getIdHabitacion());
        ps.setTimestamp(4, Timestamp.valueOf(r.getFechaEntrada()));
        ps.setTimestamp(5, Timestamp.valueOf(r.getFechaSalida()));
        ps.setTimestamp(6, Timestamp.valueOf(r.getCreatedAt()));
        ps.setString(7, r.getTipoReserva());
        ps.setString(8, r.getEstado());
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
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COALESCE(MAX(idReserva), 0) + 1 FROM reserva");
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

public static LinkedList<Reserva> listar() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM reserva ORDER BY idReserva");
        ResultSet rs = ps.executeQuery();
        LinkedList<Reserva> lista = new LinkedList();
        while (rs.next()) {
            lista.add(crearReserva(rs));
        }
        rs.close();
        ps.close();
        return lista;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Reserva buscar(int idReserva) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM reserva WHERE idReserva = ?");
        ps.setInt(1, idReserva);
        ResultSet rs = ps.executeQuery();
        Reserva r1 = null;
        if (rs.next()) {
            r1 = crearReserva(rs);
        }
        rs.close();
        ps.close();
        return r1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean modificar(int idReserva, int idCliente, int idHabitacion, LocalDateTime fechaEntrada, LocalDateTime fechaSalida, LocalDateTime createdAt, String tipoReserva, String estado) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("UPDATE reserva SET idCliente = ?, idHabitacion = ?, fechaEntrada = ?, fechaSalida = ?, createdAt = ?, tipoReserva = ?, estado = ? WHERE idReserva = ?");
        ps.setInt(1, idCliente);
        ps.setInt(2, idHabitacion);
        ps.setTimestamp(3, Timestamp.valueOf(fechaEntrada));
        ps.setTimestamp(4, Timestamp.valueOf(fechaSalida));
        ps.setTimestamp(5, Timestamp.valueOf(createdAt));
        ps.setString(6, tipoReserva);
        ps.setString(7, estado);
        ps.setInt(8, idReserva);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean eliminar(int idReserva) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("DELETE FROM reserva WHERE idReserva = ?");
        ps.setInt(1, idReserva);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static DefaultTableModel modeloReserva() throws Exception {
    LinkedList<Object[]> lr = ConexionBD.filas("SELECT r.idReserva, COALESCE(CONCAT(c.nombre, ' ', c.apellido), '(eliminado)'), "
            + "COALESCE(h.numero, '(eliminada)'), r.fechaEntrada, r.fechaSalida, r.tipoReserva, r.estado "
            + "FROM reserva r LEFT JOIN cliente c ON c.id = r.idCliente LEFT JOIN habitacion h ON h.id = r.idHabitacion "
            + "ORDER BY r.idReserva");
    String[] columnas = {"Id", "Cliente", "Habitación", "Entrada", "Salida", "Tipo", "Estado"};
    Object[][] datos = new Object[lr.size()][7];
    for (int i = 0; i < lr.size(); i++) {
        Object[] r = lr.get(i);
        datos[i][0] = r[0];
        datos[i][1] = r[1];
        datos[i][2] = r[2];
        datos[i][3] = Tablas.fecha((LocalDateTime) r[3]);
        datos[i][4] = Tablas.fecha((LocalDateTime) r[4]);
        datos[i][5] = r[5];
        datos[i][6] = r[6];
    }
    return Tablas.modelo(columnas, datos);
}

}
