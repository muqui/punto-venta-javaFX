/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Producto;
import beans.VentaDetalle;
import conexionMYSQL.ConnectionMYSQLManager;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mq12
 */
public class DaoProducto {

    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public int saveSale(ObservableList<Producto> data) {
        con = ConnectionMYSQLManager.getConnection();

        java.sql.Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

        String query = " insert into tventa (precioVentaTotal, fechaRegistro, usuario_id, pago)"
                + " values (?, ?, ?, ?)";

        try {
            con.setAutoCommit(false);
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, "12");
            preparedStmt.setTimestamp(2, timeStamp);
            preparedStmt.setInt(3, 1);
            preparedStmt.setString(4, "500");
            System.out.println("insertado");
            int primkey = 0;
            // int affectedRows = preparedStmt.executeUpdate();
            if (preparedStmt.executeUpdate() > 0) {
                // Retrieves any auto-generated keys created as a result of executing this Statement object
                java.sql.ResultSet generatedKeys = preparedStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    primkey = generatedKeys.getInt(1);
                }
            }
            System.out.println("Record updated with id = " + primkey);
            for (int i = 0; i < data.size(); i++) {

                Producto p = data.get(i);
                double precioVentaUnitario = p.getPrecioProveedor() * p.getCantidad();
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.FLOOR);
                double result = new Double(df.format(precioVentaUnitario));
                System.out.println("precio " + precioVentaUnitario);
                query = " insert into tventadetalle (idVenta, idProducto, codigoBarrasProducto, nombreProducto,precioVentaUnitarioProducto, cantidad, totalPrecioVenta,precioProveedor,  IVA, IEPS, imprimir, comosevende)"
                        + " values (?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?)";
                preparedStmt = con.prepareStatement(query);
                preparedStmt.setInt(1, primkey); // id venta
                preparedStmt.setInt(2, p.getIdProducto());  // id producto
                preparedStmt.setString(3, p.getCodigoBarras()); //  codigo de barras
                preparedStmt.setString(4, p.getNombre()); //nombre del producto
                preparedStmt.setDouble(5, p.getPrecioVentaUnitario()); //precio venta unitario  x
                preparedStmt.setDouble(6, p.getCantidad()); // cantidad de productos
                preparedStmt.setDouble(7, p.getTotalTicket()); // total de la venta
                preparedStmt.setDouble(8, result); // precio proveedor
                preparedStmt.setDouble(9, p.getIva()); //iva
                preparedStmt.setDouble(10, p.getIeps());  //ieps
                preparedStmt.setBoolean(11, true); //imprimir
                preparedStmt.setString(12, "Unitario"); //como se vende

                preparedStmt.executeUpdate();
                System.out.println("codigod es desde dao for " + p.getCodigoBarras());

                //actualizar cantidad
                if (p.getInventariar() == true) {

                    Producto productoCantidad = this.getProducto(p.getCodigoBarras());
                    double actual = productoCantidad.getCantidad();
                    double nueva = actual - p.getCantidad();
                    productoCantidad.setCantidad(nueva);
                    System.out.println("cantidad actual " + productoCantidad.getCantidad() + " descontar " + p.getCantidad() + " nueva cantidad " + productoCantidad.getCantidad());

//                    PreparedStatement stmt;
//                    stmt = con.prepareStatement("UPDATE tproducto SET cantidad = ? WHERE campoindice=?");
//                    stmt.setString(1, "14");
                    query = "UPDATE tproducto SET cantidad = ? WHERE codigoBarras=?";
                    preparedStmt = con.prepareStatement(query);
                    preparedStmt.setDouble(1, nueva); // id venta
                    preparedStmt.setString(2, p.getCodigoBarras());  // id producto

                    preparedStmt.executeUpdate();

                }

                //guarda el tipo de pago
                query = " insert into formadepago (tventa_idVenta, tipo, cantidad, referencia)"
                        + " values (?, ?, ?,?)";
                preparedStmt = con.prepareStatement(query);
                preparedStmt.setInt(1, primkey); // id venta
                preparedStmt.setString(2, "EFECTIVO");
                preparedStmt.setDouble(3, p.getCantidad());
                preparedStmt.setString(4, "------");

