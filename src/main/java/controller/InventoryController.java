/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.EntryDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.ProductDTO;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class InventoryController implements Initializable {

    @FXML
    private TableView<ProductDTO> tableProducts;
    @FXML
    private TableView<EntryDTO> tableViewEntries;
    @FXML
    private TableView<OrderDetailDTO> tableViewOutputs;

    @FXML
    private TabPane tabPaneInventory;

    @FXML
    void mouseClickTabPaneInventory(MouseEvent event) {
        int tabSeleccionado = tabPaneInventory.getSelectionModel().getSelectedIndex();

        if (tabSeleccionado == 1) {

        }
        System.out.println("tab seleccionado" + tabSeleccionado);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        try {
            entries();  //carga los datos de lasa entradas al inventario
            outputs(); //carga los datos de las salidas al inventario
            TableColumn<ProductDTO, Integer> columnbarcode = new TableColumn<>("Codigo");
            columnbarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
            columnbarcode.setPrefWidth(150); // Set specific width for barcode column

            //descripccion
            TableColumn<ProductDTO, Integer> columnDescription = new TableColumn<>("Nombre");
            columnDescription.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnDescription.setResizable(true);
            //entrada
            TableColumn<ProductDTO, Integer> columnEntry = new TableColumn<>("Entrada");
            columnEntry.setCellValueFactory(new PropertyValueFactory<>("entriy"));
            columnEntry.setResizable(true);
            //salida
            TableColumn<ProductDTO, Integer> columnOutput = new TableColumn<>("Salida");
            columnOutput.setCellValueFactory(new PropertyValueFactory<>("output"));
            columnOutput.setResizable(true);
            //saldo
            TableColumn<ProductDTO, Integer> columnStock = new TableColumn<>("Saldo");
            columnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
            columnStock.setResizable(true);

            //precio costo purchasePrice
            //saldo
            TableColumn<ProductDTO, Integer> columnPurchasePrice = new TableColumn<>("Precio costo");
            columnPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
            columnPurchasePrice.setResizable(true);

            // Create the calculated column
            TableColumn<ProductDTO, Double> columnTotalStockValue = new TableColumn<>("$ total");
            columnTotalStockValue.setCellValueFactory(cellData -> {
                ProductDTO product = cellData.getValue();
                double totalStockValue = product.getPurchasePrice() * product.getStock();
                return new javafx.beans.property.SimpleDoubleProperty(totalStockValue).asObject();
            });

            tableProducts.getColumns().addAll(columnbarcode, columnDescription, columnPurchasePrice, columnEntry, columnOutput, columnStock, columnTotalStockValue);
            tableProducts.setItems(fetchProducts());

            // Set table width listener to adjust column widths in percentages
            tableProducts.widthProperty().addListener((obs, oldVal, newVal) -> {
                double tableWidth = newVal.doubleValue();
                columnbarcode.setPrefWidth(tableWidth * 0.25); // 30% width
                columnDescription.setPrefWidth(tableWidth * 0.25); // 40% width
                columnEntry.setPrefWidth(tableWidth * 0.10); // 30% width
                columnOutput.setPrefWidth(tableWidth * 0.10); // 30% width
                columnStock.setPrefWidth(tableWidth * 0.10); // 30% width
                columnPurchasePrice.setPrefWidth(tableWidth * 0.10); // 30% width
                columnTotalStockValue.setPrefWidth(tableWidth * 0.10);
            });

        } catch (Exception e) {
        }
    }

    private ObservableList<ProductDTO> fetchProducts() throws IOException {
        URL url = new URL("http://localhost:3000/products/");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {
            Scanner scanner = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            ObjectMapper mapper = new ObjectMapper();
            List<ProductDTO> products = mapper.readValue(inline.toString(), new TypeReference<List<ProductDTO>>() {
            });

            // Convertir la lista a ObservableList
            ObservableList<ProductDTO> productList = FXCollections.observableArrayList(products);
            return productList;
        }
    }

    public void entries() {
        try {
            // Columna para la fecha
            TableColumn<EntryDTO, Date> columnDate = new TableColumn<>("Fecha");
            columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            columnDate.setPrefWidth(150); // Ajusta el ancho según sea necesario

            // Columna para el nombre del producto
            TableColumn<EntryDTO, String> columnProductBarcode = new TableColumn<>("Codigo de barras");
            columnProductBarcode.setCellValueFactory(cellData -> {
                ProductDTO product = cellData.getValue().getProduct();
                return new SimpleStringProperty(product != null ? product.getBarcode() : "");
            });
            columnProductBarcode.setPrefWidth(150); // Ajusta el ancho según sea necesario
            // Columna para el nombre del producto
            TableColumn<EntryDTO, String> columnProductName = new TableColumn<>("Nombre del Producto");
            columnProductName.setCellValueFactory(cellData -> {
                ProductDTO product = cellData.getValue().getProduct();
                return new SimpleStringProperty(product != null ? product.getName() : "");
            });
            columnProductName.setPrefWidth(150); // Ajusta el ancho según sea necesario

            // Columna para la cantidad
            TableColumn<EntryDTO, BigDecimal> columnAmount = new TableColumn<>("Cantidad");
            columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            columnAmount.setPrefWidth(100); // Ajusta el ancho según sea necesario

            // Columna para el precio
            TableColumn<EntryDTO, BigDecimal> columnPrice = new TableColumn<>("Precio compra");
            columnPrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
            columnPrice.setPrefWidth(100); // Ajusta el ancho según sea necesario

            // Agregar las columnas a la tabla
            tableViewEntries.getColumns().addAll(columnDate, columnProductBarcode, columnProductName, columnAmount, columnPrice);

            tableViewEntries.setItems(fetchEntries());
            tableViewEntries.widthProperty().addListener((obs, oldVal, newVal) -> {
                double tableWidth = newVal.doubleValue();
                columnDate.setPrefWidth(tableWidth * 0.20); // 30% width
                columnProductBarcode.setPrefWidth(tableWidth * .20);
                columnProductName.setPrefWidth(tableWidth * 0.20); // 40% width
                columnAmount.setPrefWidth(tableWidth * 0.20); // 30% width
                columnPrice.setPrefWidth(tableWidth * 0.20); // 30% width

            });

        } catch (Exception e) {
        }
    }

    private ObservableList<EntryDTO> fetchEntries() {
        ObservableList<EntryDTO> entryList = null;
        try {
            URL url = new URL("http://localhost:3000/products/entries");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                // System.out.println("JSON Response: " + inline.toString()); // Verifica el contenido de la respuesta JSON
                ObjectMapper mapper = new ObjectMapper();
                List<EntryDTO> entries = mapper.readValue(inline.toString(), new TypeReference<List<EntryDTO>>() {
                });

                System.out.println("Entries List: " + entries); // Verifica que la lista de entradas se ha deserializado correctamente

                // Imprime la lista de EntryDTO en la consola
                entries.forEach(entry -> System.out.println(entry.toString()));

                // Convertir la lista a ObservableList
                entryList = FXCollections.observableArrayList(entries);
                System.out.println("obserbable list" + entries);

            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(InventoryController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ex0 = " + ex);
        } catch (IOException ex) {
            Logger.getLogger(InventoryController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ex1 = " + ex);
        }

        return entryList;
    }

    public void outputs() {
        try {
            TableColumn<OrderDetailDTO, String> columnDate = new TableColumn<>("Fecha");
            columnDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrder().getDate()));
            columnDate.setResizable(true);

            TableColumn<OrderDetailDTO, String> columnBarcode = new TableColumn<>("Codigo de barras");
            columnBarcode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getBarcode()));
            columnBarcode.setResizable(true);

            TableColumn<OrderDetailDTO, Double> columnPrice = new TableColumn<>("Precio Menudeo");  //precio venta
            columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            columnPrice.setResizable(true);

            TableColumn<OrderDetailDTO, Integer> columnAmount = new TableColumn<>("Cantidad");
            columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            columnAmount.setResizable(true);

            TableColumn<OrderDetailDTO, String> columnNameProduct = new TableColumn<>("Producto");
            columnNameProduct.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
            columnNameProduct.setResizable(true);

            tableViewOutputs.getColumns().addAll(columnDate, columnBarcode, columnNameProduct, columnAmount, columnPrice);
            tableViewOutputs.setItems(fetchOrderDetails());
            tableViewOutputs.widthProperty().addListener((obs, oldVal, newVal) -> {
                double tableWidth = newVal.doubleValue();
                columnDate.setPrefWidth(tableWidth * 0.20); // 30% width
                columnBarcode.setPrefWidth(tableWidth * .20);
                columnNameProduct.setPrefWidth(tableWidth * 0.20); // 40% width
                columnAmount.setPrefWidth(tableWidth * 0.20); // 30% width
                columnPrice.setPrefWidth(tableWidth * 0.20); // 30% width

            });

        } catch (IOException ex) {
            Logger.getLogger(ReportesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Método para obtener la lista de pedidos como ObservableList
    private ObservableList<OrderDetailDTO> fetchOrderDetails() throws IOException {
        URL url = new URL("http://localhost:3000/orders");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {
            Scanner scanner = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            ObjectMapper mapper = new ObjectMapper();
            List<OrderDTO> orders = mapper.readValue(inline.toString(), new TypeReference<List<OrderDTO>>() {
            });

            // Flatten the structure
            ObservableList<OrderDetailDTO> orderDetailsList = FXCollections.observableArrayList();
            for (OrderDTO order : orders) {
                for (OrderDetailDTO detail : order.getOrderDetails()) {
                    detail.setOrder(order); // Set reference to the parent order
                    orderDetailsList.add(detail);
                }
            }
            return orderDetailsList;
        }
    }
}
