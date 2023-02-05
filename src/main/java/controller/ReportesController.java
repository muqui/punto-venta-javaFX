/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import beans.Producto;
import beans.Usuario;
import beans.VentaDetalle;
import dao.DaoDepartamento;
import dao.DaoPersona;
import dao.DaoProducto;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class ReportesController implements Initializable {
    
    
   
    @FXML
    private TableView<VentaDetalle> tableSales;
     @FXML
    private DatePicker dateEnd;
     @FXML
    private ComboBox<String> comboUser;

    @FXML
    private ComboBox<String> comobDepartament; 

    @FXML
    private DatePicker dateStar;
    DaoProducto daoProducto = new DaoProducto();
    DaoPersona daoPersona = new DaoPersona();
    DaoDepartamento daoDepartamento = new DaoDepartamento();

    ObservableList<VentaDetalle> data;
     @FXML
    void onActonComboDepartament(ActionEvent event) {
         System.out.println("action comobo department");
         llenarTablaVentas();
    }

    @FXML
    void onActonComboUser(ActionEvent event) {
           System.out.println("action comobo user");
         llenarTablaVentas();
    }
    
     @FXML
    void onActionDateEnd(ActionEvent event) {
       //  System.out.println("Fecha final " + dateEnd.getValue());
          llenarTablaVentas();
    }

    @FXML
    void onActionDateStart(ActionEvent event) {
          //  System.out.println("Fecha inicial " + dateStar.getValue());
             llenarTablaVentas();
             
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dateStar.setValue(LocalDate.now());
        dateEnd.setValue(LocalDate.now());
        daoDepartamento.getSellsTableData("");
       
        comboUser.setItems( daoPersona.getSellsTableData(""));
        comboUser.getSelectionModel().selectFirst();
        comobDepartament.setItems(daoDepartamento.getSellsTableData(""));
        comobDepartament.getSelectionModel().selectFirst();
         llenarTablaVentas();
    }

    public void llenarTablaVentas() {
        tableSales.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        data = daoProducto.getSellsTableData(dateStar.getValue().toString(), dateEnd.getValue().toString(), comboUser.getValue(), comobDepartament.getValue());
//        data.forEach((tab) -> {
//            tab.getBotonAgregar().setOnAction(this::eventoTabla);
//
//        });
        System.out.println("Llenar tabla");

        //celda id Venta
        TableColumn tableColumnID = new TableColumn("ID venta");
        tableColumnID.setCellValueFactory(new PropertyValueFactory<VentaDetalle, String>("idVenta"));
        tableColumnID.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        //celda Fecha
        TableColumn tableColumFecha = new TableColumn("Fecha");
        tableColumFecha.setCellValueFactory(new PropertyValueFactory<VentaDetalle, String>("fecha"));
        tableColumFecha.setMaxWidth(1f * Integer.MAX_VALUE * 15);
        //Codigo
        TableColumn tableColumCodigo = new TableColumn("Codigo");
        tableColumCodigo.setCellValueFactory(new PropertyValueFactory<VentaDetalle, String>("codigo"));
        tableColumCodigo.setMaxWidth(1f * Integer.MAX_VALUE * 8);
        //Nombre
        TableColumn tableColumNombre = new TableColumn("Nombre");
        tableColumNombre.setCellValueFactory(new PropertyValueFactory<VentaDetalle, String>("nombre"));
        tableColumNombre.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        // Departamento
        TableColumn<VentaDetalle, String> tableColumDepartamento = new TableColumn<>("Departamento");
        tableColumDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        tableColumDepartamento.setMaxWidth(1f * Integer.MAX_VALUE * 7);
        //Usuario
        TableColumn<VentaDetalle, String> tableColumUsuario = new TableColumn<>("Usuario");
        tableColumUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        tableColumUsuario.setMaxWidth(1f * Integer.MAX_VALUE * 7);
        //Cantidad
        TableColumn<VentaDetalle, String> tableColumCantidad = new TableColumn<>("Cantidad");
        tableColumCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tableColumCantidad.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        //Precio
        TableColumn<VentaDetalle, String> tableColumPrecio = new TableColumn<>("Precio");
        tableColumPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tableColumPrecio.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        //Costo
        TableColumn<VentaDetalle, String> tableColumCosto = new TableColumn<>("Costo");
        tableColumCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
        tableColumCosto.setMaxWidth(1f * Integer.MAX_VALUE * 5);

        //pago Con
        TableColumn<VentaDetalle, String> tableColumPagoCon = new TableColumn<>("Pago");
        tableColumPagoCon.setCellValueFactory(new PropertyValueFactory<>("pagoCon"));
        tableColumPagoCon.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        
         //Ganancia
        TableColumn<VentaDetalle, String> tableColumGanancia = new TableColumn<>("ganacia");
        tableColumGanancia.setCellValueFactory(new PropertyValueFactory<>("ganacia"));
        tableColumGanancia.setMaxWidth(1f * Integer.MAX_VALUE * 9);
        //total
        TableColumn<VentaDetalle, String> tableColumTotal = new TableColumn<>("Total");
        tableColumTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tableColumTotal.setMaxWidth(1f * Integer.MAX_VALUE * 9);
        tableSales.setItems(data);
        tableSales.getColumns().setAll(tableColumnID, tableColumFecha, tableColumCodigo, tableColumNombre, tableColumDepartamento, tableColumUsuario, tableColumCantidad, tableColumPrecio, tableColumCosto, tableColumPagoCon,tableColumGanancia,tableColumTotal);

    }

}