                preparedStmt.executeUpdate();

            }

            con.commit();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(DaoProducto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    public Producto getProducto(String codigo) {
        Producto producto = null;
        // Producto producto = null;
        try {

            Connection conectionProduct = ConnectionMYSQLManager.getConnection();

            String query = "SELECT * FROM tproducto where codigoBarras = ?";
            PreparedStatement preparedStmt = conectionProduct.prepareStatement(query);
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
            if (producto != null) {
                producto.setBotonAgregar(new Button("+"));
                producto.setBotonBorrar(new Button("-"));
                producto.setBotonEliminar(new Button("Quitar"));

                System.out.println("producto desde  = " + producto.getIdProducto());
            }
            conectionProduct.close();// cambie esto.
        } catch (SQLException ex) {
            Logger.getLogger(DaoProducto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return producto;
    }

    public ObservableList<Producto> getInitialTableData(String filtro) {

        Producto producto = null;

        List<Producto> list = new ArrayList<>();
        con = ConnectionMYSQLManager.getConnection();
        try {
            stmt = con.createStatement();
            //String sql = " select * from celulares where  cel_nombre LIKE '%"+filtro+"%' OR cel_color LIKE '%"+filtro+"%' OR cel_descripcion LIKE '%"+filtro+"%'";
            String sql = "SELECT * FROM tproducto where  codigobarras LIKE '%" + filtro + "%' OR nombre LIKE '%" + filtro + "%'";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                producto = new Producto();
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setCodigoBarras(rs.getString("codigoBarras"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecioVentaUnitario(rs.getDouble("precioVentaUnitario"));
                producto.setCantidad(rs.getDouble("cantidad"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecioProveedor(rs.getDouble("precioProveedor"));
                producto.setCategoria_id(rs.getInt("categoria_id"));
                producto.setPrecioMayoreo(rs.getDouble("precioMayoreo"));
                producto.setComosevende(rs.getString("comosevende"));
                producto.setInventariar(rs.getBoolean("inventariar"));
                producto.setMinimo(rs.getInt("minimo"));
                producto.setIva(rs.getDouble("IVA"));
                producto.setIeps(rs.getDouble("IEPS"));
                producto.setHabilitado(rs.getBoolean("habilitado"));
                producto.setBotonAgregar(new Button("+"));
                list.add(producto);
            }
            con.close();
        } catch (SQLException ex) {
        }
        ObservableList<Producto> data = FXCollections.observableList(list);

        return data;
    }

    public ObservableList<VentaDetalle> getSellsTableData(String fechaInicial, String FechaFinal, String usuario, String departamento) {

        VentaDetalle ventaDetalle = null;

        List<VentaDetalle> list = new ArrayList<>();
        con = ConnectionMYSQLManager.getConnection();
        try {
            if(departamento.equals("Todos")){
                departamento = "";
            }
             if(usuario.equals("Todos")){
                usuario = "";
            }
             System.out.println("usuario "+ usuario);
            // stmt = con.createStatement();
            String sql = "select d.idVenta,v.fechaRegistro, d.codigoBarrasProducto, d.nombreProducto, dep.nombre, d.cantidad, d.precioventaUnitarioProducto,v.pago, d.totalprecioventa, d.precioProveedor, u.nombre  from tventadetalle d inner join tventa v  on v.idventa = d.idventa inner join tproducto p on p.idproducto = d.idproducto inner join departamento dep on p.categoria_id = dep.id inner join usuario u on u.id = v.usuario_id  where u.nombre like ? and v.fecharegistro >=? and v.fecharegistro <=? and d.imprimir = 1 and dep.nombre like ? ORDER BY v.fechaRegistro desc";
         //  String sql = "select d.idVenta,v.fechaRegistro, d.codigoBarrasProducto, d.nombreProducto, dep.nombre, d.cantidad, d.precioventaUnitarioProducto,v.pago, d.totalprecioventa, d.precioProveedor, u.nombre  from tventadetalle d inner join tventa v  on v.idventa = d.idventa inner join tproducto p on p.idproducto = d.idproducto inner join departamento dep on p.categoria_id = dep.id inner join usuario u on u.id = v.usuario_id  where u.nombre = 'Admin' and v.fecharegistro >='2022-09-09 00:00:00' and v.fecharegistro <='2022-09-09 23:59:59' and d.imprimir = 1 and dep.nombre = 'Papeleria' ORDER BY v.fechaRegistro desc";
            System.out.println("" + sql);
            PreparedStatement preparedStmt = con.prepareStatement(sql);
            preparedStmt.setString(1, usuario+"%");
            preparedStmt.setString(2, fechaInicial +  " 00:00:00");
            preparedStmt.setString(3, FechaFinal + " 23:59:59");
            preparedStmt.setString(4,  departamento+"%");
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()) {
                ventaDetalle = new VentaDetalle();
                ventaDetalle.setIdVenta(resultSet.getInt(1));
                ventaDetalle.setFecha(resultSet.getString(2));
                ventaDetalle.setCodigo(resultSet.getString(3));
                ventaDetalle.setNombre(resultSet.getString(4));
                ventaDetalle.setDepartamento(resultSet.getString(5));
                ventaDetalle.setCantidad(resultSet.getDouble(6));
                ventaDetalle.setPrecio(resultSet.getDouble(7));
                ventaDetalle.setPagoCon(resultSet.getDouble(8));
                ventaDetalle.setTotal(resultSet.getDouble(9));
                ventaDetalle.setCosto(resultSet.getDouble(10));
                ventaDetalle.setUsuario(resultSet.getString(11));
                ventaDetalle.setGanacia(ventaDetalle.getTotal() - ventaDetalle.getCosto());
                System.out.println("detalle venta "+  ventaDetalle.getCodigo());
                list.add(ventaDetalle);
            }
            con.close();
        } catch (SQLException ex) {
        }
        ObservableList<VentaDetalle> data = FXCollections.observableList(list);

        return data;
    }

}
