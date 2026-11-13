package mundo;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesVenta {

private static Venta crearVenta(ResultSet rs) throws Exception {
    return new Venta(rs.getLong("idVenta"), rs.getTimestamp("fecha").toLocalDateTime(), rs.getBigDecimal("total"),
            rs.getBigDecimal("iva5"), rs.getBigDecimal("iva19"), rs.getBigDecimal("exento"),
            rs.getString("tipoPago"), rs.getInt("idCliente"));
}

public static void Guardar(Venta v) throws Exception{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("INSERT INTO venta (idVenta, fecha, total, iva5, iva19, exento, tipoPago, idCliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setLong(1, v.getIdVenta());
        ps.setTimestamp(2, Timestamp.valueOf(v.getFecha()));
        ps.setBigDecimal(3, v.getTotal());
        ps.setBigDecimal(4, v.getIva5());
        ps.setBigDecimal(5, v.getIva19());
        ps.setBigDecimal(6, v.getExento());
        ps.setString(7, v.getTipoPago());
        ps.setInt(8, v.getIdCliente());
        ps.executeUpdate();
        ps.close();
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static long nuevoId() throws Exception{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COALESCE(MAX(idVenta), 0) + 1 FROM venta");
        ResultSet rs = ps.executeQuery();
        rs.next();
        long id = rs.getLong(1);
        rs.close();
        ps.close();
        return id;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static BigDecimal[] calcular(BigDecimal base, String iva) {
    BigDecimal cero = BigDecimal.ZERO;
    if(iva.equals("5%")) {
        BigDecimal i5 = base.multiply(new BigDecimal("0.05"));
        return new BigDecimal[]{base.add(i5), i5, cero, cero};
    }
    if(iva.equals("19%")) {
        BigDecimal i19 = base.multiply(new BigDecimal("0.19"));
        return new BigDecimal[]{base.add(i19), cero, i19, cero};
    }
    return new BigDecimal[]{base, cero, cero, base};
}

public static LinkedList<Venta> listar() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM venta ORDER BY idVenta");
        ResultSet rs = ps.executeQuery();
        LinkedList<Venta> lista = new LinkedList();
        while (rs.next()) {
            lista.add(crearVenta(rs));
        }
        rs.close();
        ps.close();
        return lista;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Venta buscar(long id) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM venta WHERE idVenta = ?");
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        Venta v1 = null;
        if (rs.next()) {
            v1 = crearVenta(rs);
        }
        rs.close();
        ps.close();
        return v1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean modificar(long id, LocalDateTime fecha, BigDecimal total, BigDecimal iva5, BigDecimal iva19, BigDecimal exento, String tipoPago, int idCliente) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("UPDATE venta SET fecha = ?, total = ?, iva5 = ?, iva19 = ?, exento = ?, tipoPago = ?, idCliente = ? WHERE idVenta = ?");
        ps.setTimestamp(1, Timestamp.valueOf(fecha));
        ps.setBigDecimal(2, total);
        ps.setBigDecimal(3, iva5);
        ps.setBigDecimal(4, iva19);
        ps.setBigDecimal(5, exento);
        ps.setString(6, tipoPago);
        ps.setInt(7, idCliente);
        ps.setLong(8, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean eliminar(long id) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("DELETE FROM venta WHERE idVenta = ?");
        ps.setLong(1, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static DefaultTableModel modeloVenta() throws Exception {
    LinkedList<Object[]> lv = ConexionBD.filas("SELECT v.idVenta, v.fecha, COALESCE(CONCAT(c.nombre, ' ', c.apellido), '(eliminado)'), "
            + "v.tipoPago, v.exento, v.iva5, v.iva19, v.total "
            + "FROM venta v LEFT JOIN cliente c ON c.id = v.idCliente ORDER BY v.idVenta");
    String[] columnas = {"Id", "Fecha", "Cliente", "Tipo de pago", "Exento", "IVA 5%", "IVA 19%", "Total"};
    Object[][] datos = new Object[lv.size()][8];
    for (int i = 0; i < lv.size(); i++) {
        Object[] v = lv.get(i);
        datos[i][0] = v[0];
        datos[i][1] = Tablas.fecha((LocalDateTime) v[1]);
        datos[i][2] = v[2];
        datos[i][3] = v[3];
        datos[i][4] = Tablas.dinero((BigDecimal) v[4]);
        datos[i][5] = Tablas.dinero((BigDecimal) v[5]);
        datos[i][6] = Tablas.dinero((BigDecimal) v[6]);
        datos[i][7] = Tablas.dinero((BigDecimal) v[7]);
    }
    return Tablas.modelo(columnas, datos);
}

}
