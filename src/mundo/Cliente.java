/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;


public class Cliente {
    public int id;
    public String nombre;
    public String apellido;
    public int documento;
    public String correo;
    public String telefono;
    public String direccion;
    public String sexo;
    
public Cliente(int id, String nombre, String apellido, int documento, String correo, String telefono, String direccion, String sexo){
    this.id = id;
    this.nombre = nombre;
    this.apellido  = apellido;
    this.documento = documento;
    this.correo = correo;
    this.telefono = telefono;
    this.direccion = direccion;
    this.sexo = sexo;
}    
    public int getId(){
    return id;
    }
    
    public void setId(int id){
    this.id = id;
    }

    public String getNombre(){
    return nombre;
    }
    
    public void setNombre(String nombre){
    this.nombre = nombre;
    }
    
    public String getApellido(){
    return apellido;
    }
    
    public void setApellido(String apellido){
    this.apellido = apellido;
    }
    
    public int getDocumento(){
    return documento;
    }
    
    public void setDocumento(int documento){
    this.documento = documento; 
    }
    
    public String getCorreo(){
    return correo; 
    }
    
    public void setCorreo(String correo){
    this.correo = correo;
    }
    
    public String getTelefono(){
    return telefono;
    }
    
    public void setTelefono(String telefono){
    this.telefono = telefono;
    }
    
    public String getDireccion(){
    return direccion;
    }
    
    public void setDireccion(String direccion){
    this.direccion = direccion;
    }
    
    public String getSexo(){
    return sexo;
    }
    
    public void setSexo(String sexo){
    this.sexo = sexo;
    }
    
}
