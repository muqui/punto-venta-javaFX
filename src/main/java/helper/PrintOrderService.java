/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import beans.OrderDetail;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import dto.OrderServiceDTO;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javafx.collections.ObservableList;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.printing.PDFPageable;

/**
 *
 * @author albert
 */
public class PrintOrderService {

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
    public static void createdPdf80mm(ObservableList<OrderDetail> orderDetails) {
        System.out.println("SE ENVIA A PDF PARA IMPRIMIR");
        String dest = "/home/albert/Documents/miArchivo.pdf";
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        BigDecimal totalTicket = BigDecimal.ZERO; // Inicializar acumulador
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
            document.add(new Paragraph("MATI PAPELERIA")
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("CANT DESCRIPCION PRECIO TOTAL")
                    .setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            for (OrderDetail detail : orderDetails) {
                document.add(new Paragraph(detail.getProduct().getName())
                        .setBold().setFontSize(14).setTextAlignment(TextAlignment.LEFT));
                Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
                table.setBorder(Border.NO_BORDER); // Sin bordes en la tabla
                table.addCell(new Cell().add(new Paragraph(detail.getAmount().toString()))
                        .setBorder(Border.NO_BORDER));
                BigDecimal unitPrice = detail.getPrice().divide(detail.getAmount());
                table.addCell(new Cell().add(new Paragraph(decimalFormat.format(unitPrice)))
                        .setBorder(Border.NO_BORDER));
                table.addCell(new Cell().add(new Paragraph(detail.getPrice().toString()))
                        .setBorder(Border.NO_BORDER));
                // Acumular precio total
                totalTicket = totalTicket.add(detail.getPrice());
                System.out.println("ID: " + detail.getId());
                System.out.println("Producto: " + detail.getProduct().getBarcode());
                System.out.println("Cantidad: " + detail.getAmount());
                System.out.println("Precio: " + detail.getPrice());
                System.out.println("precio compra: " + detail.getPrice().divide(detail.getAmount()));
                document.add(table);
            }

            document.add(new Paragraph("Total " + totalTicket)
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.RIGHT));

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
}
