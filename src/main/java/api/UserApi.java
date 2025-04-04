/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import dto.DepartmentDTO;
import dto.UserDTO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author albert
 */
public class UserApi {
      String token = App.getUsuario().getToken();
      
    ConfigManager configManager = new ConfigManager(); //carga el archivo de config.properties
    String baseUrl = configManager.getProperty("api.base.url");
    String endpointUser = configManager.getProperty("api.endpoint.users");
   
     public ObservableList<UserDTO> fillChoiceBoxUser() {
         ObservableList<UserDTO> userList = null ;
        try {
            String urlString = baseUrl + endpointUser; // Cambia esto por la URL correcta de tu API
           // String urlString = "http://localhost:3000/users"; // Cambia esto por la URL correcta de tu API
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token); // <-- Agregamos el token aquí
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                List<UserDTO> users = mapper.readValue(inline.toString(), new TypeReference<List<UserDTO>>() {
                });

                userList = FXCollections.observableArrayList(users);
                System.out.println("lista users" +users.get(0).getName());
              

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
          return userList;
    }

    
}
