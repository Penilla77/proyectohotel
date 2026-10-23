/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OperacionesCheckin {
public static int con=0;
public static LinkedList<Checkin> lck = new LinkedList();

public static void Guardar(Checkin c){
    lck.add(con++, c);
}

public static int nuevoId(){
    int mayor=0;
    for(int i=0;i<lck.size();i++)
    {
        Checkin c1=(Checkin)lck.get(i);
        if(c1.getIdCheckin() > mayor) {
            mayor = c1.getIdCheckin();
        }
    }
    return mayor + 1;
}


public static Checkin buscar(int idCheckin) {
       Checkin c1=null;
       for(int i=0;i<lck.size();i++)
       {
           c1=(Checkin)lck.get(i);
           if(idCheckin == c1.getIdCheckin()) {
               return c1;
           }
       }

       return null;
}


public static boolean modificar(int idCheckin, LocalDateTime fechaIngreso, LocalDateTime fechaSalida, int noches, int idHabitacion, int idCliente, String transporte, String motivoViaje, String procedencia, int acompañantes)
{
       Checkin c1=null;
       for(int i=0;i<lck.size();i++)
       {
           c1=(Checkin)lck.get(i);
           if(idCheckin == c1.getIdCheckin()) {
               c1.setFechaIngreso(fechaIngreso);
               c1.setFechaSalida(fechaSalida);
               c1.setNoches(noches);
               c1.setIdHabitacion(idHabitacion);
               c1.setIdCliente(idCliente);
               c1.setTransporte(transporte);
               c1.setMotivoViaje(motivoViaje);
               c1.setProcedencia(procedencia);
               c1.setAcompañantes(acompañantes);
               return true;
           }
       }
       return false;
}


public static boolean eliminar(int idCheckin)
{
       Checkin c1=null;
       for(int i=0;i<lck.size();i++)
       {
           c1=(Checkin)lck.get(i);
           if(idCheckin == c1.getIdCheckin()) {
               lck.remove(i);
               con--;
               return true;
           }
       }
       return false;
}


public static DefaultTableModel modeloCheckin() {
    String[] columnas = {"Id", "Cliente", "Habitación", "Ingreso", "Salida", "Noches", "Transporte", "Motivo", "Procedencia", "Acompañantes"};

    Object[][] datos = new Object[lck.size()][10];
    for (int i = 0; i < lck.size(); i++) {
        Checkin c = (Checkin) lck.get(i);
        datos[i][0] = c.getIdCheckin();
        datos[i][1] = OperacionesCliente.nombreCompleto(c.getIdCliente());
        datos[i][2] = OperacionesHabitacion.numero(c.getIdHabitacion());
        datos[i][3] = Tablas.fecha(c.getFechaIngreso());
        datos[i][4] = Tablas.fecha(c.getFechaSalida());
        datos[i][5] = c.getNoches();
        datos[i][6] = c.getTransporte();
        datos[i][7] = c.getMotivoViaje();
        datos[i][8] = c.getProcedencia();
        datos[i][9] = c.getAcompañantes();
    }
    return Tablas.modelo(columnas, datos);
}


}
