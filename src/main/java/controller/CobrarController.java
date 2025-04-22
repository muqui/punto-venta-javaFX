/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.OrderApi;
import beans.Order;
import beans.OrderDetail;
import beans.Product;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import helper.PrintOrderService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.F1;
import static javafx.scene.input.KeyCode.F12;
import static javafx.scene.input.KeyCode.F2;
import static javafx.scene.input.KeyCode.F5;
import static javafx.scene.input.KeyCode.F6;
import static javafx.scene.input.KeyCode.F7;
import javafx.scene.layout.AnchorPane;
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class CobrarController implements Initializable {
    OrderApi orderApi = new OrderApi();
    private Order order;
    private boolean statusSell = false;
    private BigDecimal total;
    @FXML
    private AnchorPane anchorPaneCobrar;

    @FXML
    private Label labelTotal;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtChange;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        labelTotal.setText("" + getTotal());

        getAnchorPaneCobrar().sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                
                 // Establecer el foco en txtAmount
        Platform.runLater(() -> txtAmount.requestFocus());
                
                newScene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {

                        case F11:
                            //  eliminarTicket();
                            System.out.println("presionaste tecla f11 ?  =" + event.getCode());

                             {
                                try {
                                    imprimirCobrar(true);
                                } catch (IOException ex) {
                                    Logger.getLogger(CobrarController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;

                        case F12:
                            System.out.println("presionaste tecla f12 ?  =" + event.getCode());
                             {
                                try {
                                    imprimirCobrar(false);
                                } catch (IOException ex) {
                                    Logger.getLogger(CobrarController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;

                        default:
                            System.out.println("Tecla no asignada: " + event.getCode());
                    }
                });
            }
        });
        // Listener para detectar cambios en el campo txtAmount
        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                // Convertir el texto a BigDecimal
                BigDecimal amount = new BigDecimal(newValue);

                // Realizar la resta
                BigDecimal change = amount.subtract(total);

                // Mostrar el resultado en txtChange
                txtChange.setText(String.format("%.2f", change));
            } catch (NumberFormatException e) {
                // Si no se puede convertir a número, limpiar el campo txtChange
                txtChange.setText("");
            }
        });

    }

    @FXML
    void btnCobrarAction(ActionEvent event) {

        try {
            imprimirCobrar(false);
          
        } catch (IOException ex) {
            Logger.getLogger(CobrarController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void btnImprimiryCobrarAction(ActionEvent event) throws IOException {
        imprimirCobrar(true);
    }

    public void imprimirCobrar(boolean print) throws IOException {
        orderApi.saveOrder(getOrder());
        setStatusSell(true);
        closeWindow();
        if (print) {
               PrintOrderService.createdPdf80mm(getOrder().getOrderDetails());
           
        }

    }

    public void cobrar() {

    }

    public void setOrder(Order order) {
        this.order = order;
        // Aquí puedes hacer algo con el objeto order, como mostrar detalles en la UI
    }

    private void closeWindow() {
        // Obtiene la ventana activa a partir de anchorPaneCobrar
        Stage stage = (Stage) getAnchorPaneCobrar().getScene().getWindow();
        stage.close(); // Cierra la ventana si existe

    }

    /**
     * @return the order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @return the statusSell
     */
    public boolean isStatusSell() {
        return statusSell;
    }

    /**
     * @param statusSell the statusSell to set
     */
    public void setStatusSell(boolean statusSell) {
        this.statusSell = statusSell;
    }


   

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
        // Actualiza el Label después de establecer el valor de total
        if (labelTotal != null) {
            labelTotal.setText("" + total);
        }
        System.out.println("Total de la venta (setTotal): " + total);
    }

    /**
     * @return the anchorPaneCobrar
     */
    public AnchorPane getAnchorPaneCobrar() {
        return anchorPaneCobrar;
    }

    /**
     * @param anchorPaneCobrar the anchorPaneCobrar to set
     */
    public void setAnchorPaneCobrar(AnchorPane anchorPaneCobrar) {
        this.anchorPaneCobrar = anchorPaneCobrar;
    }

}
