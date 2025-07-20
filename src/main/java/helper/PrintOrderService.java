/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import beans.OrderDetail;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import helper.NumeroALetras;
import dto.OrderServiceDTO;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableList;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.printing.PDFPageable;

/**
 *
 * @author albert
 */
public class PrintOrderService {

public static void printOrderReparirLetter(OrderServiceDTO order) {
    System.out.println("SE ENVIA A PDF PARA IMPRIMIR");
    String dest = "/home/albert/Documents/miArchivoCarta.pdf";

    try {
        File file = new File(dest);
        file.getParentFile().mkdirs();

        PdfWriter writer = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.LETTER);
        document.setMargins(40f, 40f, 40f, 40f);

        float fontSizeTitle = 14f;
        float fontSizeMain = 12f;
        float fontSizeSmall = 11f;
        float fontSizeMicroSmall = 9f;

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.setFont(font);

        // Título
        document.add(new Paragraph("NOTA DE SERVICIO")
                .setFontSize(fontSizeTitle)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10f));

        document.add(new Paragraph("NAVARRO TECH")
                .setFontSize(fontSizeTitle)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("📍 Dirección: Calle Juarez #80 A. \n📞 33 20 87 48 74")
                .setFontSize(fontSizeMain)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20f));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = sdf.format(order.getDate());

        // Fecha y Folio
        document.add(new Paragraph("Fecha: " + formattedDate)
                .setFontSize(fontSizeMain));
        document.add(new Paragraph("Folio: " + order.getFolio())
                .setFontSize(fontSizeMain)
                .setMarginBottom(15f));

        // Tabla datos cliente
        Table clienteTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .useAllAvailableWidth();
        clienteTable.addCell(new Cell().add(new Paragraph("Nombre").setBold().setFontSize(fontSizeSmall))
                 .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBorder(new SolidBorder(1)));
        clienteTable.addCell(new Cell().add(new Paragraph("Teléfono").setBold().setFontSize(fontSizeSmall))
                 .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBorder(new SolidBorder(1)));

        clienteTable.addCell(new Cell().add(new Paragraph(order.getClient()).setFontSize(fontSizeMain))
                .setBorder(new SolidBorder(1)));
        clienteTable.addCell(new Cell().add(new Paragraph(order.getCellphone()).setFontSize(fontSizeMain))
                .setBorder(new SolidBorder(1)));

        if (order.getEmail() != null && !order.getEmail().isEmpty()) {
            clienteTable.addCell(new Cell(1, 2).add(new Paragraph("Email: " + order.getEmail())
                    .setFontSize(fontSizeMain))
                    .setBorder(new SolidBorder(1)));
        }
        document.add(clienteTable.setMarginBottom(20f));

        // Tabla datos equipo (marca y modelo)
        Table equipoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .useAllAvailableWidth();
        equipoTable.addCell(new Cell().add(new Paragraph("Marca").setBold().setFontSize(fontSizeSmall))
                 .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBorder(new SolidBorder(1)));
        equipoTable.addCell(new Cell().add(new Paragraph("Modelo").setBold().setFontSize(fontSizeSmall))
                 .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBorder(new SolidBorder(1)));

        equipoTable.addCell(new Cell().add(new Paragraph(order.getBrand()).setFontSize(fontSizeMain))
                .setBorder(new SolidBorder(1)));
        equipoTable.addCell(new Cell().add(new Paragraph(order.getModel()).setFontSize(fontSizeMain))
                .setBorder(new SolidBorder(1)));

        document.add(equipoTable.setMarginBottom(20f));

        // Tabla Servicio (dos filas, título y descripción, cada fila abarca dos columnas)
        Table servicioTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .useAllAvailableWidth();

        servicioTable.addCell(new Cell(1, 2)
                .add(new Paragraph("Servicio").setBold().setFontSize(fontSizeSmall))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBorder(new SolidBorder(1)));

        servicioTable.addCell(new Cell(1, 2)
                .add(new Paragraph(order.getService()).setFontSize(fontSizeMain))
                .setBorder(new SolidBorder(1)));

        document.add(servicioTable.setMarginBottom(20f));

        // Tabla Detalles de pago (dos filas, título y contenido, cada fila abarca dos columnas)
        if (order.getRepairCost() != null && order.getRepairCost() > 0) {
            Table pagoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth();

            pagoTable.addCell(new Cell(1, 2)
                    .add(new Paragraph("Detalles de Pago").setBold().setFontSize(fontSizeSmall))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setBorder(new SolidBorder(1)));

            String pagoDetalles = String.format("Costo: $%.2f     Abono: $%.2f     Saldo: $%.2f",
                    order.getRepairCost(), order.getPaid(), order.getLeft());
            pagoTable.addCell(new Cell(1, 2)
                    .add(new Paragraph(pagoDetalles).setFontSize(fontSizeMain))
                    .setBorder(new SolidBorder(1))
                    .setTextAlignment(TextAlignment.LEFT));

            document.add(pagoTable.setMarginBottom(20f));
        }

        // Términos y condiciones
        document.add(new Paragraph("TÉRMINOS Y CONDICIONES")
                .setBold()
                .setFontSize(fontSizeSmall)
                .setMarginBottom(5f));

          document.add(new Paragraph("Anticipo: Se requiere 50% de anticipo para todas las reparaciones.\n"
                + "Garantía: La garantía es de 1 mes para reparaciones de display, solo si el equipo no tiene daños adicionales, como golpes, humedad o intervención externa.\n"
                + "Responsabilidad: La empresa no se responsabiliza de problemas surgidos después de 30 días de la entrega del equipo.\n"
                + "Condiciones: El teléfono debe estar libre de golpes, humedad o ser abierto por terceros para que aplique la garantía.\n"
                + "Riesgos: En equipos que no encienden pueden surgir problemas adicionales, los cuales deberán ser aprobados por el cliente.")
                .setFontSize(fontSizeMicroSmall)
                .setTextAlignment(TextAlignment.LEFT));

        // Firma
        document.add(new Paragraph("\n\n______________________________")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Firma del cliente")
                .setFontSize(fontSizeMain)
                .setTextAlignment(TextAlignment.CENTER));

        document.close();
        pdfDoc.close();
        writer.close();

        sentoToPrinter(dest);

    } catch (Exception e) {
        System.out.println("Error al generar o imprimir el PDF: " + e.getMessage());
        e.printStackTrace();
    }
}


