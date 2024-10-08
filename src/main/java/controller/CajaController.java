/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class CajaController implements Initializable {

    @FXML
    void onActionCrearIngreso(ActionEvent event) {
        System.out.println("MOSTRAR DIALOGO PARA CREAR INGRESO");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/crearNombreIngreso.fxml"));
            Parent root = fxmlLoader.load();
            CrearNombreIngresoController crearNombreIngreso = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            String nombreIngreso = crearNombreIngreso.nombre;
            System.out.println("Nombre= " + nombreIngreso);
        } catch (Exception e) {
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
