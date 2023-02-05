/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import beans.Usuario;
import beans.VentaDetalle;
import conexionMYSQL.ConnectionMYSQLManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author albert
 */
public class DaoPersona {

    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public ObservableList<String> getSellsTableData(String usuario) {

        Usuario user = null;

        List<String> list = new ArrayList<>();
        con = ConnectionMYSQLManager.getConnection();
        list.add("Todos");
        try {
            // stmt = con.createStatement();
            // show active users
            String sql = "select * from usuario where activo = 1";
            //String sql = "SELECT * FROM tproducto where  codigobarras LIKE '%" + filtro + "%' OR nombre LIKE '%" + filtro + "%'";
            // rs = stmt.executeQuery(sql);
            PreparedStatement preparedStmt = con.prepareStatement(sql);
           // preparedStmt.setString(1, usuario);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()) {
                user = new Usuario();
                user.setNivel(resultSet.getInt(2));
                user.setNombre(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));
               
                list.add(resultSet.getString(3));
            }
            con.close();
        } catch (SQLException ex) {
        }
        ObservableList<String> data = FXCollections.observableList(list);

        return data;
    }

}
