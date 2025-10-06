/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.EntryApi;
import api.OrderApi;
import api.ProductApi;
import beans.Product;
import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import static controller.ProductController.calculateSellingPrice;
import dto.DepartmentDTO;
import dto.EntryDTO;
import dto.InventoryResponseDTO;
import dto.OrderDetailDTO;
import dto.PaginatedInventoryResponseDTO;
import dto.ProductDTO;
import dto.UserDTO;
import helper.AlertMessage;
import helper.DateUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class InventoryController implements Initializable {

    private UserDTO user;
    ProductApi productApi = new ProductApi();
    EntryApi entryApi = new EntryApi();
    OrderApi orderApia = new OrderApi();
    @FXML
    private TextField txtEntryName;

    @FXML
    private TableView<ProductDTO> tableProducts;
    @FXML
    private TableView<EntryDTO> tableViewEntries;
    @FXML
    private TableView<OrderDetailDTO> tableViewOutputs;

    @FXML
    private TabPane tabPaneInventory;

    @FXML
    private ComboBox<DepartmentDTO> comboCategoryProducts;

    @FXML
    private TextField txtAddInventoryAdd;

    @FXML
    private TextField txtAddInventoryBarcode;

    @FXML
    private TextField txtAddInventoryName;

    @FXML
    private TextField txtAddInventoryPrice;

    @FXML
    private TextField txtAddInventorySupplier;

    @FXML
    private TextField txtAddInventoryholesalePrice;

    @FXML
    private TextField txtAddInventorypurchasePrice;

    @FXML
    private ComboBox<String> ComboAddInventoryProfit;

    @FXML
    private Label labelTotalInventory;

    @FXML
    private DatePicker datePickerEntriesEndDate;

    @FXML
    private DatePicker datePickerEntriesSartDate;

    @FXML
    private ComboBox<DepartmentDTO> comboBoxDepartament;

    @FXML
    private Button btnEntriesUpdate;

    @FXML
    void onActionEntriesUpdate(ActionEvent event) {
        DepartmentDTO departament = comboBoxDepartament.getValue();
        System.out.println("Buscar por nombre: " + txtEntryName.getText());
        String name = txtEntryName.getText().trim();
        if (!"".equalsIgnoreCase(name)) {
            fetchEntriesTable(name);
        } else {
            fetchEntriesTable(datePickerEntriesSartDate.getValue().toString(), datePickerEntriesEndDate.getValue().toString(), departament.getName());
        }

    }

    @FXML
    void onActionBtnUpdateProduct(ActionEvent event) {
        System.out.println("AGREGAR A INVENTARIO");
        String barcode = txtAddInventoryBarcode.getText();
        String newStockText = txtAddInventoryAdd.getText();
        String purchasePriceText = txtAddInventorypurchasePrice.getText();
        String priceText = txtAddInventoryPrice.getText();
        String supplier = txtAddInventorySupplier.getText();
        String name = txtAddInventoryName.getText();
        double wholesalePrice = Double.parseDouble(txtAddInventoryholesalePrice.getText());
        // Validación individual por campo
        if (barcode == null || barcode.trim().isEmpty()) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, "Error", "El campo 'Código de barras' es obligatorio.");
            return;
        }

        if (newStockText == null || newStockText.trim().isEmpty()) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, "Error", "El campo 'Cantidad a agregar' es obligatorio.");
            return;
        }

        if (purchasePriceText == null || purchasePriceText.trim().isEmpty()) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, "Error", "El campo 'Precio de compra' es obligatorio.");
            return;
        }

        if (priceText == null || priceText.trim().isEmpty()) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, "Error", "El campo 'Precio de venta' es obligatorio.");
            return;
        }

        if (supplier == null || supplier.trim().isEmpty()) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, "Error", "El campo 'Proveedor' es obligatorio.");
            return;
        }

        int newStock;
        double purchasePrice;
        BigDecimal price;

        // Validación: conversión numérica
        try {
            newStock = Integer.parseInt(newStockText);
        } catch (NumberFormatException e) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, "Error", "El campo 'Cantidad a agregar' debe ser un número entero válido.");
            return;
        }

        try {
            purchasePrice = Double.parseDouble(purchasePriceText);
        } catch (NumberFormatException e) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, "Error", "El campo 'Precio de compra' debe ser un número válido.");
            return;
        }

        try {
            price = new BigDecimal(priceText);
        } catch (NumberFormatException e) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, "Error", "El campo 'Precio de venta' debe ser un número decimal válido.");
            return;
        }

        // Obtener producto
        ProductDTO productDto = productApi.getProductByBarcode(barcode, user.getToken());
        if (productDto == null) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, "Error", "Producto no encontrado con el código de barras proporcionado.");

            return;
        }

        // Actualizar datos
        productDto.setEntriy(newStock);
        productDto.setPurchasePrice(purchasePrice);
        productDto.setPrice(price);
        productDto.setSupplier(supplier);
        productDto.setName(name);
        productDto.setWholesalePrice(wholesalePrice);

        // Enviar actualización
        productApi.addINvetory(productDto, user.getToken());

        txtAddInventoryBarcode.setText("");
        txtAddInventoryAdd.setText("");
        txtAddInventorypurchasePrice.setText("");
        txtAddInventoryPrice.setText("");
        txtAddInventorySupplier.setText("");
        txtAddInventoryName.setText("");
        txtAddInventoryholesalePrice.setText("");
        //  showAlert("Éxito", "Inventario actualizado correctamente.");

    }

    @FXML
    void OnActionFindProduct(ActionEvent event) {
        System.out.println("BUSCAR PRODUCTO PARA ATUALIZAR.................");
        buscarProducto();
    }

    @FXML
    void mouseClickTabPaneInventory(MouseEvent event) {
        int tabSeleccionado = tabPaneInventory.getSelectionModel().getSelectedIndex();

        if (tabSeleccionado == 0) {
            fetchDataInventorie();
        }
        if (tabSeleccionado == 1) {
            entries();
        }
        if (tabSeleccionado == 1) {
            outputs();
        }
        System.out.println("tab seleccionado" + tabSeleccionado);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.user = App.getUsuario();
            fillChoiceBoxGanancias();

            productApi.fetchProductsInventary("", user.getToken());
            txtAddInventoryBarcode.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER)) {

                        System.out.println("BUSCAR PRODUCTO AÑADIR AL INVENTARIO");

                        updateProduct();

                    }
                }

            });

            // Agregar un listener al ComboBox
            ComboAddInventoryProfit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    System.out.println("Elemento seleccionado: " + newValue);
                    try {
                        // double precioMenudeo = calculateSellingPrice(Double.parseDouble(txtSavepurchasePrice.getText()), Double.parseDouble(comboSaveGanancia.getValue()));
                        BigDecimal precioMenudeo = calculateSellingPrice(new BigDecimal(txtAddInventorypurchasePrice.getText()), new BigDecimal(ComboAddInventoryProfit.getValue()));
                        txtAddInventoryPrice.setText("" + precioMenudeo);
                    } catch (NumberFormatException e) {
                        System.out.println("Formato de precio inválido");
                        // Muestra una alerta si el formato es inválido
                        AlertMessage.showAlert(Alert.AlertType.ERROR, "Error", "Formato de precio inválido");
                    }
                }
            });

            // TODO
            // Añadir listener para manejar la selección
            comboCategoryProducts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Acción al seleccionar un elemento
                    System.out.println("Seleccionado: " + newValue.getName());
                    //realizarAccion(newValue);

                    try {
                        InventoryResponseDTO inventoryResponseDTO;
                        if ("TODOS LOS DEPARTAMENTOS".equals(newValue.getName())) {
                            inventoryResponseDTO = productApi.fetchProductsInventary("", user.getToken());
                        } else {
                            inventoryResponseDTO = productApi.fetchProductsInventary(newValue.getName(), user.getToken());
                        }
                        List<ProductDTO> products = inventoryResponseDTO.getProducts();
                        labelTotalInventory.setText("Total: " + inventoryResponseDTO.getTotalInventoryCost());
                        ObservableList<ProductDTO> observableListProducts = FXCollections.observableArrayList(products);
                        tableProducts.setItems(observableListProducts);
                    } catch (IOException ex) {
                        Logger.getLogger(InventoryController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            // Añadir listener para manejar la selección
            comboBoxDepartament.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Acción al seleccionar un elemento
                    System.out.println("Seleccionado: " + newValue.getName());
                    //realizarAccion(newValue);

                    fetchEntriesTable(datePickerEntriesSartDate.getValue().toString(), datePickerEntriesEndDate.getValue().toString(), newValue.getName());

                }
            });

            try {
                entries();  //carga los datos de lasa entradas al inventario
                outputs(); //carga los datos de las salidas al inventario
                fetchDataInventorie(); // carga las informacion del inventario.
                fillChoiceBoxDepartament(); // carga los departamentos  en a vista de productos
                fillChoiceBoxDepartamentEntries(); // carga los departamentos  de entradas del inventario

                datePickerEntriesEndDate.setValue(LocalDate.now());
                datePickerEntriesSartDate.setValue(LocalDate.now());

            } catch (Exception e) {
            }

        } catch (IOException ex) {
            Logger.getLogger(InventoryController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void fetchDataInventorie() {
        try {
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
            // tableProducts.setItems(productApi.fetchProductsInventary("10"));
            InventoryResponseDTO inventoryResponseDTO = productApi.fetchProductsInventary("", user.getToken());
            System.out.println("resultado inventario =" + inventoryResponseDTO);
            System.out.println("RESPONSE INVENTARIO = " + inventoryResponseDTO);
            List<ProductDTO> products = inventoryResponseDTO.getProducts();
            labelTotalInventory.setText("Total: " + inventoryResponseDTO.getTotalInventoryCost());
            ObservableList<ProductDTO> observableListProducts = FXCollections.observableArrayList(products);
            tableProducts.setItems(observableListProducts);
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

    public void entries() {
        try {
            tableViewEntries.getColumns().clear();
            // Columna para la fecha
            TableColumn<EntryDTO, Date> columnDate = new TableColumn<>("Fecha");
            columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));

            // Columna para el nombre del producto
            TableColumn<EntryDTO, String> columnProductBarcode = new TableColumn<>("Codigo de barras");
            columnProductBarcode.setCellValueFactory(cellData -> {
                ProductDTO product = cellData.getValue().getProduct();
                return new SimpleStringProperty(product != null ? product.getBarcode() : "");
            });

            // Columna para el nombre del producto
            TableColumn<EntryDTO, String> columnProductName = new TableColumn<>("Nombre del Producto");
            columnProductName.setCellValueFactory(cellData -> {
                ProductDTO product = cellData.getValue().getProduct();
                return new SimpleStringProperty(product != null ? product.getName() : "");
            });

            // Columna para la fecha
            TableColumn<EntryDTO, String> columnSupplier = new TableColumn<>("Proveedor");
            columnSupplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));

            // Columna para la cantidad
            TableColumn<EntryDTO, BigDecimal> columnAmount = new TableColumn<>("Cantidad");
            columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

            // Columna para el precio
            TableColumn<EntryDTO, BigDecimal> columnPrice = new TableColumn<>("Precio compra");
            columnPrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));

            //Columna total precio compra
            // Columna para el precio
            TableColumn<EntryDTO, BigDecimal> columnTotalPrice = new TableColumn<>("total compra");
            columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("purchaseTotalPrice"));

            // Agregar las columnas a la tabla
            tableViewEntries.getColumns().addAll(columnDate, columnProductBarcode, columnProductName, columnSupplier, columnAmount, columnPrice, columnTotalPrice);

            tableViewEntries.setItems(entryApi.fetchEntries(DateUtil.getTodayDateString(), DateUtil.getTodayDateString(), "TODOS LOS DEPARTAMENTOS"));
            // Ajustar tamaño una vez que todo esté listo
            Platform.runLater(() -> {
                ajustarAnchoColumnas(columnDate, columnProductBarcode, columnProductName,
                        columnSupplier, columnAmount, columnPrice, columnTotalPrice);
            });

            // También ajustar al redimensionar
            tableViewEntries.widthProperty().addListener((obs, oldVal, newVal) -> {
                ajustarAnchoColumnas(columnDate, columnProductBarcode, columnProductName,
                        columnSupplier, columnAmount, columnPrice, columnTotalPrice);
            });

        } catch (Exception e) {
        }
    }

    public void outputs() {
        try {
            TableColumn<OrderDetailDTO, String> columnDate = new TableColumn<>("Fecha");
            columnDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrder().getDate()));
            columnDate.setResizable(true);

            TableColumn<OrderDetailDTO, String> columnBarcode = new TableColumn<>("Codigo de barras");
            columnBarcode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getBarcode()));
            columnBarcode.setResizable(true);

            // Columna para el precio del producto
            TableColumn<OrderDetailDTO, BigDecimal> columnPrice = new TableColumn<>("Precio Menudeo");  // Precio venta
            columnPrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
            columnPrice.setResizable(true);

            TableColumn<OrderDetailDTO, Integer> columnAmount = new TableColumn<>("Cantidad");
            columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            columnAmount.setResizable(true);

            TableColumn<OrderDetailDTO, Integer> columnTotalPrice = new TableColumn<>("Precio total");
            columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            columnTotalPrice.setResizable(true);

            TableColumn<OrderDetailDTO, String> columnNameProduct = new TableColumn<>("Producto");
            columnNameProduct.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
            columnNameProduct.setResizable(true);

            tableViewOutputs.getColumns().addAll(columnDate, columnBarcode, columnNameProduct, columnAmount, columnPrice, columnTotalPrice);
            tableViewOutputs.setItems(orderApia.fetchOrderDetails());
            tableViewOutputs.widthProperty().addListener((obs, oldVal, newVal) -> {
                double tableWidth = newVal.doubleValue();
                columnDate.setPrefWidth(tableWidth * 0.20); // 30% width
                columnBarcode.setPrefWidth(tableWidth * .20);
                columnNameProduct.setPrefWidth(tableWidth * 0.20); // 40% width
                columnAmount.setPrefWidth(tableWidth * 0.20); // 30% width
                columnPrice.setPrefWidth(tableWidth * 0.10); // 30% width
                columnTotalPrice.setPrefWidth(tableWidth * 0.10); // 30% width

            });

        } catch (IOException ex) {
            Logger.getLogger(ReportesController.class.getName()).log(Level.SEVERE, null, ex);
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

            System.out.println("CODIGO = " + codigo);
            txtAddInventoryBarcode.setText(codigo);

            // System.out.println("OBTENER CODIGO PARA BUSCAR E INSERTAR: " + codigo);
            // Aquí puedes utilizar el código obtenido para realizar otras acciones
            if (!codigo.isEmpty()) {
                Product product = productApi.ProductoToTicket(codigo, user.getToken());   //insertToTicket(codigo); // recibe el producto desde la rest api  
                txtAddInventoryName.setText(product.getName());
                txtAddInventorypurchasePrice.setText("" + product.getPurchasePrice());
                txtAddInventoryPrice.setText("" + product.getPrice());
                txtAddInventoryholesalePrice.setText("" + product.getWholesalePrice());
            }

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void fillChoiceBoxDepartament() {
        ObjectMapper mapper = new ObjectMapper();
        List<DepartmentDTO> departments = productApi.DepartamenNametList(user.getToken());

        // Crear una lista observable
        ObservableList<DepartmentDTO> departamentList = FXCollections.observableArrayList();

        // Agregar la opción "Todos los departamentos" al inicio
        DepartmentDTO allDepartmentsOption = new DepartmentDTO();
        allDepartmentsOption.setId(-1);
        allDepartmentsOption.setName("TODOS LOS DEPARTAMENTOS");
        departamentList.add(allDepartmentsOption);

        // Agregar los departamentos reales
        departamentList.addAll(departments);

        // Configurar los elementos en el ComboBox
        comboCategoryProducts.setItems(departamentList);

        // Configurar el StringConverter para mostrar solo el nombre
        comboCategoryProducts.setConverter(new StringConverter<DepartmentDTO>() {
            @Override
            public String toString(DepartmentDTO department) {
                return department.getName();
            }

            @Override
            public DepartmentDTO fromString(String string) {
                return departamentList.stream().filter(department -> department.getName().equals(string)).findFirst().orElse(null);
            }
        });

        // Establecer "Todos los departamentos" como valor por defecto
        comboCategoryProducts.setValue(allDepartmentsOption);
    }

    private void fillChoiceBoxDepartamentEntries() {
        ObjectMapper mapper = new ObjectMapper();
        List<DepartmentDTO> departments = productApi.DepartamenNametList(user.getToken());

        // Crear una lista observable
        ObservableList<DepartmentDTO> departamentList = FXCollections.observableArrayList();

        // Agregar la opción "Todos los departamentos" al inicio
        DepartmentDTO allDepartmentsOption = new DepartmentDTO();
        allDepartmentsOption.setId(-1);
        allDepartmentsOption.setName("TODOS LOS DEPARTAMENTOS");
        departamentList.add(allDepartmentsOption);

        // Agregar los departamentos reales
        departamentList.addAll(departments);

        // Configurar los elementos en el ComboBox
        comboBoxDepartament.setItems(departamentList);

        // Configurar el StringConverter para mostrar solo el nombre
        comboBoxDepartament.setConverter(new StringConverter<DepartmentDTO>() {
            @Override
            public String toString(DepartmentDTO department) {
                return department.getName();
            }

            @Override
            public DepartmentDTO fromString(String string) {
                return departamentList.stream().filter(department -> department.getName().equals(string)).findFirst().orElse(null);
            }
        });

        // Establecer "Todos los departamentos" como valor por defecto
        comboBoxDepartament.setValue(allDepartmentsOption);
    }

    private void fetchEntriesTable(String startDate, String endDate, String categorie) {

        tableViewEntries.setItems(entryApi.fetchEntries(startDate, endDate, categorie));

    }

    private void fetchEntriesTable(String name) {

        tableViewEntries.setItems(entryApi.fetchEntriesByName(name));

    }

    private void updateProduct() {
        ProductDTO product = productApi.getProductByBarcode(txtAddInventoryBarcode.getText(), user.getToken());

        txtAddInventoryName.setText(product.getName());
        txtAddInventorypurchasePrice.setText("" + product.getPurchasePrice());
        txtAddInventoryPrice.setText("" + product.getPrice());
        txtAddInventoryholesalePrice.setText("" + product.getWholesalePrice());

    }

    private void fillChoiceBoxGanancias() {
        ObservableList<String> gananciaOptions = FXCollections.observableArrayList();
        for (int i = 5; i <= 100; i += 1) {
            gananciaOptions.add(String.valueOf(i));
        }
        ComboAddInventoryProfit.setItems(gananciaOptions);
        ComboAddInventoryProfit.setItems(gananciaOptions);
        // Establecer valor por defecto
        ComboAddInventoryProfit.setValue("43");
        ComboAddInventoryProfit.setValue("43");
    }

    //calcula el precio de venta menudeo
    public static BigDecimal calculateSellingPrice(BigDecimal cost, BigDecimal profitMargin) {
        // Convertir el porcentaje de margen de beneficio a un decimal
        BigDecimal profitMarginDecimal = profitMargin.divide(BigDecimal.valueOf(100));
        // Calcular el precio de venta
        BigDecimal sellingPrice = cost.divide(BigDecimal.ONE.subtract(profitMarginDecimal), RoundingMode.HALF_UP);
        return sellingPrice;
    }

//          private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
    private void ajustarAnchoColumnas(
            TableColumn<?, ?> columnDate,
            TableColumn<?, ?> columnProductBarcode,
            TableColumn<?, ?> columnProductName,
            TableColumn<?, ?> columnSupplier,
            TableColumn<?, ?> columnAmount,
            TableColumn<?, ?> columnPrice,
            TableColumn<?, ?> columnTotalPrice
    ) {
        double tableWidth = tableViewEntries.getWidth();
        columnDate.setPrefWidth(tableWidth * 0.20);
        columnProductBarcode.setPrefWidth(tableWidth * 0.10);
        columnProductName.setPrefWidth(tableWidth * 0.20);
        columnAmount.setPrefWidth(tableWidth * 0.20);
        columnPrice.setPrefWidth(tableWidth * 0.10);
        columnTotalPrice.setPrefWidth(tableWidth * 0.10);
        columnSupplier.setPrefWidth(tableWidth * 0.10);
    }

}
