/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.ExpenseApi;
import api.IncomeApi;
import dto.ExpenseNameDTO;
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
public class CrearNombreEgresoController implements Initializable {

    IncomeApi incomeApi = new IncomeApi();
    ExpenseApi expenseApi = new ExpenseApi();
    ExpenseNameDTO expenseNameDTO = new ExpenseNameDTO();
    public String nombre = "";
    @FXML
    private TextField txtCrearNombre;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void onActionCrearNombre(ActionEvent event) {
        nombre = txtCrearNombre.getText().trim();
        expenseNameDTO.setName(nombre);
        expenseApi.crearNombreEgreso(expenseNameDTO);
        Stage stage = (Stage) txtCrearNombre.getScene().getWindow();
        stage.close();
    }

}
