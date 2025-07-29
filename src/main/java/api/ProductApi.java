/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import beans.Product;
import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import dto.DepartmentDTO;
import dto.InventoryResponseDTO;
import dto.PaginatedInventoryResponseDTO;
import dto.ProductDTO;
import dto.ProductFindDTO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.json.JSONObject;

/**
 *
 * @author albert
 */
public class ProductApi {

    ConfigManager configManager = new ConfigManager(); //carga el archivo de config.properties
    String baseUrl = configManager.getProperty("api.base.url");
    String endpointProducts = configManager.getProperty("api.endpoint.products");
    String endpointProductsAddInventory = configManager.getProperty("api.endpoint.products.add.Inventory");
    String endpointCategories = configManager.getProperty("api.endpoint.categories");
    String endpointProductsSearch = configManager.getProperty("api.products.search");
    String endpointProductsInventory = configManager.getProperty("api.products.inventory");
    String clientId = configManager.getProperty("x.client.id");
    //api.endpoint.products

    public void ProductApi() {

    }

    public ProductDTO getProductByBarcode(String barcode, String token) {
        ProductDTO product = new ProductDTO();
       // System.out.println("prueba de token directo de App " + token11);

        try {

            // Crear un cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Construir la solicitud GET
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + endpointProducts + barcode))
                    //.uri(new URI("http://localhost:3000/products/" + barcode))
                    .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                       .header("x-client-id", clientId)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            // Enviar la solicitud y recibir la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Manejar la respuesta
            if (response.statusCode() == 200) {

                // Parsear la respuesta JSON
                JSONObject jsonResponse = new JSONObject(response.body());
                // Crear una instancia de Product y asignar valores

                product.setId(jsonResponse.getInt("id"));
                product.setName(jsonResponse.getString("name"));
                product.setDescription(jsonResponse.getString("description"));
                product.setBarcode(jsonResponse.getString("barcode"));
                // product.setPrice(jsonResponse.getDouble("price"));
                product.setPrice(jsonResponse.getBigDecimal("price"));
                product.setStock(jsonResponse.getDouble("stock"));
                product.setImgUrl(jsonResponse.getString("imgUrl"));
                product.setCategoryId(jsonResponse.getInt("categoryId"));
                // product.setTotal(jsonResponse.getBigDecimal("price"));
                product.setPurchasePrice(jsonResponse.getDouble("purchasePrice"));
                product.setHowToSell(jsonResponse.getString("howToSell"));

                product.setWholesalePrice(jsonResponse.getDouble("wholesalePrice"));

                product.setMinimumStock(jsonResponse.getInt("minimumStock"));

            } else {
                System.out.println("Error en la solicitud: " + response.statusCode());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Producto no encontrado.");
                alert.showAndWait();
            }

        } catch (Exception e) {
        }
        return product;
    }

    public void addINvetory(ProductDTO product, String token) {
        System.out.println("ENVIAR A ACTUALIZAR= " + product);
        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(product);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    //endpointProductsAddInventory
                    .uri(new URI(baseUrl + endpointProductsAddInventory + product.getBarcode())) // Asegúrate de pasar el ID del producto

                    // .uri(new URI("http://localhost:3000/products/addInventory/" + product.getBarcode())) // Asegúrate de pasar el ID del producto
                    .header("Content-Type", "application/json")
                      .header("x-client-id", clientId)
                    .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiar POST a PATCH
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                showAlert("Éxito", "Producto actualizado exitosamente.");
            } else {
                String responseBody = response.body();
                System.out.println("Resultado =" + responseBody);

                // Parsear el cuerpo de la respuesta como JSON
                JSONObject jsonResponse = new JSONObject(responseBody);
                // Acceder al campo "message"
                String message = jsonResponse.getString("message");
                System.out.println("Message = " + message);

                // Manejar error de autenticación
                if (message.equalsIgnoreCase("product not found")) {
                    showAlert("Info", "El producto no existe.");
                } else {
                    showAlert("Error", "Error al actualizar el producto. Código de estado: " + response.statusCode());
                }
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            showAlert("Error", "Error al conectarse con la API.");
        }

    }

    public Product ProductoToTicket(String barcode, String token) {
        Product product = new Product();

        System.out.println("INSERTAR EN TICKET................................................." + token);

        try {

            // Crear un cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Construir la solicitud GET
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + endpointProducts + barcode))
                    // .uri(new URI("http://localhost:3000/products/" + barcode))
                    .header("Content-Type", "application/json")
                      .header("x-client-id", clientId)
                    .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .GET()
                    .build();

            // Enviar la solicitud y recibir la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Manejar la respuesta
            if (response.statusCode() == 200) {

                // Parsear la respuesta JSON
                JSONObject jsonResponse = new JSONObject(response.body());
                //  System.out.println("response COMO SE VENDE **************************" + jsonResponse.getString("howToSell"));
                // Crear una instancia de Product y asignar valores

                product.setId(jsonResponse.getInt("id"));
                product.setName(jsonResponse.getString("name"));
                product.setDescription(jsonResponse.getString("description"));
                product.setBarcode(jsonResponse.getString("barcode"));
                // product.setPrice(jsonResponse.getDouble("price"));
                product.setPrice(jsonResponse.getBigDecimal("price"));
                product.setStock(jsonResponse.getDouble("stock"));
                product.setImgUrl(jsonResponse.getString("imgUrl"));
                product.setCategoryId(jsonResponse.getInt("categoryId"));
                product.setTotal(jsonResponse.getBigDecimal("price"));
                product.setWholesalePrice(jsonResponse.getBigDecimal("wholesalePrice"));
                product.setPurchasePrice(jsonResponse.getBigDecimal("purchasePrice"));
                product.setHowToSell(jsonResponse.getString("howToSell")); //howToSell

                //   System.out.println("COMO SE VENDE 33333333333333333333333333333333333= " + product.getHowToSell());
            } else {
                // System.out.println("Error en la solicitud: " + response.statusCode());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Producto no encontrado.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            System.out.println("errororororor " + e);
        }
        return product;
    }

    public List<DepartmentDTO> DepartamenNametList(String token) {
        ObservableList<DepartmentDTO> departamentList = null;
        try {
            String urlString = baseUrl + endpointCategories; // Cambia esto por la URL correcta de tu API

            //String urlString = "http://localhost:3000/categories"; // Cambia esto por la URL correcta de tu API
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
                List<DepartmentDTO> departments = mapper.readValue(inline.toString(), new TypeReference<List<DepartmentDTO>>() {
                });

                departamentList = FXCollections.observableArrayList(departments);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return departamentList;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void updateProduct(ProductDTO product, String token) {

        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(product);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + endpointProducts + product.getBarcode())) // Asegúrate de pasar el ID del producto

                    //  .uri(new URI("http://localhost:3000/products/" + product.getBarcode())) // Asegúrate de pasar el ID del producto
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiar POST a PATCH
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                showAlert("Éxito", "Producto actualizado exitosamente.");
            } else {
                String responseBody = response.body();
                System.out.println("Resultado =" + responseBody);

                // Parsear el cuerpo de la respuesta como JSON
                JSONObject jsonResponse = new JSONObject(responseBody);
                // Acceder al campo "message"
                String message = jsonResponse.getString("message");
                System.out.println("Message = " + message);

                // Manejar error de autenticación
                if (message.equalsIgnoreCase("product not found")) {
                    showAlert("Info", "El producto no existe.");
                } else {
                    showAlert("Error", "Error al actualizar el producto. Código de estado: " + response.statusCode());
                }
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            showAlert("Error", "Error al conectarse con la API.");
        }

    }

    public List<ProductFindDTO> fetchProducts(String nameProduct, String token) throws IOException {
      //  System.out.println("token en busqueda de producto 888888 " + token);
        List<ProductFindDTO> products = null;
        String urlString = baseUrl + endpointProductsSearch + "?name=" + nameProduct;
        // String urlString = "http://localhost:3000/products/search?name=" + value;
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
            Scanner scanner = new Scanner(conn.getInputStream()); //Scanner scanner = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder(); 
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();
          //  System.out.println("Busqueda cadena= " + inline);
            ObjectMapper mapper = new ObjectMapper();
            products = mapper.readValue(inline.toString(), new TypeReference<List<ProductFindDTO>>() {
            });

            return products;
        }
    }

    public String sendProductToApi(ProductDTO product, String token) throws Exception {
        String result = "";
        System.out.println("PRODUCTO ENVIADO A LA API= " + product.toString());
        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(product);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + endpointProducts))
                    // .uri(new URI("http://localhost:3000/products"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .POST(HttpRequest.BodyPublishers.ofString(jsonProduct))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            result = "" + response.statusCode();
            if (response.statusCode() == 201 || response.statusCode() == 200) {
                showAlert("Éxito", "Producto guardado exitosamente.");
            } else {
                String responseBody = response.body();
                System.out.println("Resultado =" + response.body());

                // Parse the response body as JSON
                JSONObject jsonResponse = new JSONObject(responseBody);
                // Access the "message" field
                String message = jsonResponse.getString("message");
                System.out.println("Message = " + message);

                // Manejar error de autenticación
                if (message.equalsIgnoreCase("product exists")) {
                    //showAlert("Info", "El producto ya existe, puede actualizarlo " );
                    // showAlertUpdateProduct("Info", "El producto existe, Desea actualizarlo?");

                } else {
                    showAlert("Error", "Error al guardar el producto. Código de estado: " + response.statusCode());
                }
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            showAlert("Error", "Error al conectarse con la API.");
        }
        System.out.println("RESULTADO = " + result);
        return result;
    }

