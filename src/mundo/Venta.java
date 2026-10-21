/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Venta {
    public long idVenta;
    public LocalDateTime fecha;
    public BigDecimal total;
    public BigDecimal iva5;
    public BigDecimal iva19;
    public BigDecimal exento;
    public String tipoPago;
    public int idCliente;


    public Venta(long idVenta, LocalDateTime fecha, BigDecimal total, BigDecimal iva5, BigDecimal iva19, BigDecimal exento, String tipoPago, int idCliente){
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.total = total;
        this.iva5 = iva5;
        this.iva19 = iva19;
        this.exento = exento;
        this.tipoPago = tipoPago;
        this.idCliente = idCliente;
    }
    
    public long getIdVenta(){
    return idVenta;
    }
    
    public void setIdventa(long idVenta){
    this.idVenta = idVenta;
    }
    
    public LocalDateTime getFecha(){
    return fecha;
    }
    
    public void setFecha(LocalDateTime fecha){
    this.fecha = fecha;
    }
    
    public BigDecimal getTotal(){
    return total;
    }
    
    public void setTotal(BigDecimal total){
    this.total = total;
    }
    
    public BigDecimal getIva5(){
    return iva5;
    }
    
    public void setIva5(BigDecimal iva5){
    this.iva5 = iva5;
    }
    
    public BigDecimal getIva19(){
    return iva19;
    }
    
    public void setIva19(BigDecimal iva19){
    this.iva19 = iva19;
    }
    
    public BigDecimal getExento(){
    return exento;
    }
    
    public void setExento(BigDecimal exento){
    this.exento = exento;
    }
    
    public String getTipoPago(){
    return tipoPago;
    }
    
    public void setTipoPago(String tipoPago){
    this.tipoPago = tipoPago;
    }
    
    public int getIdCliente(){
    return idCliente;
    }
    
    public void setIdCliente(int idCliente){
    this.idCliente = idCliente;
    }
    
}