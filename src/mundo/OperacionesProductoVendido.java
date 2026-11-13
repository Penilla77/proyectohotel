package mundo;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesProductoVendido {

private static ProductoVendido crearProductoVendido(ResultSet rs) throws Exception {
    return new ProductoVendido(rs.getLong("idProductoV"), rs.getLong("idProducto"), rs.getLong("idVenta"),
            rs.getLong("cantidad"), rs.getBigDecimal("precioUnitario"));
}

public static void Guardar(ProductoVendido pv) throws Exception{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("INSERT INTO productovendido (idProductoV, idProducto, idVenta, cantidad, precioUnitario) VALUES (?, ?, ?, ?, ?)");
        ps.setLong(1, pv.getIdProductoV());
        ps.setLong(2, pv.getIdProducto());
        ps.setLong(3, pv.getIdVenta());
        ps.setLong(4, pv.getCantidad());
        ps.setBigDecimal(5, pv.getPrecioUnitario());
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
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COALESCE(MAX(idProductoV), 0) + 1 FROM productovendido");
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

public static LinkedList<ProductoVendido> listar() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM productovendido ORDER BY idProductoV");
        ResultSet rs = ps.executeQuery();
        LinkedList<ProductoVendido> lista = new LinkedList();
        while (rs.next()) {
            lista.add(crearProductoVendido(rs));
        }
        rs.close();
        ps.close();
        return lista;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static void eliminarDeVenta(long idVenta) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("DELETE FROM productovendido WHERE idVenta = ?");
        ps.setLong(1, idVenta);
        ps.executeUpdate();
        ps.close();
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static DefaultTableModel modeloResumen(LinkedList<ProductoVendido> items) throws Exception {
    LinkedList<Producto> lp = OperacionesProducto.listar();
    String[] columnas = {"Producto", "Cantidad", "Precio", "IVA", "Subtotal"};
    Object[][] datos = new Object[items.size()][5];
    for (int i = 0; i < items.size(); i++) {
        ProductoVendido pv = (ProductoVendido) items.get(i);
        Producto p = null;
        for (int j = 0; j < lp.size(); j++) {
            if (lp.get(j).getIdProducto() == pv.getIdProducto()) {
                p = lp.get(j);
            }
        }
        if (p == null) {
            datos[i][0] = "(producto eliminado)";
            datos[i][3] = "-";
        } else {
            datos[i][0] = p.getDescripcion();
            if (p.getIva() == 0) {
                datos[i][3] = "Exento";
            } else {
                datos[i][3] = p.getIva() + "%";
            }
        }
        datos[i][1] = pv.getCantidad();
        datos[i][2] = Tablas.dinero(pv.getPrecioUnitario());
        datos[i][4] = Tablas.dinero(pv.getPrecioUnitario().multiply(new BigDecimal(pv.getCantidad())));
    }
    return Tablas.modelo(columnas, datos);
}

}
