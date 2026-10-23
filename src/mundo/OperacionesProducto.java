package mundo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesProducto {
public static int con=0;
public static LinkedList<Producto> lp = new LinkedList();

public static void Guardar(Producto p){
    lp.add(con++, p);
}

public static long nuevoId(){
    long mayor=0;
    for(int i=0;i<lp.size();i++)
    {
        Producto p1=(Producto)lp.get(i);
        if(p1.getIdProducto() > mayor) {
            mayor = p1.getIdProducto();
        }
    }
    return mayor + 1;
}

public static Producto buscar(long id) {
       Producto p1=null;
       for(int i=0;i<lp.size();i++)
       {
           p1=(Producto)lp.get(i);
           if(id == p1.getIdProducto()) {
               return p1;
           }
       }

       return null;
}

public static Producto buscarPorCodigo(String codigo) {
       Producto p1=null;
       for(int i=0;i<lp.size();i++)
       {
           p1=(Producto)lp.get(i);
           if(codigo.equals(p1.getCodigo())) {
               return p1;
           }
       }

       return null;
}

public static boolean modificar(long id, String codigo, String descripcion, BigDecimal precioVenta, BigDecimal precioCompra, int iva, int existencia, int codCategoria, LocalDate vencimiento)
{
       Producto p1=null;
       for(int i=0;i<lp.size();i++)
       {
           p1=(Producto)lp.get(i);
           if(id == p1.getIdProducto()) {
               p1.setCodigo(codigo);
               p1.setDescripcion(descripcion);
               p1.setPrecioVenta(precioVenta);
               p1.setPrecioCompra(precioCompra);
               p1.setIva(iva);
               p1.setExistencia(existencia);
               p1.setCodCategoria(codCategoria);
               p1.setVencimiento(vencimiento);
               return true;
           }
       }
       return false;
}

public static boolean eliminar(long id)
{
       Producto p1=null;
       for(int i=0;i<lp.size();i++)
       {
           p1=(Producto)lp.get(i);
           if(id == p1.getIdProducto()) {
               lp.remove(i);
               con--;
               return true;
           }
       }
       return false;
}

public static DefaultTableModel modeloProducto() {
    String[] columnas = {"Id", "Código", "Descripción", "Categoría", "Precio de compra", "Precio de venta", "IVA", "Existencia", "Vencimiento"};

    Object[][] datos = new Object[lp.size()][9];
    for (int i = 0; i < lp.size(); i++) {
        Producto p = (Producto) lp.get(i);
        datos[i][0] = p.getIdProducto();
        datos[i][1] = p.getCodigo();
        datos[i][2] = p.getDescripcion();
        datos[i][3] = OperacionesCategoria.detalle(p.getCodcategoria());
        datos[i][4] = Tablas.dinero(p.getPrecioCompra());
        datos[i][5] = Tablas.dinero(p.getPrecioVenta());
        if (p.getIva() == 0) {
            datos[i][6] = "Exento";
        } else {
            datos[i][6] = p.getIva() + "%";
        }
        datos[i][7] = p.getExistencia();
        datos[i][8] = p.getVencimiento().toString();
    }
    return Tablas.modelo(columnas, datos);
}

}
