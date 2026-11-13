package mundo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Factura {
    public long idFactura;
    public long idVenta;
    public LocalDateTime fecha;
    public String cliente;
    public BigDecimal total;
    public String detalle;
    public Factura(long idFactura, long idVenta, LocalDateTime fecha, String cliente, BigDecimal total, String detalle){
    this.idFactura = idFactura;
    this.idVenta = idVenta;
    this.fecha = fecha;
    this.cliente = cliente;
    this.total = total;
    this.detalle = detalle;
    }
    public long getIdFactura(){
    return idFactura;
    }
    public void setIdFactura(long idFactura){
    this.idFactura = idFactura;
    }
    public long getIdVenta(){
    return idVenta;
    }
    public void setIdVenta(long idVenta){
    this.idVenta = idVenta;
    }
    public LocalDateTime getFecha(){
    return fecha;
    }
    public void setFecha(LocalDateTime fecha){
    this.fecha = fecha;
    }
    public String getCliente(){
    return cliente;
    }
    public void setCliente(String cliente){
    this.cliente = cliente;
    }
    public BigDecimal getTotal(){
    return total;
    }
    public void setTotal(BigDecimal total){
    this.total = total;
    }
    public String getDetalle(){
    return detalle;
    }
    public void setDetalle(String detalle){
    this.detalle = detalle;
    }
}
