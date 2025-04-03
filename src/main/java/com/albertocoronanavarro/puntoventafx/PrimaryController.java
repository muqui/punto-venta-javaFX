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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


//https://www.flaticon.es/
public class PrimaryController implements Initializable {

   // private UserDTO usuario = new UserDTO();
    Node nodeVenta, nodeProducto, nodeReporte, nodeInventory, nodeCaja, nodeReparaciones;

    @FXML
    private Button btnCaja;

    @FXML
    private AnchorPane anchorPaneContenido;

    @FXML
    private Button btnVender;

    @FXML
    private Button btnClients;

    @FXML
    private Button btnConfig;

    @FXML
    private Button btnInventory;

    @FXML
    private Button btnProductos;

    @FXML
    private Button btnReparaciones;

    @FXML
    private Button btnReports;

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
    void onActionReparaciones(ActionEvent event) {
        nodeReparaciones();
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
        
        setImageToButton(btnVender, "ventas.png");
        setImageToButton(btnProductos, "productos.png");
        setImageToButton(btnInventory, "inventario.png");
        setImageToButton(btnClients, "clientes.png");
        setImageToButton(btnConfig, "config.png");
        setImageToButton(btnReports, "reportes.png");
        setImageToButton(btnCaja, "caja.png");
        setImageToButton(btnReparaciones, "reparaciones.png");

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

    private void nodeReparaciones() {
        System.out.println("Cargar Reparaciones");
        try {
            nodeReparaciones = (Node) FXMLLoader.load(getClass().getResource("/views/celulares.fxml"));
            anchorPaneContenido.getChildren().setAll(nodeReparaciones);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Método para establecer la imagen en un botón
    private void setImageToButton(Button button, String imageName) {
        // Crear un ImageView con la imagen
        String imagePath = getClass().getResource("/images/" + imageName).toExternalForm();
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);

        // Ajustar el tamaño de la imagen (opcional)
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);

        // Asignar la imagen al botón
        button.setGraphic(imageView);
    }

  
}