//    public Product getProductByBarcode1(String barcode, String token) {
//        Product product = new Product();
//
//        try {
//
//            // Crear un cliente HTTP
//            HttpClient client = HttpClient.newHttpClient();
//
//            // Construir la solicitud GET
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI(baseUrl + endpointProducts + barcode))
//                    //  .uri(new URI("http://localhost:3000/products/" + barcode))
//                    .header("Content-Type", "application/json")
//                    .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
//                    .GET()
//                    .build();
//
//            // Enviar la solicitud y recibir la respuesta
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            // Manejar la respuesta
//            if (response.statusCode() == 200) {
//
//                // Parsear la respuesta JSON
//                JSONObject jsonResponse = new JSONObject(response.body());
//                // Crear una instancia de Product y asignar valores
//
//                product.setId(jsonResponse.getInt("id"));
//                product.setName(jsonResponse.getString("name"));
//                product.setDescription(jsonResponse.getString("description"));
//                product.setBarcode(jsonResponse.getString("barcode"));
//                // product.setPrice(jsonResponse.getDouble("price"));
//                product.setPrice(jsonResponse.getBigDecimal("price"));
//                product.setStock(jsonResponse.getInt("stock"));
//                product.setImgUrl(jsonResponse.getString("imgUrl"));
//                product.setCategoryId(jsonResponse.getInt("categoryId"));
//                product.setTotal(jsonResponse.getBigDecimal("price"));
//                product.setPurchasePrice(jsonResponse.getBigDecimal("purchasePrice"));
//                product.setHowToSell(jsonResponse.getString("howToSell"));
//
//            } else {
//             
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.initStyle(StageStyle.UTILITY);
//                alert.setTitle("Error");
//                alert.setHeaderText(null);
//                alert.setContentText("Producto no encontrado.");
//                alert.showAndWait();
//            }
//
//        } catch (Exception e) {
//        }
//        return product;
//    }

    //Muestra el inventario 
    public InventoryResponseDTO fetchProductsInventary(String departmentName, String token) throws IOException {
        InventoryResponseDTO response = null;
        URL url = new URL(baseUrl + endpointProductsInventory + "?name=" + departmentName);
        System.out.println("" + departmentName);

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
            // Leemos la respuesta en el nuevo formato OrderDetailsResponseDTO
            response = mapper.readValue(inline.toString(), InventoryResponseDTO.class);

            return response;
        }
    }
     public PaginatedInventoryResponseDTO fetchProductsInventary(String departmentName, String token, int page, int limit) throws IOException {
    PaginatedInventoryResponseDTO response = null;

    String fullUrl = baseUrl + endpointProducts + "&page=" + page + "&limit=" + limit;
    URL url = new URL("https://back-navarro-pos.duckdns.org/products?page=1&limit=5");

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Authorization", "Bearer " + token);
    conn.setRequestProperty("Content-Type", "application/json");
    conn.connect();

    int responseCode = conn.getResponseCode();
    if (responseCode != 200) {
        throw new RuntimeException("HttpResponseCode: " + responseCode);
    }
         System.out.println("codio respuesta: " +  responseCode);
    Scanner scanner = new Scanner(conn.getInputStream());
    StringBuilder inline = new StringBuilder();
    while (scanner.hasNext()) {
        inline.append(scanner.nextLine());
    }
    scanner.close();

    ObjectMapper mapper = new ObjectMapper();
    response = mapper.readValue(inline.toString(), PaginatedInventoryResponseDTO.class);
    
     List<ProductDTO> products = response.getData();
            System.out.println("respuesta con paginacion");
            for (ProductDTO product : products) {
                System.out.println(product.getBarcode());
            }
    return response;
}


}
