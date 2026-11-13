package mundo;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesCuenta {

private static Cuenta crearCuenta(ResultSet rs) throws Exception {
    return new Cuenta(rs.getInt("idCuenta"), rs.getInt("idCheckin"), rs.getInt("idCliente"), rs.getInt("idHabitacion"),
            rs.getTimestamp("fechaApertura").toLocalDateTime(), rs.getString("estado"), rs.getLong("idVenta"));
}

public static void Guardar(Cuenta c) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("INSERT INTO cuenta (idCuenta, idCheckin, idCliente, idHabitacion, fechaApertura, estado, idVenta) VALUES (?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, c.getIdCuenta());
        if (c.getIdCheckin() == 0) {
            ps.setNull(2, Types.INTEGER);
        } else {
            ps.setInt(2, c.getIdCheckin());
        }
        ps.setInt(3, c.getIdCliente());
        ps.setInt(4, c.getIdHabitacion());
        ps.setTimestamp(5, Timestamp.valueOf(c.getFechaApertura()));
        ps.setString(6, c.getEstado());
        if (c.getIdVenta() == 0) {
            ps.setNull(7, Types.BIGINT);
        } else {
            ps.setLong(7, c.getIdVenta());
        }
        ps.executeUpdate();
        ps.close();
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static int nuevoId() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COALESCE(MAX(idCuenta), 0) + 1 FROM cuenta");
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

public static Cuenta buscar(int idCuenta) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM cuenta WHERE idCuenta = ?");
        ps.setInt(1, idCuenta);
        ResultSet rs = ps.executeQuery();
        Cuenta c1 = null;
        if (rs.next()) {
            c1 = crearCuenta(rs);
        }
        rs.close();
        ps.close();
        return c1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Cuenta buscarDeCheckin(int idCheckin) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM cuenta WHERE idCheckin = ? AND estado = 'Abierta'");
        ps.setInt(1, idCheckin);
        ResultSet rs = ps.executeQuery();
        Cuenta c1 = null;
        if (rs.next()) {
            c1 = crearCuenta(rs);
        }
        rs.close();
        ps.close();
        return c1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Cuenta buscarAbiertaDeCliente(int idCliente) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM cuenta WHERE idCliente = ? AND estado = 'Abierta' ORDER BY idCuenta");
        ps.setInt(1, idCliente);
        ResultSet rs = ps.executeQuery();
        Cuenta c1 = null;
        if (rs.next()) {
            c1 = crearCuenta(rs);
        }
        rs.close();
        ps.close();
        return c1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean tieneAbierta(int idCliente) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COUNT(*) FROM cuenta WHERE idCliente = ? AND estado = 'Abierta'");
        ps.setInt(1, idCliente);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int n = rs.getInt(1);
        rs.close();
        ps.close();
        return n > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static void actualizarDeCheckin(int idCheckin, int idCliente, int idHabitacion) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("UPDATE cuenta SET idCliente = ?, idHabitacion = ? WHERE idCheckin = ? AND estado = 'Abierta'");
        ps.setInt(1, idCliente);
        ps.setInt(2, idHabitacion);
        ps.setInt(3, idCheckin);
        ps.executeUpdate();
        ps.close();
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static void cerrar(int idCuenta, long idVenta) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("UPDATE cuenta SET estado = 'Cerrada', idVenta = ? WHERE idCuenta = ?");
        ps.setLong(1, idVenta);
        ps.setInt(2, idCuenta);
        ps.executeUpdate();
        ps.close();
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static void eliminarDeCheckin(int idCheckin) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("DELETE FROM cuenta WHERE idCheckin = ? AND estado = 'Abierta'");
        ps.setInt(1, idCheckin);
        ps.executeUpdate();
        ps.close();
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static DefaultTableModel modeloCuenta() throws Exception {
    LinkedList<Object[]> lcu = ConexionBD.filas("SELECT cu.idCuenta, COALESCE(CONCAT(c.nombre, ' ', c.apellido), '(eliminado)'), "
            + "COALESCE(h.numero, '(eliminada)'), cu.fechaApertura, k.fechaSalida, k.noches, "
            + "(SELECT COALESCE(SUM(cp.cantidad * cp.precioUnitario), 0) FROM cuentaproducto cp WHERE cp.idCuenta = cu.idCuenta) "
            + "FROM cuenta cu LEFT JOIN cliente c ON c.id = cu.idCliente LEFT JOIN habitacion h ON h.id = cu.idHabitacion "
            + "LEFT JOIN checkin k ON k.idCheckin = cu.idCheckin WHERE cu.estado = 'Abierta' ORDER BY cu.idCuenta");
    String[] columnas = {"Cuenta", "Cliente", "Habitación", "Ingreso", "Salida prevista", "Noches", "Consumos"};
    Object[][] datos = new Object[lcu.size()][7];
    for (int i = 0; i < lcu.size(); i++) {
        Object[] c = lcu.get(i);
        datos[i][0] = c[0];
        datos[i][1] = c[1];
        datos[i][2] = c[2];
        datos[i][3] = Tablas.fecha((LocalDateTime) c[3]);
        datos[i][4] = Tablas.fecha((LocalDateTime) c[4]);
        datos[i][5] = c[5];
        datos[i][6] = Tablas.dinero((BigDecimal) c[6]);
    }
    return Tablas.modelo(columnas, datos);
}

}
