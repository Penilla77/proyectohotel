/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.time.LocalDateTime;
public class Reserva {
   public int idReserva;
   public int idCliente;
   public int idHabitacion;
   public LocalDateTime fechaEntrada;
   public LocalDateTime fechaSalida;
   public LocalDateTime createdAt;
   public String tipoReserva;
   public String estado;
   
   public Reserva(int idReserva, int idCliente, int idHabitacion, LocalDateTime fechaEntrada, LocalDateTime fechaSalida, LocalDateTime createdAt, String tipoReserva, String estado){
       this.idReserva = idReserva;
       this.idCliente = idCliente;
       this.idHabitacion = idHabitacion;
       this.fechaEntrada = fechaEntrada;
       this.fechaSalida = fechaSalida;
       this.createdAt = createdAt;
       this.tipoReserva = tipoReserva;
       this.estado = estado;
   }
   
   public int getIdReserva(){
   return idReserva;
   }
   
   public void setIdReserva(int idReserva){
   this.idReserva = idReserva;
   }
   
   public int getIdCliente(){
   return idCliente;
   }
   
   public void setIdCliente(int idCliente){
   this.idCliente = idCliente;
   }
   
   public int getIdHabitacion(){
   return idHabitacion;
   }
   
   public void setIdHabiatcion(int idHabitacion){
   this.idHabitacion = idHabitacion;
   }
   
   public LocalDateTime getFechaentrada(){
   return fechaEntrada;
   }
   
   public void setFechaEntrada(LocalDateTime fechaEntrada){
   this.fechaEntrada = fechaEntrada;
   }
   
   public LocalDateTime getFechaSalida(){
   return fechaSalida;
   }
   
   public void setFechaSalida(LocalDateTime fechaSalida){
   this.fechaSalida = fechaSalida;
   }
   
   public LocalDateTime getCreatedAt(){
   return createdAt;
   }
   
   public void setCreatedAt(LocalDateTime createdAt){
   this.createdAt = createdAt;
   }
   
   public String getTipoReserva(){
   return tipoReserva;
   }
   
   public void setTipoReserva(String tipoReserva){
   this.tipoReserva = tipoReserva;
   }
   
   public String getEstado(){
   return estado;
   }
   
   public void setEstado(String estado){
   this.estado = estado;
   }
   
   
              
   
   
   
}
