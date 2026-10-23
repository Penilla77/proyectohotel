/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OperacionesCliente {
public static int con=0;
public static LinkedList<Cliente> lc = new LinkedList();

public static void Guardar(Cliente p){
    lc.add(con++, p);
}

public static int nuevoId(){
    int mayor=0;
    for(int i=0;i<lc.size();i++)
    {
        Cliente c1=(Cliente)lc.get(i);
        if(c1.getId() > mayor) {
            mayor = c1.getId();
        }
    }
    return mayor + 1;
}

public static Cliente buscar(int id) {
       Cliente c1=null;
       for(int i=0;i<lc.size();i++)
       {
           c1=(Cliente)lc.get(i);
           if(id == c1.getId()) {
               return c1;
           }
       }

       return null;
}

public static Cliente buscarPorDocumento(int documento) {
       Cliente c1=null;
       for(int i=0;i<lc.size();i++)
       {
           c1=(Cliente)lc.get(i);
           if(documento == c1.getDocumento()) {
               return c1;
           }
       }

       return null;
}

public static String nombreCompleto(int id) {
       Cliente c1 = buscar(id);
       if(c1 == null) {
           return "(eliminado)";
       }
       return c1.getNombre() + " " + c1.getApellido();
}


public static boolean modificar(int id, String nombre, String apellido, int documento, String correo, String telefono, String direccion, String sexo)
{
       Cliente c1=null;
       for(int i=0;i<lc.size();i++)
       {
           c1=(Cliente)lc.get(i);
           if(id == c1.getId()) {
               c1.setNombre(nombre);
               c1.setApellido(apellido);
               c1.setDocumento(documento);
               c1.setCorreo(correo);
               c1.setTelefono(telefono);
               c1.setDireccion(direccion);
               c1.setSexo(sexo);
               return true;
           }
       }
       return false;
}


public static boolean eliminar(int id)
{
       Cliente c1=null;
       for(int i=0;i<lc.size();i++)
       {
           c1=(Cliente)lc.get(i);
           if(id == c1.getId()) {
               lc.remove(i);
               con--;
               return true;
           }
       }
       return false;
}


public static DefaultTableModel modeloCliente() {
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
