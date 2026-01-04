/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        insertToTicket();
    }

    @FXML
    void onActionBtnCancel(ActionEvent event) {
        Stage stage = (Stage) this.btnAceptar.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         txtCodigo.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            newScene.windowProperty().addListener((obsWin, oldWin, newWin) -> {
                if (newWin != null) {
                    Platform.runLater(() -> txtCodigo.requestFocus());
                }
            });
        }
    });
         
        txtCodigo.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    insertToTicket();
                }
            }

        });
        txtCantidad.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    insertToTicket();
                }
            }

        });
    }

    public void insertToTicket() {
        setCodigo(this.getTxtCodigo().getText());
        setCantidad(this.txtCantidad.getText());
        Stage stage = (Stage) this.btnAceptar.getScene().getWindow();
        stage.close();
    }

}
