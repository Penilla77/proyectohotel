package mundo;

import java.time.LocalDateTime;

public class Cuenta {
    public int idCuenta;
    public int idCheckin;
    public int idCliente;
    public int idHabitacion;
    public LocalDateTime fechaApertura;
    public String estado;
    public long idVenta;
    public Cuenta(int idCuenta, int idCheckin, int idCliente, int idHabitacion, LocalDateTime fechaApertura, String estado, long idVenta){
    this.idCuenta = idCuenta;
    this.idCheckin = idCheckin;
    this.idCliente = idCliente;
    this.idHabitacion = idHabitacion;
    this.fechaApertura = fechaApertura;
    this.estado = estado;
    this.idVenta = idVenta;
    }
    public int getIdCuenta(){
    return idCuenta;
    }
    public void setIdCuenta(int idCuenta){
    this.idCuenta = idCuenta;
    }
    public int getIdCheckin(){
    return idCheckin;
    }
    public void setIdCheckin(int idCheckin){
    this.idCheckin = idCheckin;
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
    public void setIdHabitacion(int idHabitacion){
    this.idHabitacion = idHabitacion;
    }
    public LocalDateTime getFechaApertura(){
    return fechaApertura;
    }
    public void setFechaApertura(LocalDateTime fechaApertura){
    this.fechaApertura = fechaApertura;
    }
    public String getEstado(){
    return estado;
    }
    public void setEstado(String estado){
    this.estado = estado;
    }
    public long getIdVenta(){
    return idVenta;
    }
    public void setIdVenta(long idVenta){
    this.idVenta = idVenta;
    }
}
