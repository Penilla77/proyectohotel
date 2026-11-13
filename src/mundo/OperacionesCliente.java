package mundo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesCliente {

private static Cliente crearCliente(ResultSet rs) throws Exception {
    return new Cliente(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"), rs.getInt("documento"),
            rs.getString("correo"), rs.getString("telefono"), rs.getString("direccion"), rs.getString("sexo"));
}

public static void Guardar(Cliente p) throws Exception{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("INSERT INTO cliente (id, nombre, apellido, documento, correo, telefono, direccion, sexo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, p.getId());
        ps.setString(2, p.getNombre());
        ps.setString(3, p.getApellido());
        ps.setInt(4, p.getDocumento());
        ps.setString(5, p.getCorreo());
        ps.setString(6, p.getTelefono());
        ps.setString(7, p.getDireccion());
        ps.setString(8, p.getSexo());
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
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT COALESCE(MAX(id), 0) + 1 FROM cliente");
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

public static LinkedList<Cliente> listar() throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM cliente ORDER BY id");
        ResultSet rs = ps.executeQuery();
        LinkedList<Cliente> lista = new LinkedList();
        while (rs.next()) {
            lista.add(crearCliente(rs));
        }
        rs.close();
        ps.close();
        return lista;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Cliente buscar(int id) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM cliente WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Cliente c1 = null;
        if (rs.next()) {
            c1 = crearCliente(rs);
        }
        rs.close();
        ps.close();
        return c1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static Cliente buscarPorDocumento(int documento) throws Exception {
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("SELECT * FROM cliente WHERE documento = ?");
        ps.setInt(1, documento);
        ResultSet rs = ps.executeQuery();
        Cliente c1 = null;
        if (rs.next()) {
            c1 = crearCliente(rs);
        }
        rs.close();
        ps.close();
        return c1;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static String nombreCompleto(int id) throws Exception {
       Cliente c1 = buscar(id);
       if(c1 == null) {
           return "(eliminado)";
       }
       return c1.getNombre() + " " + c1.getApellido();
}

public static boolean modificar(int id, String nombre, String apellido, int documento, String correo, String telefono, String direccion, String sexo) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("UPDATE cliente SET nombre = ?, apellido = ?, documento = ?, correo = ?, telefono = ?, direccion = ?, sexo = ? WHERE id = ?");
        ps.setString(1, nombre);
        ps.setString(2, apellido);
        ps.setInt(3, documento);
        ps.setString(4, correo);
        ps.setString(5, telefono);
        ps.setString(6, direccion);
        ps.setString(7, sexo);
        ps.setInt(8, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static boolean eliminar(int id) throws Exception
{
    ConexionBD con = null;
    try {
        con = ConexionBD.abrir();
        PreparedStatement ps = con.getConexion().prepareStatement("DELETE FROM cliente WHERE id = ?");
        ps.setInt(1, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas > 0;
    } catch (SQLIntegrityConstraintViolationException e) {
        throw new Exception("No se puede eliminar: el cliente tiene reservas, check-in, cuentas o ventas registradas");
    } finally {
        ConexionBD.cerrar(con);
    }
}

public static DefaultTableModel modeloCliente() throws Exception {
    LinkedList<Cliente> lc = listar();
    String[] columnas = {"Id", "Nombre", "Apellido", "Documento", "Correo", "Teléfono", "Dirección", "Sexo"};
    Object[][] datos = new Object[lc.size()][8];
    for (int i = 0; i < lc.size(); i++) {
        Cliente c = (Cliente) lc.get(i);
        datos[i][0] = c.getId();
        datos[i][1] = c.getNombre();
        datos[i][2] = c.getApellido();
        datos[i][3] = c.getDocumento();
        datos[i][4] = c.getCorreo();
        datos[i][5] = c.getTelefono();
        datos[i][6] = c.getDireccion();
        datos[i][7] = c.getSexo();
    }
    return Tablas.modelo(columnas, datos);
}

}
