package mundo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesFactura {

private static Factura crearFactura(ResultSet rs) throws Exception {
    return new Factura(rs.getLong("idFactura"), rs.getLong("idVenta"), rs.getTimestamp("fecha").toLocalDateTime(),
            rs.getString("cliente"), rs.getBigDecimal("total"), rs.getString("detalle"));
}

public static void Guardar(Factura f) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("INSERT INTO factura (idFactura, idVenta, fecha, cliente, total, detalle) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setLong(1, f.getIdFactura());
        if (f.getIdVenta() == 0) {
            ps.setNull(2, Types.BIGINT);
        } else {
            ps.setLong(2, f.getIdVenta());
        }
        ps.setTimestamp(3, Timestamp.valueOf(f.getFecha()));
        ps.setString(4, f.getCliente());
        ps.setBigDecimal(5, f.getTotal());
        ps.setString(6, f.getDetalle());
        ps.executeUpdate();
        ps.close();
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static long nuevoId() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COALESCE(MAX(idFactura), 0) + 1 FROM factura");
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

public static LinkedList<Factura> listar() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM factura ORDER BY idFactura DESC");
        ResultSet rs = ps.executeQuery();
        LinkedList<Factura> lista = new LinkedList();
        while (rs.next()) {
            lista.add(crearFactura(rs));
        }
        rs.close();
        ps.close();
        return lista;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Factura buscar(long idFactura) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM factura WHERE idFactura = ?");
        ps.setLong(1, idFactura);
        ResultSet rs = ps.executeQuery();
        Factura f1 = null;
        if (rs.next()) {
            f1 = crearFactura(rs);
        }
        rs.close();
        ps.close();
        return f1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static DefaultTableModel modeloFactura() throws Exception {
    LinkedList<Factura> lf = listar();
    String[] columnas = {"Factura", "Fecha", "Cliente", "Venta", "Total"};
    Object[][] datos = new Object[lf.size()][5];
    for (int i = 0; i < lf.size(); i++) {
        Factura f = (Factura) lf.get(i);
        datos[i][0] = f.getIdFactura();
        datos[i][1] = Tablas.fecha(f.getFecha());
        datos[i][2] = f.getCliente();
        if (f.getIdVenta() == 0) {
            datos[i][3] = "(eliminada)";
        } else {
            datos[i][3] = f.getIdVenta();
        }
        datos[i][4] = Tablas.dinero(f.getTotal());
    }
    return Tablas.modelo(columnas, datos);
}

}
