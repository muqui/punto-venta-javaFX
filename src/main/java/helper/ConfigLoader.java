/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author albert
 */
public class ConfigLoader {
     public static String getOrderPrinterName() {
        Properties config = new Properties();
        File file = new File("config.properties");

        try (FileInputStream in = new FileInputStream(file)) {
            config.load(in);
            return config.getProperty("printer.order.service.name", "Letter"); // Por defecto "Letter"
        } catch (IOException e) {
            e.printStackTrace();
            return "Letter"; // Fallback
        }
    }
}
