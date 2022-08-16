/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class MasDeUnProductoController implements Initializable {

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the cantidad
     */
    public String getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the txtCodigo
     */
    public TextField getTxtCodigo() {
        return txtCodigo;
    }

    /**
     * @param txtCodigo the txtCodigo to set
     */
    public void setTxtCodigo(TextField txtCodigo) {
        this.txtCodigo = txtCodigo;
    }
    private String codigo;
    private String cantidad;
    
    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtCodigo;
    
      @FXML
    void actionbtnGuardar(ActionEvent event) {
        setCodigo(this.getTxtCodigo().getText());
        setCantidad(this.txtCantidad.getText());
        Stage stage = (Stage) this.btnAceptar.getScene().getWindow();
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
