/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import beans.Producto;
import dao.DaoProducto;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class BuscarController implements Initializable {
    DaoProducto daoProducto = new DaoProducto();
    
    private String codigo = "";
    ObservableList<Producto> data;
      @FXML
    private TableView<Producto> tableBuscar;

    @FXML
    private TextField txtBuscar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
         txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                System.out.println("Texto capturado " +  txtBuscar.getText());
               // daoProducto.getInitialTableData(txtBuscar.getText().toString());
                llenarTablaBuscar(txtBuscar.getText());
            }
        });

    }    
    
    
    public void llenarTablaBuscar(String filtro) {
        tableBuscar.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        data = daoProducto.getInitialTableData(filtro);
        data.forEach((tab) -> {
            tab.getBotonAgregar().setOnAction(this::eventoTabla);
          
        });
        System.out.println("Llenar tabla");
        
        //celda Codigo
        TableColumn tableColumnCodigo = new TableColumn("Codigo de Barras");
        tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<Producto, String>("CodigoBarras"));
        tableColumnCodigo.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        //celda Nombre
        TableColumn tableColumNombre = new TableColumn("nombre");
        tableColumNombre.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombre"));
        tableColumNombre.setMaxWidth(1f * Integer.MAX_VALUE *50);
        
        //departamento
//         TableColumn tableColumDepartamento = new TableColumn("Departamento");
//        tableColumDepartamento.setCellValueFactory(new PropertyValueFactory<Producto, String>("departamento"));
//        tableColumDepartamento.setMaxWidth(1f * Integer.MAX_VALUE *50);
        //precio
         TableColumn tableColumPrecio = new TableColumn("Precio");
        tableColumPrecio.setCellValueFactory(new PropertyValueFactory<Producto, String>("precioVentaUnitario"));
        tableColumPrecio.setMaxWidth(1f * Integer.MAX_VALUE *15);
        //cantidad
        TableColumn tableColumCantidad = new TableColumn("Cantidad");
        tableColumCantidad.setCellValueFactory(new PropertyValueFactory<Producto, String>("Cantidad"));
        tableColumCantidad.setMaxWidth(1f * Integer.MAX_VALUE *15);
         
        // boton agregar a lista
        //COLUMNA
        TableColumn<Producto, String> tableColumboton = new TableColumn<>(" ");
        tableColumboton.setCellValueFactory(new PropertyValueFactory<>("botonAgregar"));
        tableColumboton.setMaxWidth(1f * Integer.MAX_VALUE * 5);

       
        tableBuscar.setItems(data);
        tableBuscar.getColumns().setAll( tableColumnCodigo, tableColumNombre,tableColumPrecio, tableColumCantidad, tableColumboton);

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

    private void eventoTabla(ActionEvent event) {
    
       //   int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex();
      //   System.out.println("BOTON PRESIONADO = " + tabSeleccionado);
      //  ObservableList<Producto> data = observableListArrayList.get(tabSeleccionado);
       // Node selectedContent = tabArrayList.get(tabSeleccionado).getContent();

       // data = FXCollections.observableList(listaProductoArrayList.get(tabSeleccionado));
       //  TableView tableView = (TableView) selectedContent.lookup("#miTabla");
        for (int i = 0; data.size() > i; i++) {
            Producto p = data.get(i);
           
            
            if (event.getSource() == p.getBotonAgregar()) {
                
                codigo = p.getCodigoBarras();

                
                System.out.println("bucar producto codigo = " + codigo);
               Stage stage = (Stage) this.tableBuscar.getScene().getWindow();
        stage.close();
            }
        }

       

   
        
        
        
    }
    
    
}
