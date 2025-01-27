/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.IncomeApi;
import dto.IncomeNameDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class CrearNombreIngresoController implements Initializable {
    IncomeApi incomeApi = new IncomeApi();
    IncomeNameDTO incomeNameDTO = new IncomeNameDTO();
    public String nombre = "";

    @FXML
    private TextField txtCrearNombre;

    @FXML
    void onActionCrearNombre(ActionEvent event) {

        nombre = txtCrearNombre.getText().trim();
        incomeNameDTO.setName(nombre);
        incomeApi.crearNombreIngreso(incomeNameDTO);
        Stage stage = (Stage) txtCrearNombre.getScene().getWindow();
        stage.close();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
