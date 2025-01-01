/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author albert
 */
public class ConfigManager {
       private Properties properties;

    // Constructor para cargar las propiedades al instanciar la clase
    public ConfigManager() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener una propiedad específica
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
}
