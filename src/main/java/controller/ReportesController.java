/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import beans.OrderDetail;
import beans.VentaDetalle;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.OrderDTO;
import dto.OrderDetailDTO;
import java.io.IOException;
import java.math.BigDecimal;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class ReportesController implements Initializable {

    private ObservableList<OrderDetail> orderDetailsList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<OrderDTO, String> TableColumnBarcode;

    @FXML
    private TableColumn<OrderDTO, Integer> TableColumnId;

    @FXML
    private TableView<OrderDetailDTO> tableSales;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private ComboBox<String> comboUser;

    @FXML
    private ComboBox<String> comobDepartament;

    @FXML
    private DatePicker dateStar;

    ObservableList<VentaDetalle> data;

    @FXML
    void onActonComboDepartament(ActionEvent event) {
        System.out.println("action comobo department");
        // llenarTablaVentas();
    }

    @FXML
    void onActonComboUser(ActionEvent event) {
        System.out.println("action comobo user");
        // llenarTablaVentas();
    }

    @FXML
    void onActionDateEnd(ActionEvent event) {
        //  System.out.println("Fecha final " + dateEnd.getValue());
        //  llenarTablaVentas();
    }

    @FXML
    void onActionDateStart(ActionEvent event) {
        //  System.out.println("Fecha inicial " + dateStar.getValue());
        //   llenarTablaVentas();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            //            TableColumn<OrderDetailDTO, Integer> column1 = new TableColumn<>("Order ID");
//            column1.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOrder().getId()).asObject());
//            column1.setResizable(true);
//            column1.setMaxWidth(1f * Integer.MAX_VALUE * 10);
            TableColumn<OrderDetailDTO, String> columnDate = new TableColumn<>("Order Date");
            columnDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrder().getDate()));
            columnDate.setResizable(true);

            //codigo de barras
            TableColumn<OrderDetailDTO, String> columnBarcode = new TableColumn<>("Codigo de barras");
            columnBarcode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getBarcode()));
            columnBarcode.setResizable(true);

            TableColumn<OrderDetailDTO, String> columnUserName = new TableColumn<>("Usuario");
            columnUserName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrder().getUser().getName()));
            columnUserName.setResizable(true);

            TableColumn<OrderDetailDTO, Double> columnPrice = new TableColumn<>("Precio venta ");  //precio venta
            columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            columnPrice.setResizable(true);

            TableColumn<OrderDetailDTO, Double> columnPurchasePrice = new TableColumn<>("Precio compra");  //precio venta
            columnPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
            columnPurchasePrice.setResizable(true);

            TableColumn<OrderDetailDTO, Integer> columnAmount = new TableColumn<>("Cantidad");
            columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            columnAmount.setResizable(true);

            TableColumn<OrderDetailDTO, String> columnName = new TableColumn<>("Producto");
            columnName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
            columnName.setResizable(true);

            // Nueva columna para la diferencia
            TableColumn<OrderDetailDTO, BigDecimal> columnProfit = new TableColumn<>("Beneficio");
            columnProfit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderDetailDTO, BigDecimal>, javafx.beans.value.ObservableValue<BigDecimal>>() {
                @Override
                public javafx.beans.value.ObservableValue<BigDecimal> call(CellDataFeatures<OrderDetailDTO, BigDecimal> param) {
                    return new ReadOnlyObjectWrapper<>(param.getValue().getProfit());
                }
            });

            tableSales.getColumns().addAll(columnDate, columnUserName, columnBarcode, columnName, columnAmount, columnPrice, columnPurchasePrice, columnProfit);
            tableSales.setItems(fetchOrderDetails());

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
