/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import dto.ApiResponseDTO;
import dto.UserDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 *
 * @author albert
 */
public class LoginApi {
     ConfigManager configManager = new ConfigManager(); //carga el archivo de config.properties
    
    public HttpResponse<String> login(UserDTO usuario){
        HttpResponse<String> response = null;
         String baseUrl = configManager.getProperty("api.base.url");
        String endpoint = configManager.getProperty("api.endpoint.login");
        
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl+endpoint))                  
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + usuario.getEmail() + "\", \"password\":\"" + usuario.getPassword() + "\"}"))
                    .build();

           response = client.send(request, HttpResponse.BodyHandlers.ofString());
          



        } catch (Exception e) {
            System.out.println("error api " + e);
           // txtError.setText("ERROR: " + e);
        }
        return response;
    }
    
}
