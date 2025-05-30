package com.albertocoronanavarro.puntoventafx;

import dto.UserDTO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

//https://www.flaticon.es/
public class PrimaryController implements Initializable {

    private UserDTO user;
    // private UserDTO usuario = new UserDTO();
    Node nodeVenta, nodeProducto, nodeReporte, nodeInventory, nodeCaja, nodeReparaciones, nodeUser;

    @FXML
    private ToolBar toolbar;

    @FXML
    private Button btnCaja;
    
        @FXML
    private Button btnClose;

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
    void onActionCloseSession(ActionEvent event) {
           System.out.println("cerrar session");

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        Parent root = loader.load();

        Stage stage = App.getStage();

        // Primero, asegurarse de quitar el modo maximizado
        stage.setMaximized(false);

        // Establecer la nueva escena
        Scene loginScene = new Scene(root);
        stage.setScene(loginScene);
        stage.setTitle("Login");

        // Quitar el modo full screen (por si acaso)
        stage.setFullScreen(false);

        // Esperar a que la escena esté aplicada antes de ajustar tamaño
        Platform.runLater(() -> {
            stage.setWidth(452);
            stage.setHeight(500);
            stage.centerOnScreen();
        });

        App.setUsuario(null); // limpiar usuario

    } catch (IOException e) {
        e.printStackTrace();
    }
    }

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

    @FXML
    void OnActionConfig(ActionEvent event) {
        nodeConfig();
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

    private void nodeConfig() {

        try {

            if (nodeUser == null) {

                nodeUser = (Node) FXMLLoader.load(getClass().getResource("/views/User.fxml"));

            }
            anchorPaneContenido.getChildren().setAll(nodeUser);

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
        this.user = App.getUsuario();
        System.out.println("usuario desde la ventana principal= " + this.user.toString());

     //   btnClients.setVisible(false);
     //   btnReparaciones.setVisible(false);
    //    toolbar.getItems().remove(btnClients);
     //   toolbar.getItems().remove(btnReparaciones);

        if (user.getIsAdmin().equalsIgnoreCase("user")) {
            // Ocultar el toolbar al iniciar
         //   toolbar.setVisible(false);
       //     toolbar.setManaged(false); // Esto hace que el espacio que ocupa desaparezca también
//            toolbar.getItems().remove(btnProductos);
//            toolbar.getItems().remove(btnConfig);
//            toolbar.getItems().remove(btnCaja);
//            toolbar.getItems().remove(btnInventory);
        }
        if (user.getIsAdmin().equalsIgnoreCase("admin")) {

        }

        pantallaVentas();

        setImageToButton(btnVender, "ventas.png");
        setImageToButton(btnProductos, "productos.png");
        setImageToButton(btnInventory, "inventario.png");
        setImageToButton(btnClients, "clientes.png");
        setImageToButton(btnConfig, "config.png");
        setImageToButton(btnReports, "reportes.png");
        setImageToButton(btnCaja, "caja.png");
        setImageToButton(btnReparaciones, "reparaciones.png");
          setImageToButton(btnClose, "cerrar-sesion.png");
        //cerrar-sesion  btnClose

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
