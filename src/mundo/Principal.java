/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import gui.Menu;
public class Principal {
    public static void main(String[] args) {
       
        mundo.DatosIniciales.cargar();
        gui.Estilo.instalar();
        Menu x = new Menu();
        x.setVisible(true);
        
        
    }
    
}
