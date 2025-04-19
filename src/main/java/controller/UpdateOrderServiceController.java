package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import api.OrderRepairApi;
import api.ProductApi;
import beans.Product;
import com.albertocoronanavarro.puntoventafx.App;
import dto.OrderServiceDTO;
import dto.ProductDTO;
import dto.SparePartDTO;
import dto.UserDTO;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class UpdateOrderServiceController implements Initializable {

    private UserDTO user;
    ProductApi productApi = new ProductApi();

    ObservableList<SparePartDTO> data;
    ArrayList<SparePartDTO> sparePartList = new ArrayList<SparePartDTO>();
    private OrderRepairApi orderRepairApi = new OrderRepairApi();
    private OrderServiceDTO orderServiceDTO;
    private String folio = "";
    @FXML
    private TextField brandField;

    @FXML
    private TextField budgetField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField imeiField;

    @FXML
    private TextArea issueField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea noteArea;

    @FXML
    private TextField paidField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextArea receivedConditionField;

    @FXML
    private TextField remainingField;

    @FXML
    private Label labelTitle;

    @FXML
    private TextField replacementCostField;

    @FXML
    private ComboBox<String> comboStatus;

    @FXML
    private Label labelprofit;

    @FXML
    private TableView<SparePartDTO> tableProducts;

    /**
     * Initializes the controller class.
     */
    @FXML
    void onActionAddProduct(ActionEvent event) {
        buscarProducto();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initializeTableColumns();

        this.user = App.getUsuario();
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
                "Pendiente",
                "En revisión",
                "En reparación",
                "Reparado",
                "Entregado",
                "Finalizada"
        );

        comboStatus.setItems(statusOptions);

    }

    @FXML
    void onActionUpdate(ActionEvent event) {
        try {

            int errorSize = validateForm().length();
            String erros = validateForm();
            if (errorSize == 0) {
                orderServiceDTO.setClient(nameField.getText().trim());
                orderServiceDTO.setCellphone(phoneField.getText().trim());
                orderServiceDTO.setEmail(emailField.getText().trim());
                orderServiceDTO.setBrand(brandField.getText().trim());
                orderServiceDTO.setModel(modelField.getText().trim());
                orderServiceDTO.setImei(imeiField.getText().trim());
                orderServiceDTO.setIssue(issueField.getText().trim());
                orderServiceDTO.setService(issueField.getText().trim());
                orderServiceDTO.setReceivedCondition(receivedConditionField.getText());
                orderServiceDTO.setNote(noteArea.getText());
                orderServiceDTO.setPasswordCellPhone(passwordField.getText());
                System.out.println("");
                orderServiceDTO.setReplacementCost(Double.parseDouble(replacementCostField.getText().trim()));
                orderServiceDTO.setStatus(comboStatus.getValue());
              
                if(sparePartList.size()> 0){
                    for (SparePartDTO part : data) {
                   
                    System.out.println("ID 11:28 = " + part.getProduct().getId() + "Costo de compra " + part.getProduct().getPurchasePrice() );
                    part.setProductId(part.getProduct().getId());
                    part.setPurchasePrice(BigDecimal.valueOf(part.getProduct().getPurchasePrice()));
                    // Agrega más campos si es necesario
                  
                }
                }
                  orderServiceDTO.setSpareParts(sparePartList);
                if (orderServiceDTO.getEmail() == null || orderServiceDTO.getEmail().trim().isEmpty()) {
                    orderServiceDTO.setEmail(null);
                }

                if (budgetField.getText().trim() == null || budgetField.getText().trim().isEmpty()) {
                    orderServiceDTO.setRepairCost(null);
                    // presupuesto = null;
                } else {
                    orderServiceDTO.setRepairCost(Double.parseDouble(budgetField.getText().trim()));
                    // presupuesto = Double.parseDouble(budgetField.getText().trim());
                }
                if (paidField.getText().trim() == null || paidField.getText().trim().isEmpty()) {
                    orderServiceDTO.setPaid(null);
                    //abono = null;
                } else {
                    // abono = Double.parseDouble(paidField.getText().trim());
                    orderServiceDTO.setPaid(Double.parseDouble(paidField.getText().trim()));
                }
                if (remainingField.getText().trim() == null || remainingField.getText().trim().isEmpty()) {
                    orderServiceDTO.setLeft(null);
                    //restante = null;
                } else {
                    orderServiceDTO.setLeft(Double.parseDouble(remainingField.getText().trim()));
                    // restante = Double.parseDouble(remainingField.getText().trim());
                }

                //  OrderServiceDTO orderServiceDTO = new OrderServiceDTO(falla, nombre, telefono, presupuesto, abono, restante, nota, correo, marca, modelo, falla, estadoRecibido, password, imei);
                // OrderServiceDTO orderServiceDTO = new OrderServiceDTO(nombre, telefono, correo, marca, modelo, imei, presupuesto, abono, restante, falla, estadoRecibido, contraseña, nota);
                System.out.println("ENVIAR A ACTUALIZAR ..................FECHA = " + orderServiceDTO.getDate());
                orderRepairApi.updateOrderRepair(orderServiceDTO);
                  Stage stage = (Stage) nameField.getScene().getWindow();
                stage.close();
            } else {
                showAlert("errores", erros);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Ingrese valores numéricos válidos para presupuesto, abono y restante.");
        }

    }

    /**
     * @return the orderRepairApi
     */
    public OrderRepairApi getOrderRepairApi() {
        return orderRepairApi;
    }

    /**
     * @param orderRepairApi the orderRepairApi to set
     */
    public void setOrderRepairApi(OrderRepairApi orderRepairApi) {
        this.orderRepairApi = orderRepairApi;
    }

    /**
     * @return the folio
     */
    public String getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(String folio) throws IOException {
        this.folio = folio;
        System.out.println("Folio recibido en el controlador: " + this.folio);

        if (phoneField != null) {
            labelTitle.setText("Orden de Reparación Folio " + folio);
            orderServiceDTO = orderRepairApi.getOrderService(folio);
            sparePartList = orderServiceDTO.getSpareParts();
            data = FXCollections.observableList(sparePartList);
            tableProducts.setItems(data);
            tableProducts.refresh();
            phoneField.setText(orderServiceDTO.getCellphone());
            if (orderServiceDTO.getEmail() == null) {
                emailField.setText("");
            } else {
                emailField.setText(orderServiceDTO.getEmail());
            }
            if (orderServiceDTO.getRepairCost() == null) {
                budgetField.setText("");
            } else {
                budgetField.setText("" + orderServiceDTO.getRepairCost());
            }

            if (orderServiceDTO.getPaid() == null) {
                paidField.setText("");
            } else {
                paidField.setText("" + orderServiceDTO.getPaid());
            }

            if (orderServiceDTO.getLeft() == null) {
                remainingField.setText("");
            } else {
                remainingField.setText("" + orderServiceDTO.getLeft());
            }

            nameField.setText(orderServiceDTO.getClient());
            brandField.setText(orderServiceDTO.getBrand());
            modelField.setText(orderServiceDTO.getModel());
            imeiField.setText(orderServiceDTO.getImei());
            // budgetField.setText("" + orderServiceDTO.getRepairCost());
            // paidField.setText("" + orderServiceDTO.getPaid());
            // remainingField.setText("" + orderServiceDTO.getLeft());
            passwordField.setText(orderServiceDTO.getPasswordCellPhone());
            issueField.setText(orderServiceDTO.getIssue());
            receivedConditionField.setText(orderServiceDTO.getReceivedCondition());
            noteArea.setText(orderServiceDTO.getNote());
            replacementCostField.setText("" + orderServiceDTO.getReplacementCost());
            labelprofit.setText(String.format("Ganancia: %.2f  ", orderServiceDTO.getProfit()));

            comboStatus.setValue(orderServiceDTO.getStatus());
        }
    }

    private String validateForm() {
        System.out.println("correo " + emailField.getText());
        StringBuilder errors = new StringBuilder();

        // Validar campos de texto obligatorios
        if (nameField.getText().trim().isEmpty()) {
            errors.append("El campo 'Nombre' no puede estar vacío.\n");
        }
        if (phoneField.getText().trim().isEmpty()) {
            errors.append("El campo 'Teléfono' no puede estar vacío.\n");
        }
        if (!emailField.getText().trim().isEmpty()) {
            if (emailField.getText().trim().isEmpty() || !isValidEmail(emailField.getText().trim())) {
                errors.append("El correo electrónico no es válido.\n");
            }
        }

        if (brandField.getText().trim().isEmpty()) {
            errors.append("El campo 'Marca' no puede estar vacío.\n");
        }
        if (modelField.getText().trim().isEmpty()) {
            errors.append("El campo 'Modelo' no puede estar vacío.\n");
        }
        if (imeiField.getText().trim().isEmpty()) {
            errors.append("El campo 'IMEI' no puede estar vacío.\n");
        }
        if (issueField.getText().trim().isEmpty()) {
            errors.append("El campo 'Problema' no puede estar vacío.\n");
        }
        if (receivedConditionField.getText().trim().isEmpty()) {
            errors.append("El campo 'Condición recibida' no puede estar vacío.\n");
        }
        Double abono = null;
        Double presupuesto = null;
        Double restante = null;
        if (!budgetField.getText().trim().isEmpty()) { //
            presupuesto = parseDouble(budgetField.getText().trim(), "Presupuesto", errors);
            // Validar valores lógicos de presupuesto, abono y restante
            if (presupuesto != null && presupuesto <= 0) {
                errors.append("El 'Presupuesto' debe ser un valor positivo.\n");
            } else {
                System.out.println("EL VALOR ES UN NUMERO VALIDO");
            }
        }
        if (!paidField.getText().trim().isEmpty() || presupuesto != null) { //
            abono = parseDouble(paidField.getText().trim(), "Abono", errors);
            if (abono != null && abono < 0) {
                errors.append("El 'Abono' no puede ser un valor negativo.\n");
            }
        }

        if (!remainingField.getText().trim().isEmpty() || presupuesto != null) { //
            restante = parseDouble(remainingField.getText().trim(), "Restante", errors);

            if (restante != null && restante < 0) {
                errors.append("El 'Restante' no puede ser un valor negativo.\n");
            }
        }

        // Validar que los valores numéricos sean válidos
        return errors.toString();
    }

    // Método para validar si un correo electrónico tiene un formato correcto
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Método para mostrar una alerta de error
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Double parseDouble(String text, String fieldName, StringBuilder errors) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            errors.append("El campo '" + fieldName + "' debe ser un número válido.\n");
            return null;
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

            if (!codigo.isEmpty()) {
                ProductDTO product = productApi.getProductByBarcode(codigo, user.getToken());   //insertToTicket(codigo); // recibe el producto desde la rest api  
                // product.setAmount(new BigDecimal("1"));
                addTable(product);
                System.out.println("usuarPRODUCTO ZZZZ para insertar en la order de reparacion como refaccion " + product.getBarcode());
            }

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initializeTableColumns() {

        tableProducts.getColumns().clear(); // Limpiar las columnas de la tabla antes de agregar nuevas
//
        TableColumn<SparePartDTO, String> columnSparePartCost = new TableColumn<>("Codigo");
        columnSparePartCost.setCellValueFactory(new PropertyValueFactory<>("price"));
//        

        // Columna para el nombre del producto
        TableColumn<SparePartDTO, String> columnProductBarcode = new TableColumn<>("Codigo");
        columnProductBarcode.setCellValueFactory(cellData -> {
            ProductDTO product = cellData.getValue().getProduct();
            return new SimpleStringProperty(product != null ? "" + product.getId() : "");
        });
        // Columna para el nombre del producto
        TableColumn<SparePartDTO, String> columnProductName = new TableColumn<>("Costo");
        columnProductName.setCellValueFactory(cellData -> {
            ProductDTO product = cellData.getValue().getProduct();
            return new SimpleStringProperty(product != null ? product.getName() : "");
        });
//
//        TableColumn<SparePartDTO, String> columnAmount = new TableColumn<>("Cantidad");
//        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
//
//        TableColumn<SparePartDTO, String> columnName = new TableColumn<>("Refaccion");
//        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//        TableColumn<SparePartDTO, Integer> columnStock = new TableColumn<>("total Precio de compra");
//        columnStock.setCellValueFactory(new PropertyValueFactory<>("total"));

//        TableColumn<SparePartDTO, Button> columnButtonAdd = new TableColumn<>("Acción");
//        columnButtonAdd.setCellValueFactory(new PropertyValueFactory<>("botonAgregar"));
//
//        TableColumn<SparePartDTO, Button> columnButtonDelete = new TableColumn<>("Acción");
//        columnButtonDelete.setCellValueFactory(new PropertyValueFactory<>("botonEliminar"));
//
//        TableColumn<SparePartDTO, Button> columnButtonless = new TableColumn<>("Acción");
//        columnButtonless.setCellValueFactory(new PropertyValueFactory<>("botonBorrar"));
        TableColumn<SparePartDTO, Void> columnButtonDelete = new TableColumn<>("Eliminar");

        Callback<TableColumn<SparePartDTO, Void>, TableCell<SparePartDTO, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<SparePartDTO, Void> call(final TableColumn<SparePartDTO, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Borrar");

                    {
                        // btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                        btn.setOnAction(event -> {
                            SparePartDTO data = getTableView().getItems().get(getIndex());
                            getTableView().getItems().remove(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        columnButtonDelete.setCellFactory(cellFactory);

        tableProducts.getColumns().addAll(columnProductBarcode, columnProductName, columnSparePartCost, columnButtonDelete);

        // Set table width listener to adjust column widths in percentages
        tableProducts.widthProperty().addListener((obs, oldVal, newVal) -> {
            double tableWidth = newVal.doubleValue();
            columnProductBarcode.setPrefWidth(tableWidth * 0.30); // 15% width
            columnProductName.setPrefWidth(tableWidth * 0.30);
            columnSparePartCost.setPrefWidth(tableWidth * 0.30); // 15% width
            columnButtonDelete.setPrefWidth(tableProducts.getWidth() * 0.10);
//          
//            columnStock.setPrefWidth(tableWidth * 0.15); // 15% width
//            columnButtonAdd.setPrefWidth(tableWidth * 0.10);
//            columnButtonless.setPrefWidth(tableWidth * 0.10);
//            columnButtonDelete.setPrefWidth(tableWidth * 0.10);
        });
    }

//         public boolean existProdcutIntable(String codigoBarras, BigDecimal cantidad) {
//        boolean exists = false;
//        for (int i = 0; i < productList.size(); i++) {
//            Product p = productList.get(i);
//            System.out.println("PRODUCTO YA ESTA EN EL TICKET" + p.toString());
//            if (p.getBarcode().equalsIgnoreCase(codigoBarras)) {
//
//                //double cantidadTotal = p.getAmount() + cantidad;
//                BigDecimal cantidadTotal = p.getAmount().add(cantidad);
//                //double total = cantidadTotal * p.getPrice();
//                BigDecimal price = cantidadTotal.multiply(p.getPrice());
//                BigDecimal purchasePrice = cantidadTotal.multiply(p.getPurchasePrice());
//                System.out.println("TOTAL NUEVO " + price);
//
//                p.setAmount(cantidadTotal);
//                p.setPrice(price);
//                p.setTotal(purchasePrice);
//                exists = true;
//                break;
//            }
//        }
//
//        return exists;
//
//    }
    private void addTable(ProductDTO product) {

//        boolean exists = existProdcutIntable(product.getBarcode(), new BigDecimal("1"));
//        if (exists == false) {  //si ek producto no esta en la tabla se agrega 1.
//            productList.add(product);
//        }
        SparePartDTO sparePartDTO = new SparePartDTO();
        sparePartDTO.setProduct(product);
        //sparePartDTO.setSparePartCost(product.getPrice());
        sparePartDTO.setPrice(product.getPrice());
        System.out.println("preicio de compra= " + product.getPurchasePrice());
        sparePartDTO.setPurchasePrice(BigDecimal.valueOf(product.getPurchasePrice()));
        sparePartDTO.setProductId(product.getId());
        sparePartList.add(sparePartDTO);

        data = FXCollections.observableList(sparePartList);
//        data.forEach((tab) -> {
//            tab.getBotonAgregar().setOnAction(this::eventoTabla);
//            tab.getBotonAgregar().setMaxWidth(Double.MAX_VALUE);
//            tab.getBotonAgregar().setMaxHeight(Double.MAX_VALUE);
//            tab.getBotonBorrar().setOnAction(this::eventoTabla);
//            tab.getBotonBorrar().setMaxWidth(Double.MAX_VALUE);
//            tab.getBotonBorrar().setMaxHeight(Double.MAX_VALUE);
//
//            tab.getBotonEliminar().setOnAction(this::eventoTabla);
//            tab.getBotonEliminar().setMaxWidth(Double.MAX_VALUE);
//            tab.getBotonEliminar().setMaxHeight(Double.MAX_VALUE);
//
//        });
        tableProducts.setItems(data);
        tableProducts.refresh();

    }
    /*  
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

        tableProducts.setItems(data);
        tableProducts.refresh();

    }
     */
}
