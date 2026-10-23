/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.time.LocalDateTime;
public class Checkin {
    public int idCheckin;
    public LocalDateTime fechaIngreso;
    public LocalDateTime fechaSalida;
    public int noches;
    public int idHabitacion;
    public int idCliente;
    public String transporte;
    public String motivoViaje;
    public String procedencia;
    public int acompañantes;
    
    
    public Checkin(int idCheckin, LocalDateTime fechaIngreso, LocalDateTime fechaSalida, int noches, int idHabitacion, int idCliente, String transporte, String motivoViaje, String procedencia, int acompañantes){
    this.idCheckin = idCheckin;
    this.fechaIngreso = fechaIngreso;
    this.fechaSalida = fechaSalida; 
    this.noches = noches;
    this.idHabitacion = idHabitacion;
    this.idCliente = idCliente;
    this.transporte = transporte;
    this.motivoViaje = motivoViaje;
    this.procedencia = procedencia;
    this.acompañantes = acompañantes; 
    }
    
    public int getIdCheckin(){
    return idCheckin;
    }
    
    public void setIdCheckin(int idCheckin){
    this.idCheckin = idCheckin;
    }
    
    public LocalDateTime getFechaIngreso(){
    return fechaIngreso;
    }
    
    public void setFechaIngreso(LocalDateTime fechaIngreso){
    this.fechaIngreso = fechaIngreso;
    }
    
    public LocalDateTime getFechaSalida(){
    return fechaSalida;
    }
    
    public void setFechaSalida(LocalDateTime fechaSalida){
    this.fechaSalida = fechaSalida;
    }
    
    public int getNoches(){
    return noches;
    }
    
    public void setNoches(int noches){
    this.noches = noches;
    }
    
    public int getIdHabitacion(){
    return idHabitacion;
    }
    
    public void setIdHabitacion(int idHabitacion){
    this.idHabitacion = idHabitacion;
    }
    
    public int getIdCliente(){
    return idCliente;
    }
    
    public void setIdCliente(int idCliente){
    this.idCliente = idCliente;
    }
    
    public String getTransporte(){
    return transporte;
    }
    
    public void setTransporte(String transporte){
    this.transporte = transporte;
    }
    
    public String getMotivoViaje(){
    return motivoViaje;
    }
    
    public void setMotivoViaje(String motivoViaje){
    this.motivoViaje = motivoViaje;
    }
    
    public String getProcedencia(){
    return procedencia;
    }
    
    public void setProcedencia(String procedencia){
    this.procedencia = procedencia;
    }
    
    public int getAcompañantes(){
    return acompañantes;
    }
    
    public void setAcompañantes(int acompañantes){
    this.acompañantes = acompañantes;
    }
    
    
}


