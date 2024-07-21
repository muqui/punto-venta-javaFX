/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.DepartmentDTO;
import dto.ProductDTO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class ProductController implements Initializable {

    @FXML
    private Button btnSaveProduct;

    @FXML
    private ChoiceBox<String> comboSaveGanancia;

    @FXML
    private ChoiceBox<String> comboSaveHowTosell;

    @FXML
    private ChoiceBox<DepartmentDTO> comboSaveDepart;

    @FXML
    private CheckBox isstocktaking;

    @FXML
    private TextField txtSaveAmount;

    @FXML
    private TextField txtSaveName;

    @FXML
    private TextField txtSaveBarcode;

    @FXML
    private TextField txtSaveDescription;

    @FXML
    private TextField txtSavePrice;

    @FXML
    private TextField txtSaveminimumStock;

    @FXML
    private TextField txtSavepurchasePrice;

    @FXML
    private TextField txtSavewholesalePrice;
    
    
    @FXML
    private TextField txtSupplier;

    @FXML
    void btnSaveAction(ActionEvent event) {
        try {
            ProductDTO product = new ProductDTO();
            product.setName(txtSaveName.getText());
            product.setDescription(txtSaveDescription.getText());
            product.setBarcode(txtSaveBarcode.getText());
            product.setPrice(Double.parseDouble(txtSavePrice.getText()));
            product.setStock(Integer.parseInt(txtSaveAmount.getText()));
            product.setImgUrl("imagen url");
            DepartmentDTO selectedDepartment = comboSaveDepart.getValue();
            int selectedId = selectedDepartment.getId();
            product.setCategoryId(selectedId);
            product.setHowToSell(comboSaveHowTosell.getValue());
            product.setPurchasePrice(Double.parseDouble(txtSavepurchasePrice.getText()));
            product.setWholesalePrice(Double.parseDouble(txtSavewholesalePrice.getText()));
            product.setStocktaking(isstocktaking.isSelected());
            product.setMinimumStock(Integer.parseInt(txtSaveminimumStock.getText()));
            product.setEntriy(Double.parseDouble(txtSaveAmount.getText()));
            product.setSupplier(txtSupplier.getText());
            product.setOutput(0);

            System.out.println(product.toString());

            sendProductToApi(product);
        } catch (Exception e) {
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillChoiceBoxGanancias();
        fillChoiceBoxDepartament();
        fillChoiceBoxHowToSell();

         txtSavepurchasePrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // El TextField ha perdido el foco
                handleTextFieldFocusLost();
            }
        });
    }

     private void handleTextFieldFocusLost() {
        // Código para manejar la pérdida de foco del TextField
        System.out.println("txtSavePrice ha perdido el foco");
        // Aquí puedes agregar cualquier acción que desees realizar
        try {
            double precioMenudeo = calculateSellingPrice(Double.parseDouble(txtSavepurchasePrice.getText()), Double.parseDouble(comboSaveGanancia.getValue()));
            txtSavePrice.setText(""+precioMenudeo);
        } catch (NumberFormatException e) {
            System.out.println("Formato de precio inválido");
            // Muestra una alerta si el formato es inválido
            showAlert("Error", "Formato de precio inválido");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fillChoiceBoxGanancias() {
        ObservableList<String> gananciaOptions = FXCollections.observableArrayList();
        for (int i = 5; i <= 100; i += 1) {
            gananciaOptions.add(String.valueOf(i));
        }
        comboSaveGanancia.setItems(gananciaOptions);
        // Establecer valor por defecto
        comboSaveGanancia.setValue("43");
    }

    private void fillChoiceBoxHowToSell() {
        ObservableList<String> howToSell = FXCollections.observableArrayList();
        howToSell.add("Unidad");
        comboSaveHowTosell.setItems(howToSell);
        // Establecer valor por defecto
        comboSaveHowTosell.setValue("Unidad");
        comboSaveHowTosell.setValue("Granel");
        comboSaveHowTosell.setValue("Paquete");
    }

    private void fillChoiceBoxDepartament() {
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
                List<DepartmentDTO> departments = mapper.readValue(inline.toString(), new TypeReference<List<DepartmentDTO>>() {});

                ObservableList<DepartmentDTO> departamentList = FXCollections.observableArrayList(departments);
                comboSaveDepart.setItems(departamentList);

                // Configurar el StringConverter para mostrar solo el nombre
                comboSaveDepart.setConverter(new StringConverter<DepartmentDTO>() {
                    @Override
                    public String toString(DepartmentDTO department) {
                        return department.getName();
                    }

                    @Override
                    public DepartmentDTO fromString(String string) {
                        return departamentList.stream().filter(department -> department.getName().equals(string)).findFirst().orElse(null);
                    }
                });

                // Establecer valor por defecto si es necesario
                if (!departamentList.isEmpty()) {
                    comboSaveDepart.setValue(departamentList.get(0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para calcular el precio de venta
    public static double calculateSellingPrice(double cost, double profitMargin) {
        return cost / (1 - profitMargin / 100);
    }

    private void sendProductToApi(ProductDTO product) throws Exception {
        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(product);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/products"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonProduct))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Resultado =" + response.body());

            if (response.statusCode() == 201) {
                showAlert("Éxito", "Producto guardado exitosamente.");
            } else {
                // Manejar error de autenticación
                showAlert("Error", "Error al guardar el producto. Código de estado: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            showAlert("Error", "Error al conectarse con la API.");
        }

    }

}
