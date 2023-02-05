/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import beans.Producto;
import beans.Usuario;
import com.albertocoronanavarro.puntoventafx.App;
import com.albertocoronanavarro.puntoventafx.PrimaryController;
//import com.sun.javafx.scene.control.skin.TableHeaderRow;
import dao.DaoProducto;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author mq12
 */
public class VentasController implements Initializable {

//    public VentasController(Usuario usuario){
//       this.usuario = usuario;
//    }
    private Usuario usuario;
    private boolean mayoreo = false;
    Producto producto = new Producto();
    double totalProductos = 0;
    String leyendaCantidadTotal = " Total productos";
    ObservableList<Producto> listProducto;
    DaoProducto daoProducto = new DaoProducto();
    //Estos 3 arraList son para llenar las tablas de venta
    ArrayList<Tab> tabArrayList = new ArrayList<Tab>(); // se crar un array de tabs.
    ArrayList<ObservableList<Producto>> observableListArrayList = new ArrayList<ObservableList<Producto>>(); // Se crea un array de ObsevableList
    ArrayList<List<Producto>> listaProductoArrayList = new ArrayList<List<Producto>>(); // Se crea una lista de lsita de productos

    @FXML
    private TextField txtCodigoBarras;
    @FXML
    private Button btnCambiarTicket;
    @FXML
    private Label labelTotalProductos;

    @FXML
    private Label txtVentasMayoreo;

    @FXML
    private Button btnCobrar;

    @FXML
    private Label labelTotal;
    @FXML
    TabPane tabPaneTicket;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtVentasMayoreo.setVisible(false);
        //recibimos el usuario desde Main(App)
        this.usuario = App.getUser();
        System.out.println("Desde APP" + this.usuario.getNombre());

