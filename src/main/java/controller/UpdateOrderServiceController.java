package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import api.OrderRepairApi;
import dto.OrderServiceDTO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class UpdateOrderServiceController implements Initializable {

    private OrderRepairApi orderRepairApi = new OrderRepairApi();
    private OrderServiceDTO orderServiceDTO;
    private String folio = "";
    @FXML
    private TextField brandField;

    @FXML
    private TextField budgetField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField imeiField;

    @FXML
    private TextArea issueField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea noteArea;

    @FXML
    private TextField paidField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextArea receivedConditionField;

    @FXML
    private TextField remainingField;

    @FXML
    private Label labelTitle;
    
       @FXML
    private TextField replacementCostField;
       
       @FXML
    private ComboBox<String> comboStatus;   
       
    
    @FXML
    private Label labelprofit;   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         ObservableList<String> statusOptions = FXCollections.observableArrayList(
             "Pendiente",     
            "En revisión",
            "En reparación",
            "Reparado",
            "Entregado"
        );
        
        comboStatus.setItems(statusOptions);

      

    }

    @FXML
    void onActionUpdate(ActionEvent event) {
        try {

            int errorSize = validateForm().length();
            String erros = validateForm();
            if (errorSize == 0) {
                orderServiceDTO.setClient(nameField.getText().trim());
                orderServiceDTO.setCellphone(phoneField.getText().trim());
                orderServiceDTO.setEmail(emailField.getText().trim());
                orderServiceDTO.setBrand(brandField.getText().trim());
                orderServiceDTO.setModel(modelField.getText().trim());
                orderServiceDTO.setImei(imeiField.getText().trim());
                orderServiceDTO.setIssue(issueField.getText().trim());
                orderServiceDTO.setService(issueField.getText().trim());
                orderServiceDTO.setReceivedCondition(receivedConditionField.getText());
                orderServiceDTO.setNote(noteArea.getText());
                orderServiceDTO.setPasswordCellPhone(passwordField.getText());
                orderServiceDTO.setReplacementCost(Double.parseDouble(replacementCostField.getText().trim()));
                orderServiceDTO.setStatus(comboStatus.getValue());
                if (orderServiceDTO.getEmail() == null || orderServiceDTO.getEmail().trim().isEmpty()) {
                    orderServiceDTO.setEmail(null);
                                   }

                if (budgetField.getText().trim() == null || budgetField.getText().trim().isEmpty()) {
                    orderServiceDTO.setRepairCost(null);
                   // presupuesto = null;
                } else {
                     orderServiceDTO.setRepairCost(Double.parseDouble(budgetField.getText().trim()));
                   // presupuesto = Double.parseDouble(budgetField.getText().trim());
                }
                if (paidField.getText().trim() == null || paidField.getText().trim().isEmpty()) {
                    orderServiceDTO.setPaid(null);
                    //abono = null;
                } else {
                   // abono = Double.parseDouble(paidField.getText().trim());
                    orderServiceDTO.setPaid(Double.parseDouble(paidField.getText().trim()));
                }
                if (remainingField.getText().trim() == null || remainingField.getText().trim().isEmpty()) {
                    orderServiceDTO.setLeft(null);
                    //restante = null;
                } else {
                    orderServiceDTO.setLeft( Double.parseDouble(remainingField.getText().trim()));
                   // restante = Double.parseDouble(remainingField.getText().trim());
                }

                //  OrderServiceDTO orderServiceDTO = new OrderServiceDTO(falla, nombre, telefono, presupuesto, abono, restante, nota, correo, marca, modelo, falla, estadoRecibido, password, imei);
                // OrderServiceDTO orderServiceDTO = new OrderServiceDTO(nombre, telefono, correo, marca, modelo, imei, presupuesto, abono, restante, falla, estadoRecibido, contraseña, nota);
                System.out.println("ENVIAR A ACTUALIZAR ..................estado = " + orderServiceDTO.getStatus());
                orderRepairApi.updateOrderRepair(orderServiceDTO);
            } else {
                showAlert("errores", erros);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Ingrese valores numéricos válidos para presupuesto, abono y restante.");
        }

    }

    /**
     * @return the orderRepairApi
     */
    public OrderRepairApi getOrderRepairApi() {
        return orderRepairApi;
    }

    /**
     * @param orderRepairApi the orderRepairApi to set
     */
    public void setOrderRepairApi(OrderRepairApi orderRepairApi) {
        this.orderRepairApi = orderRepairApi;
    }

    /**
     * @return the folio
     */
    public String getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(String folio) throws IOException {
        this.folio = folio;
        System.out.println("Folio recibido en el controlador: " + this.folio);

        if (phoneField != null) {
            labelTitle.setText("Orden de Reparación Folio " + folio);
            orderServiceDTO = orderRepairApi.getOrderService(folio);
            phoneField.setText(orderServiceDTO.getCellphone());
            if (orderServiceDTO.getEmail() == null) {
                emailField.setText("");
            } else {
                emailField.setText(orderServiceDTO.getEmail());
            }
            if(orderServiceDTO.getRepairCost()== null){
                budgetField.setText("");
            }
            else{
                budgetField.setText(""+orderServiceDTO.getRepairCost());
            }
            
            if(orderServiceDTO.getPaid()== null){
                paidField.setText("");
            }else{
                paidField.setText(""+orderServiceDTO.getPaid());
            }
            
            if(orderServiceDTO.getLeft() == null){
                remainingField.setText("");
            }else{
                remainingField.setText(""+ orderServiceDTO.getLeft() );
            }

            nameField.setText(orderServiceDTO.getClient());
            brandField.setText(orderServiceDTO.getBrand());
            modelField.setText(orderServiceDTO.getModel());
            imeiField.setText(orderServiceDTO.getImei());
           // budgetField.setText("" + orderServiceDTO.getRepairCost());
           // paidField.setText("" + orderServiceDTO.getPaid());
           // remainingField.setText("" + orderServiceDTO.getLeft());
            passwordField.setText(orderServiceDTO.getPasswordCellPhone());
            issueField.setText(orderServiceDTO.getIssue());
            receivedConditionField.setText(orderServiceDTO.getReceivedCondition());
            noteArea.setText(orderServiceDTO.getNote());
            replacementCostField.setText(""+ orderServiceDTO.getReplacementCost());
            labelprofit.setText(String.format("Ganancia: %.2f  ", orderServiceDTO.getProfit()));
            
        comboStatus.setValue(orderServiceDTO.getStatus());
        }
    }

    private String validateForm() {
        System.out.println("correo " + emailField.getText());
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

    // Método para validar si un correo electrónico tiene un formato correcto
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Método para mostrar una alerta de error
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Double parseDouble(String text, String fieldName, StringBuilder errors) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            errors.append("El campo '" + fieldName + "' debe ser un número válido.\n");
            return null;
        }
    }
}
