package com.albertocoronanavarro.puntoventafx;

import controller.VentasController;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

public class PrimaryController implements Initializable {

    Node nodeVenta, nodeProducto;

    @FXML
    private AnchorPane anchorPaneContenido;

    @FXML
    private Button btnVender;

    @FXML
    void actionBtnVender(ActionEvent event) {
        nodeVentas();
    }

    @FXML
    void actionBtnProducts(ActionEvent event) {
        nodeProductos();
    }

    private void nodeVentas() {
        pantallaVentas();
    }

    private void nodeProductos() {
        try {

            if (nodeProducto == null) {
                System.out.println("VACIO");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("success");
                alert.setHeaderText(null);
                alert.setContentText("Infromatin Message");
                alert.showAndWait();
                nodeProducto = (Node) FXMLLoader.load(getClass().getResource("/views/productos.fxml"));

            }
            anchorPaneContenido.getChildren().setAll(nodeProducto);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pantallaVentas();
    }

    private void pantallaVentas() {
        try {

            if (nodeVenta == null) {

                nodeVenta = (Node) FXMLLoader.load(getClass().getResource("/views/ventas.fxml"));

                //   VentasController controller = loader.getController();
                // controller.crearTicket("UNO");
            }

            anchorPaneContenido.getChildren().setAll(nodeVenta);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
