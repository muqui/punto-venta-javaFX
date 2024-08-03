/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import beans.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import static controller.VentasController.calculateDiscountedPrice;
import dto.DepartmentDTO;
import dto.ProductDTO;
import dto.ProductFindDTO;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class ProductController implements Initializable {

    ObservableList<Product> data;

    ArrayList<Product> productList = new ArrayList<Product>();

    @FXML
    private Button btnSaveProduct;

    @FXML
    private ChoiceBox<String> comboSaveGanancia;

    @FXML
    private ChoiceBox<String> comboSaveHowTosell;

    @FXML
    private ChoiceBox<DepartmentDTO> comboSaveDepart;

    @FXML
    private CheckBox isstocktaking;

    @FXML
    private TextField txtSaveAmount;

    @FXML
    private TextField txtSaveName;

    @FXML
    private TextField txtSaveBarcode;

    @FXML
    private TextField txtSaveDescription;

    @FXML
    private TextField txtSavePrice;

    @FXML
    private TextField txtSaveminimumStock;

    @FXML
    private TextField txtSavepurchasePrice;

    @FXML
    private TextField txtSavewholesalePrice;

    @FXML
    private TextField txtSupplier;

    @FXML
    private TableView<Product> tableViewPackage;   // ESTA TABLA MUESTRA LOS PRODUCTOS QUE SERAN GUARDADOS EN LA TABLA

    @FXML
    void btnFindProductAnction(ActionEvent event) {
        System.out.println("CARGAR BUSCAR PARA CARGAR A PAQUETE");
        buscarProducto();
    }

    @FXML
    void btnSaveAction(ActionEvent event) {
        try {
            ProductDTO product = new ProductDTO();
            product.setName(txtSaveName.getText());
            product.setDescription(txtSaveDescription.getText());
            product.setBarcode(txtSaveBarcode.getText());
            //product.setPrice(Double.parseDouble(txtSavePrice.getText()));
            product.setPrice(new BigDecimal(txtSavePrice.getText()));
            product.setStock(Integer.parseInt(txtSaveAmount.getText()));
            product.setImgUrl("imagen url");
            DepartmentDTO selectedDepartment = comboSaveDepart.getValue();
            int selectedId = selectedDepartment.getId();
            product.setCategoryId(selectedId);
            product.setHowToSell(comboSaveHowTosell.getValue());
            product.setPurchasePrice(Double.parseDouble(txtSavepurchasePrice.getText()));
            product.setWholesalePrice(Double.parseDouble(txtSavewholesalePrice.getText()));
            product.setStocktaking(isstocktaking.isSelected());
            product.setMinimumStock(Integer.parseInt(txtSaveminimumStock.getText()));
            product.setEntriy(Double.parseDouble(txtSaveAmount.getText()));
            product.setSupplier(txtSupplier.getText());
            product.setOutput(0);

            System.out.println(product.toString());

            sendProductToApi(product);
        } catch (Exception e) {
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        fillChoiceBoxGanancias();
        fillChoiceBoxDepartament();
        fillChoiceBoxHowToSell();
        initializeTableColumns();
        txtSavepurchasePrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // El TextField ha perdido el foco
                handleTextFieldFocusLost();
            }
        });
    }

    private void handleTextFieldFocusLost() {
        // Código para manejar la pérdida de foco del TextField
        System.out.println("txtSavePrice ha perdido el foco");
        // Aquí puedes agregar cualquier acción que desees realizar
        try {
            // double precioMenudeo = calculateSellingPrice(Double.parseDouble(txtSavepurchasePrice.getText()), Double.parseDouble(comboSaveGanancia.getValue()));
            BigDecimal precioMenudeo = calculateSellingPrice(new BigDecimal(txtSavepurchasePrice.getText()), new BigDecimal(comboSaveGanancia.getValue()));
            txtSavePrice.setText("" + precioMenudeo);
        } catch (NumberFormatException e) {
            System.out.println("Formato de precio inválido");
            // Muestra una alerta si el formato es inválido
            showAlert("Error", "Formato de precio inválido");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fillChoiceBoxGanancias() {
        ObservableList<String> gananciaOptions = FXCollections.observableArrayList();
        for (int i = 5; i <= 100; i += 1) {
            gananciaOptions.add(String.valueOf(i));
        }
        comboSaveGanancia.setItems(gananciaOptions);
        // Establecer valor por defecto
        comboSaveGanancia.setValue("43");
    }

    private void fillChoiceBoxHowToSell() {
        ObservableList<String> howToSell = FXCollections.observableArrayList();
        howToSell.add("Unidad");
        howToSell.add("Granel");
        howToSell.add("Paquete");
        comboSaveHowTosell.setItems(howToSell);
        // Establecer valor por defecto
        comboSaveHowTosell.setValue("Unidad");

    }

    private void fillChoiceBoxDepartament() {
        try {
            String urlString = "http://localhost:3000/categories"; // Cambia esto por la URL correcta de tu API
            URL url = new URL(urlString);
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
                List<DepartmentDTO> departments = mapper.readValue(inline.toString(), new TypeReference<List<DepartmentDTO>>() {
                });

                ObservableList<DepartmentDTO> departamentList = FXCollections.observableArrayList(departments);
                comboSaveDepart.setItems(departamentList);

                // Configurar el StringConverter para mostrar solo el nombre
                comboSaveDepart.setConverter(new StringConverter<DepartmentDTO>() {
                    @Override
                    public String toString(DepartmentDTO department) {
                        return department.getName();
                    }

                    @Override
                    public DepartmentDTO fromString(String string) {
                        return departamentList.stream().filter(department -> department.getName().equals(string)).findFirst().orElse(null);
                    }
                });

                // Establecer valor por defecto si es necesario
                if (!departamentList.isEmpty()) {
                    comboSaveDepart.setValue(departamentList.get(0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BigDecimal calculateSellingPrice(BigDecimal cost, BigDecimal profitMargin) {
        // Convertir el porcentaje de margen de beneficio a un decimal
        BigDecimal profitMarginDecimal = profitMargin.divide(BigDecimal.valueOf(100));
        // Calcular el precio de venta
        BigDecimal sellingPrice = cost.divide(BigDecimal.ONE.subtract(profitMarginDecimal), RoundingMode.HALF_UP);
        return sellingPrice;
    }

    private void sendProductToApi(ProductDTO product) throws Exception {
        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(product);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/products"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonProduct))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Resultado =" + response.body());

            if (response.statusCode() == 201) {
                showAlert("Éxito", "Producto guardado exitosamente.");
            } else {
                // Manejar error de autenticación
                showAlert("Error", "Error al guardar el producto. Código de estado: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            showAlert("Error", "Error al conectarse con la API.");
        }

    }

    private void buscarProducto() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/buscar.fxml"));
            Parent root = fxmlLoader.load();
            BuscarController buscarController = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            //stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            String codigo = buscarController.getCodigo();

            if (!codigo.isEmpty()) {
                Product product = getProductByBarcode(codigo);
                System.out.println("PRODUCTO= " + product.toString());
                productList.add(product);

            }
            data = FXCollections.observableList(productList);
            data.forEach((tab) -> {
                tab.getBotonAgregar().setOnAction(this::eventoTabla);
                tab.getBotonAgregar().setMaxWidth(Double.MAX_VALUE);
                tab.getBotonAgregar().setMaxHeight(Double.MAX_VALUE);
                tab.getBotonBorrar().setOnAction(this::eventoTabla);
                tab.getBotonBorrar().setMaxWidth(Double.MAX_VALUE);
                tab.getBotonBorrar().setMaxHeight(Double.MAX_VALUE);

                tab.getBotonEliminar().setOnAction(this::eventoTabla);
                tab.getBotonEliminar().setMaxWidth(Double.MAX_VALUE);
                tab.getBotonEliminar().setMaxHeight(Double.MAX_VALUE);

            });
            tableViewPackage.setItems(data);

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
    Regresa el producto desde la rest api.
     */
    public Product getProductByBarcode(String barcode) {
        Product product = new Product();

        try {

            // Crear un cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Construir la solicitud GET
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/products/" + barcode))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            // Enviar la solicitud y recibir la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Manejar la respuesta
            if (response.statusCode() == 200) {

                // Parsear la respuesta JSON
                JSONObject jsonResponse = new JSONObject(response.body());
                // Crear una instancia de Product y asignar valores

                product.setId(jsonResponse.getInt("id"));
                product.setName(jsonResponse.getString("name"));
                product.setDescription(jsonResponse.getString("description"));
                product.setBarcode(jsonResponse.getString("barcode"));
                // product.setPrice(jsonResponse.getDouble("price"));
                product.setPrice(jsonResponse.getBigDecimal("price"));
                product.setStock(jsonResponse.getInt("stock"));
                product.setImgUrl(jsonResponse.getString("imgUrl"));
                product.setCategoryId(jsonResponse.getInt("categoryId"));
                product.setTotal(jsonResponse.getBigDecimal("price"));
                product.setPurchasePrice(jsonResponse.getBigDecimal("purchasePrice"));
                product.setHowToSell(jsonResponse.getString("howToSell"));

            } else {
                System.out.println("Error en la solicitud: " + response.statusCode());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Producto no encontrado.");
                alert.showAndWait();
            }

        } catch (Exception e) {
        }
        return product;
    }

    private void initializeTableColumns() {
        tableViewPackage.getColumns().clear(); // Limpiar las columnas de la tabla antes de agregar nuevas

        TableColumn<Product, String> columnBarcode = new TableColumn<>("Codigo");
        columnBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        
        TableColumn<Product, String> columnAmount = new TableColumn<>("Cantidad");
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        

        TableColumn<Product, String> columnName = new TableColumn<>("Nombre");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Double> columnPrice = new TableColumn<>("Precio de venta");
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> columnStock = new TableColumn<>("Precio de compra");
        columnStock.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));

        TableColumn<Product, Button> columnButtonAdd = new TableColumn<>("Acción");
        columnButtonAdd.setCellValueFactory(new PropertyValueFactory<>("botonAgregar"));
        
         TableColumn<Product, Button> columnButtonDelete = new TableColumn<>("Acción");
        columnButtonDelete.setCellValueFactory(new PropertyValueFactory<>("botonEliminar"));
        
         TableColumn<Product, Button> columnButtonless = new TableColumn<>("Acción");
        columnButtonless.setCellValueFactory(new PropertyValueFactory<>("botonBorrar"));

        tableViewPackage.getColumns().addAll(columnBarcode, columnName,columnAmount, columnPrice, columnStock, columnButtonAdd,columnButtonless, columnButtonDelete );

        // Set table width listener to adjust column widths in percentages
        tableViewPackage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double tableWidth = newVal.doubleValue();
            columnBarcode.setPrefWidth(tableWidth * 0.15); // 15% width
             columnAmount.setPrefWidth(tableWidth * 0.10); 
            columnName.setPrefWidth(tableWidth * 0.15); // 15% width
            columnPrice.setPrefWidth(tableWidth * 0.15); // 15% width
            columnStock.setPrefWidth(tableWidth * 0.15); // 15% width
            columnButtonAdd.setPrefWidth(tableWidth * 0.10);
            columnButtonless.setPrefWidth(tableWidth * 0.10);
            columnButtonDelete.setPrefWidth(tableWidth * 0.10);
        });
    }

    private void eventoTabla(ActionEvent event) {
          for (int i = 0; data.size() > i; i++) {
            Product p = data.get(i);
            //delete item from ticket
            if (event.getSource() == p.getBotonEliminar()) {

                // delete by itself
                data.remove(p);

            }
            if (event.getSource() == p.getBotonAgregar()) {

                //  p.setAmount(p.getAmount() + 1);
                p.setAmount(p.getAmount().add(BigDecimal.ONE));
                //  p.setTotal(p.getAmount() * p.getPrice());
                p.setTotal(p.getAmount().multiply(p.getPrice()));

            }
            if (event.getSource() == p.getBotonBorrar()) {

                if (p.getAmount().compareTo(BigDecimal.ONE) > 0) {
                    p.setAmount(p.getAmount().subtract(BigDecimal.ONE));
                    p.setTotal(p.getAmount().multiply(p.getPrice()));
                    System.out.println("cantidad " + p.getAmount());
                }

//                if (p.getAmount() > 1) {
//                    p.setAmount(p.getAmount() - 1);
//                    p.setTotal(p.getAmount() * p.getPrice());
//                    System.out.println("cantidad " + p.getAmount());
//                }
            }
        }

        tableViewPackage.setItems(data);
        tableViewPackage.refresh();

    }
}
