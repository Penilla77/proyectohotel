/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.time.LocalDate;
import java.math.BigDecimal;
public class Producto {
    public long idProducto;
    public String codigo;
    public String descripcion;
    public BigDecimal precioVenta;
    public BigDecimal precioCompra;
    public int iva;
    public int existencia;
    public long idUsuario;
    public int codCategoria;
    public LocalDate vencimiento;
    
    public Producto(long idProducto, String codigo, String descripcion, BigDecimal precioVenta, BigDecimal precioCompra, int iva, int existencia, long idUsuario, int codCategoria, LocalDate vencimiento){
    this.idProducto = idProducto;
    this.codigo = codigo;
    this.descripcion = descripcion;
    this.precioVenta = precioVenta; 
    this.precioCompra = precioCompra;
    this.iva = iva;
    this.existencia = existencia;
    this.idUsuario = idUsuario;
    this.codCategoria = codCategoria;
    this.vencimiento = vencimiento;
    }
    
    public long getIdProducto(){
    return idProducto;
    }
    
    public void setIdProducto(long idProducto){
    this.idProducto = idProducto;
    }
    
    public String getCodigo(){
    return codigo;
    }
    
    public void setCodigo(String codigo){
    this.codigo = codigo;
    }
    
    public String getDescripcion(){
    return descripcion;
    }
    
    public void setDescripcion(String descripcion){
    this.descripcion = descripcion;
    }
    
    public BigDecimal getPrecioVenta(){
    return precioVenta;
    }
    
    public void setPrecioVenta(BigDecimal precioVenta){
    this.precioVenta = precioVenta;
    }
    
    public BigDecimal getPrecioCompra(){
    return precioCompra;
    }
    
    public void setPrecioCompra(BigDecimal precioCompra){
    this.precioCompra = precioCompra;
    }
    
    public int getIva(){
    return iva;
    }
    
    public void setIva(int iva){
    this.iva = iva;
    }
    
    public int getExistencia(){
    return existencia;
    }
    
    public void setExistencia(int existencia){
    this.existencia = existencia;
    }
    
    public long getIdUsuario(){
    return idUsuario;
    }
    
    public void setIdUsuario(long idUsuario){
    this.idUsuario = idUsuario;
    }
    
    public int getCodcategoria(){
    return codCategoria;
    }
    
    public void setCodCategoria(int codCategoria){
    this.codCategoria = codCategoria;
    }
    
    public LocalDate getVencimiento(){
    return vencimiento;
    }
    
    public void setVencimiento(LocalDate vencimiento){
    this.vencimiento = vencimiento;
    }
}
