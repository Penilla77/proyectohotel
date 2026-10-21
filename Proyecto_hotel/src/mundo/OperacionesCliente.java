/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import mundo.Cliente;

public class OperacionesCliente {
public static int con=0;
public static LinkedList<Cliente> lc = new LinkedList();

public static void Guardar(Cliente p){
    lc.add(con++, p);
}
public static boolean buscar(String documento)
   {
       boolean saber=false;
       Cliente c1=null;
       for(int i=0;i<lc.size();i++)
       {
           c1=(Cliente)lc.get(i);
           if(documento.equals(c1.getDocumento()))
           {
               con=i;
               saber=true;
           }
       }

       return saber;
   }


   public static void modificar(int id, String nombre, String apellido, int documento, String correo, String telefono, String direccion)
   {
         Cliente c2=null;
         c2=(Cliente)lc.get(con);
         
         c2.setId(id);
         c2.setNombre(nombre);
         c2.setApellido(apellido);
         c2.setDocumento(documento);
         c2.setCorreo(correo);
         c2.setTelefono(telefono);
         c2.setDireccion(direccion);
   }
  
  
public static JTable crearTablaCliente() {
   
    String[] columnas = {"Id", "Nombre", "Apellido", "Documento", "Correo", "Teléfono", "Dirección"};
    

    Object[][] datos = new Object[lc.size()][6];
    for (int i = 0; i < lc.size(); i++) {
        Cliente c = (Cliente) lc.get(i);
        datos[i][0] = c.getId();
        datos[i][1] = c.getNombre();
        datos[i][2] = c.getApellido();
        datos[i][3] = c.getDocumento();
        datos[i][4] = c.getCorreo();
        datos[i][5] = c.getTelefono();
        datos[i][6] = c.getDireccion();
    }
    
    DefaultTableModel modelo = new DefaultTableModel(datos, columnas) ;
    JTable tabla = new JTable(modelo);
    tabla.setRowHeight(25);
    tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    tabla.setAutoCreateRowSorter(true);
    

    tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    tabla.getColumnModel().getColumn(0).setPreferredWidth(100); // Cédula
    tabla.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
    tabla.getColumnModel().getColumn(2).setPreferredWidth(50);  // Edad
    tabla.getColumnModel().getColumn(3).setPreferredWidth(100); // Teléfono
    tabla.getColumnModel().getColumn(4).setPreferredWidth(250); // Dirección
    
    return tabla;
}

}