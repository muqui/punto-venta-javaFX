package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import api.OrderRepairApi;
import beans.Product;
import dto.OrderServiceDTO;
import dto.ProductFindDTO;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class CelularesController implements Initializable {

    OrderRepairApi orderRepairApi = new OrderRepairApi();

    @FXML
    private TableView<OrderServiceDTO> tableOrdersRepair;

    @FXML
    private AnchorPane anchorPaneReparaciones;

    @FXML
    private Button btnFilter;

    @FXML
    private ComboBox<String> comboBoxStatus;

    @FXML
    private DatePicker datePickerEnd;

    @FXML
    private DatePicker datePickerStar;

    @FXML
    void onActionFilter(ActionEvent event) {
      
       filterOrderService();   
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            datePickerEnd.setValue(LocalDate.now());
            datePickerStar.setValue(LocalDate.now().minusDays(7));
            
              ObservableList<String> statusOptions = FXCollections.observableArrayList(
                "Todos",
                "Pendiente",
                "En revision",
                "En reparacion",
                "Reparado",
                "Entregado",
                "Cancelado",
                "Finalizada"
        );

        comboBoxStatus.setItems(statusOptions);
        comboBoxStatus.setValue("Todos"); // Valor por defecto
        
            // TODO
            initializeTableColumns();
            tableOrdersRepair.setItems(fetchOrdersServices(""));
        } catch (IOException ex) {
            Logger.getLogger(CelularesController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
          comboBoxStatus.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal != null) {
          //  System.out.println("Nuevo estado seleccionado: " + newVal);
            filterOrderService();
        }
    });
    }

    @FXML
    void onActionCrearOrden(ActionEvent event) {
        System.out.println("Crear orden .................");
        abrirModalCrerOrden();
    }

    private void abrirModalCrerOrden() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/crearOrdenReparaciones.fxml"));
            Parent root = fxmlLoader.load();
            CrearOrdenReparacionesController crearOrdenReparacionesController = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            //stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            //  String codigo = buscarController.getCodigo();
            //  initializeTableColumns();
            // tableOrdersRepair.getColumns().clear(); // Limpiar las columnas de la tabla antes de agregar nuevas
            tableOrdersRepair.setItems(fetchOrdersServices(""));

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initializeTableColumns() {
        tableOrdersRepair.getColumns().clear(); // Limpiar las columnas de la tabla antes de agregar nuevas

        TableColumn<OrderServiceDTO, String> columnBarcode = new TableColumn<>("Fecha Ingresos");
        columnBarcode.setCellValueFactory(new PropertyValueFactory<>("date"));

        //client
        TableColumn<OrderServiceDTO, String> columnClient = new TableColumn<>("Cliente");
        columnClient.setCellValueFactory(new PropertyValueFactory<>("client"));

        TableColumn<OrderServiceDTO, String> columnName = new TableColumn<>("folio");
        columnName.setCellValueFactory(new PropertyValueFactory<>("folio"));

        TableColumn<OrderServiceDTO, Double> columnPrice = new TableColumn<>("Equipo");
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<OrderServiceDTO, Integer> columnStock = new TableColumn<>("Modelo");
        columnStock.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<OrderServiceDTO, Integer> columnService = new TableColumn<>("Servicio");
        columnService.setCellValueFactory(new PropertyValueFactory<>("service"));

        //replacementCost
        TableColumn<OrderServiceDTO, Integer> columnReplacementCost = new TableColumn<>("Costo Refacciones");
        columnReplacementCost.setCellValueFactory(new PropertyValueFactory<>("replacementCost"));

        //repairCost
        TableColumn<OrderServiceDTO, Integer> columnRepairCost = new TableColumn<>("Costo reparacion");
        columnRepairCost.setCellValueFactory(new PropertyValueFactory<>("repairCost"));

        //status
        TableColumn<OrderServiceDTO, Integer> columnStatus = new TableColumn<>("Estado");
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        //profit
        TableColumn<OrderServiceDTO, Integer> columnProfit = new TableColumn<>("Ganancia");
        columnProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));

        TableColumn<OrderServiceDTO, Void> actionColumn = new TableColumn<>("Acción");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Ver");

            {
                button.setOnAction(event -> {
                    OrderServiceDTO order = getTableView().getItems().get(getIndex());
                    System.out.println("Botón presionado para: " + order.getFolio());
                    OpenModalUpdateOrderService(order.getFolio());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });

        tableOrdersRepair.getColumns().addAll(columnBarcode, columnClient, columnName, columnPrice, columnStock, columnService, columnRepairCost, columnReplacementCost, columnProfit, columnStatus, actionColumn);

        // Set table width listener to adjust column widths in percentages
        tableOrdersRepair.widthProperty().addListener((obs, oldVal, newVal) -> {
            double tableWidth = newVal.doubleValue();
            columnBarcode.setPrefWidth(tableWidth * 0.10); // 15% width
            columnClient.setPrefWidth(tableWidth * 0.10);
            columnName.setPrefWidth(tableWidth * 0.10); // 15% width
            columnPrice.setPrefWidth(tableWidth * 0.10); // 15% width
            columnStock.setPrefWidth(tableWidth * 0.10); // 15% width
            actionColumn.setPrefWidth(tableWidth * 0.04);
            columnService.setPrefWidth(tableWidth * 0.10);
            columnReplacementCost.setPrefWidth(tableWidth * 0.10);
            columnRepairCost.setPrefWidth(tableWidth * 0.10);
            columnStatus.setPrefWidth(tableWidth * 0.10);
            columnProfit.setPrefWidth(tableWidth * 0.05);
        });
    }

    private ObservableList<OrderServiceDTO> fetchOrdersServices(String value) throws IOException {

    
        
        
        List<OrderServiceDTO> orderServices = orderRepairApi.fetchOrdersService(datePickerStar.getValue().toString(), datePickerEnd.getValue().toString(), comboBoxStatus.getValue().toString());

        return FXCollections.observableArrayList(orderServices);

    }

    private void OpenModalUpdateOrderService(String folio) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/updateOrderService.fxml"));
            Parent root = fxmlLoader.load();
            UpdateOrderServiceController updateOrderServiceController = fxmlLoader.getController();
            System.out.println("Folio antes de ser enviado " + folio);
            updateOrderServiceController.setFolio(folio);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            //stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            tableOrdersRepair.setItems(fetchOrdersServices(""));

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void filterOrderService() {
           System.out.println("FILTRAR REPARACIONES");
        System.out.println("Fecha Fin " + datePickerEnd.getValue());
        System.out.println("Fecha inicio " + datePickerStar.getValue());
        System.out.println("Status " +  comboBoxStatus.getValue().toString()); 
        try {
            tableOrdersRepair.setItems(fetchOrdersServices(""));
            tableOrdersRepair.refresh();
        } catch (IOException ex) {
            Logger.getLogger(CelularesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}
