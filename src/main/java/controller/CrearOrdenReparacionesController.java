package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import api.OrderRepairApi;
import config.ConfigManager;
import dto.OrderServiceDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class CrearOrdenReparacionesController implements Initializable {
     ConfigManager configManager = new ConfigManager(); //carga el archivo de config.properties
   

    OrderRepairApi orderRepairApi = new OrderRepairApi();

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField imeiField;

    @FXML
    private TextField budgetField;

    @FXML
    private TextField paidField;

    @FXML
    private TextField remainingField;

    @FXML
    private TextArea issueField;

    @FXML
    private TextArea receivedConditionField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextArea noteArea;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void onActionCrearOrden(ActionEvent event) {
         boolean baseUrlRemoteFlag = Boolean.parseBoolean(configManager.getProperty("api.base.url.flag"));
        try {

            int errorSize = validateForm().length();
            String erros = validateForm();
            if (errorSize == 0) {
                String nombre = nameField.getText().trim();
                String telefono = phoneField.getText().trim();
                String correo = emailField.getText().trim();

                String marca = brandField.getText().trim();
                String modelo = modelField.getText().trim();
                String imei = imeiField.getText().trim();
                Double presupuesto = null;
                Double abono = null;
                Double restante = null;
                String falla = issueField.getText().trim();
                String estadoRecibido = receivedConditionField.getText().trim();
                String password = passwordField.getText().trim();
                String nota = noteArea.getText().trim();

                // Aquí puedes agregar la lógica para guardar la orden
                System.out.println("Orden guardada:");
                System.out.println("Nombre: " + nombre);
                System.out.println("Teléfono: " + telefono);
                System.out.println("Correo: " + correo);
                System.out.println("Marca: " + marca);
                System.out.println("Modelo: " + modelo);
                System.out.println("IMEI: " + imei);
                System.out.println("Presupuesto: " + presupuesto);
                System.out.println("Abono: " + abono);
                System.out.println("Restante: " + restante);
                System.out.println("Falla a reparar: " + falla);
                System.out.println("Estado en el que se recibe: " + estadoRecibido);
                System.out.println("Contraseña: " + password);
                System.out.println("Nota: " + nota);

                if (correo == null || correo.trim().isEmpty()) {
                    correo = null;
                }

                if (budgetField.getText().trim() == null || budgetField.getText().trim().isEmpty()) {
                    presupuesto = null;
                } else {
                    presupuesto = Double.parseDouble(budgetField.getText().trim());
                }
                if (paidField.getText().trim() == null || paidField.getText().trim().isEmpty()) {
                    abono = null;
                } else {
                    abono = Double.parseDouble(paidField.getText().trim());
                }
                if (remainingField.getText().trim() == null || remainingField.getText().trim().isEmpty()) {
                    restante = null;
                } else {
                    restante = Double.parseDouble(remainingField.getText().trim());
                }

                if(baseUrlRemoteFlag){
                     System.out.println("GUARADA REMOTO Y LOCAL");
                                   OrderServiceDTO orderServiceDTO = new OrderServiceDTO(falla, nombre, telefono, presupuesto, abono, restante, nota, correo, marca, modelo, falla, estadoRecibido, password, imei);
                // OrderServiceDTO orderServiceDTO = new OrderServiceDTO(nombre, telefono, correo, marca, modelo, imei, presupuesto, abono, restante, falla, estadoRecibido, contraseña, nota);
                String folio = orderRepairApi.createdOrderServiceRemote(orderServiceDTO);  
                System.out.println("FOLIO DESDE REMOTO= " +  folio);
                orderServiceDTO.setFolio(folio);
                orderRepairApi.createdOrderService(orderServiceDTO);
                }
                else{
                    System.out.println("GUARADA SOLO LOCAL");
                                   OrderServiceDTO orderServiceDTO = new OrderServiceDTO(falla, nombre, telefono, presupuesto, abono, restante, nota, correo, marca, modelo, falla, estadoRecibido, password, imei);
                orderRepairApi.createdOrderService(orderServiceDTO);
                }
                    
                
 
                // Cerrar la ventana
                Stage stage = (Stage) nameField.getScene().getWindow();
                stage.close();
            } else {
                showAlert("errores", erros);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Ingrese valores numéricos válidos para presupuesto, abono y restante.");
        }

    }

    private String validateForm() {
        StringBuilder errors = new StringBuilder();

        // Validar campos de texto obligatorios
        if (nameField.getText().trim().isEmpty()) {
            errors.append("El campo 'Nombre' no puede estar vacío.\n");
        }
        if (phoneField.getText().trim().isEmpty()) {
            errors.append("El campo 'Teléfono' no puede estar vacío.\n");
        }
        if (!emailField.getText().trim().isEmpty()) {
            if (emailField.getText().trim().isEmpty() || !isValidEmail(emailField.getText().trim())) {
                errors.append("El correo electrónico no es válido.\n");
            }
        }

        if (brandField.getText().trim().isEmpty()) {
            errors.append("El campo 'Marca' no puede estar vacío.\n");
        }
        if (modelField.getText().trim().isEmpty()) {
            errors.append("El campo 'Modelo' no puede estar vacío.\n");
        }
        if (imeiField.getText().trim().isEmpty()) {
            errors.append("El campo 'IMEI' no puede estar vacío.\n");
        }
        if (issueField.getText().trim().isEmpty()) {
            errors.append("El campo 'Problema' no puede estar vacío.\n");
        }
        if (receivedConditionField.getText().trim().isEmpty()) {
            errors.append("El campo 'Condición recibida' no puede estar vacío.\n");
        }
        Double abono = null;
        Double presupuesto = null;
        Double restante = null;
        if (!budgetField.getText().trim().isEmpty()) { //
            presupuesto = parseDouble(budgetField.getText().trim(), "Presupuesto", errors);
            // Validar valores lógicos de presupuesto, abono y restante
            if (presupuesto != null && presupuesto <= 0) {
                errors.append("El 'Presupuesto' debe ser un valor positivo.\n");
            } else {
                System.out.println("EL VALOR ES UN NUMERO VALIDO");
            }
        }
        if (!paidField.getText().trim().isEmpty() || presupuesto != null) { //
            abono = parseDouble(paidField.getText().trim(), "Abono", errors);
            if (abono != null && abono < 0) {
                errors.append("El 'Abono' no puede ser un valor negativo.\n");
            }
        }

        if (!remainingField.getText().trim().isEmpty() || presupuesto != null) { //
            restante = parseDouble(remainingField.getText().trim(), "Restante", errors);

            if (restante != null && restante < 0) {
                errors.append("El 'Restante' no puede ser un valor negativo.\n");
            }
        }

        // Validar que los valores numéricos sean válidos
        return errors.toString();
    }

    private Double parseDouble(String text, String fieldName, StringBuilder errors) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            errors.append("El campo '" + fieldName + "' debe ser un número válido.\n");
            return null;
        }
    }

    // Método para mostrar una alerta de error
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para validar si un correo electrónico tiene un formato correcto
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

}
