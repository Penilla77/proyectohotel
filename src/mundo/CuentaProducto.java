package mundo;

import java.math.BigDecimal;

public class CuentaProducto {
    public long idCuentaProducto;
    public int idCuenta;
    public long idProducto;
    public long cantidad;
    public BigDecimal precioUnitario;
    public CuentaProducto(long idCuentaProducto, int idCuenta, long idProducto, long cantidad, BigDecimal precioUnitario){
    this.idCuentaProducto = idCuentaProducto;
    this.idCuenta = idCuenta;
    this.idProducto = idProducto;
    this.cantidad = cantidad;
    this.precioUnitario = precioUnitario;
    }
    public long getIdCuentaProducto(){
    return idCuentaProducto;
    }
    public void setIdCuentaProducto(long idCuentaProducto){
    this.idCuentaProducto = idCuentaProducto;
    }
    public int getIdCuenta(){
    return idCuenta;
    }
    public void setIdCuenta(int idCuenta){
    this.idCuenta = idCuenta;
    }
    public long getIdProducto(){
    return idProducto;
    }
    public void setIdProducto(long idProducto){
    this.idProducto = idProducto;
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