        crearTicket("Ticket 1");
        txtCodigoBarras.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {

                    insertarProductoTicket(txtCodigoBarras.getText(), 1);
                }
            }
        });

    }

    //Busca de producto
    @FXML
    void btnBuscarAction(ActionEvent event) {
        buscarProducto();
    }

    @FXML
    void btnMayoreoAction(ActionEvent event) {
        mayoreo();
    }

    @FXML // cambia el ticket seleccionado.
    void onActionBtnCambiarTicket(ActionEvent event) {
        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex();
        int tabsTotal = tabPaneTicket.getTabs().size();

        if (tabSeleccionado == tabsTotal - 1) {
            tabPaneTicket.getSelectionModel().selectFirst();
        }
        tabPaneTicket.getSelectionModel().select(tabSeleccionado + 1);

    }

    @FXML
    void btnCobrarAction(ActionEvent event) {
        precobrar();
    }

    @FXML
    void ActionBtnCrearTicket(ActionEvent event) {

        int numTabs = tabPaneTicket.getTabs().size();

        if (numTabs < 10) {
            crearTicket("Ticket " + (numTabs + 1));
            // set total to zero
            labelTotal.setText("0");
        }

    }

    //Abre ventana para registrar mas de un pago
    @FXML
    void actionbtnInsertarMasDe1(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/masDeUnProducto.fxml"));
            Parent root = fxmlLoader.load();
            MasDeUnProductoController masdeuno = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            String codigo = masdeuno.getCodigo();
            double cantidad = Double.parseDouble(masdeuno.getCantidad());

            System.out.println("CANTIDAD " + cantidad + " CODIGO " + codigo);
            insertarProductoTicket(codigo, cantidad);

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void tabSelected(MouseEvent event) {
        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex();
        // suma del precio total
        labelTotal.setText("" + totalTicket(tabSeleccionado));
        //Suma la cantidad total de productos
        labelTotalProductos.setText("" + CantidadProductosTicket(tabSeleccionado) + leyendaCantidadTotal);
    }

    @FXML
    void ActionBtnAgregarProducto(ActionEvent event) {
        insertarProductoTicket(txtCodigoBarras.getText(), 1);
    }

    @FXML //Elimina tickets menos el primero.
    void ActionBtnEliminarTicket(ActionEvent event) {

        int numTabs = tabPaneTicket.getTabs().size() - 1;
        if (numTabs > 0) // elimina todos los tickes exepto el primero
        {
            tabPaneTicket.getTabs().remove(numTabs);
            tabArrayList.remove(numTabs);
            listaProductoArrayList.remove(numTabs);
        }

    }

    /**
     * Initializes the controller class.
     */
    public void crearTicket(String nombre) {

        TableView tableView = new TableView();
        tableView.setId("miTabla");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // NECESARIO PARA CAMBIAR EL ANCHO DE LA TABLA
        //evita el reordenamiento( las columnas no se mueven de lugar).
//        tableView.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
//                TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
//                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                        header.setReordering(false);
//                    }
//                });
//            }
//        });
        int numTabs = tabPaneTicket.getTabs().size();

        TableColumn<Producto, String> column1 = new TableColumn<>("CODIGO");
        column1.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
        column1.setResizable(true);
        column1.setMaxWidth(1f * Integer.MAX_VALUE * 20);

        //COLUMNA
        TableColumn<Producto, String> column2 = new TableColumn<>("NOMBRE");
        column2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        column2.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        //COLUMNA
        TableColumn<Producto, String> column3 = new TableColumn<>("PRECIO");
        column3.setCellValueFactory(new PropertyValueFactory<>("precioVentaUnitario"));
        column3.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        //COLUMNA
        TableColumn<Producto, String> column4 = new TableColumn<>("CANTIDAD");
        column4.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        column4.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        //COLUMNA
        TableColumn<Producto, String> column5 = new TableColumn<>("TOTAL");
        column5.setCellValueFactory(new PropertyValueFactory<>("totalTicket"));
        column5.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        //COLUMNA
        TableColumn<Producto, String> column6 = new TableColumn<>(" ");
        column6.setCellValueFactory(new PropertyValueFactory<>("botonAgregar"));
        column6.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        //COLUMNA
        TableColumn<Producto, String> column7 = new TableColumn<>(" ");
        column7.setCellValueFactory(new PropertyValueFactory<>("botonBorrar"));
        column7.setMaxWidth(1f * Integer.MAX_VALUE * 5);

        //COLUMNA
        TableColumn<Producto, String> column8 = new TableColumn<>(" ");
        column8.setCellValueFactory(new PropertyValueFactory<>("botonEliminar"));
        column8.setMaxWidth(1f * Integer.MAX_VALUE * 5);

        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);
        tableView.getColumns().add(column6);
        tableView.getColumns().add(column7);
        tableView.getColumns().add(column8);

        Tab tab = new Tab(nombre);

        StackPane tabLayout = new StackPane();
        tabLayout.getChildren().add(tableView);
        tab.setContent(tabLayout);

        tabArrayList.add(tab);

        // INSERTA observableList VACIO
        observableListArrayList.add(FXCollections.observableArrayList());
        listaProductoArrayList.add(new ArrayList<>());
        tabPaneTicket.getTabs().add(
                tabArrayList.get(numTabs)
        );

        tabPaneTicket.getSelectionModel().select(tab);
        labelTotalProductos.setText("" + CantidadProductosTicket(numTabs) + leyendaCantidadTotal);

    }

    /*
    *
    * Agrega producto al ticket
    *
     */
    public void insertarProductoTicket(String codigo, double cantidad) {
        //String codigoBarras = txtCodigoBarras.getText();
        String codigoBarras = codigo;
        Producto producto = getProduct(codigoBarras, cantidad);
        // System.out.println("cantidad product " + producto.getCantidad());
        if (producto == null) {
            txtCodigoBarras.setText("");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No existe el producto! " + this.usuario.getNombre());
            alert.showAndWait();
        } else {  //Existe el producto INICIO
            // producto.setCantidad(cantidad);

            int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex(); // Selecciona el tab Seleccionado.    
            boolean existe = existeProductoEnticket(tabSeleccionado, codigoBarras, cantidad);
            Node selectedContent = tabArrayList.get(tabSeleccionado).getContent();
            if (listaProductoArrayList.get(tabSeleccionado).size() <= 0) {

                System.out.println(" inserta producto.................... xxxxxxab ");
                listaProductoArrayList.get(tabSeleccionado).add(producto); // add producto to list
            } else {
                if (existe == false) { // if product doesn't exist add it to ticket  

                    listaProductoArrayList.get(tabSeleccionado).add(getProduct(codigoBarras, cantidad)); // add producto to list

                }
            }

            labelTotal.setText("" + totalTicket(tabSeleccionado));
            labelTotalProductos.setText("" + CantidadProductosTicket(tabSeleccionado) + leyendaCantidadTotal);
            ObservableList<Producto> data = observableListArrayList.get(tabSeleccionado);

            data = FXCollections.observableList(listaProductoArrayList.get(tabSeleccionado));

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
            TableView tableView = (TableView) selectedContent.lookup("#miTabla");

            tableView.setItems(data);
            // alto de la fila
            tableView.setFixedCellSize(50);
            tableView.refresh();
            //clear field
            txtCodigoBarras.setText("");
        }//Existe el producto FIN

    }

    /*
    * Return Producto from database but update amonut to 1, set the amount total.
     */
    private Producto getProduct(String codigoBarras, double cantidad) {
        Producto p = daoProducto.getProducto(codigoBarras); // get Producto from dababase

        if (p != null) {

            //selecciona el precio mayoreo o menudeo.
            double precio = mayoreo ? p.getPrecioMayoreo() : p.getPrecioVentaUnitario();
            p.setPrecioVentaUnitario(precio);
            p.setTotalTicket(p.getPrecioVentaUnitario() * cantidad);
            System.out.println("aqui registra" + p.getPrecioVentaUnitario());
            p.setCantidad(cantidad);
            // p.setCantidad(1); // update amount to one
        }

        return p;
    }

    /*
    * Check if exist product in ticket
     */
    private boolean existeProductoEnticket(int tabSeleccionado, String codigoBarras, double mas) {
        boolean existe = false;
        for (int i = 0; i < listaProductoArrayList.get(tabSeleccionado).size(); i++) {
            Producto p = listaProductoArrayList.get(tabSeleccionado).get(i);
            if (p.getCodigoBarras().equalsIgnoreCase(codigoBarras)) {

                double cantidad = p.getCantidad() + mas;
                double total = cantidad * p.getPrecioVentaUnitario();

                p.setCantidad(cantidad);
                p.setTotalTicket(total);
                existe = true;
                break;
            }
        }
        return existe;
    }

    // Suma total (precio)
    private double totalTicket(int tabSeleccionado) {
        double total = 0;
        for (int i = 0; i < listaProductoArrayList.get(tabSeleccionado).size(); i++) {
            Producto p = listaProductoArrayList.get(tabSeleccionado).get(i);
            total = total + p.getTotalTicket();
        }
        return total;
    }

    /*
    Suma el total de productos (double)
     */
    private double CantidadProductosTicket(int tabSeleccionado) {

        double total = 0;
        for (int i = 0; i < listaProductoArrayList.get(tabSeleccionado).size(); i++) {
            Producto p = listaProductoArrayList.get(tabSeleccionado).get(i);

            total = total + p.getCantidad();
        }
        return total;
    }

    private void eventoTabla(ActionEvent event) {

        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex();
        System.out.println("BOTON PRESIONADO = " + tabSeleccionado);
        ObservableList<Producto> data = observableListArrayList.get(tabSeleccionado);
        Node selectedContent = tabArrayList.get(tabSeleccionado).getContent();

        data = FXCollections.observableList(listaProductoArrayList.get(tabSeleccionado));
        TableView tableView = (TableView) selectedContent.lookup("#miTabla");
        for (int i = 0; data.size() > i; i++) {
            Producto p = data.get(i);
            //delete item from ticket
            if (event.getSource() == p.getBotonEliminar()) {

                // delete by itself
                data.remove(p);

            }
            if (event.getSource() == p.getBotonAgregar()) {

                p.setCantidad(p.getCantidad() + 1);
                p.setTotalTicket(p.getCantidad() * p.getPrecioVentaUnitario());

            }
            if (event.getSource() == p.getBotonBorrar()) {

                if (p.getCantidad() > 1) {
                    p.setCantidad(p.getCantidad() - 1);
                    p.setTotalTicket(p.getCantidad() * p.getPrecioVentaUnitario());
                    System.out.println("cantidad " + p.getCantidad());
                }

            }
        }

        tableView.setItems(data);
        tableView.refresh();
        // suma del precio total
        labelTotal.setText("" + totalTicket(tabSeleccionado));
        //Suma la cantidad total de productos
        labelTotalProductos.setText("" + CantidadProductosTicket(tabSeleccionado) + leyendaCantidadTotal);
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private void buscarProducto() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/buscar.fxml"));
            Parent root = fxmlLoader.load();
            BuscarController buscarController = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            // stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            if (!buscarController.getCodigo().equalsIgnoreCase("")) {
                insertarProductoTicket(buscarController.getCodigo(), 1);
            }

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void mayoreo() {
        mayoreo = !mayoreo;
        txtVentasMayoreo.setVisible(mayoreo);
        System.out.println("Mayoreo " + mayoreo);

    }

    private void precobrar() {

        //Carga el modal de cobrar.
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/cobrar.fxml"));
            Parent root = fxmlLoader.load();
            CobrarController cobrarController = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex(); // Selecciona el tab Seleccionado.    

        System.out.println("cobrar......" + totalTicket(tabSeleccionado));

        ObservableList<Producto> data = observableListArrayList.get(tabSeleccionado);

        data = FXCollections.observableList(listaProductoArrayList.get(tabSeleccionado));

        daoProducto.saveSale(data);
        // clear ticket
        Node selectedContent = tabArrayList.get(tabSeleccionado).getContent();
        TableView tableView = (TableView) selectedContent.lookup("#miTabla");
        for (int i = data.size() - 1; i >= 0; i--) {
            Producto p = data.get(i);
            //delete item from ticket
            data.remove(p);
        }
        tableView.setItems(data);
        tableView.refresh();
       
        labelTotal.setText("$ 0");
       
        labelTotalProductos.setText("0"  + leyendaCantidadTotal);

    }

}
