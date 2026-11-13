package mundo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesCheckin {

private static Checkin crearCheckin(ResultSet rs) throws Exception {
    return new Checkin(rs.getInt("idCheckin"), rs.getTimestamp("fechaIngreso").toLocalDateTime(),
            rs.getTimestamp("fechaSalida").toLocalDateTime(), rs.getInt("noches"), rs.getInt("idHabitacion"),
            rs.getInt("idCliente"), rs.getString("transporte"), rs.getString("motivoViaje"),
            rs.getString("procedencia"), rs.getInt("acompañantes"));
}

public static void Guardar(Checkin c) throws Exception{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("INSERT INTO checkin (idCheckin, fechaIngreso, fechaSalida, noches, idHabitacion, idCliente, transporte, motivoViaje, procedencia, acompañantes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, c.getIdCheckin());
        ps.setTimestamp(2, Timestamp.valueOf(c.getFechaIngreso()));
        ps.setTimestamp(3, Timestamp.valueOf(c.getFechaSalida()));
        ps.setInt(4, c.getNoches());
        ps.setInt(5, c.getIdHabitacion());
        ps.setInt(6, c.getIdCliente());
        ps.setString(7, c.getTransporte());
        ps.setString(8, c.getMotivoViaje());
        ps.setString(9, c.getProcedencia());
        ps.setInt(10, c.getAcompañantes());
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
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COALESCE(MAX(idCheckin), 0) + 1 FROM checkin");
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

public static LinkedList<Checkin> listar() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM checkin ORDER BY idCheckin");
        ResultSet rs = ps.executeQuery();
        LinkedList<Checkin> lista = new LinkedList();
        while (rs.next()) {
            lista.add(crearCheckin(rs));
        }
        rs.close();
        ps.close();
        return lista;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Checkin buscar(int idCheckin) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM checkin WHERE idCheckin = ?");
        ps.setInt(1, idCheckin);
        ResultSet rs = ps.executeQuery();
        Checkin c1 = null;
        if (rs.next()) {
            c1 = crearCheckin(rs);
        }
        rs.close();
        ps.close();
        return c1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean modificar(int idCheckin, LocalDateTime fechaIngreso, LocalDateTime fechaSalida, int noches, int idHabitacion, int idCliente, String transporte, String motivoViaje, String procedencia, int acompañantes) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("UPDATE checkin SET fechaIngreso = ?, fechaSalida = ?, noches = ?, idHabitacion = ?, idCliente = ?, transporte = ?, motivoViaje = ?, procedencia = ?, acompañantes = ? WHERE idCheckin = ?");
        ps.setTimestamp(1, Timestamp.valueOf(fechaIngreso));
        ps.setTimestamp(2, Timestamp.valueOf(fechaSalida));
        ps.setInt(3, noches);
        ps.setInt(4, idHabitacion);
        ps.setInt(5, idCliente);
        ps.setString(6, transporte);
        ps.setString(7, motivoViaje);
        ps.setString(8, procedencia);
        ps.setInt(9, acompañantes);
        ps.setInt(10, idCheckin);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean eliminar(int idCheckin) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("DELETE FROM checkin WHERE idCheckin = ?");
        ps.setInt(1, idCheckin);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static DefaultTableModel modeloCheckin() throws Exception {
    LinkedList<Object[]> lck = ConexionBD.filas("SELECT k.idCheckin, COALESCE(CONCAT(c.nombre, ' ', c.apellido), '(eliminado)'), "
            + "COALESCE(h.numero, '(eliminada)'), k.fechaIngreso, k.fechaSalida, k.noches, k.transporte, k.motivoViaje, "
            + "k.procedencia, k.acompañantes "
            + "FROM checkin k LEFT JOIN cliente c ON c.id = k.idCliente LEFT JOIN habitacion h ON h.id = k.idHabitacion "
            + "ORDER BY k.idCheckin");
    String[] columnas = {"Id", "Cliente", "Habitación", "Ingreso", "Salida", "Noches", "Transporte", "Motivo", "Procedencia", "Acompañantes"};
    Object[][] datos = new Object[lck.size()][10];
    for (int i = 0; i < lck.size(); i++) {
        Object[] c = lck.get(i);
        datos[i][0] = c[0];
        datos[i][1] = c[1];
        datos[i][2] = c[2];
        datos[i][3] = Tablas.fecha((LocalDateTime) c[3]);
        datos[i][4] = Tablas.fecha((LocalDateTime) c[4]);
        datos[i][5] = c[5];
        datos[i][6] = c[6];
        datos[i][7] = c[7];
        datos[i][8] = c[8];
        datos[i][9] = c[9];
    }
    return Tablas.modelo(columnas, datos);
}

}
