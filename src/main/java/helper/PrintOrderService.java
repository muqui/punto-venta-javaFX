/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import dto.OrderServiceDTO;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.printing.PDFPageable;

/**
 *
 * @author albert
 */
public class PrintOrderService {

    public static void sentToPrinter(OrderServiceDTO order) {

        System.out.println("SE ENVIA A PDF PARA IMPRIMIR");
        PdfWriter writer = null;
        PdfDocument pdfDoc = null;
        Document document = null;
        String dest = "/home/albert/Documents/miArchivo.pdf";
        /*
        id             ya     
 date          ya     
 folio         ya     
 service            
 client        ya     
 note                
 cellphone     ya      
 email         ya      
 brand               
 model                
 issue                
 received_condition   
 password_cell_phone 
 imei                 
 repair_cost         
 paid                
 left  
         */

        try {
            // Crear archivo y directorios si no existen
            File file = new File(dest);
            file.getParentFile().mkdirs();
            // Inicializar el escritor de PDF
            writer = new PdfWriter(new FileOutputStream(file));
            // Inicializar el documento PDF
            pdfDoc = new PdfDocument(writer);
            document = new Document(pdfDoc, PageSize.A4);
            Table table = new Table(2);
            // Establecer el ancho de la tabla al 100% de la página
            table.setWidth(UnitValue.createPercentValue(100));
            // Colocar el título centrado en la primera celda
            Cell titleCell = new Cell().add(new Paragraph("Orden de servicio").setBold().setFontSize(12).setTextAlignment(TextAlignment.RIGHT));
            titleCell.setBorder(Border.NO_BORDER);
            // Colocar el folio alineado a la derecha en la segunda celda
            Cell folioCell = new Cell().add(new Paragraph("Folio: " + order.getFolio()).setBold().setFontSize(12).setTextAlignment(TextAlignment.RIGHT));
            folioCell.setBorder(Border.NO_BORDER);
            // Agregar ambas celdas a la tabla
            table.addCell(titleCell);
            table.addCell(folioCell);
            // Agregar la tabla al documento
            document.add(table);
            // Agregar Fecha
            document.add(new Paragraph("Fecha: " + order.getDate()).setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT));
            document.add(new Paragraph("DATOS DEL CLIENTE ").setBold().setFontSize(11).setTextAlignment(TextAlignment.LEFT));
            Table tableClient = new Table(2);
            // Establecer el ancho de la tabla al 100% de la página
            tableClient.setWidth(UnitValue.createPercentValue(100));
            // Colocar el título centrado en la primera celda
            Cell nameCell = new Cell().add(new Paragraph("Nombre: " + order.getClient()).setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT));
            nameCell.setBorder(Border.NO_BORDER);
            // Colocar el folio alineado a la derecha en la segunda celda
            Cell phoneCell = new Cell().add(new Paragraph("Celular: " + order.getCellphone()).setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT));
            phoneCell.setBorder(Border.NO_BORDER);
            // Agregar ambas celdas a la tabla
            tableClient.addCell(nameCell);
            tableClient.addCell(phoneCell);
            // Agregar la tabla al documento
            document.add(tableClient);
            document.add(new Paragraph("Correo: " + order.getEmail()).setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT));
            document.add(new Paragraph("DATOS DEL EQUIPO ").setBold().setFontSize(11).setTextAlignment(TextAlignment.LEFT));

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
                try {
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(PrintOrderService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        sentToPaper(dest);

    }

    public static void createdPdf80mm(OrderServiceDTO order) {
        System.out.println("SE ENVIA A PDF PARA IMPRIMIR");
        String dest = "/home/albert/Documents/miArchivo.pdf";

        try {
            // Crear archivo y directorios si no existen
            File file = new File(dest);
            file.getParentFile().mkdirs();

            // Inicializar el documento PDF con tamaño 80mm de ancho
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            PdfDocument pdfDoc = new PdfDocument(writer);
            PageSize thermalPageSize = new PageSize(226.77f, 1000f); // 80mm ≈ 226.77 puntos, altura flexible
            Document document = new Document(pdfDoc, thermalPageSize);
            document.setMargins(3f, 5f, 3f, 5f); // Agregar márgenes mínimos para evitar cortes

            document.add(new Paragraph("ORDER DE SERVICIO")
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Navarro TECH")
                    .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("33 20 87 48 74")
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));

            // Agregar contenido de izquierda a derecha y de arriba hacia abajo
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String formattedDate = sdf.format(order.getDate());
            document.add(new Paragraph("📆 Fecha: " + formattedDate)
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.LEFT));

            document.add(new Paragraph("📌 FOLIO: " + order.getFolio())
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.LEFT));

            document.add(new Paragraph("🛠️ DATOS DEL CLIENTE")
                    .setBold().setFontSize(11).setTextAlignment(TextAlignment.LEFT));

            document.add(new Paragraph()
                    .add(new Text("👤 Cliente: ").setBold()) // "Cliente" en negritas
                    .add(new Text(order.getClient())) // El nombre del cliente en texto normal
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT));

            document.add(new Paragraph()
                    .add(new Text("Telefono: ").setBold())
                    .add(new Text(order.getCellphone()))
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT));

            if (order.getEmail() != null && !order.getEmail().isEmpty()) {
                document.add(new Paragraph()
                        .add(new Text("Email: ").setBold())
                        .add(new Text(order.getEmail()))
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.LEFT));
            }

            document.add(new Paragraph("🛠️ DATOS DEL EQUIPO")
                    .setBold().setFontSize(11).setTextAlignment(TextAlignment.LEFT));

            document.add(new Paragraph()
                    .add(new Text("Marca: ").setBold())
                    .add(new Text(order.getBrand()))
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT));

            document.add(new Paragraph()
                    .add(new Text("Modelo: ").setBold())
                    .add(new Text(order.getModel()))
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT));

            document.add(new Paragraph()
                    .add(new Text("Servicio: ").setBold())
                    .add(new Text(order.getService()))
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT));
            if (order.getRepairCost() != null && order.getRepairCost() > 0) {

                document.add(new Paragraph("COSTO DEL SERVICIO")
                        .setBold().setFontSize(14).setTextAlignment(TextAlignment.LEFT));

                document.add(new Paragraph()
                        .add(new Text("Costo: ").setBold())
                        .add(new Text(String.format("%.2f", order.getRepairCost())))
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.LEFT));

                document.add(new Paragraph()
                        .add(new Text("Abono: ").setBold())
                        .add(new Text(String.format("%.2f", order.getPaid())))
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.LEFT));

                document.add(new Paragraph()
                        .add(new Text("Saldo: ").setBold())
                        .add(new Text(String.format("%.2f", order.getLeft())))
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.LEFT));

            }

            document.add(new Paragraph("TÉRMINOS Y CONDICIONES\n"
                    + "Anticipo: Se requiere 50% de anticipo para todas las reparaciones.\n"
                    + "Garantía: La garantía es de 1 mes para reparaciones de display, solo si el equipo no tiene daños adicionales, como golpes, humedad o intervención externa.\n"
                    + "Responsabilidad: La empresa no se responsabiliza de problemas surgidos después de 30 días de la entrega del equipo.\n"
                    + "Condiciones: El teléfono debe estar libre de golpes, humedad o ser abierto por terceros para que aplique la garantía.\n"
                    + "Riesgos: En equipos que no encienden pueden surgir problemas adicionales, los cuales deberán ser aprobados por el cliente.")
                    .setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED));

            // Cerrar el documento
            document.close();
            pdfDoc.close();
            writer.close();

            // Enviar a la impresora
           // sentToPaper(dest);
           impWin();
        } catch (Exception e) {
            System.out.println("Error al generar o imprimir el PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void impWin() throws PrinterException, IOException {
        String dest = "/home/albert/Documents/miArchivo.pdf";
        // Cargar el PDF
        File file = new File(dest);

        if (!file.exists()) {
            System.err.println("Error: El archivo PDF no existe.");
            return;
        }
        PDDocument document = Loader.loadPDF(file);
        // Crear un trabajo de impresión
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        // Mostrar el cuadro de diálogo de impresión
        if (job.printDialog()) {
            job.print();
            System.out.println("Impresión completada.");
        }

        // Cerrar el documento
        document.close();

    }

    private static void sentToPaper(String pdfFilePath) {
        try (InputStream pdfInputStream = new FileInputStream(pdfFilePath)) {
            Doc pdfDoc = new SimpleDoc(pdfInputStream, DocFlavor.INPUT_STREAM.PDF, null);
            PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
            printAttributes.add(new MediaPrintableArea(0, 0, 80, 297, MediaPrintableArea.MM));
            printAttributes.add(OrientationRequested.PORTRAIT);
            printAttributes.add(MediaSizeName.ISO_A5);

            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            if (printServices.length == 0) {
                System.out.println("No se encontraron impresoras.");
                return;
            }

            PrintService selectedService = (PrintService) javax.swing.JOptionPane.showInputDialog(
                    null, "Seleccione una impresora:", "Seleccionar impresora",
                    javax.swing.JOptionPane.PLAIN_MESSAGE, null, printServices, printServices[0]);

            if (selectedService != null) {
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
}
