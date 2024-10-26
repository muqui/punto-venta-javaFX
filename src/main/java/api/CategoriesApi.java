/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.DepartmentDTO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

/**
 *
 * @author albert
 */
public class CategoriesApi {

    public ObservableList<DepartmentDTO> fillChoiceBoxDepartament() {
         ObservableList<DepartmentDTO> departamentList = null ;
        try {
            String urlString = "http://localhost:3000/categories"; // Cambia esto por la URL correcta de tu API
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
                List<DepartmentDTO> departments = mapper.readValue(inline.toString(), new TypeReference<List<DepartmentDTO>>() {
                });

                departamentList = FXCollections.observableArrayList(departments);
                System.out.println("lista " +departments.get(0).getName());
              

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
          return departamentList;
    }

}
