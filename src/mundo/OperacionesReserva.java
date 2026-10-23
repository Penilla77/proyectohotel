/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OperacionesReserva {
public static int con=0;
public static LinkedList<Reserva> lr = new LinkedList();

public static void Guardar(Reserva r){
    lr.add(con++, r);
}

public static int nuevoId(){
    int mayor=0;
    for(int i=0;i<lr.size();i++)
    {
        Reserva r1=(Reserva)lr.get(i);
        if(r1.getIdReserva() > mayor) {
            mayor = r1.getIdReserva();
        }
    }
    return mayor + 1;
}


public static Reserva buscar(int idReserva) {
       Reserva r1=null;
       for(int i=0;i<lr.size();i++)
       {
           r1=(Reserva)lr.get(i);
           if(idReserva == r1.getIdReserva()) {
               return r1;
           }
       }

       return null;
}


public static boolean modificar(int idReserva, int idCliente, int idHabitacion, LocalDateTime fechaEntrada, LocalDateTime fechaSalida, LocalDateTime createdAt, String tipoReserva, String estado)
{
       Reserva r1=null;
       for(int i=0;i<lr.size();i++)
       {
           r1=(Reserva)lr.get(i);
           if(idReserva == r1.getIdReserva()) {
               r1.setIdCliente(idCliente);
               r1.setIdHabitacion(idHabitacion);
               r1.setFechaEntrada(fechaEntrada);
               r1.setFechaSalida(fechaSalida);
               r1.setCreatedAt(createdAt);
               r1.setTipoReserva(tipoReserva);
               r1.setEstado(estado);
               return true;
           }
       }
       return false;
}


public static boolean eliminar(int idReserva)
{
       Reserva r1=null;
       for(int i=0;i<lr.size();i++)
       {
           r1=(Reserva)lr.get(i);
           if(idReserva == r1.getIdReserva()) {
               lr.remove(i);
               con--;
               return true;
           }
       }
       return false;
}


public static DefaultTableModel modeloReserva() {
    String[] columnas = {"Id", "Cliente", "Habitación", "Entrada", "Salida", "Tipo", "Estado"};

    Object[][] datos = new Object[lr.size()][7];
    for (int i = 0; i < lr.size(); i++) {
        Reserva r = (Reserva) lr.get(i);
        datos[i][0] = r.getIdReserva();
        datos[i][1] = OperacionesCliente.nombreCompleto(r.getIdCliente());
        datos[i][2] = OperacionesHabitacion.numero(r.getIdHabitacion());
        datos[i][3] = Tablas.fecha(r.getFechaEntrada());
        datos[i][4] = Tablas.fecha(r.getFechaSalida());
        datos[i][5] = r.getTipoReserva();
        datos[i][6] = r.getEstado();
    }
    return Tablas.modelo(columnas, datos);
}


}