public static void printOrderReparir58mm(OrderServiceDTO order) {
    System.out.println("SE ENVIA A PDF PARA IMPRIMIR");
    String dest = "/home/albert/Documents/miArchivo58mm.pdf";

    try {
        File file = new File(dest);
        file.getParentFile().mkdirs();

        PdfWriter writer = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Tamaño de página ajustado para 58mm (165pt de ancho)
        PageSize thermalPageSize = new PageSize(165f, 1000f); // 58mm x altura ajustable
        Document document = new Document(pdfDoc, thermalPageSize);
        document.setMargins(3f, 5f, 3f, 5f); // Márgenes más pequeños para aprovechar el ancho

        float fontSizeTitle = 8.5f;
        float fontSizeMain = 7f;
        float fontSizeSmall = 6.5f;
        float fontSizeMicroSmall = 5f;

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.setFont(font);

        document.add(new Paragraph("ORDEN DE SERVICIO")
                .setBold().setFontSize(fontSizeTitle)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Navarro TECH")
                .setBold().setFontSize(fontSizeTitle)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("33 20 87 48 74")
                .setBold().setFontSize(fontSizeMain)
                .setTextAlignment(TextAlignment.CENTER));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = sdf.format(order.getDate());
        document.add(new Paragraph("📆 Fecha: " + formattedDate)
                .setFontSize(fontSizeMain));

        document.add(new Paragraph("📌 FOLIO: " + order.getFolio())
                .setFontSize(fontSizeMain));

        document.add(new Paragraph("🛠️ DATOS DEL CLIENTE")
                .setBold().setFontSize(fontSizeSmall));

        document.add(new Paragraph("👤 Cliente: " + order.getClient())
                .setFontSize(fontSizeMain));

        document.add(new Paragraph("📞 Teléfono: " + order.getCellphone())
                .setFontSize(fontSizeMain));

        if (order.getEmail() != null && !order.getEmail().isEmpty()) {
            document.add(new Paragraph("✉️ Email: " + order.getEmail())
                    .setFontSize(fontSizeMain));
        }

        document.add(new Paragraph("🛠️ DATOS DEL EQUIPO")
                .setBold().setFontSize(fontSizeSmall));

        document.add(new Paragraph("📱 Marca: " + order.getBrand())
                .setFontSize(fontSizeMain));
        document.add(new Paragraph("📲 Modelo: " + order.getModel())
                .setFontSize(fontSizeMain));
        document.add(new Paragraph("🔧 Servicio: " + order.getService())
                .setFontSize(fontSizeMain));

        if (order.getRepairCost() != null && order.getRepairCost() > 0) {
            document.add(new Paragraph("💵 COSTO DEL SERVICIO")
                    .setBold().setFontSize(fontSizeSmall));

            document.add(new Paragraph("💰 Costo: " + String.format("%.2f", order.getRepairCost()))
                    .setFontSize(fontSizeMain));
            document.add(new Paragraph("💸 Abono: " + String.format("%.2f", order.getPaid()))
                    .setFontSize(fontSizeMain));
            document.add(new Paragraph("📉 Saldo: " + String.format("%.2f", order.getLeft()))
                    .setFontSize(fontSizeMain));
        }

        document.add(new Paragraph("📜 TÉRMINOS Y CONDICIONES")
                .setBold().setFontSize(fontSizeSmall));

        document.add(new Paragraph(
                "• Anticipo: 50% requerido.\n" +
                "• Garantía: 1 mes en display.\n" +
                "• No aplica si hay golpes, humedad o fue abierto.\n" +
                "• No responsable por fallas tras 30 días.\n" +
                "• Riesgo: Equipos que no encienden pueden fallar más."
        ).setFontSize(fontSizeMicroSmall)
         .setTextAlignment(TextAlignment.LEFT));

        document.close();
        pdfDoc.close();
        writer.close();

        sentoToPrinter(dest);
    } catch (Exception e) {
        System.out.println("Error al generar o imprimir el PDF: " + e.getMessage());
        e.printStackTrace();
    }
}

