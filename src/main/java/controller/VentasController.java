/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import api.ProductApi;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;

/**
 * FXML Controller class
 *
 * @author mq12
 */
public class VentasController implements Initializable {

    ProductApi productApi = new ProductApi();

    private UserDTO user;
    private boolean mayoreo = false;
    // Producto producto = new Producto();
    double totalProductos = 0;
    String leyendaCantidadTotal = " Total productos";
    String discount = "";
    ArrayList<Tab> tabArrayList = new ArrayList<Tab>(); // se crar un array de tabs.

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
    private Button btnDescuento;

    @FXML
    private Button btnSuprimir;

    @FXML
    private Button btnVerificar;

    @FXML
    private TextField txtDiscount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //OCULTAR ESTO BOTONES TEMPORALMENTE HASTA CODIFICAR
        //  btnMayoreo.setVisible(false);
        btnSuprimir.setVisible(false);
        btnVerificar.setVisible(false);

        txtVentasMayoreo.setVisible(false);
        //recibimos el usuario desde Main(App)
        this.user = App.getUsuario();
        System.out.println("Desde ventas controller token= " + this.user.toString());

        crearTicket("Ticket 1");
        txtCodigoBarras.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    insertProduct();
                }
            }

        });
        // Asegura que el TextField siempre tenga el foco
        txtCodigoBarras.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Si pierde el foco
                txtCodigoBarras.requestFocus(); // Recuperar el foco
            }
        });

        // Solicitar foco al inicio
        Platform.runLater(() -> txtCodigoBarras.requestFocus());

        tabPaneTicket.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {

                Platform.runLater(() -> txtCodigoBarras.requestFocus());

                newScene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case F1:
                            // crearTicketNuevo();
                            buscarProducto();
                            break;
                        case F2:
                            // eliminarTicket();
                            insertarMasdeUnProducto();
                            break;
                        case F3:
                            descuento();
                            break;
                        case F5:
                            // guardarTicket();
                            int numTabs = tabPaneTicket.getTabs().size();

                            if (numTabs < 10) {
                                crearTicket("Ticket " + (numTabs + 1));
                                // set total to zero
                                labelTotal.setText("0");
                            }
                            break;
                        case F6:
                            cambiarTicket();
                            break;
                        case F7:
                            eliminarTicket();
                            break;
                        case F12: {
                            try {
                                precobrar();
                            } catch (IOException ex) {
                                Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        break;

                        default:
                            System.out.println("Tecla no asignada: " + event.getCode());
                    }
                });
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
        descuento();
    }

    @FXML // cambia el ticket seleccionado.
    void onActionBtnCambiarTicket(ActionEvent event) {
        cambiarTicket();
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
        insertarMasdeUnProducto();
    }

    @FXML
    void tabSelected(MouseEvent event) {
        updatePriceAfterChangeTicket();
    }

    @FXML
    void ActionBtnAgregarProducto(ActionEvent event) {
        // insertarProductoTicket(txtCodigoBarras.getText(), 1);
        insertProduct();
    }

    @FXML //Elimina tickets menos el primero.
    void ActionBtnEliminarTicket(ActionEvent event) {

        eliminarTicket();

    }

    /**
     * Initializes the controller class.
     */
    public void crearTicket(String nombre) {

        TableView tableView = new TableView();
        tableView.setId("miTabla");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // NECESARIO PARA CAMBIAR EL ANCHO DE LA TABLA

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
    * Check if exist product in ticket, update amoutn an d total price
     */
    private boolean existeProductoEnticket(int tabSeleccionado, String codigoBarras, BigDecimal cantidad) {
        boolean existe = false;
        for (int i = 0; i < listaProductoArrayList.get(tabSeleccionado).size(); i++) {
            Product p = listaProductoArrayList.get(tabSeleccionado).get(i);
            //  System.out.println("PRODUCTO YA ESTA EN EL TICKET" + p.toString());
            if (p.getBarcode().equalsIgnoreCase(codigoBarras)) {

                System.out.println("CANTIDAD DE UNO POR UNO = " + p.getAmount() + " stock= " + p.getStock());
                if (p.getAmount().compareTo(BigDecimal.valueOf(p.getStock())) >= 0 && !p.getHowToSell().equalsIgnoreCase("Paquete")) {
                    System.out.println("Solo hay " + p.getStock());
                    showAlert(Alert.AlertType.ERROR, "Error", "No hay mas productos en stock. \n Solo  hay " + p.getStock() + " productos");
                    existe = true;
                    break;
                }

                //double cantidadTotal = p.getAmount() + cantidad;
                BigDecimal cantidadTotal = p.getAmount().add(cantidad);
                //double total = cantidadTotal * p.getPrice();
                BigDecimal total = cantidadTotal.multiply(p.getPrice());
                //  System.out.println("TOTAL NUEVO " + total);

                p.setAmount(cantidadTotal);
                p.setTotal(total);
                existe = true;
                break;
            }
        }

        return existe;

    }

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
        // System.out.println("BOTON PRESIONADO = " + tabSeleccionado);
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
                if (p.getAmount().compareTo(BigDecimal.valueOf(p.getStock())) >= 0 && !p.getHowToSell().equalsIgnoreCase("Paquete")) {
                    System.out.println("Solo hay " + p.getStock());
                    showAlert(Alert.AlertType.ERROR, "Error", "No hay mas productos en stock. \n Solo  hay " + p.getStock() + " productos");
                    // existe = true;
                    break;
                }
                //  p.setAmount(p.getAmount() + 1);
                p.setAmount(p.getAmount().add(BigDecimal.ONE));
                //  p.setTotal(p.getAmount() * p.getPrice());
                p.setTotal(p.getAmount().multiply(p.getPrice()));

            }
            if (event.getSource() == p.getBotonBorrar()) {

                if (p.getAmount().compareTo(BigDecimal.ONE) > 0) {
                    p.setAmount(p.getAmount().subtract(BigDecimal.ONE));
                    p.setTotal(p.getAmount().multiply(p.getPrice()));
                    //  System.out.println("cantidad " + p.getAmount());
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

            // System.out.println("OBTENER CODIGO PARA BUSCAR E INSERTAR: " + codigo);
            // Aquí puedes utilizar el código obtenido para realizar otras acciones
            if (!codigo.isEmpty()) {
                Product product = productApi.ProductoToTicket(codigo, user.getToken());   //insertToTicket(codigo); // recibe el producto desde la rest api  
                //  System.out.println("COMO SME VENDE= " + product.getHowToSell());
                BigDecimal cantidad = new BigDecimal("1");
                //  product.setHowToSell("Unidad");
                if (product.getHowToSell().equalsIgnoreCase("Granel")) {

                    cantidad = dialogAmount();

                    System.out.println("condicion para cambiar la cantiadad granel 0");
                }
                if (!"".equals(discount)) {
                    BigDecimal discountedPrice = calculateDiscount(product);
                    System.out.println("PRECIO SI HAY DESCUENTO = " + product.getWholesalePrice());
                    product.setPrice(discountedPrice);
                    product.setTotal(discountedPrice);
                }

                if (product.getBarcode() != null) {  // si el producto existe se carga al ticket
                    //   product.setTotal(product.getPrice() * 1);  // Calcula el total.
                    product.setTotal(product.getPrice().multiply(BigDecimal.ONE));  // Calcula el total.

                    insertarProductoTicket(product, cantidad); // Envia el producto al ticket (tableView)
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void descuento() {
        // mayoreo = !mayoreo;
        // txtVentasMayoreo.setVisible(mayoreo);
        //   System.out.println("Mayoreo " + mayoreo);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/discount.fxml"));
            Parent root;
            root = fxmlLoader.load();
            DiscountController discountController = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            //stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            discount = discountController.getDescuento();
            txtVentasMayoreo.setVisible(true);
            txtVentasMayoreo.setText(discount);
            System.out.println("RECIBE EL DESCUENTO= " + discount);
        } catch (IOException ex) {
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void precobrar() throws IOException {
        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex(); // Selecciona el tab Seleccionado.    

        //    System.out.println("cobrar......" + totalTicket(tabSeleccionado));
        //creamos la orden de venta
        Order order = new Order();
        User user = new User();
        Product product = new Product();

        user.setId(this.user.getId());
        order.setUser(user);

        ObservableList<Product> products = observableListArrayList.get(tabSeleccionado);

        products = FXCollections.observableList(listaProductoArrayList.get(tabSeleccionado));

        for (int i = 0; i < products.size(); i++) {

            //  OrderDetail orderDetail = new OrderDetail(products.get(i).getTotal(), products.get(i).getAmount(), products.get(i).getId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setPrice(products.get(i).getTotal());
            orderDetail.setAmount(products.get(i).getAmount());
            orderDetail.getProduct().setId(products.get(i).getId());
            orderDetail.getProduct().setName(products.get(i).getName());
            orderDetail.getProduct().setBarcode(products.get(i).getBarcode());
            // orderDetail.setPurchasePrice(products.get(i).getPurchasePrice() * products.get(i).getAmount());
            orderDetail.setPurchasePrice(products.get(i).getPurchasePrice().multiply(products.get(i).getAmount()));

            order.addOrderDetail(orderDetail);
            //  order.setOrderDeta

        }
        System.out.println("cantida de productos a vender = " + order.getOrderDetails().size());
        if (order.getOrderDetails().size() < 1) {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Ingrese productos al ticket.");
            return;
        }
        System.out.println("");
        //  printProductsToPdf1(products, "/home/albert/Documents/miArchivo.pdf", totalTicket(tabSeleccionado));

        //Carga el modal de cobrar.
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/cobrar.fxml"));
            Parent root = fxmlLoader.load();
            CobrarController cobrarController = fxmlLoader.getController();
            cobrarController.setOrder(order);

            cobrarController.setTotal(totalTicket(tabSeleccionado));

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            System.out.println("mensaje status se guardo= " + cobrarController.isStatusSell());
            if (cobrarController.isStatusSell() == true) {
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
        } catch (IOException ex) {
            System.out.println("error al imprimir pdf " + ex);
            Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public void insertarProductoTicket(Product product, BigDecimal cantidad) {
        BigDecimal stock = new BigDecimal(product.getStock());

        if (stock.compareTo(cantidad) < 0 && !product.getHowToSell().equalsIgnoreCase("Paquete")) {
            showAlert(Alert.AlertType.ERROR, "Error", "No hay productos suficientes.");
            System.out.println("La cantidad solicitada excede la cantidad disponible en inventario.");
            txtCodigoBarras.setText("");
            return; // Interrumpe la ejecución del método
        }

        product.setAmount(cantidad);
        System.out.println("TOTAL cantidad xxxxxxxxxx" + product.getAmount() + " total = " + product.getTotal());
        // product.setTotal(product.getAmount().multiply(product.getTotal()));
        product.setTotal(cantidad.multiply(product.getPrice()));
        //product.setTotal(new BigDecimal("0.00"));

        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex(); // Selecciona el tab Seleccionado.    

        boolean existe = existeProductoEnticket(tabSeleccionado, product.getBarcode(), cantidad);  //comentario temporal
        //  System.out.println("existe= " + existe);
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
        BigDecimal cantidad = new BigDecimal("0");
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ventaGranel.fxml"));
            Parent root;
            root = fxmlLoader.load();
            System.out.println("ABRE DIALOGO PARA LA CANTIDAD DE VENTA A GRANEL");
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

//    public void printProductsToPdf1(ObservableList<Product> products, String dest, BigDecimal totalTicket) throws IOException {
//        System.out.println("SE ENVIA A PDF PARA IMPRIMIR");
//        PdfWriter writer = null;
//        PdfDocument pdfDoc = null;
//        Document document = null;
//
//        try {
//            // Crear archivo y directorios si no existen
//            File file = new File(dest);
//            file.getParentFile().mkdirs();
//
//            // Inicializar el escritor de PDF
//            writer = new PdfWriter(new FileOutputStream(file));
//
//            // Inicializar el documento PDF
//            pdfDoc = new PdfDocument(writer);
//            document = new Document(pdfDoc, PageSize.A4);
//
//            // Agregar título
//            document.add(new Paragraph("MATI PAPELERÍA").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
//            // Crear tabla con 5 columnas
//            //  float[] columnWidths = {2, 4, 3, 3, 4}; // Ajustar los tamaños de las columnas según sea necesario
//            // Table table = new Table(columnWidths);
//            Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();
//
//            // Encabezado de la tabla
//            table.addHeaderCell(new Cell().add(new Paragraph("Código")));
//            table.addHeaderCell(new Cell().add(new Paragraph("Nombre")));
//            table.addHeaderCell(new Cell().add(new Paragraph("Precio")));
//            table.addHeaderCell(new Cell().add(new Paragraph("Cantidad")));
//            table.addHeaderCell(new Cell().add(new Paragraph("Total")));
//
//            // Crear un formateador de decimales
//            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
//            // Agregar datos de los productos a la tabla
//            for (Product product : products) {
//                table.addCell(new Cell().add(new Paragraph(product.getBarcode())));
//                table.addCell(new Cell().add(new Paragraph(product.getName())));
//                table.addCell(new Cell().add(new Paragraph(product.getPrice().toString())));
//                table.addCell(new Cell().add(new Paragraph(product.getAmount().toString())));
//                BigDecimal total = product.getPrice().multiply(product.getAmount());
//                table.addCell(new Cell().add(new Paragraph(decimalFormat.format(total))));
//
//            }
//
//            // Agregar tabla al documento
//            document.add(table);
//            // Agregar total después de la tabla y alinearlo a la derecha
//            String totalText = "Total $ " + decimalFormat.format(totalTicket);
//            Paragraph totalParagraph = new Paragraph(totalText)
//                    .setBold()
//                    .setFontSize(18)
//                    .setMarginTop(10)
//                    .setTextAlignment(TextAlignment.RIGHT); // Alineación a la derecha
//            document.add(totalParagraph);
//
//            // Enviar el PDF creado a la impresora seleccionada por el usuario
//            // printPdf(dest);
//        } catch (IOException e) {
//            System.out.println("error desde metod imprimir" + e);
//            e.printStackTrace();
//        } finally {
//            // Cerrar el documento y el escritor
//            if (document != null) {
//                document.close();
//            }
//            if (pdfDoc != null) {
//                pdfDoc.close();
//            }
//            if (writer != null) {
//                writer.close();
//            }
//
//        }
//        printPdf(dest);
//    }
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

//    private void printPdf(String pdfFilePath) {
//        try (InputStream pdfInputStream = new FileInputStream(pdfFilePath)) {
//            // Crear un documento imprimible a partir del PDF
//            Doc pdfDoc = new SimpleDoc(pdfInputStream, DocFlavor.INPUT_STREAM.PDF, null);
//
//            // Configurar atributos de impresión
//            PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
//            printAttributes.add(MediaSizeName.ISO_A4);
//
//            // Obtener todas las impresoras disponibles
//            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
//            if (printServices.length == 0) {
//                System.out.println("No se encontraron impresoras.");
//                return;
//            }
//
//            // Mostrar opciones de impresora y permitir selección
//            PrintService selectedService = (PrintService) javax.swing.JOptionPane.showInputDialog(
//                    null,
//                    "Seleccione una impresora:",
//                    "Seleccionar impresora",
//                    javax.swing.JOptionPane.PLAIN_MESSAGE,
//                    null,
//                    printServices,
//                    printServices[0]
//            );
//
//            if (selectedService != null) {
//                // Enviar el PDF a la impresora seleccionada
//                DocPrintJob printJob = selectedService.createPrintJob();
//                printJob.print(pdfDoc, printAttributes);
//                System.out.println("Documento enviado a la impresora: " + selectedService.getName());
//            } else {
//                System.out.println("Impresión cancelada por el usuario.");
//            }
//
//        } catch (Exception e) {
//            System.out.println("Error al enviar el PDF a la impresora: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);  // Usar el tipo de alerta proporcionado como parámetro
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void insertarMasdeUnProducto() {
        System.out.println("INSERTAR MAS DE 1 ****************************************");
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

            Product product = productApi.ProductoToTicket(codigo, user.getToken());//insertToTicket(codigo); // recibe el producto desde la rest api  
            if (!"".equals(discount)) {
                BigDecimal discountedPrice = calculateDiscount(product);
                System.out.println("PRECIO SI HAY DESCUENTO = " + product.getWholesalePrice());
                product.setPrice(discountedPrice);
                product.setTotal(discountedPrice);
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

    private void eliminarTicket() {
        int numTabs = tabPaneTicket.getTabs().size() - 1;
        if (numTabs > 0) // elimina todos los tickes exepto el primero
        {
            tabPaneTicket.getTabs().remove(numTabs);
            tabArrayList.remove(numTabs);
            listaProductoArrayList.remove(numTabs);
        }
    }

    private void cambiarTicket() {
        int tabSeleccionado = tabPaneTicket.getSelectionModel().getSelectedIndex();
        int tabsTotal = tabPaneTicket.getTabs().size();

        if (tabSeleccionado == tabsTotal - 1) {
            tabPaneTicket.getSelectionModel().selectFirst();
        }
        tabPaneTicket.getSelectionModel().select(tabSeleccionado + 1);

        updatePriceAfterChangeTicket();

    }

    private void insertProduct() {

        Product product = productApi.ProductoToTicket(txtCodigoBarras.getText(), user.getToken());       //insertToTicket(txtCodigoBarras.getText()); // recibe el producto desde la rest api
        System.out.println("CORONA" + product.getId());
        if (product.getId() == 0) {
            txtCodigoBarras.setText("");
            return;
        }

        System.out.println("" + product.getStock());

        //   System.out.println("producto qu no regresa como se vende= " + product.toString());
        BigDecimal cantidad = new BigDecimal("1");
        // product.setHowToSell("Unidad");

        if (product.getHowToSell().equalsIgnoreCase("Granel")) {

            cantidad = dialogAmount();
            System.out.println("condicion para cambiar la cantiadad granel 1");
        }

        if (!"".equals(discount)) {
            BigDecimal discountedPrice = calculateDiscount(product);
            System.out.println("PRECIO SI HAY DESCUENTO = " + product.getWholesalePrice());
            product.setPrice(discountedPrice);
            product.setTotal(discountedPrice);
        }

        if (product.getBarcode() != null) {
            insertarProductoTicket(product, cantidad); //inserta el producto al ticket
        }
    }

    private BigDecimal calculateDiscount(Product product) {

        BigDecimal discountedPrice = new BigDecimal("0");
        if ("purchasePrice".equals(discount)) {
            discountedPrice = product.getPurchasePrice();

        } else if ("wholesalePrice".equals(discount)) {
            discountedPrice = product.getWholesalePrice();

        } else {

            BigDecimal discountPercentage = new BigDecimal(discount);
            discountPercentage = discountPercentage.setScale(2, RoundingMode.HALF_UP);
            if (discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
                discountedPrice = calculateDiscountedPrice(product.getPrice(), discountPercentage);
            }
        }

        return discountedPrice;
    }

}
