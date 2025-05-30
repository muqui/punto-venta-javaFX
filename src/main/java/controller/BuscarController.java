/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.ProductApi;
import beans.Product;
import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ProductDTO;
import dto.ProductFindDTO;
import dto.UserDTO;
import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class BuscarController implements Initializable {

    ProductApi productApi = new ProductApi();
    private UserDTO user;
    private String codigo = "";
    ObservableList<Product> data;
    @FXML
    private TableView<ProductFindDTO> tableBuscar;

    @FXML
    private TextField txtBuscar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
          this.user = App.getUsuario();
        txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {

                try {
                    initializeTableColumns();

                    tableBuscar.setItems(fetchProducts(txtBuscar.getText()));

                } catch (IOException ex) {
                    Logger.getLogger(BuscarController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

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

    private ObservableList<ProductFindDTO> fetchProducts(String value) throws IOException {

        List<ProductFindDTO> products = productApi.fetchProducts(value, user.getToken());

        products.forEach(product -> {
            Button button = new Button("Agregar");
            button.setOnAction(event -> {
               // System.out.println("Código de barras: " + product.getBarcode());
                setCodigo(product.getBarcode());

                Stage stage = (Stage) button.getScene().getWindow();
                stage.close();
            });
            product.setButton(button);
        });
        return FXCollections.observableArrayList(products);

    }

    private void initializeTableColumns() {
        tableBuscar.getColumns().clear(); // Limpiar las columnas de la tabla antes de agregar nuevas

        TableColumn<ProductFindDTO, String> columnBarcode = new TableColumn<>("Codigo");
        columnBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));

        TableColumn<ProductFindDTO, String> columnName = new TableColumn<>("Nombre");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ProductFindDTO, Double> columnPrice = new TableColumn<>("Precio");
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<ProductFindDTO, Integer> columnStock = new TableColumn<>("Saldo");
        columnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<ProductFindDTO, Button> columnButton = new TableColumn<>("Acción");
        columnButton.setCellValueFactory(new PropertyValueFactory<>("button"));

        tableBuscar.getColumns().addAll(columnBarcode, columnName, columnPrice, columnStock, columnButton);

        // Set table width listener to adjust column widths in percentages
        tableBuscar.widthProperty().addListener((obs, oldVal, newVal) -> {
            double tableWidth = newVal.doubleValue();
            columnBarcode.setPrefWidth(tableWidth * 0.25); // 15% width
            columnName.setPrefWidth(tableWidth * 0.25); // 15% width
            columnPrice.setPrefWidth(tableWidth * 0.20); // 15% width
            columnStock.setPrefWidth(tableWidth * 0.20); // 15% width
            columnButton.setPrefWidth(tableWidth * 0.10);
        });
    }

}
