/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.math.BigDecimal;
public class Habitacion {
    public int id;
    public String numero;
    public String estado;
    public String tipoHabitacion;
    public BigDecimal precioNoche;
    public int capacidad;
    public int idCliente;
    
 public Habitacion(int id, String numero, String estado, String tipoHabitacion, BigDecimal precioNoche, int capacidad, int idCliente){
    this.id =id;
    this.numero = numero;
    this.estado = estado;
    this.tipoHabitacion = tipoHabitacion;
    this.precioNoche = precioNoche;
    this.capacidad = capacidad;
    this.idCliente = idCliente;
 }
 
    public int getId(){
    return id;
    }
    
    public void setId(int id){
    this.id = id;
    }
    
    public String getNumero(){
    return numero;
    }
    
    public void setNumero(String numero){
    this.numero = numero;
    }
    
    public String getEstado(){
    return estado;
    }
    
    public void setEstado(String estado){
    this.estado = estado;
    }
    
    public String getTipoHabitacion(){
    return tipoHabitacion;
    }
    
    public void setTipoHabitacion(String tipoHabitacion){
    this.tipoHabitacion = tipoHabitacion;
    }
    
    public BigDecimal getPrecioNoche(){
    return precioNoche;
    }
    
    public void setPrecioNoche(BigDecimal precioNoche){
    this.precioNoche = precioNoche;
    }
    
    public int getCapacidad(){
    return capacidad;
    }
    
    public void setCapacidad(int capacidad){
    this.capacidad = capacidad;
    }
    
    public int getIdCliente(){
    return idCliente;
    }
    
    public void setIdCliente(int idCliente){
    this.idCliente = idCliente;
    }
}
