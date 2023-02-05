/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author albert
 */
public class VentaDetalle {

   
    

    /**
     * @return the idVenta
     */
    public int getIdVenta() {
        return idVenta.get();
    }

    /**
     * @param idVenta the idVenta to set
     */
    public void setIdVenta(int idVenta) {
        this.idVenta.set(idVenta);
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha.get();
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo.get();
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    /**
     * @return the departamento
     */
    public String getDepartamento() {
        return departamento.get();
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(String departamento) {
        this.departamento.set(departamento);
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario.get();
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario.set(usuario);
    }

    /**
     * @return the cantidad
     */
    public double getCantidad() {
        return cantidad.get();
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(double cantidad) {
        this.cantidad.set(cantidad);
    }

    /**
     * @return the precio
     */
    public double getPrecio() {
        return precio.get();
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    /**
     * @return the costo
     */
    public double getCosto() {
        return costo.get();
    }

    /**
     * @param costo the costo to set
     */
    public void setCosto(double costo) {
        this.costo.set(costo);
    }

    /**
     * @return the pagoCon
     */
    public double getPagoCon() {
        return pagoCon.get();
    }

    /**
     * @param pagoCon the pagoCon to set
     */
    public void setPagoCon(double pagoCon) {
        this.pagoCon.set(pagoCon);
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total.get();
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total.set(total);
    }
     /**
     * @return the ganacia
     */
    public double getGanacia() {
        return ganacia.get();
    }

    /**
     * @param ganacia the ganacia to set
     */
    public void setGanacia(double ganacia) {
        this.ganacia.set(ganacia);
    }

     private IntegerProperty idVenta  =new SimpleIntegerProperty(); //1
     private StringProperty fecha=  new SimpleStringProperty();//2
     private StringProperty codigo=  new SimpleStringProperty();//3
     private StringProperty nombre = new SimpleStringProperty();   //4          
     private StringProperty departamento= new SimpleStringProperty();//5
     private StringProperty usuario=  new SimpleStringProperty();//6
     private DoubleProperty cantidad = new SimpleDoubleProperty();//7
     private DoubleProperty precio = new SimpleDoubleProperty();//8
     
     private DoubleProperty costo = new SimpleDoubleProperty();//9
     private DoubleProperty pagoCon=  new SimpleDoubleProperty();//10
     private DoubleProperty total=  new SimpleDoubleProperty();//11
     private DoubleProperty ganacia = new SimpleDoubleProperty();//12

   
   

   
     
   
    
}
