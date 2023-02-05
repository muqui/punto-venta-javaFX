/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import beans.Usuario;
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
public class DaoDepartamento {
    
     private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public ObservableList<String> getSellsTableData(String usuario) {

  

        List<String> list = new ArrayList<>();
        con = ConnectionMYSQLManager.getConnection();
        list.add("Todos");
        try {
           
            String sql = "select * from departamento where activo = 1";
           
            PreparedStatement preparedStmt = con.prepareStatement(sql);
           // preparedStmt.setString(1, usuario);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()) {
               
               
                list.add(resultSet.getString(2));
            }
            con.close();
        } catch (SQLException ex) {
        }
        ObservableList<String> data = FXCollections.observableList(list);

        return data;
    }
    
}
