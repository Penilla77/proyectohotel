/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.math.BigDecimal;
public class ProductoVendido {
    public long idProductoV;
    public long idProducto;
    public long idVenta;
    public long cantidad;
    public BigDecimal precioUnitario;
    
    public ProductoVendido(long idProductoV, long idProducto, long idVenta, long cantidad, BigDecimal precioUnitario){
    this.idProductoV = idProductoV;
    this.idProducto = idProducto;
    this.idVenta = idVenta;
    this.cantidad = cantidad;
    this.precioUnitario = precioUnitario;
    }
    
    public long getIdProductoV(){
    return idProductoV;
    }
    
    public void setIdProductoV(long idProductoV){
    this.idProductoV = idProductoV;
    }
    
     public long getIdProducto(){
    return idProducto;
    }
    
    public void setIdProducto(long idProducto){
    this.idProducto = idProducto;
    }
    
     public long getIdVenta(){
    return idVenta;
    }
    
    public void setIdVenta(long idVenta){
    this.idVenta = idVenta;
    }
    
    public long getCantidad(){
    return cantidad;
    }
    
    public void setCantidad(long cantidad){
    this.cantidad = cantidad;
    }
    
    public BigDecimal getPrecioUnitario(){
    return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario){
    this.precioUnitario = precioUnitario;
    }
}



