/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import beans.Order;
import beans.OrderDetail;
import beans.Product;
import beans.User;

import com.albertocoronanavarro.puntoventafx.App;
import dto.UserDTO;
//import com.sun.javafx.scene.control.skin.TableHeaderRow;

import java.io.IOException;
import java.math.BigDecimal;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author mq12
 */
public class VentasController implements Initializable {

    // Product productFromDataBase;
//    public VentasController(Usuario usuario){
//       this.usuario = usuario;
//    }
    private UserDTO usuario;
    private boolean mayoreo = false;
    // Producto producto = new Producto();
    double totalProductos = 0;
    String leyendaCantidadTotal = " Total productos";
//    ObservableList<Producto> listProducto;
//    DaoProducto daoProducto = new DaoProducto();
    //Estos 3 arraList son para llenar las tablas de venta
    ArrayList<Tab> tabArrayList = new ArrayList<Tab>(); // se crar un array de tabs.
    // ArrayList<ObservableList<Producto>> observableListArrayList = new ArrayList<ObservableList<Producto>>(); // Se crea un array de ObsevableList
    // ArrayList<List<Producto>> listaProductoArrayList = new ArrayList<List<Producto>>(); // Se crea una lista de lsita de productos

    ArrayList<ObservableList<Product>> observableListArrayList = new ArrayList<ObservableList<Product>>(); // Se crea un array de ObsevableList
    ArrayList<List<Product>> listaProductoArrayList = new ArrayList<List<Product>>(); // Se crea una lista de lsita de productos

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

    @FXML
    private Button btnMayoreo;

    @FXML
    private Button btnSuprimir;

    @FXML
    private Button btnVerificar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //OCULTAR ESTO BOTONES TEMPORALMENTE HASTA CODIFICAR
        btnMayoreo.setVisible(false);
        btnSuprimir.setVisible(false);
        btnVerificar.setVisible(false);

        txtVentasMayoreo.setVisible(false);
        //recibimos el usuario desde Main(App)
        this.usuario = App.getUsuario();
        System.out.println("Desde APP" + this.usuario.getEmail());

