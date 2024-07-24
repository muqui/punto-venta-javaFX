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
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import dto.UserDTO;
import java.io.File;
import java.io.FileOutputStream;
//import com.sun.javafx.scene.control.skin.TableHeaderRow;

import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
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
import javafx.print.Paper;
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

    @FXML
    private TextField txtDiscount;

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

                    BigDecimal cantidad = new BigDecimal("1");

                    if (product.getHowToSell().equalsIgnoreCase("Granel")) {

                        cantidad = dialogAmount();

                    }

                    //hacer descuento
                    if (!txtDiscount.getText().trim().equalsIgnoreCase("")) {
                        BigDecimal discountPercentage = new BigDecimal(txtDiscount.getText());
                        discountPercentage = discountPercentage.setScale(2, RoundingMode.HALF_UP);

                        System.out.println("DESCUENTO TEXT =" + discountPercentage);
                        if (discountPercentage.compareTo(BigDecimal.ZERO) > 0) {

                            BigDecimal discountedPrice = calculateDiscountedPrice(product.getPrice(), discountPercentage);

                            System.out.println("precio regresado con descuento= " + discountedPrice);

                            System.out.println("precio de venta Normal= " + product.getPrice());

                            product.setPrice(discountedPrice);
                            product.setTotal(discountedPrice);
                            System.out.println("precio de venta Con descuento= " + discountedPrice);

                        }

                    }

                    if (product.getBarcode() != null) {
                        insertarProductoTicket(product, cantidad); //inserta el producto al ticket
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
    void btnCobrarAction(ActionEvent event) throws IOException {
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
                       //hacer descuento
                    if (!txtDiscount.getText().trim().equalsIgnoreCase("")) {
                        BigDecimal discountPercentage = new BigDecimal(txtDiscount.getText());
                        discountPercentage = discountPercentage.setScale(2, RoundingMode.HALF_UP);

                        System.out.println("DESCUENTO TEXT =" + discountPercentage);
                        if (discountPercentage.compareTo(BigDecimal.ZERO) > 0) {

                            BigDecimal discountedPrice = calculateDiscountedPrice(product.getPrice(), discountPercentage);

                            System.out.println("precio regresado con descuento= " + discountedPrice);

                            System.out.println("precio de venta Normal= " + product.getPrice());

                            product.setPrice(discountedPrice);
                            product.setTotal(discountedPrice);
                            System.out.println("precio de venta Con descuento= " + discountedPrice);

                        }

                    }

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
                BigDecimal cantidad = new BigDecimal("1");
                if (product.getHowToSell().equalsIgnoreCase("Granel")) {

                    cantidad = dialogAmount();
                }

                           //hacer descuento
                    if (!txtDiscount.getText().trim().equalsIgnoreCase("")) {
                        BigDecimal discountPercentage = new BigDecimal(txtDiscount.getText());
                        discountPercentage = discountPercentage.setScale(2, RoundingMode.HALF_UP);

                        System.out.println("DESCUENTO TEXT =" + discountPercentage);
                        if (discountPercentage.compareTo(BigDecimal.ZERO) > 0) {

                            BigDecimal discountedPrice = calculateDiscountedPrice(product.getPrice(), discountPercentage);

                            System.out.println("precio regresado con descuento= " + discountedPrice);

                            System.out.println("precio de venta Normal= " + product.getPrice());

                            product.setPrice(discountedPrice);
                            product.setTotal(discountedPrice);
                            System.out.println("precio de venta Con descuento= " + discountedPrice);

                        }

                    }
                if (product.getBarcode() != null) {  // si el producto existe se carga al ticket
                    //   product.setTotal(product.getPrice() * 1);  // Calcula el total.
                    product.setTotal(product.getPrice().multiply(BigDecimal.ONE));  // Calcula el total.

                    insertarProductoTicket(product, cantidad); // Envia el producto al ticket (tableView)
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

    private void precobrar() throws IOException {
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
        printProductsToPdf1(products, "/home/albert/Documents/miArchivo.pdf", totalTicket(tabSeleccionado));

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

        // printProducts(products);
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

    private BigDecimal dialogAmount() {
        BigDecimal cantidad = new BigDecimal("1");
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ventaGranel.fxml"));
            Parent root;
            root = fxmlLoader.load();
            System.out.println("ES VENTA A GRANEL");
            VentaGranelController ventaGranelController = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            //stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            cantidad = ventaGranelController.getCantidad();
            System.out.println("cantidad en venta granel");

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public void printProductsToPdf(ObservableList<Product> products, String dest) {
        try {
            File file = new File(dest);
            file.getParentFile().mkdirs();

            // Initialize PDF writer
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));

            // Initialize PDF document
            PdfDocument pdf = new PdfDocument(writer);

            // Initialize document
            Document document = new Document(pdf);

            // Add content
            for (Product product : products) {
                document.add(new Paragraph(product.getName())); // Ajusta esto para mostrar la información deseada del producto
            }

            // Close document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printProducts(ObservableList<Product> products) {
        // Mostrar diálogo de selección de impresora
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null || !job.showPrintDialog(null)) {
            System.out.println("No printer selected or print dialog was cancelled.");
            return;
        }

        Printer printer = job.getPrinter();

        if (printer == null) {
            System.out.println("No default printer available.");
            return;
        }

        // Definir un tamaño de papel personalizado (80 mm de ancho, longitud ajustable)
        double width = 80 * 2.83465; // Ancho en puntos
        double height = 297 * 2.83465; // Altura en puntos (puedes ajustar según sea necesario)

        Paper customPaper = Paper.A4; // Inicializa con A4, ya que no podemos crear un nuevo objeto Paper directamente

        // Crear un PageLayout con las dimensiones personalizadas
        PageLayout pageLayout = printer.createPageLayout(customPaper, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

        VBox vbox = new VBox();
        for (Product product : products) {
            Label label = new Label(product.toString()); // Ajusta esto para mostrar la información deseada del producto
            vbox.getChildren().add(label);
        }

        Scene scene = new Scene(vbox);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        boolean success = job.printPage(pageLayout, vbox);
        if (success) {
            job.endJob();
        } else {
            System.out.println("Print job failed");
        }

        stage.close();
    }

    public void printProductsToPdf1(ObservableList<Product> products, String dest, BigDecimal totalTicket) throws IOException {
        PdfWriter writer = null;
        PdfDocument pdfDoc = null;
        Document document = null;

        try {
            // Crear archivo y directorios si no existen
            File file = new File(dest);
            file.getParentFile().mkdirs();

            // Inicializar el escritor de PDF
            writer = new PdfWriter(new FileOutputStream(file));

            // Inicializar el documento PDF
            pdfDoc = new PdfDocument(writer);
            document = new Document(pdfDoc, PageSize.A4);

            // Agregar título
            document.add(new Paragraph("MATI PAPELERÍA").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            // Crear tabla con 5 columnas
            //  float[] columnWidths = {2, 4, 3, 3, 4}; // Ajustar los tamaños de las columnas según sea necesario
            // Table table = new Table(columnWidths);
            Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();

            // Encabezado de la tabla
            table.addHeaderCell(new Cell().add(new Paragraph("Código")));
            table.addHeaderCell(new Cell().add(new Paragraph("Nombre")));
            table.addHeaderCell(new Cell().add(new Paragraph("Precio")));
            table.addHeaderCell(new Cell().add(new Paragraph("Cantidad")));
            table.addHeaderCell(new Cell().add(new Paragraph("Total")));

            // Crear un formateador de decimales
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            // Agregar datos de los productos a la tabla
            for (Product product : products) {
                table.addCell(new Cell().add(new Paragraph(product.getBarcode())));
                table.addCell(new Cell().add(new Paragraph(product.getName())));
                table.addCell(new Cell().add(new Paragraph(product.getPrice().toString())));
                table.addCell(new Cell().add(new Paragraph(product.getAmount().toString())));
                BigDecimal total = product.getPrice().multiply(product.getAmount());
                table.addCell(new Cell().add(new Paragraph(decimalFormat.format(total))));

            }

            // Agregar tabla al documento
            document.add(table);
            // Agregar total después de la tabla y alinearlo a la derecha
            String totalText = "Total $ " + decimalFormat.format(totalTicket);
            Paragraph totalParagraph = new Paragraph(totalText)
                    .setBold()
                    .setFontSize(18)
                    .setMarginTop(10)
                    .setTextAlignment(TextAlignment.RIGHT); // Alineación a la derecha
            document.add(totalParagraph);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cerrar el documento y el escritor
            if (document != null) {
                document.close();
            }
            if (pdfDoc != null) {
                pdfDoc.close();
            }
            if (writer != null) {
                writer.close();
            }

        }
    }

    public static BigDecimal calculateDiscountedPrice(BigDecimal originalPrice, BigDecimal discountPercentage) {
        // Convertir el porcentaje de descuento a un decimal
        BigDecimal discountDecimal = discountPercentage.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        // Calcular el precio con descuento
        BigDecimal discountAmount = originalPrice.multiply(discountDecimal);
        BigDecimal discountedPrice = originalPrice.subtract(discountAmount);
        // Redondear a dos decimales
        discountedPrice = discountedPrice.setScale(2, RoundingMode.HALF_UP);
        return discountedPrice;
    }
}
