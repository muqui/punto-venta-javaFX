/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.CategoriesApi;
import api.ProductApi;

import beans.PackageContent;
import beans.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.DepartmentDTO;
import dto.ProductDTO;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

    ProductApi productApi = new ProductApi();
    ProductDTO productoToUpdate = new ProductDTO();
    CategoriesApi CategoriesApi = new CategoriesApi();

    ObservableList<Product> data;

    ArrayList<Product> productList = new ArrayList<Product>();

    @FXML
    private Button btnSaveProduct;
    @FXML
    private AnchorPane anchorPanePackage;
    @FXML
    private AnchorPane anchorPaneStock;

    @FXML
    private ChoiceBox<String> comboSaveGanancia;

    @FXML
    private ChoiceBox<String> comboSaveHowTosell;

    @FXML
    private ChoiceBox<DepartmentDTO> comboSaveDepart;

    @FXML
    private ChoiceBox<DepartmentDTO> comboUpdateDepart;

    @FXML
    private ChoiceBox<String> comboUpdateGanancia;

    @FXML
    private ChoiceBox<String> comboUpdateHowTosell;

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
    private TextField txtCrearDepartamento;

    @FXML
    private TabPane tabPaneAddProduct;

    @FXML
    private TextField txtUpdateBarcode;
    @FXML
    private TextField txtUpdateAmount;

    @FXML
    private TextField txtUpdateDescription;

    @FXML
    private TextField txtUpdateName;

    @FXML
    private TextField txtUpdatePrice;

    @FXML
    private TextField txtUpdateSupplier;

    @FXML
    private TextField txtUpdateminimumStock;

    @FXML
    private TextField txtUpdatepurchasePrice;

    @FXML
    private TextField txtUpdatewholesalePrice;

    @FXML
    void btnUpdateAction(ActionEvent event) {
        //ACTUALIZAR PRODUCTO

        productoToUpdate.setName(txtUpdateName.getText());
        productoToUpdate.setDescription(txtUpdateDescription.getText());
        productoToUpdate.setPurchasePrice(Double.parseDouble(txtUpdatepurchasePrice.getText()));
        productoToUpdate.setPrice(new BigDecimal(txtUpdatePrice.getText()));
        productoToUpdate.setWholesalePrice(Double.parseDouble(txtUpdatewholesalePrice.getText()));
        productoToUpdate.setStock(Double.parseDouble(txtUpdateAmount.getText()));
        productoToUpdate.setMinimumStock(Integer.parseInt(txtUpdateminimumStock.getText()));
        System.out.println("ACTUALIZAR COMO SE VENDEN= " + comboUpdateDepart.getValue());
        productoToUpdate.setHowToSell(comboUpdateHowTosell.getValue());
        DepartmentDTO selectedDepartment = comboUpdateDepart.getValue();
        int selectedId = selectedDepartment.getId();
        productoToUpdate.setCategoryId(selectedId);

        System.out.println("Producto actualizado=  " + productoToUpdate.toString());
        productoToUpdate.setSupplier("sin informacion");

        productApi.updateProduct(productoToUpdate);
        txtUpdateName.setText("");
        txtUpdateDescription.setText("");
        txtUpdatepurchasePrice.setText("");
        txtUpdatePrice.setText("");
        txtUpdatewholesalePrice.setText("");
        txtUpdateAmount.setText("");
        txtUpdateminimumStock.setText("");
        txtUpdateBarcode.setText("");

    }

    @FXML
    void tabPaneAddProductMouseCliked(MouseEvent event) {
        int tabSeleccionado = tabPaneAddProduct.getSelectionModel().getSelectedIndex();
        System.out.println("presionaste crear producto" + tabSeleccionado);

        if (tabSeleccionado == 0) {
            fillChoiceBoxDepartament();
        }

    }

    @FXML
    void OnActionBtnCrearDepartamento(ActionEvent event) {
        DepartmentDTO departamentDto = new DepartmentDTO();
        departamentDto.setName(txtCrearDepartamento.getText().trim());
        CategoriesApi.createDepartment(departamentDto);

    }

    @FXML
    void OnKeyComboHowToSell(KeyEvent event) {

    }

    @FXML
    void OnMouseClickedComboHowTosell(MouseEvent event) {

    }

    @FXML
    void btnFindProductAnction(ActionEvent event) {
        System.out.println("CARGAR BUSCAR PARA CARGAR A PAQUETE");

        Product product = buscarProducto();
        addTable(product);
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
            product.setQuantity(100.00);

// Iterar sobre la lista usando el índice
            boolean save = true;
            if (comboSaveHowTosell.getValue().equalsIgnoreCase("Paquete")) {

                if (data == null) {
                    System.out.println("ESTA VACIA LA LISTA");
                    showAlert("Error", "Productos del paquete no puede estar vacio.");
                    save = false;
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        PackageContent content = new PackageContent();
                        content.setProductId(data.get(i).getId());
                        content.setQuantity(data.get(i).getAmount());
                        product.getPackageContents().add(content);

                    }
                    save = true;
                }

            }

            System.out.println(product.toString());
            if (save) {
                productApi.sendProductToApi(product);
                //sendProductToApi(product);
                txtSaveBarcode.setText("");
                txtSaveName.setText("");
                txtSaveDescription.setText("");
                txtSavePrice.setText("");
                txtSavepurchasePrice.setText("");
                txtSavewholesalePrice.setText("");
                txtSaveAmount.setText("");
                txtSaveminimumStock.setText("");
                txtSupplier.setText("");

            }

        } catch (Exception e) {
            System.out.println("exection " + e);
        }

    }

    @FXML
    void OnActionFindProductToUpdateProduct(ActionEvent event) {
        updateProduct();
    }

    @FXML
    void btnFindProductUpdateAnction(ActionEvent event) {

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //anchorPanePackage.setVisible(false);
        //anchorPanePackage.setManaged(false);
        fillChoiceBoxUpdateDepartament();
        fillChoiceBoxGanancias();
        fillChoiceBoxDepartament();
        fillChoiceBoxHowToSell();
        initializeTableColumns();

        txtUpdateBarcode.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {

                    System.out.println("BUSCAR PRODUCTO ACTUALIZAR");

                    updateProduct();

                }
            }

        });
        txtSavepurchasePrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // El TextField ha perdido el foco
                handleTextFieldFocusLost();
            }
        });
        // Listener para detectar cambios en el ComboBox
        comboSaveHowTosell.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("COMO SE VENDE COMBO: " + newValue);
            if ("Paquete".equalsIgnoreCase(newValue)) {
                // anchorPanePackage.setVisible(true);
                // anchorPanePackage.setManaged(true);

                //  anchorPaneStock.setVisible(false);
                //  anchorPaneStock.setManaged(false);
            } else {
                //  anchorPanePackage.setVisible(false);
                //  anchorPanePackage.setManaged(false);

                //  anchorPaneStock.setVisible(true);
                //  anchorPaneStock.setManaged(true);
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
        comboUpdateGanancia.setItems(gananciaOptions);
        // Establecer valor por defecto
        comboSaveGanancia.setValue("43");
        comboUpdateGanancia.setValue("43");
    }

    private void fillChoiceBoxHowToSell() {
        ObservableList<String> howToSell = FXCollections.observableArrayList();
        howToSell.add("Unidad");
        howToSell.add("Granel");
        howToSell.add("Paquete");
        comboUpdateHowTosell.setItems(howToSell);
        comboSaveHowTosell.setItems(howToSell);

        // Establecer valor por defecto
        comboSaveHowTosell.setValue("Unidad");
        comboUpdateHowTosell.setValue("Unidad");
    }

    private void fillChoiceBoxDepartament() {

        ObjectMapper mapper = new ObjectMapper();
        List<DepartmentDTO> departments = productApi.DepartamenNametList();

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

    private void fillChoiceBoxUpdateDepartament() {

        ObjectMapper mapper = new ObjectMapper();
        List<DepartmentDTO> departments = productApi.DepartamenNametList();

        ObservableList<DepartmentDTO> departamentList = FXCollections.observableArrayList(departments);
        comboUpdateDepart.setItems(departamentList);

        // Configurar el StringConverter para mostrar solo el nombre
        comboUpdateDepart.setConverter(new StringConverter<DepartmentDTO>() {
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
            comboUpdateDepart.setValue(departamentList.get(0));
        }

    }

    public static BigDecimal calculateSellingPrice(BigDecimal cost, BigDecimal profitMargin) {
        // Convertir el porcentaje de margen de beneficio a un decimal
        BigDecimal profitMarginDecimal = profitMargin.divide(BigDecimal.valueOf(100));
        // Calcular el precio de venta
        BigDecimal sellingPrice = cost.divide(BigDecimal.ONE.subtract(profitMarginDecimal), RoundingMode.HALF_UP);
        return sellingPrice;
    }

//    private void sendProductToApi(ProductDTO product) throws Exception {
//        System.out.println("PRODUCTO ENVIADO A LA API= " + product.toString());
//        try {
//            // Convertir ProductDTO a JSON
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonProduct = objectMapper.writeValueAsString(product);
//
//            // Crear cliente HTTP
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI("http://localhost:3000/products"))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(jsonProduct))
//                    .build();
//
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() == 201) {
//                showAlert("Éxito", "Producto guardado exitosamente.");
//            } else {
//                String responseBody = response.body();
//                System.out.println("Resultado =" + response.body());
//
//                // Parse the response body as JSON
//                JSONObject jsonResponse = new JSONObject(responseBody);
//                // Access the "message" field
//                String message = jsonResponse.getString("message");
//                System.out.println("Message = " + message);
//
//                // Manejar error de autenticación
//                if (message.equalsIgnoreCase("product exists")) {
//                    //showAlert("Info", "El producto ya existe, puede actualizarlo " );
//                    showAlertUpdateProduct("Info", "El producto existe, Desea actualizarlo?");
//
//                } else {
//                    showAlert("Error", "Error al guardar el producto. Código de estado: " + response.statusCode());
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println("Error API " + e);
//            showAlert("Error", "Error al conectarse con la API.");
//        }
//
//    }

    private Product buscarProducto() {
        Product product = null;
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
               // product = getProductByBarcode(codigo);
               product= productApi.getProductByBarcode1(codigo);
                product.setAmount(new BigDecimal("1"));
                product.setTotal(product.getPurchasePrice());

            }

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return product;
    }

    private void addTable(Product product) {

        boolean exists = existProdcutIntable(product.getBarcode(), new BigDecimal("1"));
        if (exists == false) {  //si ek producto no esta en la tabla se agrega 1.
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
        tableViewPackage.refresh();

    }

    /*
    Regresa el producto desde la rest api.
     */
//    public Product getProductByBarcode(String barcode) {
//        Product product = new Product();
//
//        try {
//
//            // Crear un cliente HTTP
//            HttpClient client = HttpClient.newHttpClient();
//
//            // Construir la solicitud GET
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI("http://localhost:3000/products/" + barcode))
//                    .header("Content-Type", "application/json")
//                    .GET()
//                    .build();
//
//            // Enviar la solicitud y recibir la respuesta
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            // Manejar la respuesta
//            if (response.statusCode() == 200) {
//
//                // Parsear la respuesta JSON
//                JSONObject jsonResponse = new JSONObject(response.body());
//                // Crear una instancia de Product y asignar valores
//
//                product.setId(jsonResponse.getInt("id"));
//                product.setName(jsonResponse.getString("name"));
//                product.setDescription(jsonResponse.getString("description"));
//                product.setBarcode(jsonResponse.getString("barcode"));
//                // product.setPrice(jsonResponse.getDouble("price"));
//                product.setPrice(jsonResponse.getBigDecimal("price"));
//                product.setStock(jsonResponse.getInt("stock"));
//                product.setImgUrl(jsonResponse.getString("imgUrl"));
//                product.setCategoryId(jsonResponse.getInt("categoryId"));
//                product.setTotal(jsonResponse.getBigDecimal("price"));
//                product.setPurchasePrice(jsonResponse.getBigDecimal("purchasePrice"));
//                product.setHowToSell(jsonResponse.getString("howToSell"));
//
//            } else {
//                System.out.println("Error en la solicitud: " + response.statusCode());
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.initStyle(StageStyle.UTILITY);
//                alert.setTitle("Error");
//                alert.setHeaderText(null);
//                alert.setContentText("Producto no encontrado.");
//                alert.showAndWait();
//            }
//
//        } catch (Exception e) {
//        }
//        return product;
//    }

    private void initializeTableColumns() {

        System.out.println("se lanzo");
        tableViewPackage.getColumns().clear(); // Limpiar las columnas de la tabla antes de agregar nuevas

        TableColumn<Product, String> columnBarcode = new TableColumn<>("Codigo");
        columnBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));

        TableColumn<Product, String> columnAmount = new TableColumn<>("Cantidad");
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Product, String> columnName = new TableColumn<>("Nombre");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Double> columnPrice = new TableColumn<>("Precio de compra");
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));

        TableColumn<Product, Integer> columnStock = new TableColumn<>("total Precio de compra");
        columnStock.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Product, Button> columnButtonAdd = new TableColumn<>("Acción");
        columnButtonAdd.setCellValueFactory(new PropertyValueFactory<>("botonAgregar"));

        TableColumn<Product, Button> columnButtonDelete = new TableColumn<>("Acción");
        columnButtonDelete.setCellValueFactory(new PropertyValueFactory<>("botonEliminar"));

        TableColumn<Product, Button> columnButtonless = new TableColumn<>("Acción");
        columnButtonless.setCellValueFactory(new PropertyValueFactory<>("botonBorrar"));

        tableViewPackage.getColumns().addAll(columnBarcode, columnName, columnAmount, columnPrice, columnStock, columnButtonAdd, columnButtonless, columnButtonDelete);

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
                p.setTotal(p.getAmount().multiply(p.getPurchasePrice()));

            }
            if (event.getSource() == p.getBotonBorrar()) {

                if (p.getAmount().compareTo(BigDecimal.ONE) > 0) {
                    p.setAmount(p.getAmount().subtract(BigDecimal.ONE));
                    p.setTotal(p.getAmount().multiply(p.getPurchasePrice()));
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

    public boolean existProdcutIntable(String codigoBarras, BigDecimal cantidad) {
        boolean exists = false;
        for (int i = 0; i < productList.size(); i++) {
            Product p = productList.get(i);
            System.out.println("PRODUCTO YA ESTA EN EL TICKET" + p.toString());
            if (p.getBarcode().equalsIgnoreCase(codigoBarras)) {

                //double cantidadTotal = p.getAmount() + cantidad;
                BigDecimal cantidadTotal = p.getAmount().add(cantidad);
                //double total = cantidadTotal * p.getPrice();
                BigDecimal price = cantidadTotal.multiply(p.getPrice());
                BigDecimal purchasePrice = cantidadTotal.multiply(p.getPurchasePrice());
                System.out.println("TOTAL NUEVO " + price);

                p.setAmount(cantidadTotal);
                p.setPrice(price);
                p.setTotal(purchasePrice);
                exists = true;
                break;
            }
        }

        return exists;

    }

    public void showAlertUpdateProduct(String title, String message) {
        // Crear una alerta de tipo INFORMACIÓN
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Añadir los botones "OK" y "Actualizar"
        ButtonType cancelButton = new ButtonType("Cancelar");
        ButtonType updateButton = new ButtonType("Actualizar");

        alert.getButtonTypes().setAll(cancelButton, updateButton);

        // Mostrar la alerta y esperar a que el usuario responda
        ButtonType result = alert.showAndWait().orElse(cancelButton);

        // Manejar la respuesta del usuario
        if (result == updateButton) {
            // Acción cuando se presiona el botón "Actualizar"
            System.out.println("Actualizar presionado");
            // Aquí puedes agregar el código para manejar la actualización
            btnSaveProduct.setText("Actualizar");
        } else {
            // Acción cuando se presiona el botón "OK"
            System.out.println("cancel presionado");
            // Aquí puedes agregar el código para manejar la opción "OK"
        }
    }

    private void updateProduct() {

        productoToUpdate = productApi.getProductByBarcode(txtUpdateBarcode.getText());
        System.out.println("Producto " + productoToUpdate.toString());

        txtUpdateName.setText((productoToUpdate.getName()));
        txtUpdateDescription.setText(productoToUpdate.getDescription());
        txtUpdatepurchasePrice.setText(String.valueOf(productoToUpdate.getPurchasePrice()));
        txtUpdatePrice.setText(String.valueOf(productoToUpdate.getPrice()));
        txtUpdatewholesalePrice.setText(String.valueOf(productoToUpdate.getWholesalePrice()));
        txtUpdateAmount.setText("" + productoToUpdate.getStock());
        txtUpdateminimumStock.setText("" + productoToUpdate.getMinimumStock());
        comboUpdateHowTosell.setValue("" + productoToUpdate.getHowToSell());
        comboUpdateGanancia.setValue("43");

    }
}
