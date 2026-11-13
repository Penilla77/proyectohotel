package mundo;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class OperacionesCuentaProducto {

private static CuentaProducto crearCuentaProducto(ResultSet rs) throws Exception {
    return new CuentaProducto(rs.getLong("idCuentaProducto"), rs.getInt("idCuenta"), rs.getLong("idProducto"),
            rs.getLong("cantidad"), rs.getBigDecimal("precioUnitario"));
}

public static void Guardar(CuentaProducto cp) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("INSERT INTO cuentaproducto (idCuentaProducto, idCuenta, idProducto, cantidad, precioUnitario) VALUES (?, ?, ?, ?, ?)");
        ps.setLong(1, cp.getIdCuentaProducto());
        ps.setInt(2, cp.getIdCuenta());
        ps.setLong(3, cp.getIdProducto());
        ps.setLong(4, cp.getCantidad());
        ps.setBigDecimal(5, cp.getPrecioUnitario());
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
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COALESCE(MAX(idCuentaProducto), 0) + 1 FROM cuentaproducto");
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

public static LinkedList<CuentaProducto> listarDeCuenta(int idCuenta) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM cuentaproducto WHERE idCuenta = ? ORDER BY idCuentaProducto");
        ps.setInt(1, idCuenta);
        ResultSet rs = ps.executeQuery();
        LinkedList<CuentaProducto> lista = new LinkedList();
        while (rs.next()) {
            lista.add(crearCuentaProducto(rs));
        }
        rs.close();
        ps.close();
        return lista;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean eliminar(long idCuentaProducto) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("DELETE FROM cuentaproducto WHERE idCuentaProducto = ?");
        ps.setLong(1, idCuentaProducto);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

}
