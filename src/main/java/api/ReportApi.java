/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.IncomesResponseDTO;
import dto.ReportCompleteDTO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 *
 * @author albert
 */
public class ReportApi {
    
     public ReportCompleteDTO RepoertCompleResponseDTO(String startDay, String endDay) {
       ReportCompleteDTO response = null;
       
        // Codificar el userName para que no tenga espacios
         //   String encodedcategory = URLEncoder.encode(category, StandardCharsets.UTF_8);

        
                try {
            //http://localhost:3000/incomes?startDate=2024-1-10&endDate=2024-12-25&name=navarro        
            String urlString = "http://localhost:3000/reports?startDate="+startDay+"&endDate="+endDay; // Cambia esto por la URL correcta de tu API
                    System.out.println("Icome String " + urlString);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
               
                 // Leemos la respuesta en el nuevo formato OrderDetailsResponseDTO
             response = mapper.readValue(inline.toString(), ReportCompleteDTO.class);

              
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
                
                return response;
    }
    
    
}
