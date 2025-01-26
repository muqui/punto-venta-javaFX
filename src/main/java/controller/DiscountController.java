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
import javafx.scene.control.Slider;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class DiscountController implements Initializable {

    private String descuento;
    @FXML
    private Button btnMayoreo;

    @FXML
    private Button btnNormal;

    @FXML
    private Button btnPorcentaje;

    @FXML
    private Slider sliderPorcentaje;

    @FXML
    void onACtionPorcentaje(ActionEvent event) {
        setDescuento("" + (int) sliderPorcentaje.getValue());
        Stage stage = (Stage) sliderPorcentaje.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onActionCompra(ActionEvent event) {
        setDescuento("purchasePrice");
        Stage stage = (Stage) sliderPorcentaje.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onActionMayoreo(ActionEvent event) {
        setDescuento("wholesalePrice");
        Stage stage = (Stage) sliderPorcentaje.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onActionNormal(ActionEvent event) {
        setDescuento("");
        Stage stage = (Stage) sliderPorcentaje.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        sliderPorcentaje.setMin(0); // Valor mínimo
        sliderPorcentaje.setMax(100); // Valor máximo
        sliderPorcentaje.setValue(50); // Valor inicial
        sliderPorcentaje.setShowTickLabels(true); // Mostrar etiquetas
        sliderPorcentaje.setShowTickMarks(true); // Mostrar marcas
        sliderPorcentaje.setMajorTickUnit(10); // Intervalo entre etiquetas principales
        sliderPorcentaje.setMinorTickCount(5); // Intervalos entre marcas menores
        sliderPorcentaje.setBlockIncrement(1); // Incremento por bloque

        // Label label = new Label("Valor: " + (int) slider.getValue());
        System.out.println("" + (int) sliderPorcentaje.getValue());
        // Agregar un listener para actualizar el Label mientras se mueve el Slider
        sliderPorcentaje.valueProperty().addListener((observable, oldValue, newValue) -> {
            // label.setText("Valor: " + newValue.intValue());
            System.out.println("" + (int) sliderPorcentaje.getValue());
        });
    }

    /**
     * @return the descuento
     */
    public String getDescuento() {
        return descuento;
    }

    /**
     * @param descuento the descuento to set
     */
    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

}
