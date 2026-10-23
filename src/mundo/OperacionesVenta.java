/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OperacionesVenta {
public static int con=0;
public static LinkedList<Venta> lv = new LinkedList();

public static void Guardar(Venta v){
    lv.add(con++, v);
}

public static long nuevoId(){
    long mayor=0;
    for(int i=0;i<lv.size();i++)
    {
        Venta v1=(Venta)lv.get(i);
        if(v1.getIdVenta() > mayor) {
            mayor = v1.getIdVenta();
        }
    }
    return mayor + 1;
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


public static Venta buscar(long id) {
       Venta v1=null;
       for(int i=0;i<lv.size();i++)
       {
           v1=(Venta)lv.get(i);
           if(id == v1.getIdVenta()) {
               return v1;
           }
       }

       return null;
}


public static boolean modificar(long id, LocalDateTime fecha, BigDecimal total, BigDecimal iva5, BigDecimal iva19, BigDecimal exento, String tipoPago, int idCliente)
{
       Venta v1=null;
       for(int i=0;i<lv.size();i++)
       {
           v1=(Venta)lv.get(i);
           if(id == v1.getIdVenta()) {
               v1.setFecha(fecha);
               v1.setTotal(total);
               v1.setIva5(iva5);
               v1.setIva19(iva19);
               v1.setExento(exento);
               v1.setTipoPago(tipoPago);
               v1.setIdCliente(idCliente);
               return true;
           }
       }
       return false;
}


public static boolean eliminar(long id)
{
       Venta v1=null;
       for(int i=0;i<lv.size();i++)
       {
           v1=(Venta)lv.get(i);
           if(id == v1.getIdVenta()) {
               lv.remove(i);
               con--;
               return true;
           }
       }
       return false;
}


public static DefaultTableModel modeloVenta() {
    String[] columnas = {"Id", "Fecha", "Cliente", "Tipo de pago", "Exento", "IVA 5%", "IVA 19%", "Total"};

    Object[][] datos = new Object[lv.size()][8];
    for (int i = 0; i < lv.size(); i++) {
        Venta v = (Venta) lv.get(i);
        datos[i][0] = v.getIdVenta();
        datos[i][1] = Tablas.fecha(v.getFecha());
        datos[i][2] = OperacionesCliente.nombreCompleto(v.getIdCliente());
        datos[i][3] = v.getTipoPago();
        datos[i][4] = Tablas.dinero(v.getExento());
        datos[i][5] = Tablas.dinero(v.getIva5());
        datos[i][6] = Tablas.dinero(v.getIva19());
        datos[i][7] = Tablas.dinero(v.getTotal());
    }
    return Tablas.modelo(columnas, datos);
}


}
