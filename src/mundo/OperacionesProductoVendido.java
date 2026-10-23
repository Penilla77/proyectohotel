package mundo;

import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class OperacionesProductoVendido {
public static int con=0;
public static LinkedList<ProductoVendido> lpv = new LinkedList();

public static void Guardar(ProductoVendido pv){
    lpv.add(con++, pv);
}

public static long nuevoId(){
    long mayor=0;
    for(int i=0;i<lpv.size();i++)
    {
        ProductoVendido pv1=(ProductoVendido)lpv.get(i);
        if(pv1.getIdProductoV() > mayor) {
            mayor = pv1.getIdProductoV();
        }
    }
    return mayor + 1;
}

public static void eliminarDeVenta(long idVenta)
{
       ProductoVendido pv1=null;
       for(int i=0;i<lpv.size();i++)
       {
           pv1=(ProductoVendido)lpv.get(i);
           if(idVenta == pv1.getIdVenta()) {
               lpv.remove(i);
               con--;
               i--;
           }
       }
}

public static DefaultTableModel modeloResumen(LinkedList<ProductoVendido> items) {
    String[] columnas = {"Producto", "Cantidad", "Precio", "IVA", "Subtotal"};

    Object[][] datos = new Object[items.size()][5];
    for (int i = 0; i < items.size(); i++) {
        ProductoVendido pv = (ProductoVendido) items.get(i);
        Producto p = OperacionesProducto.buscar(pv.getIdProducto());
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
        datos[i][4] = Tablas.dinero(pv.getPrecioUnitario().multiply(new java.math.BigDecimal(pv.getCantidad())));
    }
    return Tablas.modelo(columnas, datos);
}

}
