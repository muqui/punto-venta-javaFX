/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Producto;
import conexionMYSQL.ConnectionMYSQLManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;

/**
 *
 * @author mq12
 */
public class DaoProducto {

    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public Producto getProducto(String codigo) {
        Producto producto = null;
       // Producto producto = null;
        try {

            con = ConnectionMYSQLManager.getConnection();

            String query = "SELECT * FROM tproducto where codigoBarras = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, codigo);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()) {
                producto = new Producto();
                producto.setIdProducto(resultSet.getInt("idProducto"));
                producto.setCodigoBarras(resultSet.getString("codigoBarras"));
                producto.setNombre(resultSet.getString("nombre"));
                producto.setPrecioVentaUnitario(resultSet.getDouble("precioVentaUnitario"));
                producto.setCantidad(resultSet.getDouble("cantidad"));
                producto.setDescripcion(resultSet.getString("descripcion"));
                producto.setPrecioProveedor(resultSet.getDouble("precioProveedor"));
                producto.setCategoria_id(resultSet.getInt("categoria_id"));
                producto.setPrecioMayoreo(resultSet.getDouble("precioMayoreo"));
                producto.setComosevende(resultSet.getString("comosevende"));
                producto.setInventariar(resultSet.getBoolean("inventariar"));
                producto.setMinimo(resultSet.getInt("minimo"));
                producto.setIva(resultSet.getDouble("IVA"));
                producto.setIeps(resultSet.getDouble("IEPS"));
                producto.setHabilitado(resultSet.getBoolean("habilitado"));
               
            }
            if(producto != null){
                 producto.setBotonAgregar(new Button("+"));
               producto.setBotonBorrar(new Button("-"));
               producto.setBotonEliminar(new Button("Quitar"));
              
            System.out.println("producto desde  = " + producto.getIdProducto());
            }
              
        } catch (SQLException ex) {
            Logger.getLogger(DaoProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        return producto;
    }
}
