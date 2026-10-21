/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;


public class Categoria {
    public int codCategoria;
    public String detalle;
  
    public Categoria(int codCategoria, String detalle){
    this.codCategoria = codCategoria;
    this.detalle = detalle;
    }
    
    public int getCodCategoria(){
    return codCategoria;
    }
    
    public void setCodCategoria(int codCategoria){
    this.codCategoria = codCategoria;
    }
    
    public String getDetalle(){
    return detalle;
    }
    
    public void setDetalle(String detalle){
    this.detalle = detalle;
    }
}
