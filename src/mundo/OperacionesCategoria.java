package mundo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesCategoria {

private static Categoria crearCategoria(ResultSet rs) throws Exception {
    return new Categoria(rs.getInt("codCategoria"), rs.getString("detalle"));
}

public static void Guardar(Categoria c) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("INSERT INTO categoria (codCategoria, detalle) VALUES (?, ?)");
        ps.setInt(1, c.getCodCategoria());
        ps.setString(2, c.getDetalle());
        ps.executeUpdate();
        ps.close();
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static int nuevoCod() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COALESCE(MAX(codCategoria), 0) + 1 FROM categoria");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int cod = rs.getInt(1);
        rs.close();
        ps.close();
        return cod;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static LinkedList<Categoria> listar() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM categoria ORDER BY codCategoria");
        ResultSet rs = ps.executeQuery();
        LinkedList<Categoria> lista = new LinkedList();
        while (rs.next()) {
            lista.add(crearCategoria(rs));
        }
        rs.close();
        ps.close();
        return lista;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Categoria buscar(int cod) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM categoria WHERE codCategoria = ?");
        ps.setInt(1, cod);
        ResultSet rs = ps.executeQuery();
        Categoria c1 = null;
        if (rs.next()) {
            c1 = crearCategoria(rs);
        }
        rs.close();
        ps.close();
        return c1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Categoria buscarPorDetalle(String detalle) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM categoria WHERE detalle = ?");
        ps.setString(1, detalle);
        ResultSet rs = ps.executeQuery();
        Categoria c1 = null;
        if (rs.next()) {
            c1 = crearCategoria(rs);
        }
        rs.close();
        ps.close();
        return c1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static String detalle(int cod) throws Exception {
       Categoria c1 = buscar(cod);
       if(c1 == null) {
           return "(sin categoría)";
       }
       return c1.getDetalle();
}

public static boolean esHabitacion(int cod) throws Exception {
       Categoria c1 = buscar(cod);
       return c1 != null && c1.getDetalle().equals("Habitación");
}

public static boolean modificar(int cod, String detalle) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("UPDATE categoria SET detalle = ? WHERE codCategoria = ?");
        ps.setString(1, detalle);
        ps.setInt(2, cod);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean eliminar(int cod) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("DELETE FROM categoria WHERE codCategoria = ?");
        ps.setInt(1, cod);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } catch (SQLIntegrityConstraintViolationException e) {
        throw new Exception("No se puede eliminar: la categoría tiene productos registrados");
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static DefaultTableModel modeloCategoria() throws Exception {
    LinkedList<Categoria> lcat = listar();
    String[] columnas = {"Código", "Detalle"};
    Object[][] datos = new Object[lcat.size()][2];
    for (int i = 0; i < lcat.size(); i++) {
        Categoria c = (Categoria) lcat.get(i);
        datos[i][0] = c.getCodCategoria();
        datos[i][1] = c.getDetalle();
    }
    return Tablas.modelo(columnas, datos);
}

}
