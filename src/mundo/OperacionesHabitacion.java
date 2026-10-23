/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.math.BigDecimal;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OperacionesHabitacion {
public static int con=0;
public static LinkedList<Habitacion> lh = new LinkedList();

public static void Guardar(Habitacion h){
    lh.add(con++, h);
}

public static int nuevoId(){
    int mayor=0;
    for(int i=0;i<lh.size();i++)
    {
        Habitacion h1=(Habitacion)lh.get(i);
        if(h1.getId() > mayor) {
            mayor = h1.getId();
        }
    }
    return mayor + 1;
}

public static Habitacion buscar(int id) {
       Habitacion h1=null;
       for(int i=0;i<lh.size();i++)
       {
           h1=(Habitacion)lh.get(i);
           if(id == h1.getId()) {
               return h1;
           }
       }

       return null;
}

public static Habitacion buscarPorNumero(String numero) {
       Habitacion h1=null;
       for(int i=0;i<lh.size();i++)
       {
           h1=(Habitacion)lh.get(i);
           if(numero.equals(h1.getNumero())) {
               return h1;
           }
       }

       return null;
}

public static String numero(int id) {
       Habitacion h1 = buscar(id);
       if(h1 == null) {
           return "(eliminada)";
       }
       return h1.getNumero();
}


public static boolean modificar(int id, String numero, String estado, String tipoHabitacion, BigDecimal precioNoche, int capacidad)
{
       Habitacion h1=null;
       for(int i=0;i<lh.size();i++)
       {
           h1=(Habitacion)lh.get(i);
           if(id == h1.getId()) {
               h1.setNumero(numero);
               h1.setEstado(estado);
               h1.setTipoHabitacion(tipoHabitacion);
               h1.setPrecioNoche(precioNoche);
               h1.setCapacidad(capacidad);
               return true;
           }
       }
       return false;
}


public static boolean eliminar(int id)
{
       Habitacion h1=null;
       for(int i=0;i<lh.size();i++)
       {
           h1=(Habitacion)lh.get(i);
           if(id == h1.getId()) {
               lh.remove(i);
               con--;
               return true;
           }
       }
       return false;
}


public static DefaultTableModel modeloHabitacion() {
    String[] columnas = {"Id", "Número", "Estado", "Tipo", "Precio por noche", "Capacidad", "Cliente"};

    Object[][] datos = new Object[lh.size()][7];
    for (int i = 0; i < lh.size(); i++) {
        Habitacion h = (Habitacion) lh.get(i);
        datos[i][0] = h.getId();
        datos[i][1] = h.getNumero();
        datos[i][2] = h.getEstado();
        datos[i][3] = h.getTipoHabitacion();
        datos[i][4] = Tablas.dinero(h.getPrecioNoche());
        datos[i][5] = h.getCapacidad();
        if (h.getIdCliente() == 0) {
            datos[i][6] = "-";
        } else {
            datos[i][6] = OperacionesCliente.nombreCompleto(h.getIdCliente());
        }
    }
    return Tablas.modelo(columnas, datos);
}


}
