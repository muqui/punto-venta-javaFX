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
    private boolean statusSell = true;
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
            System.out.println("total de la venta 1= " + total);
        } catch (IOException ex) {
            Logger.getLogger(CobrarController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void btnImprimiryCobrarAction(ActionEvent event) throws IOException {
//        saveOrder(getOrder());
//        closeWindow(event);
//        sentTicketToPdf(order.getOrderDetails(), "/home/albert/Documents/miArchivo.pdf");
        imprimirCobrar(true);
    }

    public void imprimirCobrar(boolean print) throws IOException {
        orderApi.saveOrder(getOrder());
        closeWindow();
        if (print) {
            sentTicketToPdf(getOrder().getOrderDetails(), "/home/albert/Documents/miArchivo.pdf");
        }

    }

    public void cobrar() {

    }

    public void setOrder(Order order) {
        this.order = order;
        // Aquí puedes hacer algo con el objeto order, como mostrar detalles en la UI
    }

//    public void saveOrder(Order order) {
//
//        System.out.println("Orden " + order.toString());
//
//        //Enviamos order al endpoint
//        // Convertir la orden a JSON
//        // Convertir la orden a JSON
//        JSONObject jsonOrder = new JSONObject();
//        jsonOrder.put("userId", order.getUser().getId());
//
//        JSONArray jsonOrderDetails = new JSONArray();
//        for (OrderDetail detail : order.getOrderDetails()) {
//            JSONObject jsonDetail = new JSONObject();
//            jsonDetail.put("price", detail.getPrice());
//            jsonDetail.put("amount", detail.getAmount());
//            jsonDetail.put("purchasePrice", detail.getPurchasePrice());
//            System.out.println("codigo de barras detail.getProduct().getBarcode()" + detail.getProduct().getBarcode());
//            jsonDetail.put("productId", detail.getProduct().getId());
//            jsonOrderDetails.put(jsonDetail);
//        }
//        jsonOrder.put("orderDetails", jsonOrderDetails);
//
//        // Imprimir el JSON para depuración
//        System.out.println("DEPURACION XXXXX:   " + jsonOrder);
//
//        try {
//            // Crear HttpClient
//            HttpClient client = HttpClient.newHttpClient();
//
//            // Crear HttpRequest
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI("http://localhost:3000/orders"))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(jsonOrder.toString()))
//                    .build();
//
//            // Enviar la solicitud y obtener la respuesta
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            // Manejar la respuesta
//            if (response.statusCode() == 200 || response.statusCode() == 201) {
//                System.out.println("Orden insertada con éxito: " + response.body());
//            } else {
//                this.setStatusSell(false);
//                System.out.println("Error al insertar la orden: " + response.statusCode() + " " + response.body());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

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

    private void sentTicketToPdf(ObservableList<OrderDetail> orderDetails, String dest) throws IOException {
        System.out.println("SE ENVIA A PDF PARA IMPRIMIR");
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
//            for (Product product : products) {
//                table.addCell(new Cell().add(new Paragraph(product.getBarcode())));
//                table.addCell(new Cell().add(new Paragraph(product.getName())));
//                table.addCell(new Cell().add(new Paragraph(product.getPrice().toString())));
//                table.addCell(new Cell().add(new Paragraph(product.getAmount().toString())));
//                BigDecimal total = product.getPrice().multiply(product.getAmount());
//                table.addCell(new Cell().add(new Paragraph(decimalFormat.format(total))));
//
//            }
            for (OrderDetail detail : orderDetails) {
                table.addCell(new Cell().add(new Paragraph(detail.getProduct().getBarcode())));
                table.addCell(new Cell().add(new Paragraph(detail.getProduct().getName())));
                table.addCell(new Cell().add(new Paragraph(detail.getProduct().getPrice().toString())));
                table.addCell(new Cell().add(new Paragraph(detail.getAmount().toString())));
                BigDecimal total = detail.getPrice().multiply(detail.getAmount());
                table.addCell(new Cell().add(new Paragraph(decimalFormat.format(total))));
                System.out.println("ID: " + detail.getId());
                System.out.println("Producto: " + detail.getProduct().getBarcode());
                System.out.println("Cantidad: " + detail.getAmount());
                System.out.println("Precio: " + detail.getPrice());
            }

            // Agregar tabla al documento
            document.add(table);
            // Agregar total después de la tabla y alinearlo a la derecha
            //  String totalText = "Total $ " + decimalFormat.format(totalTicket);
            String totalText = "Total $ xxx.xx";
            Paragraph totalParagraph = new Paragraph(totalText)
                    .setBold()
                    .setFontSize(18)
                    .setMarginTop(10)
                    .setTextAlignment(TextAlignment.RIGHT); // Alineación a la derecha
            document.add(totalParagraph);

            // Enviar el PDF creado a la impresora seleccionada por el usuario
            // printPdf(dest);
        } catch (IOException e) {
            System.out.println("error desde metod imprimir" + e);
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
        sentToPrinter(dest);

    }

    private void sentToPrinter(String pdfFilePath) {
        try (InputStream pdfInputStream = new FileInputStream(pdfFilePath)) {
            // Crear un documento imprimible a partir del PDF
            Doc pdfDoc = new SimpleDoc(pdfInputStream, DocFlavor.INPUT_STREAM.PDF, null);

            // Configurar atributos de impresión
            PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
            printAttributes.add(MediaSizeName.ISO_A4);

            // Obtener todas las impresoras disponibles
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            if (printServices.length == 0) {
                System.out.println("No se encontraron impresoras.");
                return;
            }

            // Mostrar opciones de impresora y permitir selección
            PrintService selectedService = (PrintService) javax.swing.JOptionPane.showInputDialog(
                    null,
                    "Seleccione una impresora:",
                    "Seleccionar impresora",
                    javax.swing.JOptionPane.PLAIN_MESSAGE,
                    null,
                    printServices,
                    printServices[0]
            );

            if (selectedService != null) {
                // Enviar el PDF a la impresora seleccionada
                DocPrintJob printJob = selectedService.createPrintJob();
                printJob.print(pdfDoc, printAttributes);
                System.out.println("Documento enviado a la impresora: " + selectedService.getName());
            } else {
                System.out.println("Impresión cancelada por el usuario.");
            }

        } catch (Exception e) {
            System.out.println("Error al enviar el PDF a la impresora: " + e.getMessage());
            e.printStackTrace();
        }
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
