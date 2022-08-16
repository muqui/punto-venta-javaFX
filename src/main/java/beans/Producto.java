/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

/**
 *
 * @author mq12
 */
public class Producto {

   
    private IntegerProperty idProducto  =new SimpleIntegerProperty();
     private StringProperty codigoBarras=  new SimpleStringProperty();
     private StringProperty nombre=  new SimpleStringProperty();
     private DoubleProperty precioVentaUnitario = new SimpleDoubleProperty();             
     private DoubleProperty cantidad= new SimpleDoubleProperty();
     private StringProperty descripcion=  new SimpleStringProperty();
     private DoubleProperty precioProveedor = new SimpleDoubleProperty();
     private IntegerProperty categoria_id =new SimpleIntegerProperty();
     private DoubleProperty precioMayoreo = new SimpleDoubleProperty();
     private StringProperty comosevende=  new SimpleStringProperty();
     private BooleanProperty inventariar=  new SimpleBooleanProperty();
     private IntegerProperty minimo=new SimpleIntegerProperty();
     private DoubleProperty iva = new SimpleDoubleProperty();
     private DoubleProperty ieps= new SimpleDoubleProperty();
     private BooleanProperty habilitado = new SimpleBooleanProperty();
     private DoubleProperty totalTicket = new SimpleDoubleProperty();
     private Button botonAgregar;
     private Button botonBorrar;
     private Button botonEliminar;

    public Producto(int idProducto,String codigoBarras, String nombre, double precioVentaUnitario, double cantidad, String descripcion,
    double precioProveedor, int categoria_id, double precioMayoreo, String comosevende, boolean inventariar, int minimo, double iva, double ieps, boolean habilitado, 
    Button botonAgregar , Button botonBorrar, Button botonEliminar ) {
        
           this.idProducto  =new SimpleIntegerProperty(idProducto);
       this.codigoBarras=  new SimpleStringProperty(codigoBarras);
       this.nombre=  new SimpleStringProperty(nombre);
       this.precioVentaUnitario = new SimpleDoubleProperty(precioVentaUnitario);             
       this.cantidad= new SimpleDoubleProperty(cantidad);
       this.descripcion=  new SimpleStringProperty(descripcion);
       this.precioProveedor = new SimpleDoubleProperty(precioProveedor);
       this.categoria_id =new SimpleIntegerProperty(categoria_id);
       this.precioMayoreo = new SimpleDoubleProperty(precioMayoreo);
       this.comosevende=  new SimpleStringProperty(comosevende);
       this.inventariar=  new SimpleBooleanProperty(inventariar);
       this.minimo=new SimpleIntegerProperty(minimo);
       this.iva = new SimpleDoubleProperty(iva);
       this.ieps= new SimpleDoubleProperty(ieps);
       this.habilitado = new SimpleBooleanProperty(habilitado);
       this.botonAgregar = botonAgregar;
       this.botonBorrar = botonBorrar;
       this.botonEliminar = botonEliminar;
       this.botonAgregar = new Button("+");
       this.botonBorrar = new Button("-"); 
       this.botonEliminar = new Button("Borrar");
      
        
    }

    public Producto() {
       
    }

    

   

    /**
     * @return the idProducto
     */
    public int getIdProducto() {
       
        return idProducto.get();
    }

    /**
     * @param idProducto the idProducto to set
     */
    public void setIdProducto(int idProducto) {
       
        this.idProducto.set(idProducto);
    }

    /**
     * @return the codigoBarras
     */
    public String getCodigoBarras() {
        return codigoBarras.get();
    }

    /**
     * @param codigoBarras the codigoBarras to set
     */
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras.set(codigoBarras);
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
     * @return the precioVentaUnitario
     */
    public double getPrecioVentaUnitario() {
        return precioVentaUnitario.get();
    }

    /**
     * @param precioVentaUnitario the precioVentaUnitario to set
     */
    public void setPrecioVentaUnitario(double precioVentaUnitario) {
        this.precioVentaUnitario.set(precioVentaUnitario);
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
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion.get();
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    /**
     * @return the precioProveedor
     */
    public double getPrecioProveedor() {
        return precioProveedor.get();
    }

    /**
     * @param precioProveedor the precioProveedor to set
     */
    public void setPrecioProveedor(double precioProveedor) {
        this.precioProveedor.set(precioProveedor);
    }

    /**
     * @return the categoria_id
     */
    public int getCategoria_id() {
        return categoria_id.get();
    }

    /**
     * @param categoria_id the categoria_id to set
     */
    public void setCategoria_id(int categoria_id) {
        this.categoria_id.set(categoria_id);
    }

    /**
     * @return the precioMayoreo
     */
    public double getPrecioMayoreo() {
        return precioMayoreo.get();
    }

    /**
     * @param precioMayoreo the precioMayoreo to set
     */
    public void setPrecioMayoreo(double precioMayoreo) {
        this.precioMayoreo.set(precioMayoreo);
    }

    /**
     * @return the comosevende
     */
    public String getComosevende() {
        return comosevende.get();
    }

    /**
     * @param comosevende the comosevende to set
     */
    public void setComosevende(String comosevende) {
        this.comosevende.set(comosevende);
    }

    /**
     * @return the inventariar
     */
    public boolean getInventariar() {
        return inventariar.get();
    }

    /**
     * @param inventariar the inventariar to set
     */
    public void setInventariar(boolean inventariar) {
        this.inventariar.set(inventariar);
    }

    /**
     * @return the minimo
     */
    public int getMinimo() {
        return minimo.get();
    }

    /**
     * @param minimo the minimo to set
     */
    public void setMinimo(int minimo) {
        this.minimo.set(minimo);
    }

    /**
     * @return the iva
     */
    public double getIva() {
        return iva.get();
    }

    /**
     * @param iva the iva to set
     */
    public void setIva(double iva) {
        this.iva.set(iva);
    }
     /**
     * @return the totalTicket
     */
    public double getTotalTicket() {
        return totalTicket.get();
    }

    /**
     * @param totalTicket the totalTicket to set
     */
    public void setTotalTicket(double totalTicket) {
        this.totalTicket.set(totalTicket);
    }
    /**
     * @return the ieps
     */
    public double getIeps() {
        return ieps.get();
    }

    /**
     * @param ieps the ieps to set
     */
    public void setIeps(double ieps) {
        this.ieps.set(ieps);
    }

    /**
     * @return the habilitado
     */
    public boolean getHabilitado() {
        return habilitado.get();
    }

    /**
     * @param habilitado the habilitado to set
     */
    public void setHabilitado(boolean habilitado) {
        this.habilitado.set(habilitado);
    }
    /**
     * @return the botonAgregar
     */
    public Button getBotonAgregar() {
        return botonAgregar;
    }

    /**
     * @param botonAgregar the botonAgregar to set
     */
    public void setBotonAgregar(Button botonAgregar) {
        this.botonAgregar = botonAgregar;
    }

    /**
     * @return the botonBorrar
     */
    public Button getBotonBorrar() {
        return botonBorrar;
    }

    /**
     * @param botonBorrar the botonBorrar to set
     */
    public void setBotonBorrar(Button botonBorrar) {
        this.botonBorrar = botonBorrar;
    }  

    /**
     * @return the botonEliminar
     */
    public Button getBotonEliminar() {
        return botonEliminar;
    }

    /**
     * @param botonEliminar the botonEliminar to set
     */
    public void setBotonEliminar(Button botonEliminar) {
        this.botonEliminar = botonEliminar;
    }

   
    
}