public static void printOrderReparir80mm(OrderServiceDTO order) {
    System.out.println("SE ENVIA A PDF PARA IMPRIMIR");
    String dest = "/home/albert/Documents/miArchivo.pdf";

    try {
        File file = new File(dest);
        file.getParentFile().mkdirs();

        PdfWriter writer = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDoc = new PdfDocument(writer);
        PageSize thermalPageSize = new PageSize(226.77f, 1000f);
        Document document = new Document(pdfDoc, thermalPageSize);
        document.setMargins(3f, 30f, 3f, 5f);

        float fontSizeTitle = 12f;
        float fontSizeMain = 10f;
        float fontSizeSmall = 9f;
        float fontSizeMicroSmall = 7f;

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.setFont(font);

        document.add(new Paragraph("ORDER DE SERVICIO")
                .setBold().setFontSize(fontSizeTitle)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(1f));

        document.add(new Paragraph("Navarro TECH")
                .setBold().setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(1f));

        document.add(new Paragraph("33 20 87 48 74")
                .setBold().setFontSize(fontSizeTitle)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(1f));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = sdf.format(order.getDate());
        document.add(new Paragraph("📆 Fecha: " + formattedDate)
                .setFontSize(fontSizeMain)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(1f));

        document.add(new Paragraph("📌 FOLIO: " + order.getFolio())
                .setFontSize(fontSizeMain)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(1f));

        document.add(new Paragraph("🛠️ DATOS DEL CLIENTE")
                .setBold().setFontSize(fontSizeSmall)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(1f));

        document.add(new Paragraph()
                .add(new Text("👤 Cliente: ").setBold())
                .add(new Text(order.getClient()))
                .setFontSize(fontSizeMain)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(1f));

        document.add(new Paragraph()
                .add(new Text("Telefono: ").setBold())
                .add(new Text(order.getCellphone()))
                .setFontSize(fontSizeMain)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(1f));

        if (order.getEmail() != null && !order.getEmail().isEmpty()) {
            document.add(new Paragraph()
                    .add(new Text("Email: ").setBold())
                    .add(new Text(order.getEmail()))
                    .setFontSize(fontSizeMain)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMultipliedLeading(1f));
        }

        document.add(new Paragraph("🛠️ DATOS DEL EQUIPO")
                .setBold().setFontSize(fontSizeSmall)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(1f));

        document.add(new Paragraph()
                .add(new Text("Marca: ").setBold())
                .add(new Text(order.getBrand()))
                .setFontSize(fontSizeMain)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(1f));

        document.add(new Paragraph()
                .add(new Text("Modelo: ").setBold())
                .add(new Text(order.getModel()))
                .setFontSize(fontSizeMain)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(1f));

        document.add(new Paragraph()
                .add(new Text("Servicio: ").setBold())
                .add(new Text(order.getService()))
                .setFontSize(fontSizeMain)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(1f));

        if (order.getRepairCost() != null && order.getRepairCost() > 0) {
            document.add(new Paragraph("COSTO DEL SERVICIO")
                    .setBold().setFontSize(fontSizeMain)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMultipliedLeading(1f));

            document.add(new Paragraph()
                    .add(new Text("Costo: ").setBold())
                    .add(new Text(String.format("%.2f", order.getRepairCost())))
                    .setFontSize(fontSizeMain)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMultipliedLeading(1f));

            document.add(new Paragraph()
                    .add(new Text("Abono: ").setBold())
                    .add(new Text(String.format("%.2f", order.getPaid())))
                    .setFontSize(fontSizeMain)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMultipliedLeading(1f));

            document.add(new Paragraph()
                    .add(new Text("Saldo: ").setBold())
                    .add(new Text(String.format("%.2f", order.getLeft())))
                    .setFontSize(fontSizeMain)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMultipliedLeading(1f));
        }

        document.add(new Paragraph("TÉRMINOS Y CONDICIONES\n"
                + "Anticipo: Se requiere 50% de anticipo para todas las reparaciones.\n"
                + "Garantía: La garantía es de 1 mes para reparaciones de display, solo si el equipo no tiene daños adicionales, como golpes, humedad o intervención externa.\n"
                + "Responsabilidad: La empresa no se responsabiliza de problemas surgidos después de 30 días de la entrega del equipo.\n"
                + "Condiciones: El teléfono debe estar libre de golpes, humedad o ser abierto por terceros para que aplique la garantía.\n"
                + "Riesgos: En equipos que no encienden pueden surgir problemas adicionales, los cuales deberán ser aprobados por el cliente.")
                .setFontSize(fontSizeMicroSmall)
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setMultipliedLeading(1f));

        document.close();
        pdfDoc.close();
        writer.close();

        sentoToPrinter(dest);
    } catch (Exception e) {
        System.out.println("Error al generar o imprimir el PDF: " + e.getMessage());
        e.printStackTrace();
    }
}

    private static void sentoToPrinter(String dest) throws PrinterException, IOException {
      //  String dest = "/home/albert/Documents/miArchivo.pdf";
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

    /*
    ** Imprime el ticket de la venta del POS
     */
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
            document.setMargins(5f, 10f, 5f, 10f); // Agregar márgenes mínimos para evitar cortes
            document.add(new Paragraph("MATI PAPELERIA")
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                    .setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("CANT DESCRIPCION PRECIO TOTAL")
                    .setBold().setFontSize(10).setTextAlignment(TextAlignment.CENTER));
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

            String enLetras = NumeroALetras.convertir(totalTicket);
            document.add(new Paragraph(enLetras)
                    .setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT));

            // Cerrar el documento
            document.close();
            pdfDoc.close();
            writer.close();

            // Enviar a la impresora
            // sentToPaper(dest);
            sentoToPrinter(dest);
        } catch (Exception e) {
            System.out.println("Error al generar o imprimir el PDF: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void createdPdfLetterTiclekPOS(ObservableList<OrderDetail> orderDetails) {
        // Obtener timestamp actual
        long timestamp = System.currentTimeMillis();
        System.out.println("SE ENVIA A PDF PARA IMPRIMIR EN TAMAÑO CARTA");
        // Ruta con nombre de archivo que incluye el timestamp
        String dest = "/home/albert/Documents/miArchivo_Carta_" + timestamp + ".pdf";
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        BigDecimal totalTicket = BigDecimal.ZERO;

        try {
            File file = new File(dest);
            file.getParentFile().mkdirs();

            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Cambiar tamaño a carta
            Document document = new Document(pdfDoc, PageSize.LETTER);
            document.setMargins(40f, 40f, 40f, 40f); // Márgenes amplios para carta

            // Encabezado
            document.add(new Paragraph("MATI PAPELERIA")
                    .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Fecha: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                    .setFontSize(12).setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("PRESUPUESTO")
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20));

            // Títulos de columna
            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 5, 2, 2}))
                    .useAllAvailableWidth();
            headerTable.addHeaderCell(new Cell().add(new Paragraph("Cant").setBold()));
            headerTable.addHeaderCell(new Cell().add(new Paragraph("Descripción").setBold()));
            headerTable.addHeaderCell(new Cell().add(new Paragraph("P/U").setBold()));
            headerTable.addHeaderCell(new Cell().add(new Paragraph("Total").setBold()));

            // Cuerpo de la tabla
            for (OrderDetail detail : orderDetails) {
                BigDecimal unitPrice = detail.getPrice().divide(detail.getAmount(), 2, RoundingMode.HALF_UP);
                totalTicket = totalTicket.add(detail.getPrice());

                headerTable.addCell(new Cell().add(new Paragraph(detail.getAmount().toString())));
                headerTable.addCell(new Cell().add(new Paragraph(detail.getProduct().getName())));
                headerTable.addCell(new Cell().add(new Paragraph(decimalFormat.format(unitPrice))));
                headerTable.addCell(new Cell().add(new Paragraph(decimalFormat.format(detail.getPrice()))));

                // Opcional: logs
                System.out.println("ID: " + detail.getId());
                System.out.println("Producto: " + detail.getProduct().getBarcode());
                System.out.println("Cantidad: " + detail.getAmount());
                System.out.println("Precio: " + detail.getPrice());
            }

            document.add(headerTable);

            // Total
            document.add(new Paragraph("Total: $" + decimalFormat.format(totalTicket))
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20));

            // Total en letras
            String enLetras = NumeroALetras.convertir(totalTicket);
            document.add(new Paragraph("Total en letras: " + enLetras)
                    .setFontSize(12).setTextAlignment(TextAlignment.LEFT).setMarginTop(10));
            
            // Total en letras
            String leyenda = "Este presupuesto tiene una vigencia de 8 días naturales a partir de su emisión.";
            document.add(new Paragraph(leyenda)
                    .setFontSize(12).setTextAlignment(TextAlignment.LEFT).setMarginTop(10));

            document.close();
            pdfDoc.close();
            writer.close();

        
            sentoToPrinter(dest);

        } catch (Exception e) {
            System.out.println("Error al generar o imprimir el PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
