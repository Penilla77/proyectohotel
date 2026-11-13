package mundo;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesProducto {

private static Producto crearProducto(ResultSet rs) throws Exception {
    LocalDate vencimiento = null;
    if (rs.getDate("vencimiento") != null) {
        vencimiento = rs.getDate("vencimiento").toLocalDate();
    }
    return new Producto(rs.getLong("idProducto"), rs.getString("codigo"), rs.getString("descripcion"),
            rs.getBigDecimal("precioVenta"), rs.getBigDecimal("precioCompra"), rs.getInt("iva"),
            rs.getInt("existencia"), rs.getLong("idUsuario"), rs.getInt("codCategoria"), vencimiento);
}

public static void Guardar(Producto p) throws Exception{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("INSERT INTO producto (idProducto, codigo, descripcion, precioVenta, precioCompra, iva, existencia, idUsuario, codCategoria, vencimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setLong(1, p.getIdProducto());
        ps.setString(2, p.getCodigo());
        ps.setString(3, p.getDescripcion());
        ps.setBigDecimal(4, p.getPrecioVenta());
        ps.setBigDecimal(5, p.getPrecioCompra());
        ps.setInt(6, p.getIva());
        ps.setInt(7, p.getExistencia());
        ps.setLong(8, p.getIdUsuario());
        ps.setInt(9, p.getCodcategoria());
        ps.setDate(10, Date.valueOf(p.getVencimiento()));
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
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COALESCE(MAX(idProducto), 0) + 1 FROM producto");
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

public static LinkedList<Producto> listar() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM producto ORDER BY idProducto");
        ResultSet rs = ps.executeQuery();
        LinkedList<Producto> lista = new LinkedList();
        while (rs.next()) {
            lista.add(crearProducto(rs));
        }
        rs.close();
        ps.close();
        return lista;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Producto buscar(long id) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM producto WHERE idProducto = ?");
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        Producto p1 = null;
        if (rs.next()) {
            p1 = crearProducto(rs);
        }
        rs.close();
        ps.close();
        return p1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Producto buscarPorCodigo(String codigo) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM producto WHERE codigo = ?");
        ps.setString(1, codigo);
        ResultSet rs = ps.executeQuery();
        Producto p1 = null;
        if (rs.next()) {
            p1 = crearProducto(rs);
        }
        rs.close();
        ps.close();
        return p1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean modificar(long id, String codigo, String descripcion, BigDecimal precioVenta, BigDecimal precioCompra, int iva, int existencia, int codCategoria, LocalDate vencimiento) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("UPDATE producto SET codigo = ?, descripcion = ?, precioVenta = ?, precioCompra = ?, iva = ?, existencia = ?, codCategoria = ?, vencimiento = ? WHERE idProducto = ?");
        ps.setString(1, codigo);
        ps.setString(2, descripcion);
        ps.setBigDecimal(3, precioVenta);
        ps.setBigDecimal(4, precioCompra);
        ps.setInt(5, iva);
        ps.setInt(6, existencia);
        ps.setInt(7, codCategoria);
        ps.setDate(8, Date.valueOf(vencimiento));
        ps.setLong(9, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static void cambiarExistencia(long id, int cantidad) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("UPDATE producto SET existencia = existencia + ? WHERE idProducto = ?");
        ps.setInt(1, cantidad);
        ps.setLong(2, id);
        ps.executeUpdate();
        ps.close();
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean eliminar(long id) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("DELETE FROM producto WHERE idProducto = ?");
        ps.setLong(1, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } catch (SQLIntegrityConstraintViolationException e) {
        throw new Exception("No se puede eliminar: el producto tiene ventas o cuentas registradas");
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static DefaultTableModel modeloProducto() throws Exception {
    LinkedList<Object[]> lp = ConexionBD.filas("SELECT p.idProducto, p.codigo, p.descripcion, COALESCE(c.detalle, '(sin categoría)'), "
            + "p.precioCompra, p.precioVenta, p.iva, p.existencia, p.vencimiento "
            + "FROM producto p LEFT JOIN categoria c ON c.codCategoria = p.codCategoria ORDER BY p.idProducto");
    String[] columnas = {"Id", "Código", "Descripción", "Categoría", "Precio de compra", "Precio de venta", "IVA", "Existencia", "Vencimiento"};
    Object[][] datos = new Object[lp.size()][9];
    for (int i = 0; i < lp.size(); i++) {
        Object[] p = lp.get(i);
        datos[i][0] = p[0];
        datos[i][1] = p[1];
        datos[i][2] = p[2];
        datos[i][3] = p[3];
        datos[i][4] = Tablas.dinero((BigDecimal) p[4]);
        datos[i][5] = Tablas.dinero((BigDecimal) p[5]);
        if ((int) p[6] == 0) {
            datos[i][6] = "Exento";
        } else {
            datos[i][6] = p[6] + "%";
        }
        datos[i][7] = p[7];
        datos[i][8] = p[8].toString();
    }
    return Tablas.modelo(columnas, datos);
}

}
