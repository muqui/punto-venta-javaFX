/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class VentaGranelController implements Initializable {
    
    private BigDecimal cantidad = new BigDecimal("1");

    @FXML
    private TextField txtAmount;

    @FXML
    void keyPressedTxtAmount(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            // Tu código para manejar el evento al presionar Enter
            System.out.println("Enter key pressed");
            // Añade aquí la lógica que deseas ejecutar al presionar Enter
            setCantidad(new BigDecimal(txtAmount.getText()));
            
            
        // Lógica para cerrar la ventana
        Stage stage = (Stage) txtAmount.getScene().getWindow();
        stage.close();
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
    }

    /**
     * @return the cantidad
     */
    public BigDecimal getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the txtAmount
     */
    public TextField getTxtAmount() {
        return txtAmount;
    }

    /**
     * @param txtAmount the txtAmount to set
     */
    public void setTxtAmount(TextField txtAmount) {
        this.txtAmount = txtAmount;
    }

}