        crearTicket("Ticket 1");
        txtCodigoBarras.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {

                    Product product = insertToTicket(txtCodigoBarras.getText()); // recibe el producto desde la rest api

                    System.out.println("Producto capturado desde textCodigodeBarras = " + product.getBarcode());
                    if (product.getBarcode() != null) {
                        insertarProductoTicket(product, new BigDecimal("1")); //inserta el producto al ticket
                    }

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

        updatePriceAfterChangeTicket();
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

    /*
    Inserta mas de un producto a travez de un modal
     */
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

            // double cantidad = Double.parseDouble(masdeuno.getCantidad());
            BigDecimal cantidad = new BigDecimal(masdeuno.getCantidad());

            Product product = insertToTicket(codigo); // recibe el producto desde la rest api  

            if (product.getBarcode() != null) {  // si el producto existe se carga al ticket
                //product.setTotal(product.getPrice() * cantidad);  // Calcula el total.
                product.setTotal(product.getPrice().multiply(cantidad));

                insertarProductoTicket(product, cantidad); // Envia el producto al ticket (tableView)
            }

//
        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void tabSelected(MouseEvent event) {
//        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex();
//        // suma del precio total
//        labelTotal.setText("" + totalTicket(tabSeleccionado));
//        //Suma la cantidad total de productos
//        labelTotalProductos.setText("" + CantidadProductosTicket(tabSeleccionado) + leyendaCantidadTotal);
        updatePriceAfterChangeTicket();
    }

    @FXML
    void ActionBtnAgregarProducto(ActionEvent event) {
        // insertarProductoTicket(txtCodigoBarras.getText(), 1);
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

        TableColumn<Product, String> column1 = new TableColumn<>("CODIGO");
        column1.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        column1.setResizable(true);
        column1.setMaxWidth(1f * Integer.MAX_VALUE * 20);

        //COLUMNA
        TableColumn<Product, String> column2 = new TableColumn<>("NOMBRE");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        column2.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        //COLUMNA
        TableColumn<Product, String> column3 = new TableColumn<>("PRECIO");
        column3.setCellValueFactory(new PropertyValueFactory<>("price"));
        column3.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        //COLUMNA
        TableColumn<Product, String> column4 = new TableColumn<>("CANTIDAD");
        column4.setCellValueFactory(new PropertyValueFactory<>("amount"));
        column4.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        //COLUMNA
        TableColumn<Product, String> column5 = new TableColumn<>("TOTAL");
        column5.setCellValueFactory(new PropertyValueFactory<>("total"));
        column5.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        //COLUMNA
        TableColumn<Product, String> column6 = new TableColumn<>(" ");
        column6.setCellValueFactory(new PropertyValueFactory<>("botonAgregar"));
        column6.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        //COLUMNA
        TableColumn<Product, String> column7 = new TableColumn<>(" ");
        column7.setCellValueFactory(new PropertyValueFactory<>("botonBorrar"));
        column7.setMaxWidth(1f * Integer.MAX_VALUE * 5);

        //COLUMNA
        TableColumn<Product, String> column8 = new TableColumn<>(" ");
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
//    public void insertarProductoTicket(String codigo, double cantidad) {
//        //String codigoBarras = txtCodigoBarras.getText();
//        String codigoBarras = codigo;
//        Producto producto = getProduct(codigoBarras, cantidad);
//        // System.out.println("cantidad product " + producto.getCantidad());
//        if (producto == null) {
//            txtCodigoBarras.setText("");
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.initStyle(StageStyle.UTILITY);
//            alert.setTitle("Error");
//            alert.setHeaderText(null);
//            alert.setContentText("No existe el producto! " + this.usuario.getNombre());
//            alert.showAndWait();
//        } else {  //Existe el producto INICIO
//            // producto.setCantidad(cantidad);
//
//            int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex(); // Selecciona el tab Seleccionado.    
//            boolean existe = existeProductoEnticket(tabSeleccionado, codigoBarras, cantidad);
//            Node selectedContent = tabArrayList.get(tabSeleccionado).getContent();
//            if (listaProductoArrayList.get(tabSeleccionado).size() <= 0) {
//
//                System.out.println(" inserta producto.................... xxxxxxab ");
//                listaProductoArrayList.get(tabSeleccionado).add(producto); // add producto to list
//            } else {
//                if (existe == false) { // if product doesn't exist add it to ticket  
//
//                    listaProductoArrayList.get(tabSeleccionado).add(getProduct(codigoBarras, cantidad)); // add producto to list
//
//                }
//            }
//
//            labelTotal.setText("" + totalTicket(tabSeleccionado));
//            labelTotalProductos.setText("" + CantidadProductosTicket(tabSeleccionado) + leyendaCantidadTotal);
//            ObservableList<Producto> data = observableListArrayList.get(tabSeleccionado);
//
//            data = FXCollections.observableList(listaProductoArrayList.get(tabSeleccionado));
//
//            data.forEach((tab) -> {
//                tab.getBotonAgregar().setOnAction(this::eventoTabla);
//                tab.getBotonAgregar().setMaxWidth(Double.MAX_VALUE);
//                tab.getBotonAgregar().setMaxHeight(Double.MAX_VALUE);
//                tab.getBotonBorrar().setOnAction(this::eventoTabla);
//                tab.getBotonBorrar().setMaxWidth(Double.MAX_VALUE);
//                tab.getBotonBorrar().setMaxHeight(Double.MAX_VALUE);
//
//                tab.getBotonEliminar().setOnAction(this::eventoTabla);
//                tab.getBotonEliminar().setMaxWidth(Double.MAX_VALUE);
//                tab.getBotonEliminar().setMaxHeight(Double.MAX_VALUE);
//
//            });
//            TableView tableView = (TableView) selectedContent.lookup("#miTabla");
//
//            tableView.setItems(data);
//            // alto de la fila
//            tableView.setFixedCellSize(50);
//            tableView.refresh();
//            //clear field
//            txtCodigoBarras.setText("");
//        }//Existe el producto FIN
//
//    }

    /*
    * Return Producto from database but update amonut to 1, set the amount total.
     */
//    private Producto getProduct(String codigoBarras, double cantidad) {
//        Producto p = daoProducto.getProducto(codigoBarras); // get Producto from dababase
//
//        if (p != null) {
//
//            //selecciona el precio mayoreo o menudeo.
//            double precio = mayoreo ? p.getPrecioMayoreo() : p.getPrecioVentaUnitario();
//            p.setPrecioVentaUnitario(precio);
//            p.setTotalTicket(p.getPrecioVentaUnitario() * cantidad);
//            System.out.println("aqui registra" + p.getPrecioVentaUnitario());
//            p.setCantidad(cantidad);
//            // p.setCantidad(1); // update amount to one
//        }
//
//        return p;
//    }

    /*
    * Check if exist product in ticket, update amoutn an d total price
     */
    private boolean existeProductoEnticket(int tabSeleccionado, String codigoBarras, BigDecimal cantidad) {
        boolean existe = false;
        for (int i = 0; i < listaProductoArrayList.get(tabSeleccionado).size(); i++) {
            Product p = listaProductoArrayList.get(tabSeleccionado).get(i);
            System.out.println("PRODUCTO YA ESTA EN EL TICKET" + p.toString());
            if (p.getBarcode().equalsIgnoreCase(codigoBarras)) {

                //double cantidadTotal = p.getAmount() + cantidad;
                BigDecimal cantidadTotal = p.getAmount().add(cantidad);
                //double total = cantidadTotal * p.getPrice();
                BigDecimal total = cantidadTotal.multiply(p.getPrice());
                System.out.println("TOTAL NUEVO " + total);

                p.setAmount(cantidadTotal);
                p.setTotal(total);
                existe = true;
                break;
            }
        }

        return existe;

    }

//    // Suma total (precio)
//    private BigDecimal totalTicket(int tabSeleccionado) {
//        double total = 0;
//        for (int i = 0; i < listaProductoArrayList.get(tabSeleccionado).size(); i++) {
//            Product p = listaProductoArrayList.get(tabSeleccionado).get(i);
//            total = total + p.getTotal();
//        }
//        return total;
//    }
    private BigDecimal totalTicket(int tabSeleccionado) {
        BigDecimal total = BigDecimal.ZERO;  // Inicializa total como BigDecimal.ZERO

        List<Product> productos = listaProductoArrayList.get(tabSeleccionado);
        for (Product p : productos) {
            BigDecimal productTotal = p.getTotal();  // Asegúrate de que p.getTotal() devuelva BigDecimal
            total = total.add(productTotal);  // Usa add para sumar BigDecimal
        }

        return total;  // Devuelve el total como BigDecimal
    }

    /*
    Suma el total de productos (double)
     */
    private BigDecimal CantidadProductosTicket(int tabSeleccionado) {

        BigDecimal total = new BigDecimal("0");
        for (int i = 0; i < listaProductoArrayList.get(tabSeleccionado).size(); i++) {
            Product p = listaProductoArrayList.get(tabSeleccionado).get(i);

            // total = total + p.getAmount();
            total = total.add(p.getAmount());
        }
        return total;
    }

    private void eventoTabla(ActionEvent event) {

        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex();
        System.out.println("BOTON PRESIONADO = " + tabSeleccionado);
        ObservableList<Product> data = observableListArrayList.get(tabSeleccionado);
        Node selectedContent = tabArrayList.get(tabSeleccionado).getContent();

        data = FXCollections.observableList(listaProductoArrayList.get(tabSeleccionado));
        TableView tableView = (TableView) selectedContent.lookup("#miTabla");
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
    public UserDTO getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
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

            System.out.println("OBTENER CODIGO PARA BUSCAR E INSERTAR: " + codigo);

            // Aquí puedes utilizar el código obtenido para realizar otras acciones
            if (!codigo.isEmpty()) {
                Product product = insertToTicket(codigo); // recibe el producto desde la rest api  

                if (product.getBarcode() != null) {  // si el producto existe se carga al ticket
                   //   product.setTotal(product.getPrice() * 1);  // Calcula el total.
                   product.setTotal(product.getPrice().multiply(BigDecimal.ONE));  // Calcula el total.

                    insertarProductoTicket(product, new BigDecimal("1")); // Envia el producto al ticket (tableView)
                }
            }
//
//            if (!buscarController.getCodigo().equalsIgnoreCase("")) {
//                insertarProductoTicket(buscarController.getCodigo(), 1);
//            }
//
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
        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex(); // Selecciona el tab Seleccionado.    

        System.out.println("cobrar......" + totalTicket(tabSeleccionado));

        //creamos la orden de venta
        Order order = new Order();
        User user = new User();
        Product product = new Product();

        user.setId(this.usuario.getId());
        order.setUser(user);

        ObservableList<Product> products = observableListArrayList.get(tabSeleccionado);

        products = FXCollections.observableList(listaProductoArrayList.get(tabSeleccionado));

        for (int i = 0; i < products.size(); i++) {

            //  OrderDetail orderDetail = new OrderDetail(products.get(i).getTotal(), products.get(i).getAmount(), products.get(i).getId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setPrice(products.get(i).getTotal());
            orderDetail.setAmount(products.get(i).getAmount());
            orderDetail.getProduct().setId(products.get(i).getId());
           // orderDetail.setPurchasePrice(products.get(i).getPurchasePrice() * products.get(i).getAmount());
           orderDetail.setPurchasePrice(products.get(i).getPurchasePrice().multiply(products.get(i).getAmount()));

            order.addOrderDetail(orderDetail);
            //  order.setOrderDeta

        }

        //Carga el modal de cobrar.
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/cobrar.fxml"));
            Parent root = fxmlLoader.load();
            CobrarController cobrarController = fxmlLoader.getController();
            cobrarController.setOrder(order);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // clear ticket
        Node selectedContent = tabArrayList.get(tabSeleccionado).getContent();
        TableView tableView = (TableView) selectedContent.lookup("#miTabla");

        for (int i = products.size() - 1; i >= 0; i--) {
            Product p = products.get(i);
            //delete item from ticket
            products.remove(p);
        }
        tableView.setItems(products);
        tableView.refresh();

        labelTotal.setText("$ 0");

        labelTotalProductos.setText("0" + leyendaCantidadTotal);

    }

    public void insertarProductoTicket(Product product, BigDecimal cantidad) {

        product.setAmount(cantidad);

        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex(); // Selecciona el tab Seleccionado.    

        boolean existe = existeProductoEnticket(tabSeleccionado, product.getBarcode(), cantidad);  //comentario temporal
        System.out.println("existe= " + existe);
        // boolean existe = false;
        Node selectedContent = tabArrayList.get(tabSeleccionado).getContent();

        if (existe == false) { // if product doesn't exist add it to ticket  

            listaProductoArrayList.get(tabSeleccionado).add(product); // add producto to list
        }

        labelTotal.setText("" + totalTicket(tabSeleccionado));
        labelTotalProductos.setText("" + CantidadProductosTicket(tabSeleccionado) + leyendaCantidadTotal);
        ObservableList<Product> data = observableListArrayList.get(tabSeleccionado);

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

    }

    /*
    Regresa el producto desde la rest api.
     */
    public Product insertToTicket(String barcode) {
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

    /**
     * Actualiza el costo total al cambiar en ticket
     */
    public void updatePriceAfterChangeTicket() {
        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex();
        // suma del precio total
        labelTotal.setText("" + totalTicket(tabSeleccionado));
        //Suma la cantidad total de productos
        labelTotalProductos.setText("" + CantidadProductosTicket(tabSeleccionado) + leyendaCantidadTotal);
    }

}
