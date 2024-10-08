package com.albertocoronanavarro.puntoventafx;

import dto.UserDTO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class PrimaryController implements Initializable {

    UserDTO usuario = new UserDTO();
    Node nodeVenta, nodeProducto, nodeReporte, nodeInventory, nodeCaja;

    @FXML
    private Button btnCaja;

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

    @FXML
    void actionBtnReports(ActionEvent event) {
        nodeReports();
    }

    @FXML
    void actionBtnInventory(ActionEvent event) {
        nodeInventory();
    }

    private void nodeVentas() {
        pantallaVentas();
    }

    @FXML
    void onActionCaja(ActionEvent event) {

        pantallaCaja();
    }

    private void pantallaCaja() {
        System.out.println("Cargar Caja");
        try {

            if (nodeCaja == null) {
//                Usuario u = new Usuario();
//                u.setNombre("Alngel");
                nodeCaja = (Node) FXMLLoader.load(getClass().getResource("/views/caja.fxml"));

            }

            anchorPaneContenido.getChildren().setAll(nodeCaja);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void nodeProductos() {
        System.out.println("Cargar alta producto");
        try {

            if (nodeProducto == null) {

                nodeProducto = (Node) FXMLLoader.load(getClass().getResource("/views/Product.fxml"));

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
//                Usuario u = new Usuario();
//                u.setNombre("Alngel");
                nodeVenta = (Node) FXMLLoader.load(getClass().getResource("/views/ventas.fxml"));

            }

            anchorPaneContenido.getChildren().setAll(nodeVenta);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void nodeReports() {

        System.out.println("Cargar reporte");
        try {

//            if (nodeReporte == null) {
////                Usuario u = new Usuario();
////                u.setNombre("Alngel");
//                nodeReporte = (Node) FXMLLoader.load(getClass().getResource("/views/Reportes.fxml"));
//                
//            }
            nodeReporte = (Node) FXMLLoader.load(getClass().getResource("/views/Reportes.fxml"));

            anchorPaneContenido.getChildren().setAll(nodeReporte);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void nodeInventory() {
        System.out.println("Cargar inventario");
        try {
            nodeInventory = (Node) FXMLLoader.load(getClass().getResource("/views/Inventory.fxml"));
            anchorPaneContenido.getChildren().setAll(nodeInventory);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
