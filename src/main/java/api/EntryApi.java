/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import controller.InventoryController;
import dto.EntryDTO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author albert
 */
public class EntryApi {
    
      String token = App.getUsuario().getToken();

    public ObservableList<EntryDTO> fetchEntries(String startDate, String endDate, String departament) {
        ConfigManager configManager = new ConfigManager(); //carga el archivo de config.properties
        String baseUrl = configManager.getProperty("api.base.url");
        String endpointProductsEntries = configManager.getProperty("api.products.entries");
        //api.products.entries =/products/entries

        ObservableList<EntryDTO> entryList = null;
        try {
            // Codificar el department para que no tenga espacios
            String encodedDepartmentName = URLEncoder.encode(departament, StandardCharsets.UTF_8);
             URL url = new URL(baseUrl + endpointProductsEntries + "?startDate="+ startDate+"&endDate=" + endDate + "&departmentName=" + encodedDepartmentName);
            // URL url = new URL(baseUrl+endpointProductsEntries);
            //http://localhost:3000/products/entries?startDate=2025-01-01&endDate=2025-01-01&departmentName=papeleria
           
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
             conn.setRequestProperty("Authorization", "Bearer " + token); // <-- Agregamos el token aquí
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                 Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                // System.out.println("JSON Response: " + inline.toString()); // Verifica el contenido de la respuesta JSON
                ObjectMapper mapper = new ObjectMapper();
                List<EntryDTO> entries = mapper.readValue(inline.toString(), new TypeReference<List<EntryDTO>>() {
                });

                System.out.println("Entries List: " + entries); // Verifica que la lista de entradas se ha deserializado correctamente

                // Imprime la lista de EntryDTO en la consola
                entries.forEach(entry -> System.out.println(entry.toString()));

                // Convertir la lista a ObservableList
                entryList = FXCollections.observableArrayList(entries);
                System.out.println("obserbable list" + entries);

            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(InventoryController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ex0 = " + ex);
        } catch (IOException ex) {
            Logger.getLogger(InventoryController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ex1 = " + ex);
        }

        return entryList;
    }

}
